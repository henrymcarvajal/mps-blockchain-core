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
    
    private static final String BLOCKCHAIN_QUEUE_NAME = "BLOCKCHAIN_QUEUE_NAME";
    private static final String BLOCKCHAIN_QUEUE_SERVICE_URL = "BLOCKCHAIN_QUEUE_SERVICE_URL";
    private static String blockchainQueueName;
    
    static {
        blockchainQueueName = CommonConfiguration.getEnvOrThrow(BLOCKCHAIN_QUEUE_NAME);
    }
    
    @Bean
    public ConnectionFactory connectionFactory() throws URISyntaxException {
        
        final URI ampqUrl = new URI(CommonConfiguration.getEnvOrThrow(BLOCKCHAIN_QUEUE_SERVICE_URL));
        
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(ampqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(ampqUrl.getUserInfo().split(":")[1]);
        factory.setHost(ampqUrl.getHost());
        factory.setPort(ampqUrl.getPort());
        factory.setVirtualHost(ampqUrl.getPath().substring(1));
        
        return factory;
    }
    
    @Bean
    public AmqpAdmin amqpAdmin() throws URISyntaxException {
        return new RabbitAdmin(connectionFactory());
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate() throws URISyntaxException {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        
        template.setRoutingKey(blockchainQueueName);
        template.setDefaultReceiveQueue(blockchainQueueName);
        return template;
    }
    
    @Bean
    public Queue queue() {
        return new Queue(blockchainQueueName);
    }
}
