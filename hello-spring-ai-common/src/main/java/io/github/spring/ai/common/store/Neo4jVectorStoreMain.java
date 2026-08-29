package io.github.spring.ai.common.store;

import io.github.spring.ai.common.Util;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Neo4j Labs * GenAI Ecosystem * GenAI Frameworks * SpringAI * https://neo4j.com/labs/genai-ecosystem/spring-ai/
 * <p>
 * Spring AI * Vector Databases * Neo4j: https://docs.spring.io/spring-ai/reference/api/vectordbs/neo4j.html
 * <p>
 * Vector Index: https://neo4j.com/docs/cypher-manual/current/indexes/semantic-indexes/vector-indexes/
 */
public class Neo4jVectorStoreMain {

    private static final Logger log = LoggerFactory.getLogger(Neo4jVectorStoreMain.class);

    private static final Neo4jVectorStore VECTOR_STORE;

    static {
        try {
            VECTOR_STORE = Conf.newVectorStore();
            // initializeSchema
            VECTOR_STORE.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GraphRAG Core
     */
    String doGraphRAG(String question) {
        List<Document> results = VECTOR_STORE.similaritySearch(question);

        System.out.println("Id list to graph: " + results.stream()
                .map(Document::getId)
                .collect(Collectors.toList()));

        // GraphRAG： 查询相关的实体 Chunk
//        List<Chunk> docList = repo.getRelatedEntitiesForSimilarChunks(results.stream()
//                .map(Document::getId)
//                .collect(Collectors.toList()));
//
//        var template = new PromptTemplate("""
//                You are a helpful question-answering agent. Your task is to analyze
//                and synthesize information from the top result from a similarity search
//                and relevant data from a graph database.
//                Given the user's query: {question}, provide a meaningful and efficient answer based
//                on the insights derived from the following data:
//
//                {graph_result}
//                """
//        ).create(Map.of("question", question,
//                "graph_result", docList.stream().map(chunk -> chunk.toString()).collect(Collectors.joining("\n"))));
//        System.out.println("----- PROMPT -----");
//        System.out.println(template);

//        return client.prompt(template).call().content();
        return null;
    }


    static void main() throws Exception {
        System.out.println("VectorStore init");
    }

    public static class Conf {

        private static final String DB = "hello-spring-ai";
        private static final String USERNAME = "neo4j";
        private static final String PASSWORD = "12345678";
        private static final int EMBEDDING_DIMS = 384;


        /**
         * @see Neo4jVectorStore#afterPropertiesSet()
         */
        public static Neo4jVectorStore newVectorStore() throws Exception {
            //
            Driver driver = GraphDatabase.driver(
                    "bolt://localhost:7687",
                    AuthTokens.basic(USERNAME, PASSWORD)
            );

            //
            EmbeddingModel embeddingModel = Util.newEmbeddingModel();

            //
            return Neo4jVectorStore.builder(driver, embeddingModel)
                    .databaseName(DB)                                            // Optional: defaults to "neo4j"
                    // Table
                    .label(Neo4jVectorStore.DEFAULT_LABEL)                            // Optional: defaults to "Document"
                    .textProperty(Neo4jVectorStore.DEFAULT_TEXT_PROPERTY)
                    // Unique Index
                    .constraintName(Neo4jVectorStore.DEFAULT_CONSTRAINT_NAME)
                    .idProperty(Neo4jVectorStore.DEFAULT_ID_PROPERTY)
                    // Vector Index
                    .indexName(Neo4jVectorStore.DEFAULT_INDEX_NAME)                   // Optional: defaults to "spring-ai-document-index"
                    .embeddingProperty(Neo4jVectorStore.DEFAULT_EMBEDDING_PROPERTY)   // Optional: defaults to "embedding"
                    .embeddingDimension(EMBEDDING_DIMS)                                          // Optional: defaults to 1536
                    .distanceType(Neo4jVectorStore.Neo4jDistanceType.COSINE)          // Optional: defaults to COSINE
                    //
                    .initializeSchema(true)                                            // Optional: defaults to false
                    .batchingStrategy(new TokenCountBatchingStrategy())                // Optional: defaults to TokenCountBatchingStrategy
                    .build();
        }


    }
}
