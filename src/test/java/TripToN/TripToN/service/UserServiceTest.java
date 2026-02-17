package TripToN.TripToN.service;

import TripToN.TripToN.domain.Concern;
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
    @DisplayName("전체 흐름: 사용자 입력 → Concern 생성 → 응답 생성 → Luggage 생성")
    void fullUserFlow() {
        // given
        String userName = "홍길동";
        String concernText = "취업이 안 돼서 고민입니다";
        String password = "1234";

        // when - Concern 생성 및 응답 할당
        Concern concern = new Concern(userName, concernText, password);
        ResponseService responseService = new DefaultService();
        String response = responseService.response(concern);
        concern.setResponse(response);

        // then - Concern 검증
        assertThat(concern.getUserName()).isEqualTo(userName);
        assertThat(concern.matchPassword(password)).isTrue();
        assertThat(concern.getResponse()).isNotBlank();

        // when - Luggage 생성
        Luggage luggage = new Luggage(concern, LuggageType.LUGGAGE);

        // then - Luggage 검증
        assertThat(luggage.isComplete()).isTrue();
        assertThat(luggage.getDateTime()).isNotNull();
        assertThat(luggage.getLuggageType()).isEqualTo(LuggageType.LUGGAGE);
    }

    @Test
    @DisplayName("응답 없이 Luggage 생성 시 isComplete가 false이다")
    void luggageWithoutResponseIsIncomplete() {
        // given
        Concern concern = new Concern("홍길동", "고민", "1234");

        // when
        Luggage luggage = new Luggage(concern, LuggageType.BAG);

        // then
        assertThat(luggage.isComplete()).isFalse();
    }
}
