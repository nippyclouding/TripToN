package TripToN.TripToN.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LuggageTest {

    private Luggage createCompleteLuggage() {
        Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.LUGGAGE);
        luggage.assignResponse("조언");
        return luggage;
    }

    @Nested
    @DisplayName("Luggage 생성")
    class Create {

        @Test
        @DisplayName("생성 시 dateTime이 현재 시각으로 자동 설정된다")
        void dateTimeIsSetToNow() {
            // given
            LocalDateTime before = LocalDateTime.now();

            // when
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.LUGGAGE);
            luggage.assignResponse("조언");

            // then
            assertThat(luggage.getDateTime()).isNotNull();
            assertThat(luggage.getDateTime()).isAfterOrEqualTo(before);
            assertThat(luggage.getDateTime()).isBeforeOrEqualTo(LocalDateTime.now());
        }

        @Test
        @DisplayName("concern과 luggageType이 올바르게 설정된다")
        void fieldsAreSetCorrectly() {
            // given & when
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.CART);

            // then
            assertThat(luggage.getConcern()).isNotNull();
            assertThat(luggage.getConcern().getUserName()).isEqualTo("홍길동");
            assertThat(luggage.getConcern().getConcern()).isEqualTo("취업 고민");
            assertThat(luggage.getLuggageType()).isEqualTo(LuggageType.CART);
        }

        @Test
        @DisplayName("LID는 생성 직후 null이다 (DB에서 자동 생성)")
        void lidIsNullBeforePersist() {
            // given & when
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.BAG);

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
            Luggage luggage = createCompleteLuggage();

            // when
            boolean result = luggage.isComplete();

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("response가 null이면 false")
        void falseWhenResponseIsNull() {
            // given
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.CART);

            // when
            boolean result = luggage.isComplete();

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("concern이 null이면 false (via reflection)")
        void falseWhenConcernIsNull() throws Exception {
            // given - use reflection to create a Luggage with null concern
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.LUGGAGE);
            java.lang.reflect.Field concernField = Luggage.class.getDeclaredField("concern");
            concernField.setAccessible(true);
            concernField.set(luggage, null);

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
