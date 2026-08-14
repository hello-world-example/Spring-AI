package io.github.hello.spring.ai.rag.advance;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class AdvanceRagRunner implements CommandLineRunner {


    private final VectorStoreService vectorStoreService;

    private final ChatClient chatClient;


    public AdvanceRagRunner(VectorStoreService vectorStoreService, ChatClient.Builder chatClientBuilder) {
        this.vectorStoreService = vectorStoreService;
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public void run(String... args) throws Exception {
        /**
         *
         */
        VectorStore vectorStore = vectorStoreService.initVectorStore();

        /**
         *
         */
        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .similarityThreshold(0.50)
                .vectorStore(vectorStore)
                .build();

        ContextualQueryAugmenter queryAugmenter = ContextualQueryAugmenter.builder()
                // By default, the RetrievalAugmentationAdvisor does not allow the retrieved context to be empty.
                // When that happens, it instructs the model not to answer the user query.
                // You can allow empty context as follows.
                .allowEmptyContext(true)
                .build();
        /**
         *
         */
        Advisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(queryAugmenter)
                .build();

        String answer = chatClient.prompt()
                .advisors(ragAdvisor)
//                .user(question)
                .call()
                .content();

    }


}
