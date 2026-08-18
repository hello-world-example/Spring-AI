package io.github.hello.spring.ai.rag.etl;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class E_Tika_Main {

    static void main() {
        ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
        Resource resource = resourceLoader.getResource("classpath:/simple/Who-is-Kai.md");

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        System.out.println(tikaDocumentReader.read());
    }

}
