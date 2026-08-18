package io.github.hello.spring.ai.rag.etl;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher.SummaryType;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class T_Summary_Main {

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

    static void main() {
        List<SummaryType> summaryTypes = List.of(SummaryType.PREVIOUS, SummaryType.CURRENT, SummaryType.NEXT);
        SummaryMetadataEnricher enricher = new SummaryMetadataEnricher(chatModel, summaryTypes);

        Document doc1 = new Document("Content of document 1");
        Document doc2 = new Document("Content of document 2");

        List<Document> enrichedDocs = enricher.apply(List.of(doc1, doc2));

        // Check the metadata of the enriched documents
        for (Document doc : enrichedDocs) {
            System.out.println("Current summary: " + doc.getMetadata().get("section_summary"));
            System.out.println("Previous summary: " + doc.getMetadata().get("prev_section_summary"));
            System.out.println("Next summary: " + doc.getMetadata().get("next_section_summary"));
            System.out.println("----------------------------------------");
        }
    }
}
