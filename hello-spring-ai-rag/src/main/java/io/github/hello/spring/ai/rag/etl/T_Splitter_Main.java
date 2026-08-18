package io.github.hello.spring.ai.rag.etl;

import com.knuddels.jtokkit.api.EncodingType;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import java.util.List;
import java.util.Map;

public class T_Splitter_Main {
    static void main() {
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                // (default: CL100K_BASE) 设置 Token 编码格式 (如 CL100K_BASE)
                .withEncodingType(EncodingType.CL100K_BASE)
                // (default: 800) 设置每个切片最大 800 个 Token
                .withChunkSize(800)
                // @formatter:off    For each chunk
                    // (default: 350) 设置切片最小字符数为 50 字符
                    .withMinChunkSizeChars(350)
                    // (default: true) 切分时保留分隔标点
                    .withKeepSeparator(true)
                    // 指定切分边界判定标点
                    .withPunctuationMarks(List.of('.', '!', '?', ';', '\n'))
                    // (default: 10000) This process continues until all tokens are processed or maxNumChunks is reached.
                    .withMaxNumChunks(1000)
                    // (default: 5) Any remaining text is added as a final chunk if it’s longer than minChunkLengthToEmbed
                    .withMinChunkLengthToEmbed(10)
                // @formatter::on
                .build();

        Document doc1 = new Document("This is a long piece of text that needs to be split into smaller chunks for processing.",
                Map.of("source", "example.txt"));
        Document doc2 = new Document("Another document with content that will be split based on token count.",
                Map.of("source", "example2.txt"));

        List<Document> splitDocuments = splitter.apply(List.of(doc1, doc2));

        for (Document doc : splitDocuments) {
            System.out.println("Chunk: " + doc.getText());
            System.out.println("Metadata: " + doc.getMetadata());
        }


    }
}
