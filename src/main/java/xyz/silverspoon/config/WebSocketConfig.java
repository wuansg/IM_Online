package xyz.silverspoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import xyz.silverspoon.component.WSHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WSHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/message/*").setAllowedOrigins("*");
    }

    @Bean
    @Nullable
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setThreadNamePrefix("SockJS-");
        threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolScheduler;
    }
}
