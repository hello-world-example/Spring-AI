package io.github.hello.spring.ai.otel.runner;

import io.micrometer.observation.ObservationFilter;
import io.micrometer.observation.ObservationHandler;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PrintBeansRunner implements CommandLineRunner {

    @Resource
    Map<String, ObservationFilter> observationFilterMap;
    @Resource
    Map<String, ObservationHandler> observationHandlerMap;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("PrintBeansRunner ===== " + ObservationFilter.class);
        observationFilterMap.forEach((key, value) -> System.out.println(key + ":" + value));
        System.out.println("PrintBeansRunner ===== " + ObservationHandler.class);
        observationHandlerMap.forEach((key, value) -> System.out.println(key + ":" + value));
        System.out.println("PrintBeansRunner ===== End");

    }
}
