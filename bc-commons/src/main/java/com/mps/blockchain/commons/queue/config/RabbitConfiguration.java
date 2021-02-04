package com.mps.blockchain.commons.queue.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    
    private final String BLOCKCHAIN_QUEUE_NAME = "BLOCKCHAIN_QUEUE_NAME";
    private static final String BLOCKCHAIN_QUEUE_SERVICE_URL = "BLOCKCHAIN_QUEUE_SERVICE_URL";
    
    @Bean
    public ConnectionFactory connectionFactory() {
        
        final URI ampqUrl;
        try {
            ampqUrl = new URI(getEnv(BLOCKCHAIN_QUEUE_SERVICE_URL));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(ampqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(ampqUrl.getUserInfo().split(":")[1]);
        factory.setHost(ampqUrl.getHost());
        factory.setPort(ampqUrl.getPort());
        factory.setVirtualHost(ampqUrl.getPath().substring(1));
        
        return factory;
    }
    
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(getEnv(BLOCKCHAIN_QUEUE_NAME));
        template.setDefaultReceiveQueue(getEnv(BLOCKCHAIN_QUEUE_NAME));
        return template;
    }
    
    @Bean
    public Queue queue() {
        return new Queue(getEnv(BLOCKCHAIN_QUEUE_NAME));
    }
    
    private String getEnv(String name) {
        return CommonConfiguration.getEnvOrThrow(name);
    }
}
