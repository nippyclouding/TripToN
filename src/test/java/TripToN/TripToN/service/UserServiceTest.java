package TripToN.TripToN.service;

import TripToN.TripToN.domain.Luggage;
import TripToN.TripToN.domain.LuggageType;
import TripToN.TripToN.service.responseService.DefaultService;
import TripToN.TripToN.service.responseService.ResponseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 흐름 통합 테스트 (단위)")
class UserServiceTest {

    @Test
    @DisplayName("전체 흐름: 사용자 입력 → Luggage 생성 → 응답 생성 → 응답 할당")
    void fullUserFlow() {
        // given
        String userName = "홍길동";
        String concernText = "취업이 안 돼서 고민입니다";
        String password = "1234";

        // when - Luggage 생성 (Aggregate Root가 Concern 생성을 관장)
        Luggage luggage = Luggage.create(userName, concernText, password, LuggageType.LUGGAGE);
        ResponseService responseService = new DefaultService();
        String response = responseService.response(luggage.getConcern());
        luggage.assignResponse(response);

        // then - Concern 검증
        assertThat(luggage.getConcern().getUserName()).isEqualTo(userName);
        assertThat(luggage.matchPassword(password)).isTrue();
        assertThat(luggage.getConcern().getResponse()).isNotBlank();

        // then - Luggage 검증
        assertThat(luggage.isComplete()).isTrue();
        assertThat(luggage.getDateTime()).isNotNull();
        assertThat(luggage.getLuggageType()).isEqualTo(LuggageType.LUGGAGE);
    }

    @Test
    @DisplayName("응답 없이 Luggage 생성 시 isComplete가 false이다")
    void luggageWithoutResponseIsIncomplete() {
        // given & when
        Luggage luggage = Luggage.create("홍길동", "고민", "1234", LuggageType.BAG);

        // then
        assertThat(luggage.isComplete()).isFalse();
    }
}
