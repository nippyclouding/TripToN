package TripToN.TripToN.service.responseService;

import TripToN.TripToN.config.dto.GeminiRequest;
import TripToN.TripToN.config.dto.GeminiResponse;
import TripToN.TripToN.domain.Concern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GeminiService implements ResponseService {

    private final RestClient geminiRestClient;
    private final String apiKey;

    @Override
    public String response(Concern concern) {
        String concernText = concern.getConcern();
        String userName = concern.getUserName();

        try {
            log.info("Gemini API call started - User: {}", userName);
            String prompt = createPrompt(concernText, userName);

            GeminiRequest request = new GeminiRequest(
                    List.of(new GeminiRequest.Content(
                            List.of(new GeminiRequest.Part(prompt))
                    ))
            );

            GeminiResponse response = geminiRestClient.post()
                    .uri("/models/gemini-2.5-flash-lite:generateContent?key={key}", apiKey)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

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

            log.warn("Gemini API returned empty response, falling back to default");
            return new DefaultService().response(concern);

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Gemini API client error ({}): {}", e.getStatusCode(), e.getMessage());
            return new DefaultService().response(concern);
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            log.error("Gemini API server error ({}): {}", e.getStatusCode(), e.getMessage());
            return new DefaultService().response(concern);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("Gemini API network error: {}", e.getMessage());
            return new DefaultService().response(concern);
        } catch (Exception e) {
            log.error("Unexpected error calling Gemini API: {}", e.getMessage(), e);
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