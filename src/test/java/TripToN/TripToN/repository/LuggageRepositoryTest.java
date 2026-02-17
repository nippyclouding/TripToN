package TripToN.TripToN.repository;

import TripToN.TripToN.domain.Luggage;
import TripToN.TripToN.domain.LuggageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LuggageRepositoryTest {

    @Autowired
    private LuggageRepository luggageRepository;

    private Luggage createLuggage(String userName, String concernText, LuggageType type) {
        Luggage luggage = Luggage.create(userName, concernText, "1234", type);
        luggage.assignResponse("테스트 응답");
        return luggage;
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        @DisplayName("저장 시 ID가 자동 생성된다")
        void generatesIdOnSave() {
            // given
            Luggage luggage = createLuggage("홍길동", "취업 고민", LuggageType.LUGGAGE);

            // when
            Luggage saved = luggageRepository.save(luggage);

            // then
            assertThat(saved.getLID()).isNotNull();
            assertThat(saved.getLID()).isPositive();
        }

        @Test
        @DisplayName("저장된 데이터의 모든 필드가 올바르게 영속화된다")
        void persistsAllFields() {
            // given
            Luggage luggage = createLuggage("홍길동", "취업 고민", LuggageType.CART);

            // when
            Luggage saved = luggageRepository.save(luggage);
            Luggage found = luggageRepository.findById(saved.getLID()).orElseThrow();

            // then
            assertThat(found.getConcern().getUserName()).isEqualTo("홍길동");
            assertThat(found.getConcern().getConcern()).isEqualTo("취업 고민");
            assertThat(found.getLuggageType()).isEqualTo(LuggageType.CART);
            assertThat(found.getDateTime()).isNotNull();
        }

        @Test
        @DisplayName("비밀번호가 해싱되어 저장되고, matchPassword로 검증 가능하다")
        void passwordIsHashedAndVerifiable() {
            // given
            Luggage luggage = createLuggage("홍길동", "고민", LuggageType.LUGGAGE);

            // when
            Luggage saved = luggageRepository.save(luggage);
            Luggage found = luggageRepository.findById(saved.getLID()).orElseThrow();

            // then
            assertThat(found.getConcern().getPassword()).startsWith("$2a$");
            assertThat(found.getConcern().getPassword()).isNotEqualTo("1234");
            assertThat(found.matchPassword("1234")).isTrue();
            assertThat(found.matchPassword("wrong")).isFalse();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 Optional에 Luggage가 담겨 반환된다")
        void returnsLuggageWhenExists() {
            // given
            Luggage saved = luggageRepository.save(
                    createLuggage("홍길동", "취업 고민", LuggageType.BAG));

            // when
            Optional<Luggage> found = luggageRepository.findById(saved.getLID());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getLuggageType()).isEqualTo(LuggageType.BAG);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 Optional.empty를 반환한다")
        void returnsEmptyWhenNotExists() {
            // given - DB에 데이터 없음

            // when
            Optional<Luggage> found = luggageRepository.findById(999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("저장된 모든 Luggage를 조회한다")
        void returnsAllLuggages() {
            // given
            luggageRepository.save(createLuggage("홍길동", "고민1", LuggageType.LUGGAGE));
            luggageRepository.save(createLuggage("김철수", "고민2", LuggageType.BAG));
            luggageRepository.save(createLuggage("이영희", "고민3", LuggageType.CART));

            // when
            List<Luggage> all = luggageRepository.findAll();

            // then
            assertThat(all).hasSize(3);
        }

        @Test
        @DisplayName("데이터가 없으면 빈 리스트를 반환한다")
        void returnsEmptyListWhenNoData() {
            // given - DB에 데이터 없음

            // when
            List<Luggage> all = luggageRepository.findAll();

            // then
            assertThat(all).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Test
        @DisplayName("삭제 후 findById가 Optional.empty를 반환한다")
        void deletedEntityIsNotFound() {
            // given
            Luggage saved = luggageRepository.save(
                    createLuggage("홍길동", "고민", LuggageType.LUGGAGE));
            Long savedId = saved.getLID();

            // when
            luggageRepository.deleteById(savedId);

            // then
            assertThat(luggageRepository.findById(savedId)).isEmpty();
        }
    }
}
