package TripToN.TripToN.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LuggageTest {

    private Concern createConcernWithResponse() {
        Concern concern = new Concern("홍길동", "취업 고민", "1234");
        concern.assignResponse("조언");
        return concern;
    }

    @Nested
    @DisplayName("Luggage 생성")
    class Create {

        @Test
        @DisplayName("생성 시 dateTime이 현재 시각으로 자동 설정된다")
        void dateTimeIsSetToNow() {
            // given
            LocalDateTime before = LocalDateTime.now();
            Concern concern = createConcernWithResponse();

            // when
            Luggage luggage = new Luggage(concern, LuggageType.LUGGAGE);

            // then
            assertThat(luggage.getDateTime()).isNotNull();
            assertThat(luggage.getDateTime()).isAfterOrEqualTo(before);
            assertThat(luggage.getDateTime()).isBeforeOrEqualTo(LocalDateTime.now());
        }

        @Test
        @DisplayName("concern과 luggageType이 올바르게 설정된다")
        void fieldsAreSetCorrectly() {
            // given
            Concern concern = createConcernWithResponse();

            // when
            Luggage luggage = new Luggage(concern, LuggageType.CART);

            // then
            assertThat(luggage.getConcern()).isEqualTo(concern);
            assertThat(luggage.getLuggageType()).isEqualTo(LuggageType.CART);
        }

        @Test
        @DisplayName("LID는 생성 직후 null이다 (DB에서 자동 생성)")
        void lidIsNullBeforePersist() {
            // given & when
            Luggage luggage = new Luggage(createConcernWithResponse(), LuggageType.BAG);

            // then
            assertThat(luggage.getLID()).isNull();
        }
    }

    @Nested
    @DisplayName("isComplete() 검증")
    class IsComplete {

        @Test
        @DisplayName("concern과 response가 모두 존재하면 true")
        void trueWhenConcernAndResponseExist() {
            // given
            Luggage luggage = new Luggage(createConcernWithResponse(), LuggageType.BAG);

            // when
            boolean result = luggage.isComplete();

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("response가 null이면 false")
        void falseWhenResponseIsNull() {
            // given
            Concern concern = new Concern("홍길동", "취업 고민", "1234");
            Luggage luggage = new Luggage(concern, LuggageType.CART);

            // when
            boolean result = luggage.isComplete();

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("concern이 null이면 false")
        void falseWhenConcernIsNull() {
            // given
            Luggage luggage = new Luggage(null, LuggageType.LUGGAGE);

            // when
            boolean result = luggage.isComplete();

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("LuggageType enum")
    class LuggageTypeTest {

        @Test
        @DisplayName("3가지 타입이 정의되어 있다")
        void hasThreeTypes() {
            assertThat(LuggageType.values()).hasSize(3);
            assertThat(LuggageType.values())
                    .containsExactly(LuggageType.LUGGAGE, LuggageType.CART, LuggageType.BAG);
        }
    }
}
