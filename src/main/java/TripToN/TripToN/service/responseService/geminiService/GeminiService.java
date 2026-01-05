package TripToN.TripToN.service.responseService.geminiService;



import TripToN.TripToN.domain.Concern;
import TripToN.TripToN.service.responseService.DefaultService;
import TripToN.TripToN.service.responseService.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GeminiService implements ResponseService {

    private final WebClient geminiWebClient;
    private final String apiKey;

    @Override
    public String response(Concern concern) {
        String concernText = concern.getConcern();
        String userName = concern.getUserName();

        try {
            log.info("=== Gemini API Call Started ===");
            log.info("API Key length: {}", apiKey != null ? apiKey.length() : "NULL");
            log.info("API Key first 10 chars: {}", apiKey != null && apiKey.length() >= 10 ? apiKey.substring(0, 10) : "N/A");
            log.info("User: {}, Concern: {}", userName, concernText);
            String prompt = createPrompt(concernText, userName);

            GeminiRequest request = new GeminiRequest(
                    List.of(new GeminiRequest.Content(
                            List.of(new GeminiRequest.Part(prompt))
                    ))
            );

            Mono<GeminiResponse> responseMono = geminiWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/models/gemini-2.5-flash-lite:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class);

            GeminiResponse response = responseMono.block();

            log.info("Received response from Gemini API");
            log.info("Response is null: {}", response == null);

            // 검증 성공 시 AI 응답 리턴
            if (response != null &&
                    response.getCandidates() != null &&
                    !response.getCandidates().isEmpty()) {

                String aiResponse = response.getCandidates().get(0)
                        .getContent()
                        .getParts()
                        .get(0)
                        .getText();

                log.info("Gemini response generated for user: {}", userName);
                return aiResponse.trim();
            }

            // 검증 실패 시 기본 응답 리턴
            return new DefaultService().response(concern);

        } catch (Exception e) {
            // 예외 발생 시 기본 응답 리턴
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            return new DefaultService().response(concern);
        }
    }

    private String createPrompt(String concernText, String userName) {
        return String.format(
                "당신은 사람들의 고민 상담사입니다. %s님이 '다음과 같은 고민을 가지고 있습니다: '%s'. " +
                        "이 고민에 대해 따뜻하고 공감적이며 실용적인 조언을 2-3문장으로 제공해주세요. " +
                        "답변은 한국어로 하고, 격려와 구체적인 해결방안을 포함해주세요. 응답 결과에 사용자의 고민 크기를 반영하여 반드시 한글로 300자 안으로 되게 해주세요." +
                        "만약 입력이 고민이 아니라 다른 대화라면 적절히 응답해주세요.",
                userName, concernText
        );
    }
}