package TripToN.TripToN.service.responseService;

import TripToN.TripToN.config.dto.GeminiRequest;
import TripToN.TripToN.config.dto.GeminiResponse;
import TripToN.TripToN.domain.Concern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private GeminiService geminiService;

    @BeforeEach
    void setUp() {
        geminiService = new GeminiService(restClient, "test-api-key");
    }

    private void setupMockChain() {
        given(restClient.post()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.uri(anyString(), any(Object[].class))).willReturn(requestBodySpec);
        given(requestBodySpec.body(any(GeminiRequest.class))).willReturn(requestBodySpec);
        given(requestBodySpec.retrieve()).willReturn(responseSpec);
    }

    private GeminiResponse createSuccessResponse(String text) {
        GeminiResponse.Part part = new GeminiResponse.Part();
        part.setText(text);
        GeminiResponse.Content content = new GeminiResponse.Content();
        content.setParts(List.of(part));
        GeminiResponse.Candidate candidate = new GeminiResponse.Candidate();
        candidate.setContent(content);
        GeminiResponse response = new GeminiResponse();
        response.setCandidates(List.of(candidate));
        return response;
    }

    private Concern createTestConcern(String text) {
        return new Concern("홍길동", text, "1234");
    }

    @Nested
    @DisplayName("API 정상 응답")
    class SuccessResponse {

        @Test
        @DisplayName("Gemini API가 정상 응답하면 AI 응답 텍스트를 반환한다")
        void returnsAiResponse() {
            // given
            setupMockChain();
            GeminiResponse geminiResponse = createSuccessResponse("  AI 조언입니다.  ");
            given(responseSpec.body(GeminiResponse.class)).willReturn(geminiResponse);
            Concern concern = createTestConcern("취업 고민");

            // when
            String result = geminiService.response(concern);

            // then
            assertThat(result).isEqualTo("AI 조언입니다.");
        }

        @Test
        @DisplayName("응답 텍스트의 앞뒤 공백이 제거된다")
        void trimsResponseText() {
            // given
            setupMockChain();
            GeminiResponse geminiResponse = createSuccessResponse("\n  응답 텍스트  \n");
            given(responseSpec.body(GeminiResponse.class)).willReturn(geminiResponse);

            // when
            String result = geminiService.response(createTestConcern("고민"));

            // then
            assertThat(result).isEqualTo("응답 텍스트");
        }
    }

    @Nested
    @DisplayName("API 응답 실패 시 DefaultService fallback")
    class FallbackToDefault {

        @Test
        @DisplayName("API가 null을 반환하면 DefaultService로 fallback한다")
        void fallbackOnNullResponse() {
            // given
            setupMockChain();
            given(responseSpec.body(GeminiResponse.class)).willReturn(null);

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
            assertThat(result).contains("고민");
        }

        @Test
        @DisplayName("candidates가 빈 리스트이면 DefaultService로 fallback한다")
        void fallbackOnEmptyCandidates() {
            // given
            setupMockChain();
            GeminiResponse response = new GeminiResponse();
            response.setCandidates(Collections.emptyList());
            given(responseSpec.body(GeminiResponse.class)).willReturn(response);

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }

        @Test
        @DisplayName("candidates가 null이면 DefaultService로 fallback한다")
        void fallbackOnNullCandidates() {
            // given
            setupMockChain();
            GeminiResponse response = new GeminiResponse();
            response.setCandidates(null);
            given(responseSpec.body(GeminiResponse.class)).willReturn(response);

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }
    }

    @Nested
    @DisplayName("API 예외 발생 시 DefaultService fallback")
    class ExceptionFallback {

        @Test
        @DisplayName("401 Unauthorized 에러 시 DefaultService로 fallback한다")
        void fallbackOnUnauthorized() {
            // given
            setupMockChain();
            given(requestBodySpec.retrieve()).willThrow(
                    HttpClientErrorException.create(
                            HttpStatus.UNAUTHORIZED, "Unauthorized",
                            HttpHeaders.EMPTY, new byte[0], null));

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }

        @Test
        @DisplayName("429 Rate Limit 에러 시 DefaultService로 fallback한다")
        void fallbackOnRateLimit() {
            // given
            setupMockChain();
            given(requestBodySpec.retrieve()).willThrow(
                    HttpClientErrorException.create(
                            HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests",
                            HttpHeaders.EMPTY, new byte[0], null));

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }

        @Test
        @DisplayName("500 Server Error 시 DefaultService로 fallback한다")
        void fallbackOnServerError() {
            // given
            setupMockChain();
            given(requestBodySpec.retrieve()).willThrow(
                    HttpServerErrorException.create(
                            HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                            HttpHeaders.EMPTY, new byte[0], null));

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }

        @Test
        @DisplayName("네트워크 연결 실패 시 DefaultService로 fallback한다")
        void fallbackOnNetworkError() {
            // given
            setupMockChain();
            given(requestBodySpec.retrieve()).willThrow(
                    new ResourceAccessException("Connection refused"));

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }

        @Test
        @DisplayName("예상치 못한 예외 발생 시에도 DefaultService로 fallback한다")
        void fallbackOnUnexpectedException() {
            // given
            setupMockChain();
            given(requestBodySpec.retrieve()).willThrow(
                    new RuntimeException("Unexpected error"));

            // when
            String result = geminiService.response(createTestConcern("짧은 고민"));

            // then
            assertThat(result).isNotBlank();
        }
    }
}
