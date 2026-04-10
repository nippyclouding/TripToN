package server.TripToN.AiResponse.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class GeminiClient {

    private final WebClient webClient;
    private final String apiKey;

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com";
    private static final String MODEL    = "gemini-1.5-flash";

    public GeminiClient(WebClient.Builder builder,
                        @Value("${gemini.api.key}") String apiKey) {
        this.webClient = builder.baseUrl(BASE_URL).build();
        this.apiKey    = apiKey;
    }

    public String generate(String prompt) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );

        GeminiResponse response = webClient.post()
                .uri("/v1beta/models/" + MODEL + ":generateContent?key=" +
                        apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block(); // 동기 처리

        return response != null ? response.extractText() : null;
    }
}