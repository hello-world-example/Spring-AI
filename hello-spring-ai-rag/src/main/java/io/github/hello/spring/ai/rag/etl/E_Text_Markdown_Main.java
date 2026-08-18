package io.github.hello.spring.ai.rag.etl;

import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class E_Text_Markdown_Main {

    static void main() {
        /*
         * 一般文本
         */
//        runTextReader();

        /*
         * Markdown，可以按照 Header 自动切分成多个 Document
         */
        runMarkdownReader();
    }

    private static void runMarkdownReader() {
        ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
        Resource resource = resourceLoader.getResource("classpath:/simple/Who-is-Kai.md");
        //
        MarkdownDocumentReaderConfig.Builder configBuilder = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(true)
                .withIncludeBlockquote(true);

        if (null != resource.getFilename()) {
            configBuilder.withAdditionalMetadata("filename", resource.getFilename());
        }

        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, configBuilder.build());
        reader.get().forEach(System.out::println);
    }

    private static void runTextReader() {
        ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
        Resource resource = resourceLoader.getResource("classpath:/simple/Who-is-Kai.md");
        //
        TextReader textReader = new TextReader(resource);
        System.out.println(textReader.get());
    }
}
