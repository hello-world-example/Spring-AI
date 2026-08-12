//package xyz.kail.github;
//
//import jakarta.annotation.Resource;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.ai.ollama.api.OllamaApi;
//import org.springframework.ai.ollama.api.OllamaChatOptions;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileUrlResource;
//import org.springframework.util.MimeTypeUtils;
//
//@SpringBootApplication
//public class DeepseekOcrMain implements ApplicationRunner {
//
//    @Resource(name = "deepSeekOcrClient")
//    private ChatClient chatClient;
//
//    @Bean
//    public static ChatClient deepSeekOcrClient() {
//        // 1. 手动初始化底层 API 接口 (Base URL)
//        OllamaApi ollamaApi = OllamaApi.builder().baseUrl("http://localhost:11434").build();
//
//
//        // 2. 手动配置模型参数
//        OllamaChatOptions options = OllamaChatOptions.builder()
//                .model("deepseek-ocr:3b")
//                .temperature(0.0D) // 设为 0 以保证结果确定性
//                .disableThinking()
//                .build();
//
//
//        // 3. 构造 ChatModel
//        OllamaChatModel ollamaModel = OllamaChatModel.builder()
//                .ollamaApi(ollamaApi)
//                .defaultOptions(options)
//                .build();
//
//
//        // 4. 构建并返回 ChatClient 门面
//        return ChatClient.builder(ollamaModel).build();
//    }
//
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // 加载图片资源（也可以从外部 URL 或上传的文件获取）
////        var imageResource = new FileUrlResource("/Users/kevin/Downloads/Weixin_Image_20260310134741_182_88.png");
//         var imageResource = new FileUrlResource("/Users/kevin/Downloads/中国驾照.jpg");
////        var imageResource = new FileUrlResource("/Users/kevin/Downloads/机动车行驶证.jpg");
////        var imageResource = new FileUrlResource("/Users/kevin/Downloads/复杂表格.jpg");
//
//
//        String content = chatClient.prompt()
//                .user(u -> {
//                    u.text("识别这图片的内容，并以 JSON 格式输出")
//                            //.text("vin/车架号/车辆识别码 是一样的, 你只需要告诉我识别出来的 VIN 码就行了")
//                            .media(MimeTypeUtils.IMAGE_JPEG, imageResource)
//                    ;
//
//                })
//                .call()
//                .content();
//
//        System.out.println(content);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(DeepseekOcrMain.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
//        application.run(args);
//    }
//}