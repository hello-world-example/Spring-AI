//package xyz.kail.github;
//
//import jakarta.annotation.Resource;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.content.Media;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.MediaType;
//
//@SpringBootApplication
//public class DeepseekOcr2Main2 implements ApplicationRunner {
//
//    private final ChatClient chatClient;
//
//    @Resource
//    private ChatModel chatModel;
//
//    public DeepseekOcr2Main2(ChatClient.Builder builder) {
//        this.chatClient = builder.build();
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        // 1. 准备图片资源
//        var imageResource = new FileSystemResource("/Users/kevin/Downloads/xiaobao.png");
//
//        // 优化后的提示词
//        String advancedPrompt = """
//                <image>
//                <|grounding|>
//                任务：高精度 OCR 识别。
//                要求：仅输出 JSON，包含'姓名'和'身份证号码'字段。
//                """;
//
//        UserMessage userMessage = UserMessage.builder()
//                .text(advancedPrompt)
//                .media(Media.builder()
//                        .data(imageResource)
//                        .mimeType(MediaType.IMAGE_PNG)
//                        .build()
//                )
//                .build();
//
//        Prompt prompt = new Prompt(userMessage);
//
//        // 3. 调用 oMLX 服务
//        String content = chatClient.prompt(prompt)
//                .call()
//                .content();
//
//        System.out.println(content);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(DeepseekOcr2Main2.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
//        application.run(args);
//    }
//
//}