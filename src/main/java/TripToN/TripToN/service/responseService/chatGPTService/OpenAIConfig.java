package TripToN.TripToN.service.responseService.chatGPTService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = false)
public class OpenAIConfig {
    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public WebClient chatGptWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
