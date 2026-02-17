package TripToN.TripToN.service.responseService;

import TripToN.TripToN.domain.Concern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultServiceTest {

    private final DefaultService defaultService = new DefaultService();

    private Concern createConcernWithText(String text) {
        return new Concern("홍길동", text, "1234");
    }

    @Nested
    @DisplayName("고민 길이에 따른 응답 분류")
    class ResponseByLength {

        @ParameterizedTest
        @ValueSource(strings = {"짧은 고민", "한줄 고민입니다", "가벼운"})
        @DisplayName("40자 미만 고민 → '가벼운 고민' 응답")
        void shortConcernReturnsLightResponse(String text) {
            // given
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(text.length()).isLessThan(40);
            assertThat(response).contains("가벼운 고민");
            assertThat(response).isNotBlank();
        }

        @Test
        @DisplayName("40~79자 고민 → '작지 않은 고민' 응답")
        void mediumConcernReturnsMediumResponse() {
            // given
            String text = "이것은 40자가 넘는 중간 길이의 고민입니다. 어떻게 해야 할지 모르겠어요. 도움이 필요합니다.";
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(text.length()).isGreaterThanOrEqualTo(40);
            assertThat(text.length()).isLessThan(80);
            assertThat(response).contains("작지 않은 고민");
        }

        @Test
        @DisplayName("80자 이상 고민 → '큰 고민' 응답")
        void longConcernReturnsHeavyResponse() {
            // given
            String text = "이것은 매우 긴 고민입니다. 회사에서도 힘들고 집에서도 힘들고 친구들과의 관계도 어려워요. " +
                    "정말 어떻게 해야 할지 모르겠습니다. 누군가에게 이야기를 하고 싶지만 주변에 마땅한 사람이 없어요.";
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(text.length()).isGreaterThanOrEqualTo(80);
            assertThat(response).contains("큰 고민");
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValues {

        @Test
        @DisplayName("정확히 39자 → '가벼운 고민' 응답")
        void exactly39CharsIsLight() {
            // given
            String text = "a".repeat(39);
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(response).contains("가벼운 고민");
        }

        @Test
        @DisplayName("정확히 40자 → '작지 않은 고민' 응답")
        void exactly40CharsIsMedium() {
            // given
            String text = "a".repeat(40);
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(response).contains("작지 않은 고민");
        }

        @Test
        @DisplayName("정확히 79자 → '작지 않은 고민' 응답")
        void exactly79CharsIsMedium() {
            // given
            String text = "a".repeat(79);
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(response).contains("작지 않은 고민");
        }

        @Test
        @DisplayName("정확히 80자 → '큰 고민' 응답")
        void exactly80CharsIsHeavy() {
            // given
            String text = "a".repeat(80);
            Concern concern = createConcernWithText(text);

            // when
            String response = defaultService.response(concern);

            // then
            assertThat(response).contains("큰 고민");
        }
    }
}
