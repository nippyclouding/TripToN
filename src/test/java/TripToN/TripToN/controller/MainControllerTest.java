package TripToN.TripToN.controller;

import TripToN.TripToN.domain.Concern;
import TripToN.TripToN.domain.Luggage;
import TripToN.TripToN.domain.LuggageType;
import TripToN.TripToN.service.LuggageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LuggageService luggageService;

    private Luggage createTestLuggage() {
        Concern concern = new Concern("홍길동", "취업 고민", "1234");
        concern.assignResponse("AI 조언");
        return new Luggage(concern, LuggageType.LUGGAGE);
    }

    @Nested
    @DisplayName("GET 페이지 라우팅")
    class PageRouting {

        @Test
        @DisplayName("GET / → 메인 페이지(1_main) 반환")
        void mainPage() throws Exception {
            // given - 별도 준비 없음

            // when & then
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("1_main"));
        }

        @Test
        @DisplayName("GET /introduce → 소개 페이지(2_introduce) 반환")
        void introducePage() throws Exception {
            // given - 별도 준비 없음

            // when & then
            mockMvc.perform(get("/introduce"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("2_introduce"));
        }

        @Test
        @DisplayName("GET /select → 선택 페이지(3_select) 반환")
        void selectPage() throws Exception {
            // given - 별도 준비 없음

            // when & then
            mockMvc.perform(get("/select"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("3_select"));
        }
    }

    @Nested
    @DisplayName("POST /concern - 고민 등록")
    class ConcernSubmit {

        @Test
        @DisplayName("정상 입력 시 5_info 페이지와 concern 모델을 반환한다")
        void successfulSubmission() throws Exception {
            // given
            Luggage luggage = createTestLuggage();
            given(luggageService.generateResponse(any(Concern.class))).willReturn("AI 조언");
            given(luggageService.saveLuggage(any(Luggage.class))).willReturn(luggage);

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "LuggageA")
                            .param("concern", "취업 고민")
                            .param("userName", "홍길동")
                            .param("password", "1234"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("5_info"))
                    .andExpect(model().attributeExists("concern"));

            then(luggageService).should().generateResponse(any(Concern.class));
            then(luggageService).should().saveLuggage(any(Luggage.class));
        }

        @Test
        @DisplayName("빈 고민 입력 시 에러 메시지와 함께 선택 페이지로 돌아간다")
        void emptyConcernReturnsError() throws Exception {
            // given - 빈 concern 파라미터

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "LuggageA")
                            .param("concern", "")
                            .param("userName", "홍길동")
                            .param("password", "1234"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("3_select"))
                    .andExpect(model().attribute("error", "고민을 입력해주세요."));
        }

        @Test
        @DisplayName("빈 사용자 이름 시 에러 메시지와 함께 선택 페이지로 돌아간다")
        void emptyUserNameReturnsError() throws Exception {
            // given - 빈 userName 파라미터

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "LuggageA")
                            .param("concern", "취업 고민")
                            .param("userName", "")
                            .param("password", "1234"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("3_select"))
                    .andExpect(model().attribute("error", "사용자 이름을 입력해주세요."));
        }

        @Test
        @DisplayName("빈 비밀번호 시 에러 메시지와 함께 선택 페이지로 돌아간다")
        void emptyPasswordReturnsError() throws Exception {
            // given - 빈 password 파라미터

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "LuggageA")
                            .param("concern", "취업 고민")
                            .param("userName", "홍길동")
                            .param("password", ""))
                    .andExpect(status().isOk())
                    .andExpect(view().name("3_select"))
                    .andExpect(model().attribute("error", "비밀번호를 입력해주세요."));
        }

        @Test
        @DisplayName("잘못된 luggageType 시 '유효하지 않은 가방 타입' 에러를 반환한다")
        void invalidLuggageTypeReturnsError() throws Exception {
            // given - 존재하지 않는 luggageType

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "InvalidType")
                            .param("concern", "취업 고민")
                            .param("userName", "홍길동")
                            .param("password", "1234"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("3_select"))
                    .andExpect(model().attribute("error", "유효하지 않은 가방 타입입니다."));
        }

        @Test
        @DisplayName("LuggageB 타입으로 정상 등록된다")
        void luggageBTypeWorks() throws Exception {
            // given
            Concern concern = new Concern("김철수", "고민", "1234");
            concern.assignResponse("응답");
            Luggage luggage = new Luggage(concern, LuggageType.CART);
            given(luggageService.generateResponse(any(Concern.class))).willReturn("응답");
            given(luggageService.saveLuggage(any(Luggage.class))).willReturn(luggage);

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "LuggageB")
                            .param("concern", "고민")
                            .param("userName", "김철수")
                            .param("password", "1234"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("5_info"));
        }

        @Test
        @DisplayName("LuggageC 타입으로 정상 등록된다")
        void luggageCTypeWorks() throws Exception {
            // given
            Concern concern = new Concern("이영희", "고민", "1234");
            concern.assignResponse("응답");
            Luggage luggage = new Luggage(concern, LuggageType.BAG);
            given(luggageService.generateResponse(any(Concern.class))).willReturn("응답");
            given(luggageService.saveLuggage(any(Luggage.class))).willReturn(luggage);

            // when & then
            mockMvc.perform(post("/concern")
                            .param("luggageType", "LuggageC")
                            .param("concern", "고민")
                            .param("userName", "이영희")
                            .param("password", "1234"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("5_info"));
        }
    }

    @Nested
    @DisplayName("GET /info - 고민 정보 조회")
    class InfoPage {

        @Test
        @DisplayName("세션에 Luggage가 있으면 5_info 페이지와 concern 모델을 반환한다")
        void returnsInfoPageWithSession() throws Exception {
            // given
            Luggage luggage = createTestLuggage();
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("saveLuggage", luggage);

            // when & then
            mockMvc.perform(get("/info").session(session))
                    .andExpect(status().isOk())
                    .andExpect(view().name("5_info"))
                    .andExpect(model().attributeExists("concern"));
        }

        @Test
        @DisplayName("세션에 Luggage가 없으면 /select로 리다이렉트한다")
        void redirectsWhenNoSession() throws Exception {
            // given - 빈 세션

            // when & then
            mockMvc.perform(get("/info"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/select"));
        }

        @Test
        @DisplayName("세션의 Luggage에 concern이 null이면 /select로 리다이렉트한다")
        void redirectsWhenConcernNull() throws Exception {
            // given
            Luggage luggage = new Luggage(null, LuggageType.LUGGAGE);
            MockHttpSession session = new MockHttpSession();
            session.setAttribute("saveLuggage", luggage);

            // when & then
            mockMvc.perform(get("/info").session(session))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/select"));
        }
    }

    @Nested
    @DisplayName("GET /result - 결과 목록")
    class ResultPage {

        @Test
        @DisplayName("Luggage 목록이 있으면 모델에 포함하여 반환한다")
        void returnsResultPageWithList() throws Exception {
            // given
            List<Luggage> list = List.of(createTestLuggage());
            Page<Luggage> page = new PageImpl<>(list);
            given(luggageService.findAllPaged(0, 5)).willReturn(page);

            // when & then
            mockMvc.perform(get("/result"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("6_result"))
                    .andExpect(model().attribute("luggageList", list));
        }

        @Test
        @DisplayName("데이터가 없으면 빈 리스트를 반환한다")
        void returnsEmptyList() throws Exception {
            // given
            Page<Luggage> emptyPage = new PageImpl<>(Collections.emptyList());
            given(luggageService.findAllPaged(0, 5)).willReturn(emptyPage);

            // when & then
            mockMvc.perform(get("/result"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("6_result"))
                    .andExpect(model().attribute("luggageList", Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("GET /{lid} - 상세 조회")
    class LuggageDetail {

        @Test
        @DisplayName("존재하는 ID이면 서비스를 호출하고 luggage_detail 뷰를 반환한다")
        void returnsDetailPageWhenFound() throws Exception {
            // given
            Luggage luggage = createTestLuggage();
            setLuggageId(luggage, 1L);
            given(luggageService.findById(1L)).willReturn(luggage);

            // when & then
            mockMvc.perform(get("/1"))
                    .andExpect(status().isOk());
            then(luggageService).should().findById(1L);
        }

        @Test
        @DisplayName("존재하지 않는 ID이면 /result로 리다이렉트한다")
        void redirectsWhenNotFound() throws Exception {
            // given
            given(luggageService.findById(999L))
                    .willThrow(new IllegalArgumentException("데이터를 찾을 수 없습니다"));

            // when & then
            mockMvc.perform(get("/999"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/result"));
        }

        private void setLuggageId(Luggage luggage, Long id) throws Exception {
            java.lang.reflect.Field lidField = Luggage.class.getDeclaredField("LID");
            lidField.setAccessible(true);
            lidField.set(luggage, id);
        }
    }

    @Nested
    @DisplayName("POST /verify-password - 비밀번호 검증")
    class VerifyPassword {

        @Test
        @DisplayName("올바른 비밀번호이면 success: true를 반환한다")
        void returnsTrueForCorrectPassword() throws Exception {
            // given
            Luggage luggage = createTestLuggage();
            given(luggageService.findById(1L)).willReturn(luggage);

            // when & then
            mockMvc.perform(post("/verify-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"lid\":\"1\",\"password\":\"1234\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("잘못된 비밀번호이면 success: false를 반환한다")
        void returnsFalseForWrongPassword() throws Exception {
            // given
            Luggage luggage = createTestLuggage();
            given(luggageService.findById(1L)).willReturn(luggage);

            // when & then
            mockMvc.perform(post("/verify-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"lid\":\"1\",\"password\":\"9999\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("존재하지 않는 ID이면 success: false를 반환한다")
        void returnsFalseForNonExistentId() throws Exception {
            // given
            given(luggageService.findById(999L))
                    .willThrow(new IllegalArgumentException("데이터를 찾을 수 없습니다"));

            // when & then
            mockMvc.perform(post("/verify-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"lid\":\"999\",\"password\":\"1234\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @DisplayName("잘못된 JSON 요청 시 success: false를 반환한다")
        void returnsFalseForInvalidRequest() throws Exception {
            // given - 잘못된 lid 형식

            // when & then
            mockMvc.perform(post("/verify-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"lid\":\"abc\",\"password\":\"1234\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("GET /api/luggage/{lid}/response - 응답 조회 API")
    class LuggageResponseApi {

        @Test
        @DisplayName("존재하는 Luggage의 response를 반환한다")
        void returnsResponseText() throws Exception {
            // given
            Luggage luggage = createTestLuggage();
            given(luggageService.findById(1L)).willReturn(luggage);

            // when & then
            mockMvc.perform(get("/api/luggage/1/response"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("AI 조언"));
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 전파된다")
        void throwsWhenNotFound() throws Exception {
            // given
            given(luggageService.findById(999L))
                    .willThrow(new IllegalArgumentException("데이터를 찾을 수 없습니다"));

            // when & then
            org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                    mockMvc.perform(get("/api/luggage/999/response"))
            ).hasCauseInstanceOf(IllegalArgumentException.class);
        }
    }
}
