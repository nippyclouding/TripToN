package TripToN.TripToN.service.responseService.chatGPTService;


import TripToN.TripToN.domain.Concern;
import TripToN.TripToN.service.responseService.DefaultService;
import TripToN.TripToN.service.responseService.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ChatGptService implements ResponseService {

    private final WebClient chatGptWebClient;

    @Override
    public String response(Concern concern) {
        String concernText = concern.getConcern();
        String userName = concern.getUserName();

        try {
            String prompt = createPrompt(concernText, userName);

            ChatGptRequest request = new ChatGptRequest(
                    "gpt-5-nano",
                    List.of(new ChatGptRequest.Message("user", prompt)),
                    150,
                    0.7
            );

            Mono<ChatGptResponse> responseMono = chatGptWebClient
                    .post()
                    .uri("/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatGptResponse.class);

            ChatGptResponse response = responseMono.block(); // 동기적으로 처리


            //검증 성공 시 ai 응답 리턴
            if (response != null &&
                    response.getChoices() != null &&
                    !response.getChoices().isEmpty()) {

                String aiResponse = response.getChoices().get(0).getMessage().getContent();
                log.info("ChatGPT response generated for user: {}", userName);
                return aiResponse.trim();
            }

            //검증 실패 시 기본 응답 리턴
            return new DefaultService().response(concern);

        } catch (Exception e) {
            //예외 발생 시 기본 응답 리턴
            log.error("Error calling ChatGPT API: ", e);
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