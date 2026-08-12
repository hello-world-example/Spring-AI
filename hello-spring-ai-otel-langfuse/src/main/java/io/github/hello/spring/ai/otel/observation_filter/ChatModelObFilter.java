package io.github.hello.spring.ai.otel.observation_filter;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.content.Content;
import org.springframework.ai.observation.ObservabilityHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
@NullMarked
public class ChatModelObFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        if (!(context instanceof ChatModelObservationContext ctx)) {
            return context;
        }

        var prompts = processPrompts(ctx);
        var completions = processCompletion(ctx);

        ctx.addHighCardinalityKeyValue(new KeyValue() {
            @Override
            public String getKey() {
                return "gen_ai.prompt";
            }

            @Override
            public String getValue() {
                return ObservabilityHelper.concatenateStrings(prompts);
            }
        });

        ctx.addHighCardinalityKeyValue(new KeyValue() {
            @Override
            public String getKey() {
                return "gen_ai.completion";
            }

            @Override
            public String getValue() {
                return ObservabilityHelper.concatenateStrings(completions);
            }
        });

        return ctx;
    }

    private List<String> processPrompts(ChatModelObservationContext ctx) {
        List<Message> messages = ctx.getRequest().getInstructions();
        //
        return CollectionUtils.isEmpty(messages) ? List.of() : messages.stream().map(Content::getText).toList();
    }

    private List<String> processCompletion(ChatModelObservationContext context) {
        return Optional.of(context)
                .map(ChatModelObservationContext::getResponse)
                .map(ChatResponse::getResults)
                .orElse(List.of())
                .stream()
                .filter(Objects::nonNull)
                .filter((generation) -> StringUtils.hasText(generation.getOutput().getText()))
                .map((generation) -> generation.getOutput().getText())
                .toList();
    }
}