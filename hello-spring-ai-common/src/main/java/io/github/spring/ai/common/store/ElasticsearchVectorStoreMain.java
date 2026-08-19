package io.github.spring.ai.common.store;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import io.github.spring.ai.common.Util;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStoreOptions;
import org.springframework.ai.vectorstore.elasticsearch.SimilarityFunction;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class ElasticsearchVectorStoreMain {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchVectorStoreMain.class);

    private static final ElasticsearchVectorStore VECTOR_STORE;

    static {
        try {
            VECTOR_STORE = Conf.newVectorStore();
            // 内置的初始化索引，Not work because initializeSchema=false
            // vectorStore.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 1. 【新增 / Insert】添加文档入库
     * Spring AI 会自动调用 EmbeddingModel 生成文本向量，并存入 Elasticsearch。
     */
    public static void addDocuments() {
        List<Document> documents = List.of(
                new Document("doc-1", "Spring AI 是一个用于简化 AI 应用程序开发的 Spring 框架。", Map.of("category", "tech", "author", "Alice")),
                new Document("doc-2", "Elasticsearch 是分布式、RESTful 风格的搜索和数据分析引擎。", Map.of("category", "tech", "author", "Bob")),
                new Document("doc-3", "杭州西湖是中国著名的风景名胜区，四季宜人。", Map.of("category", "travel", "author", "Alice"))
        );

//        for (Document document : documents) {
//            System.out.println(document.getFormattedContent());
//        }

        // 写入 ES
        VECTOR_STORE.add(documents);
    }

    /**
     * 2. 【修改 / Update】更新现有文档
     * VectorStore 借助于唯一 ID 执行“覆盖更新”（Upsert），即若 ID 已存在则替换旧记录。
     */
    public static void updateDocument(String docId, String updatedContent, Map<String, Object> updatedMetadata) {
        Document updatedDoc = new Document(docId, updatedContent, updatedMetadata);
        // 如果相同 ID 已存在，ES 会直接覆写旧内容并自动重新生成 Embedding 向量
        VECTOR_STORE.add(List.of(updatedDoc));
    }

    /**
     * 4. 【查询 / Similarity Search】KNN 向量相似度查询与 Metadata 过滤
     * 支持阈值过滤 (Similarity Threshold) 以及基于元数据的精确筛选 (Filter Expression)。
     */
    public static List<Document> searchDocuments(String queryText) {
        // 创建过滤条件表达式：category == 'tech' AND author == 'Alice'
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression expression = b.and(
                b.eq("category", "tech"),
                b.eq("author", "Alice")
        ).build();
        expression = b.eq("category", "tech").build();

        SearchRequest request = SearchRequest.builder()
                .query(queryText)                 // 查询目标文本
                .topK(5)                          // 返回 Top 5 最相似结果
                .similarityThreshold(0.75)        // 相似度打分阈值，过滤不相关文档
                .filterExpression(expression)     // 携带元数据过滤表达式
                .build();

        // 检索符合条件的文档列表
        return VECTOR_STORE.similaritySearch(request);
    }

    static void main() throws Exception {
        // 初始化索引
        Conf.initIndex(VECTOR_STORE.<ElasticsearchClient>getNativeClient().get());

        //
        addDocuments();

        //
        List<Document> documents = searchDocuments("Elasticsearch 能干啥");
        documents.forEach(System.out::println);

    }

    public static class Conf {

        private static final String USERNAME = "elastic";
        private static final String PASSWORD = "123456";

        private static final String DOCUMENT_INDEX = "spring_ai_document_index";
        private static final String EMBEDDING_FIELD_NAME = "embedding";
        private static final int EMBEDDING_DIMS = 384;


        /**
         * 自定义配置并在容器启动时初始化优化的 Index Mapping
         */
        public static void initIndex(ElasticsearchClient client) throws IOException {
            boolean exists = client.indices().exists(ExistsRequest.of(e -> e.index(DOCUMENT_INDEX))).value();
            if (exists) {
                log.info("索引 [{}] 已存在，跳过初始化。", DOCUMENT_INDEX);
                return;
            }

            log.info("索引 [{}] 不存在，开始创建优化后的 Vector 索引 Mapping...", DOCUMENT_INDEX);

            // 1. embedding 字段: dense_vector 类型，开启动态 HNSW 索引加速 KNN 查询
            // 2. content 字段: text 类型，使用 ik_max_word 或 standard 分词器
            // 3. metadata 字段: 动态映射，默认转成 keyword 以提高 Filter过滤 效率
            String indexMappingJson = """
                    {
                        "settings": {
                            "index": {
                                "number_of_shards": "3",
                                "number_of_replicas": "1",
                                "refresh_interval": "1s",
                                "translog": {
                                    "durability": "async",
                                    "sync_interval": "30s",
                                    "flush_threshold_size": "1000mb"
                                }
                            }
                        },
                        "mappings": {
                            "dynamic_templates": [
                                {
                                    "metadata_strings_as_keywords": {
                                        "path_match": "metadata.*",
                                        "match_mapping_type": "string",
                                        "mapping": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                }
                            ],
                            "properties": {
                                "content": {
                                    "type": "text"
                                },
                                "embedding": {
                                    "type": "dense_vector",
                                    "dims": %s,
                                    "index": true,
                                    "similarity": "cosine",
                                    "index_options": {
                                        "type": "bbq_hnsw",
                                        "m": 16,
                                        "ef_construction": 100
                                    }
                                },
                                "metadata": {
                                    "type": "object",
                                    "dynamic": true
                                }
                            }
                        }
                    }
                    """.formatted(EMBEDDING_DIMS);

            CreateIndexRequest request = CreateIndexRequest.of(builder ->
                    builder.index(DOCUMENT_INDEX).withJson(new StringReader(indexMappingJson))
            );

            client.indices().create(request);
            log.info("索引 [{}] 创建成功！", DOCUMENT_INDEX);
        }


        static Rest5Client newClient() {
            // 1. 设置目标主机
            HttpHost host = new HttpHost("http", "localhost", 9200);

            String credentials = Base64.getEncoder().encodeToString("%s:%s".formatted(USERNAME, PASSWORD).getBytes());
            Header[] defaultHeaders = new Header[]{new BasicHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials)};

            // 3. 构建 Rest5Client (低级别客户端)
            return Rest5Client.builder(host)
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        }

        /**
         * 声明 ElasticsearchVectorStore Bean
         */
        public static ElasticsearchVectorStore newVectorStore() throws Exception {
            ElasticsearchVectorStoreOptions options = new ElasticsearchVectorStoreOptions();
            options.setIndexName(DOCUMENT_INDEX);
            options.setEmbeddingFieldName(EMBEDDING_FIELD_NAME);
            options.setSimilarity(SimilarityFunction.cosine);
            options.setDimensions(EMBEDDING_DIMS);


            Rest5Client rest5Client = newClient();

            EmbeddingModel embeddingModel = Util.newEmbeddingModel();
            //
            return ElasticsearchVectorStore.builder(rest5Client, embeddingModel)
                    .options(options)
                    .initializeSchema(false)
                    .build();
        }


    }
}
