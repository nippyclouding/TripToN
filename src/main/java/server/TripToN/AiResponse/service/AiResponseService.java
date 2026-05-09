package server.TripToN.AiResponse.service;

import server.TripToN.AiResponse.client.GeminiApiException;
import server.TripToN.AiResponse.client.GeminiClient;
import server.TripToN.AiResponse.entity.AiResponse;
import server.TripToN.AiResponse.entity.AiRequestLog;
import server.TripToN.AiResponse.repository.AiRequestLogRepository;
import server.TripToN.AiResponse.repository.AiResponseRepository;
import server.TripToN.concern.entity.Concern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiResponseService {

    private final AiResponseRepository aiResponseRepository;
    private final AiRequestLogRepository aiRequestLogRepository;
    private final GeminiClient geminiClient;

    @Transactional
    public AiResponse getResponse(Concern concern) {
        String prompt = buildPrompt(concern);

        try {
            String aiText = geminiClient.generate(prompt);
            if (aiText == null) {
                saveLog(concern, false, "GEMINI_RESPONSE_EMPTY");
                return null;
            }

            // null이 아닌 정상 응답이라면
            AiResponse aiResponse = AiResponse.builder()
                    .concern(concern)
                    .responseContent(aiText)
                    .build();

            AiResponse saved = aiResponseRepository.save(aiResponse);
            saveLog(concern, true, null);
            return saved;
        } catch (GeminiApiException e) {
            saveLog(concern, false, e.getCode());
            return null;
        }
    }

    private void saveLog(Concern concern, boolean requestStatus, String failureReason) {
        aiRequestLogRepository.save(
                AiRequestLog.builder()
                        .requestStatus(requestStatus)
                        .requestFailureReason(failureReason)
                        .requestMemberId(concern.getMember().getMemberId())
                        .requestMemberNickname(concern.getMember().getMemberNickname())
                        .build()
        );
    }

    private String buildPrompt(Concern concern) {
        return String.format(
                "당신은 여행자의 고민을 들어주는 전문 상담사입니다.\n\n" +
                        "고민 제목: %s\n" +
                        "고민 내용: %s\n\n" +
                        "위 고민에 대해 공감하며 실질적인 조언을 300자 이내로 해주세요.",
                concern.getConcernTitle(),
                concern.getConcernContent()
        );
    }
}
