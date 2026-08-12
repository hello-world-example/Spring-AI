package xyz.kail.github.agentscope;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.model.AnthropicChatModel;
import io.agentscope.core.model.OpenAIChatModel;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuickStart {

    public static void main(String[] args) {
        // 准备工具
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new SimpleTools());


        OpenAIChatModel chatModel = OpenAIChatModel.builder()
                .baseUrl("http://127.0.0.1:26688/v1")
                .apiKey("none")
                .modelName("gemma-4-26b-a4b-it-4bit")
                // .enableSearch(true)
                // .enableThinking(true)
                .build();

        // 创建智能体
        ReActAgent jarvis = ReActAgent.builder()
                .name("Jarvis")
                .sysPrompt("你是一个名为 Jarvis 的助手")
                .model(chatModel)
                .toolkit(toolkit)
                .build();

        // 发送消息
        Msg msg = Msg.builder()
                .textContent("你好！Jarvis，现在几点了？")
                .build();

        Msg response = jarvis.call(msg).block();
        System.out.println(response.getTextContent());
    }
}

// 工具类
class SimpleTools {

    @Tool(name = "get_time", description = "获取当前时间")
    public String getTime(@ToolParam(name = "zone", description = "时区，例如：北京") String zone) {
        System.out.println("工具被调用了，参数 zone = " + zone);
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}