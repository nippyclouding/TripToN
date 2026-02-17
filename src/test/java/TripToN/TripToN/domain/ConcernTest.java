package TripToN.TripToN.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConcernTest {

    @Nested
    @DisplayName("Concern 생성")
    class Create {

        @Test
        @DisplayName("생성 시 비밀번호가 BCrypt로 해싱된다")
        void passwordIsHashedOnCreation() {
            // given
            String rawPassword = "1234";

            // when
            Concern concern = new Concern("홍길동", "취업 고민", rawPassword);

            // then
            assertThat(concern.getPassword()).isNotEqualTo(rawPassword);
            assertThat(concern.getPassword()).startsWith("$2a$");
            assertThat(concern.getPassword()).hasSize(60);
        }

        @Test
        @DisplayName("동일한 비밀번호로 생성해도 매번 다른 해시가 생성된다")
        void samePasswordProducesDifferentHash() {
            // given
            String rawPassword = "1234";

            // when
            Concern concern1 = new Concern("홍길동", "고민1", rawPassword);
            Concern concern2 = new Concern("김철수", "고민2", rawPassword);

            // then
            assertThat(concern1.getPassword()).isNotEqualTo(concern2.getPassword());
        }

        @Test
        @DisplayName("userName, concern 필드가 올바르게 설정된다")
        void fieldsAreSetCorrectly() {
            // given & when
            Concern concern = new Concern("홍길동", "취업 고민", "1234");

            // then
            assertThat(concern.getUserName()).isEqualTo("홍길동");
            assertThat(concern.getConcern()).isEqualTo("취업 고민");
            assertThat(concern.getResponse()).isNull();
        }
    }

    @Nested
    @DisplayName("비밀번호 검증 - matchPassword()")
    class MatchPassword {

        @Test
        @DisplayName("올바른 비밀번호로 매칭 시 true 반환")
        void matchesCorrectPassword() {
            // given
            Concern concern = new Concern("홍길동", "취업 고민", "1234");

            // when
            boolean result = concern.matchPassword("1234");

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("잘못된 비밀번호로 매칭 시 false 반환")
        void rejectsWrongPassword() {
            // given
            Concern concern = new Concern("홍길동", "취업 고민", "1234");

            // when
            boolean result = concern.matchPassword("5678");

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("빈 문자열 비밀번호로 매칭 시 false 반환")
        void rejectsEmptyPassword() {
            // given
            Concern concern = new Concern("홍길동", "취업 고민", "1234");

            // when
            boolean result = concern.matchPassword("");

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("응답(response) 관리")
    class Response {

        @Test
        @DisplayName("response를 설정하고 조회할 수 있다")
        void setAndGetResponse() {
            // given
            Concern concern = new Concern("홍길동", "취업 고민", "1234");

            // when
            concern.assignResponse("따뜻한 조언입니다.");

            // then
            assertThat(concern.getResponse()).isEqualTo("따뜻한 조언입니다.");
        }

        @Test
        @DisplayName("response 초기값은 null이다")
        void responseIsInitiallyNull() {
            // given & when
            Concern concern = new Concern("홍길동", "취업 고민", "1234");

            // then
            assertThat(concern.getResponse()).isNull();
        }
    }
}
