package com.mps.blockchain.queue.worker;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ErrorHandler;

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
    
    public static void main(String[] args) {
        
        final OperationManager blockchainManager = new OperationManager();
        blockchainManager.loadOperations();
       
        @SuppressWarnings("resource")
        final ApplicationContext httpClientConfig = new AnnotationConfigApplicationContext(HttpClientConfiguration.class);
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
        listenerContainer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                final BlockchainOperationQueueRequest blockchainOperationMessage = (BlockchainOperationQueueRequest) messageConverter
                        .fromMessage(message);
                System.out.println("Received from RabbitMQ: " + blockchainOperationMessage);
                
                if (blockchainOperationMessage.getName() != null) {
                    
                    Optional<Operation> blockchainOperationOptional = blockchainManager
                            .getOperation(blockchainOperationMessage.getName());
                    
                    if (blockchainOperationOptional.isPresent()) {
                        System.out.println("Operation present: " + blockchainOperationMessage.getName());
                        Operation blockchainOperation = blockchainOperationOptional.get();
                        if (blockchainOperationMessage.getPayload() != null
                                && !blockchainOperationMessage.getPayload().isEmpty()) {
                            blockchainOperation.setInputs(blockchainOperationMessage.getPayload());
                            Map<String, String> outputs = new HashMap<>();
                            System.out.println("Executing operation... ");
                            OperationResult result = blockchainOperation.execute(outputs);
                            
                            System.out.println("Sending results back... ");                            
                            BlockchainOperationQueueResponse response = new BlockchainOperationQueueResponse();
                            response.setOperationId(blockchainOperationMessage.getOperationId());
                            response.setDate(LocalDateTime.now());
                            response.setResult(result);
                            response.setOutputs(outputs);
                            client.sendUpdate(response);
                            
                        } else {
                            System.out.println("No payload to process");
                        }
                    } else {
                        System.out.println("No operation with name '" + blockchainOperationMessage.getName() + "'");
                    }
                } else {
                    System.out.println("No name present");
                }
                System.out.println("Message processed");
            }
        });
        
        // set a simple error handler
        listenerContainer.setErrorHandler(new ErrorHandler() {
            public void handleError(Throwable t) {
                t.printStackTrace();
            }
        });
        
        // register a shutdown hook with the JVM
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println();
                System.out.println(String.format("Shutting down message listener at %s... ", LocalDateTime.now()));
                listenerContainer.shutdown();
            }
        });
        
        // start up the listener. this will block until JVM is killed.
        listenerContainer.start();
        System.out.println(String.format("Message listener started at %s", LocalDateTime.now()));
    }
}
