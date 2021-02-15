package com.mps.blockchain.client.http;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.utils.StringUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    
    private static final Logger lOGGER = LoggerFactory.getLogger(HttpClient.class);
    
    private final OkHttpClient client;
    
    private URL queueBackendUrl;
    
    public HttpClient() {
        this.client = new OkHttpClient();
    }
    
    public HttpClient(OkHttpClient httpClient) {
        this.client = httpClient;
    }
    
    public void setURL(URL url) {
        this.queueBackendUrl = url;
    }
    
    public void sendUpdate(BlockchainOperationQueueResponse response) {
        
        // json formatted data
        String jsonString = StringUtils.toPrettyString(response);
        
        // json request body
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));
        
        Request request = new Request.Builder().url(queueBackendUrl).addHeader("User-Agent", "OkHttp Bot").put(body)
                .build();
        
        try (Response httpResponse = client.newCall(request).execute()) {
            
            if (!httpResponse.isSuccessful()) {
                String message = String.format("Unexpected code from callback URL (%s): %s", httpResponse.code(),
                        response);
                lOGGER.error(message);
                throw new IOException(message);
            }
            
            // Get response body
            if (httpResponse.body() != null) {
                if (lOGGER.isInfoEnabled()) {
                    lOGGER.info(String.format("Response: %S", httpResponse.body().string()));
                }
            } else {
                lOGGER.info("no response body");
            }
        } catch (Exception e) {
            lOGGER.error(StringUtils.toString(e));
        }
    }
}
