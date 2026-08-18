
package io.github.hello.spring.ai.rag.etl;

import org.springframework.ai.document.DefaultContentFormatter;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;

import java.util.Map;

public class T_ContentFormat_Main {

    public static void main(String[] args) {
        // 1. 创建包含元数据的原始文档
        Document document = new Document(
                "Spring AI 能够极大地简化 Java AI 应用的开发。",
                Map.of(
                        "title", "Spring AI 入门指南",
                        "author", "张三",
                        "internal_id", "SYS_9981"
                )
        );

        // 2. 通过 Builder 构建格式化器
        DefaultContentFormatter formatter = DefaultContentFormatter.builder()
                // 定义整体输出结构：{content} 对应文本，{metadata} 对应拼接后的元数据
                .withTextTemplate("{content}\n[元数据信息]:\n{metadata_string}")
                // 单条元数据的展现形式
                .withMetadataTemplate("{key}: {value}")
                // 元数据之间的分隔符
                .withMetadataSeparator("\n")
                // 向量化（Embedding）时排除 internal_id，避免无意义的系统 ID 干扰向量计算
                .withExcludedEmbedMetadataKeys("internal_id")
                // 提交给大模型（Inference）时排除 author，节省上下文 Token
                .withExcludedInferenceMetadataKeys("author")
                .build();

        // 3. 输出向量化模式下的格式化结果
        String embedText = formatter.format(document, MetadataMode.EMBED);
        System.out.println("=== 向量化存储时的内容 ===");
        System.out.println(embedText);

        // 4. 输出提交给大模型模式下的格式化结果
        String inferenceText = formatter.format(document, MetadataMode.INFERENCE);
        System.out.println("\n=== 提交给 LLM 时的内容 ===");
        System.out.println(inferenceText);
    }
}