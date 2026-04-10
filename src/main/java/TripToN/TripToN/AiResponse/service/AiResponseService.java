package TripToN.TripToN.AiResponse.service;

import TripToN.TripToN.AiResponse.client.GeminiClient;
import TripToN.TripToN.AiResponse.entity.AiResponse;
import TripToN.TripToN.AiResponse.repository.AiResponseRepository;
import TripToN.TripToN.concern.entity.Concern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiResponseService {

    private final AiResponseRepository aiResponseRepository;
    private final GeminiClient geminiClient;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AiResponse getResponse(Concern concern) {
        // 1. 대답 얻기
        String prompt = buildPrompt(concern);           // 프롬프트 생성
        String aiText = geminiClient.generate(prompt);  // 프롬프트 전달
        if (aiText == null) return null;

        // 2. 대답 저장
        AiResponse aiResponse = AiResponse.builder()
                .concern(concern)
                .responseContent(aiText)
                .build();

        // 3. 대답 리턴
        return aiResponseRepository.save(aiResponse);
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
