package com.mps.blockchain.client.http.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mps.blockchain.client.http.HttpClient;
import com.mps.blockchain.commons.queue.config.CommonConfiguration;

import okhttp3.OkHttpClient;

@Configuration
public class HttpClientConfiguration {
    
    private static final String BLOCKCHAIN_QUEUE_CALLBACK_URL = "BLOCKCHAIN_QUEUE_CALLBACK_URL";
    
    @Bean
    public HttpClient HttpClient() throws MalformedURLException {
        String queueBackendUrl = CommonConfiguration.getEnvOrThrow(BLOCKCHAIN_QUEUE_CALLBACK_URL);
        HttpClient httpClient = new HttpClient(new OkHttpClient());
        httpClient.setURL(new URL(queueBackendUrl));
        return httpClient;
    }
}
