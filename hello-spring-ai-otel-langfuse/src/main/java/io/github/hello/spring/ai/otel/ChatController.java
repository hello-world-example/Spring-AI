package io.github.hello.spring.ai.otel;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.trace.SpanProcessor;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

    @Resource
    private ChatService chatService;
    @Resource
    private Tracer tracer;
    @Resource
    private SpanProcessor spanProcessor;

    @GetMapping
    public String get() {
        return chatService.testAiCall();
    }
}