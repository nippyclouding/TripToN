package TripToN.TripToN.service.responseService;

import TripToN.TripToN.service.responseService.geminiService.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ServiceConfig {

    // Default Service, 조건부로 빈 등록
    @Bean @ConditionalOnProperty(name = "gemini.enabled", havingValue = "false", matchIfMissing = true)
    public ResponseService defaultResponseService() { return new DefaultService(); }


    // Gemini Service (gemini.enabled=true 일 때만 활성화)
    @Bean
    @ConditionalOnProperty(name = "gemini.enabled", havingValue = "true")
    @Primary
    public ResponseService geminiResponseService(
            @Autowired WebClient geminiWebClient,
            @Value("${gemini.api.key}") String apiKey) {

        log.info("=== Creating GeminiService Bean ===");
        log.info("API Key provided: {}", apiKey != null && !apiKey.isEmpty() ? "YES" : "NO");
        log.info("API Key length: {}", apiKey != null ? apiKey.length() : 0);

        return new GeminiService(geminiWebClient, apiKey);
    }

}
