package io.github.hello.spring.ai.rag.simple;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// @Component
public class SimpleRagRunner implements CommandLineRunner {

    private final EmbeddingModel embeddingModel;

    private final ChatClient chatClient;

    private final ResourceLoader resourceLoader;

    public SimpleRagRunner(EmbeddingModel embeddingModel, ChatClient.Builder clientBuilder, ResourceLoader resourceLoader) {
        this.embeddingModel = embeddingModel;
        this.chatClient = clientBuilder.build();
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        // 创建，并初始化数据（加载数据到内存）
        SimpleVectorStore vectorStore = this.initSimpleVectorStore();
        // optional 自定义查询
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(10) // default 4
                .similarityThreshold(0.8D) // default 0
                // .filterExpression(...) // Metadata filter
                .build();
        //
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(searchRequest)
                .build();
        //
        ChatResponse response = chatClient.prompt()
                .advisors(
                        questionAnswerAdvisor,
                        // logging.level.org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor=debug
                        SimpleLoggerAdvisor.builder().build()
                )
                .user("Who is Kai?")
                .call()
                .chatResponse();
    }

    /**
     * 初始化内存中的向量数据
     */
    private @NonNull SimpleVectorStore initSimpleVectorStore() throws IOException {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel)
                //.observationRegistry(null)
                .build();

        // SimpleVectorStore 处理后的数据文件，是个 json
        File dbFile = new File(this.getClass().getResource("/simple/").getFile() + "SimpleVectorStore.db");
        if (dbFile.exists()) {
            // 直接从 db 数据文件加载
            vectorStore.load(dbFile);
            return vectorStore;
        }

        // 原始文件
        Resource sourceResource = resourceLoader.getResource("classpath:/simple/Who-is-Kai.md");
        vectorStore.add(List.of(new Document(
                sourceResource.getContentAsString(StandardCharsets.UTF_8),
                Map.of("filename", Objects.requireNonNull(sourceResource.getFilename()))
        )));
        vectorStore.save(dbFile);

        return vectorStore;
    }


}
