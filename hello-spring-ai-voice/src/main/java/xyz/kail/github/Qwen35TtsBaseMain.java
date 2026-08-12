package xyz.kail.github;

import com.openai.client.OpenAIClient;
import com.openai.client.OpenAIClientImpl;
import com.openai.core.ClientOptions;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Qwen35TtsBaseMain {

    public static void main(String[] args) throws IOException {
        OpenAIClient openAIClient = new OpenAIClientImpl(ClientOptions.builder().baseUrl("http://localhost:11434").apiKey("none").build());

        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model("Qwen3-TTS-12Hz-1.7B-Base-8bit")
                .voice("female")
                .speed(1.0D)
                .build();

        OpenAiAudioSpeechModel audioSpeechModel = OpenAiAudioSpeechModel.builder()
                .openAiClient(openAIClient)
                .options(options)
                .build();


        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt("Hello, this is a test of the Qwen3.5 TTS model.");

        TextToSpeechResponse speechResponse = audioSpeechModel.call(speechPrompt);
        //
        byte[] output = speechResponse.getResult().getOutput();
        Files.write(Paths.get("/Users/kevin/Downloads/qwen35-tts-base.wav"), output);

    }

}