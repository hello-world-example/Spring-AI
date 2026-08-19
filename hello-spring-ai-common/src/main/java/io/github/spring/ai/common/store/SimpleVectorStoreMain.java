package io.github.spring.ai.common.store;

import io.github.spring.ai.common.Util;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @see io.github.spring.ai.common.embedding.AllMiniLmL6v2EmbeddingMain
 */
public class SimpleVectorStoreMain {

    private final SimpleVectorStore vectorStore;

    public SimpleVectorStoreMain(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * 1. 增 (Create) - 批量添加文档
     */
    public void addDocuments() {
        Document doc1 = Document.builder()
                .id("doc-001")
                .text("Spring AI 是一个专为 Java 开发者打造的生成式 AI 开发框架。")
                .metadata(Map.of("category", "framework", "author", "Spring"))
                .build();

        Document doc2 = Document.builder()
                .id("doc-002")
                .text("SimpleVectorStore 是内存型向量数据库，适合轻量级开发与演示。")
                .metadata(Map.of("category", "database", "author", "AI"))
                .build();

        // 写入向量数据库（后台会自动调用 EmbeddingModel 计算向量特征）
        vectorStore.add(List.of(doc1, doc2));
    }

    /**
     * 2. 查 (Read) - 语义相似度搜索
     */
    public List<Document> searchDocuments(String queryText) {
        // 构建检索请求
        SearchRequest request = SearchRequest.builder()
                .query(queryText)         // 用户的查询语义
                .topK(2)                 // 返回相关性最高的前 K 条
                .similarityThreshold(0.5) // 设定相似度阈值 (0 ~ 1)
                .build();

        return vectorStore.similaritySearch(request);
    }

    /**
     * 3. 改 (Update) - 更新已有的文档
     * Spring AI VectorStore 采用覆盖机制：当插入相同 ID 的 Document 时自动覆盖
     */
    public void updateDocument(String docId, String newContent, Map<String, Object> newMetadata) {
        Document updatedDoc = Document.builder()
                .id(docId)
                .text(newContent)
                .metadata(newMetadata)
                .build();

        // 重新调用 add，自动更新相同 ID 的内容与向量
        vectorStore.add(List.of(updatedDoc));
    }

    /**
     * 4. 删 (Delete) - 根据 ID 列表删除文档
     */
    public void deleteDocuments(List<String> docIds) {
        vectorStore.delete(docIds);
    }

    /**
     * 5. 持久化 (Persistence) - 保存至本地 JSON 文件
     * 由于 SimpleVectorStore 是基于内存的，应用重启后数据会丢失，可以通过此方法将数据落地
     */
    public void saveToFile(File file) {
        vectorStore.save(file);
    }

    /**
     * 6. 从本地 JSON 文件加载数据
     */
    public void loadFromFile(File file) {
        vectorStore.load(file);
    }

    static void main() throws Exception {
        //
        EmbeddingModel embeddingModel = newEmbeddingModel();
        //
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        //
        SimpleVectorStoreMain demo = new SimpleVectorStoreMain(vectorStore);
        demo.addDocuments();
        demo.saveToFile(new File(Util.srcMainResources() + "/SimpleVectorStore.json.db"));
        //
        List<Document> documents = demo.searchDocuments("Spring AI 有哪些 Vector Store 支持");
        documents.forEach(System.out::println);

    }

    private static EmbeddingModel newEmbeddingModel() throws Exception {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        embeddingModel.setResourceCacheDirectory(Util.srcMainResources() + "/spring-ai-onnx-model");
        embeddingModel.afterPropertiesSet();
        return embeddingModel;
    }

}
