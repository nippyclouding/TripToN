package server.TripToN.AiResponse.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "gemini.enabled", havingValue = "true", matchIfMissing = false)
public class GeminiConfig {

    @Bean
    public RestClient geminiRestClient(
            @Value("${gemini.connect-timeout}") Duration connectTimeout,
            @Value("${gemini.read-timeout}") Duration readTimeout
    ) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
