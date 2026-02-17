package TripToN.TripToN.service;

import TripToN.TripToN.repository.LuggageRepository;
import TripToN.TripToN.domain.Concern;
import TripToN.TripToN.domain.Luggage;
import TripToN.TripToN.domain.LuggageType;
import TripToN.TripToN.service.responseService.ResponseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class LuggageServiceTest {

    @Mock
    private LuggageRepository luggageRepository;

    @Mock
    private ResponseService responseService;

    @InjectMocks
    private LuggageService luggageService;

    private Luggage createCompleteLuggage(String userName, String concernText, LuggageType type) {
        Luggage luggage = Luggage.create(userName, concernText, "1234", type);
        luggage.assignResponse("테스트 응답");
        return luggage;
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("저장된 모든 Luggage 목록을 반환한다")
        void returnsAllLuggages() {
            // given
            Luggage luggage1 = createCompleteLuggage("홍길동", "고민1", LuggageType.LUGGAGE);
            Luggage luggage2 = createCompleteLuggage("김철수", "고민2", LuggageType.BAG);
            given(luggageRepository.findAll()).willReturn(List.of(luggage1, luggage2));

            // when
            List<Luggage> result = luggageService.findAll();

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getConcern().getUserName()).isEqualTo("홍길동");
            assertThat(result.get(1).getConcern().getUserName()).isEqualTo("김철수");
            then(luggageRepository).should().findAll();
        }

        @Test
        @DisplayName("저장된 데이터가 없으면 빈 리스트를 반환한다")
        void returnsEmptyListWhenNoData() {
            // given
            given(luggageRepository.findAll()).willReturn(Collections.emptyList());

            // when
            List<Luggage> result = luggageService.findAll();

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 해당 Luggage를 반환한다")
        void returnsLuggageWhenFound() {
            // given
            Luggage luggage = createCompleteLuggage("홍길동", "취업 고민", LuggageType.LUGGAGE);
            given(luggageRepository.findById(1L)).willReturn(Optional.of(luggage));

            // when
            Luggage result = luggageService.findById(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getConcern().getUserName()).isEqualTo("홍길동");
            assertThat(result.getConcern().getConcern()).isEqualTo("취업 고민");
            then(luggageRepository).should().findById(1L);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 IllegalArgumentException이 발생한다")
        void throwsExceptionWhenNotFound() {
            // given
            given(luggageRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> luggageService.findById(999L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("데이터를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("saveLuggage()")
    class SaveLuggage {

        @Test
        @DisplayName("complete한 Luggage를 정상적으로 저장한다")
        void savesCompleteLuggage() {
            // given
            Luggage luggage = createCompleteLuggage("홍길동", "취업 고민", LuggageType.LUGGAGE);
            given(luggageRepository.save(any(Luggage.class))).willReturn(luggage);

            // when
            Luggage result = luggageService.saveLuggage(luggage);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getConcern().getUserName()).isEqualTo("홍길동");
            then(luggageRepository).should().save(luggage);
        }

        @Test
        @DisplayName("response가 없는 Luggage 저장 시 IllegalStateException이 발생한다")
        void throwsExceptionWhenResponseMissing() {
            // given
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.LUGGAGE);

            // when & then
            assertThatThrownBy(() -> luggageService.saveLuggage(luggage))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("response is null");

            then(luggageRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("concern이 null인 Luggage 저장 시 IllegalStateException이 발생한다")
        void throwsExceptionWhenConcernNull() throws Exception {
            // given - use reflection to create a Luggage with null concern
            Luggage luggage = Luggage.create("홍길동", "고민", "1234", LuggageType.BAG);
            java.lang.reflect.Field concernField = Luggage.class.getDeclaredField("concern");
            concernField.setAccessible(true);
            concernField.set(luggage, null);

            // when & then
            assertThatThrownBy(() -> luggageService.saveLuggage(luggage))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("concern is null");

            then(luggageRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("generateResponse()")
    class SetResponse {

        @Test
        @DisplayName("ResponseService를 호출하여 응답 문자열을 반환한다")
        void delegatesToResponseService() {
            // given
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.LUGGAGE);
            Concern concern = luggage.getConcern();
            given(responseService.response(concern)).willReturn("AI 조언입니다.");

            // when
            String response = luggageService.generateResponse(concern);

            // then
            assertThat(response).isEqualTo("AI 조언입니다.");
            then(responseService).should().response(concern);
        }

        @Test
        @DisplayName("ResponseService가 null을 반환하면 null을 그대로 전달한다")
        void returnsNullWhenServiceReturnsNull() {
            // given
            Luggage luggage = Luggage.create("홍길동", "취업 고민", "1234", LuggageType.LUGGAGE);
            Concern concern = luggage.getConcern();
            given(responseService.response(concern)).willReturn(null);

            // when
            String response = luggageService.generateResponse(concern);

            // then
            assertThat(response).isNull();
        }
    }
}
