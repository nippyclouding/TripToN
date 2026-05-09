package server.TripToN.AiResponse.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "gemini.enabled", havingValue = "true", matchIfMissing = false)
public class GeminiClient {

    private final RestClient geminiRestClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String MODEL = "gemini-2.5-flash-lite";

    public String generate(String prompt) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );

        try {
            GeminiResponse response = geminiRestClient.post()
                    .uri("/models/{model}:generateContent?key={key}", MODEL, apiKey)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response == null) {
                log.warn("Gemini API 응답 바디가 비어있습니다.");
                return null;
            }

            String text = response.extractText();
            if (text == null || text.isBlank()) {
                log.warn("Gemini API 응답 본문이 비어있습니다.");
                return null;
            }

            return text;

        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Gemini API 요청 한도 초과 (429): {}", e.getMessage());
            throw new GeminiApiException("GEMINI_RATE_LIMIT", "Gemini API 요청 한도 초과", e);
        } catch (HttpClientErrorException e) {
            log.error("Gemini API 클라이언트 오류 ({}): {}", e.getStatusCode(), e.getMessage());
            throw new GeminiApiException("GEMINI_CLIENT_ERROR", "Gemini API 클라이언트 오류", e);
        } catch (HttpServerErrorException e) {
            log.error("Gemini API 서버 오류 ({}): {}", e.getStatusCode(), e.getMessage());
            throw new GeminiApiException("GEMINI_SERVER_ERROR", "Gemini API 서버 오류", e);
        } catch (ResourceAccessException e) {
            log.error("Gemini API 네트워크 오류: {}", e.getMessage());
            throw new GeminiApiException("GEMINI_NETWORK_ERROR", "Gemini API 네트워크 오류", e);
        } catch (Exception e) {
            log.error("Gemini API 호출 중 예외: {}", e.getMessage(), e);
            throw new GeminiApiException("GEMINI_UNKNOWN_ERROR", "Gemini API 호출 중 예외", e);
        }
    }
}
