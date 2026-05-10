package server.TripToN.AiResponse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.TripToN.AiResponse.client.GeminiApiException;
import server.TripToN.AiResponse.client.GeminiClient;
import server.TripToN.AiResponse.entity.AiRequestLog;
import server.TripToN.AiResponse.entity.AiResponse;
import server.TripToN.AiResponse.repository.AiRequestLogRepository;
import server.TripToN.AiResponse.repository.AiResponseRepository;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.entity.LuggageType;
import server.TripToN.member.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiResponseServiceTest {

    @Mock
    private AiResponseRepository aiResponseRepository;

    @Mock
    private AiRequestLogRepository aiRequestLogRepository;

    @Mock
    private GeminiClient geminiClient;

    @InjectMocks
    private AiResponseService aiResponseService;

    @Test
    void getResponse_geminiSuccess_savesResponseAndSuccessLog() {
        // given
        Concern concern = concern();
        when(geminiClient.generate(any(String.class))).thenReturn("AI 응답입니다.");
        when(aiResponseRepository.save(any(AiResponse.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        AiResponse result = aiResponseService.getResponse(concern);

        // then
        assertThat(result.getResponseContent()).isEqualTo("AI 응답입니다.");
        ArgumentCaptor<AiRequestLog> logCaptor = ArgumentCaptor.forClass(AiRequestLog.class);
        verify(aiRequestLogRepository).save(logCaptor.capture());
        assertThat(logCaptor.getValue().getRequestStatus()).isTrue();
        assertThat(logCaptor.getValue().getRequestFailureReason()).isNull();
    }

    @Test
    void getResponse_geminiReturnsNull_savesFailureLogAndReturnsNull() {
        // given
        Concern concern = concern();
        when(geminiClient.generate(any(String.class))).thenReturn(null);

        // when
        AiResponse result = aiResponseService.getResponse(concern);

        // then
        assertThat(result).isNull();
        ArgumentCaptor<AiRequestLog> logCaptor = ArgumentCaptor.forClass(AiRequestLog.class);
        verify(aiRequestLogRepository).save(logCaptor.capture());
        assertThat(logCaptor.getValue().getRequestStatus()).isFalse();
        assertThat(logCaptor.getValue().getRequestFailureReason()).isEqualTo("GEMINI_RESPONSE_EMPTY");
    }

    @Test
    void getResponse_geminiThrowsException_savesFailureLogAndReturnsNull() {
        // given
        Concern concern = concern();
        when(geminiClient.generate(any(String.class)))
                .thenThrow(new GeminiApiException("GEMINI_RATE_LIMIT", "요청 한도 초과", null));

        // when
        AiResponse result = aiResponseService.getResponse(concern);

        // then
        assertThat(result).isNull();
        ArgumentCaptor<AiRequestLog> logCaptor = ArgumentCaptor.forClass(AiRequestLog.class);
        verify(aiRequestLogRepository).save(logCaptor.capture());
        assertThat(logCaptor.getValue().getRequestStatus()).isFalse();
        assertThat(logCaptor.getValue().getRequestFailureReason()).isEqualTo("GEMINI_RATE_LIMIT");
    }

    private Concern concern() {
        Member member = Member.builder()
                .memberId(1L)
                .memberEmail("user@tripton.com")
                .memberNickname("traveler")
                .memberLoginPassword("password")
                .build();

        return Concern.builder()
                .concernId(1L)
                .member(member)
                .concernTitle("여행 고민")
                .concernContent("어디로 갈지 모르겠어요.")
                .luggageType(LuggageType.a)
                .build();
    }
}
