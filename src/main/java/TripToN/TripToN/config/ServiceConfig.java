package TripToN.TripToN.config;

import TripToN.TripToN.service.responseService.DefaultService;
import TripToN.TripToN.service.responseService.GeminiService;
import TripToN.TripToN.service.responseService.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

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
            @Autowired RestClient geminiRestClient,
            @Value("${gemini.api.key}") String apiKey) {

        log.info("Creating GeminiService - API Key provided: {}", apiKey != null && !apiKey.isEmpty() ? "YES" : "NO");

        return new GeminiService(geminiRestClient, apiKey);
    }

}
