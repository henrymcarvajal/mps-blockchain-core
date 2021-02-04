package com.mps.blockchain.client.http;

import java.io.IOException;
import java.net.URL;

import com.mps.blockchain.commons.queue.messages.BlockchainOperationQueueResponse;
import com.mps.blockchain.utils.StringUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {

    private final OkHttpClient httpClient;
    
    private URL queueBackendUrl;
    
    public HttpClient() {
        this.httpClient = new OkHttpClient();
    }
    
    public HttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    public void setURL(URL url) {
        this.queueBackendUrl = url;
    }

    public void sendUpdate(BlockchainOperationQueueResponse response) {

        // json formatted data
        String jsonString = StringUtils.toPrettyString(response);

        // json request body
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder().url(queueBackendUrl).addHeader("User-Agent", "OkHttp Bot").put(body).build();

        try (Response httpResponse = httpClient.newCall(request).execute()) {

            if (!httpResponse.isSuccessful())
                throw new IOException(String.format("Unexpected code from callback URL (%s): %s", httpResponse.code(), response));

            // Get response body
            if (httpResponse.body() != null) {
                System.out.println(httpResponse.body().string());
            } else {
                System.out.println("no response body");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
