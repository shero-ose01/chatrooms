package com.example.chatrooms.config;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); // TODO: set to correct port once live
    }

    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/rooms");
        registry.setApplicationDestinationPrefixes("/send");
    }
}
