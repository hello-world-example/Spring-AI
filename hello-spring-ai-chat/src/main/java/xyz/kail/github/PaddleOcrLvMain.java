package xyz.kail.github;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;

@SpringBootApplication
public class PaddleOcrLvMain implements ApplicationRunner {

    private final ChatClient chatClient;

    public PaddleOcrLvMain(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public void run(ApplicationArguments args) {
        // 1. 准备图片资源
        // var imageResource = new FileSystemResource("/Users/kevin/Downloads/xiaobao.png");
        var imageResource = new FileSystemResource("/Users/kevin/Downloads/woolworth-receipt.jpeg");

        // 2. 构建用户消息
        UserMessage userMessage = UserMessage.builder()
                .text("").media(Media.builder()
                        .data(imageResource)
                        .mimeType(MediaType.IMAGE_PNG)
                        .build()
                )
                .build();

        // 3. 调用 oMLX 服务
        String content = chatClient.prompt(new Prompt(userMessage)).call().content();

        System.out.println(content);
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PaddleOcrLvMain.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

}