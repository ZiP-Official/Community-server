package com.zip.community.common.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Profile("prod")
public class RabbitMQWebSocketConfig implements WebSocketMessageBrokerConfigurer {
}
