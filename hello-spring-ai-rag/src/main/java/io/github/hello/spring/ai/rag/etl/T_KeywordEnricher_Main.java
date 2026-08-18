package io.github.hello.spring.ai.rag.etl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;

import java.util.List;
import java.util.Map;

class T_KeywordEnricher_Main {

    static ChatModel chatModel;

    static {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl("http://localhost:11434")
                .build();

        OllamaChatOptions chatOptions = OllamaChatOptions.builder()
                .model("gemma4:12b-mlx")
                .build();

        chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .options(chatOptions)
                .build();
    }

    static List<Document> enrichDocuments(List<Document> documents) {

        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(chatModel)
                .keywordCount(5)
                .build();

        return enricher.apply(documents);
    }

    static void main() {
        Document document = new Document(
                "The KeywordMetadataEnricher is a DocumentTransformer that uses a generative AI model to extract keywords from document content and add them as metadata.",
                Map.of("source", "https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html#_keywordmetadataenricher")
        );
        List<Document> documents = enrichDocuments(List.of(document));
        //
        for (Document doc : documents) {
            System.out.println(doc.getMetadata().get("excerpt_keywords"));
        }
    }
}