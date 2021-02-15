package com.mps.blockchain.queue.worker;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mps.blockchain.client.http.HttpClient;
import com.mps.blockchain.client.http.config.HttpClientConfiguration;
import com.mps.blockchain.commons.operations.OperationResult;
import com.mps.blockchain.commons.queue.config.RabbitConfiguration;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueRequest;
import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.operations.Operation;
import com.mps.blockchain.operations.OperationManager;

/**
 * Worker for receiving and processing blockchain operations asynchronously.
 */
public class BlockchainOperationWorker {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainOperationWorker.class);
    
    public static void main(String[] args) {
        
        final OperationManager blockchainManager = new OperationManager();
        blockchainManager.loadOperations();
        
        @SuppressWarnings("resource")
        final ApplicationContext httpClientConfig = new AnnotationConfigApplicationContext(
                HttpClientConfiguration.class);
        final HttpClient client = httpClientConfig.getBean(HttpClient.class);
        
        @SuppressWarnings("resource")
        final ApplicationContext rabbitConfig = new AnnotationConfigApplicationContext(RabbitConfiguration.class);
        final ConnectionFactory rabbitConnectionFactory = rabbitConfig.getBean(ConnectionFactory.class);
        final Queue rabbitQueue = rabbitConfig.getBean(Queue.class);
        final MessageConverter messageConverter = new SimpleMessageConverter();
        
        // create a listener container, which is required for asynchronous message
        // consumption.
        // AmqpTemplate cannot be used in this case
        final SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(rabbitConnectionFactory);
        listenerContainer.setQueueNames(rabbitQueue.getName());
        
        // set the callback for message handling
        listenerContainer.setMessageListener(message -> {
            
            final BlockchainOperationQueueRequest blockchainOperationMessage = (BlockchainOperationQueueRequest) messageConverter
                    .fromMessage(message);
            LOGGER.info(String.format("Received from RabbitMQ: %s", blockchainOperationMessage));
            
            if (blockchainOperationMessage.getName() != null) {
                
                Optional<Operation> blockchainOperationOptional = blockchainManager
                        .getOperation(blockchainOperationMessage.getName());
                
                if (blockchainOperationOptional.isPresent()) {
                    LOGGER.info(String.format("Operation present: %s", blockchainOperationMessage.getName()));
                    Operation blockchainOperation = blockchainOperationOptional.get();
                    if (blockchainOperationMessage.getPayload() != null
                            && !blockchainOperationMessage.getPayload().isEmpty()) {
                        blockchainOperation.setInputs(blockchainOperationMessage.getPayload());
                        Map<String, String> outputs = new HashMap<>();
                        
                        LOGGER.info("Executing operation... ");
                        OperationResult result = blockchainOperation.execute(outputs);
                        LOGGER.info(result.getValue());
                        
                        LOGGER.info("Sending results back... ");
                        BlockchainOperationQueueResponse response = new BlockchainOperationQueueResponse();
                        response.setOperationId(blockchainOperationMessage.getOperationId());
                        response.setDate(LocalDateTime.now());
                        response.setResult(result);
                        response.setOutputs(outputs);
                        client.sendUpdate(response);
                        
                    } else {
                        LOGGER.info("No payload to process");
                    }
                } else {
                    LOGGER.info(String.format("No operation with name '%s'", blockchainOperationMessage.getName()));
                }
            } else {
                LOGGER.info("No name present");
            }
            LOGGER.info("Message processed");
        });
        
        // set a simple error handler
        listenerContainer.setErrorHandler(Throwable::printStackTrace);
        
        // register a shutdown hook with the JVM
        Runtime.getRuntime().addShutdownHook(new Thread() {
            
            @Override
            public void run() {
                LOGGER.info(null);
                LOGGER.info(String.format("Shutting down message listener at %s... ", LocalDateTime.now()));
                listenerContainer.shutdown();
            }
        });
        
        // start up the listener. this will block until JVM is killed.
        listenerContainer.start();
        LOGGER.info("Message listener started at {}", LocalDateTime.now());
    }
}
