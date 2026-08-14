//package xyz.kail.github;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.ollama.api.OllamaChatOptions;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class IdCardMain implements ApplicationRunner {
//
//    private final ChatClient chatClient;
//
//    public IdCardMain(ChatClient.Builder builder) {
//        OllamaChatOptions defaultOptions = OllamaChatOptions.builder()
//                .model("qwen3.5:9b")
//                .disableThinking()
//                .build();
//
//        this.chatClient = builder
//                .defaultSystem("""
//                    # Role: 身份特征信息提取专家
//
//                    # Profile:
//                    你是一个专门用于从非结构化文本（如 OCR 识别结果、表单文本）中提取公民身份信息的专业助手。你能够精准识别姓名、性别、民族、地址、
//                    出生日期和身份证号等关键字段，并能自动清洗格式。
//
//                    # Task:
//                    请分析输入文本，从中提取出 6 个指定字段的准确信息，并按照严格的 JSON 格式输出。
//
//                    # Workflow:
//                    1. **信息清洗**：忽略文本中的标签（如“姓名”、“性别”、“住”）、标点符号和多余空格，仅保留纯数据值。
//                    2. **格式转换**：将日期格式"YYYY 年 MM 月 DD 日”统一转换为"YYYY-MM-DD"。
//                    3. **地址解析**：提取完整的省/市/区/路信息，去除前缀“住”。
//                    4. **字段映射**：确保输出仅包含指定的 6 个键，不添加如 status、error 等额外键。
//
//                    # Fields (Fields Schema):
//                    - name: 姓名（String, 去除前后空格）
//                    - sex: 性别（String, 例如“男”或“女”，若原文为拼音请转为汉字）
//                    - ethnicity: 民族（String, 例如“汉”、“回”等）
//                    - idNumber: 公民身份号码（String, 18 位数字，保持原样）
//                    - birthDate: 出生年月日（String, 格式必须为 YYYY-MM-dd）
//                    - address: 户籍或常住地址（String, 完整路径）
//
//                    # Constraints (Critical):
//                    - **输出格式**：必须是合法的 JSON 对象，不要包含 Markdown 代码块标记（如 ```json），直接输出纯文本。
//                    - **字段数量**：JSON 对象中 key 的数量必须严格等于 6 个。
//                    - **容错处理**：如果某字段无法识别，使用空字符串""填充，不要添加 error 提示。
//                    - **隐私安全**：本任务仅针对测试数据，若检测到真实身份证号码（18 位），请保留数字但注意不要在非生产环境发送敏感信息。
//
//                    # Output Example:
//                    {"name": "支小宝", "sex": "女", "ethnicity": "汉", "idNumber": "123456202001011234", "birthDate": "2020-01-01",\s
//                    "address": "四川省成都市万塘路"}
//                        """)
//                .defaultOptions(defaultOptions)
//                .build();
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//
//        String content = chatClient.prompt()
//                // 需要处理的文本
//                .user("""
//                    支小宝姓名
//                    性别女民族汉2020年01月01日住四川省成都市万塘路
//                    公民身份号码123456202001011234
//                        """)
//                .call()
//                .content();
//
//
//        System.out.println(content);
//
//        // TODO 字符串解析
//    }
//
//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(IdCardMain.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
//        application.run(args);
//    }
//}