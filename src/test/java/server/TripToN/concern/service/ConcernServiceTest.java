package server.TripToN.concern.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.TripToN.AiResponse.dto.AiResponseDto;
import server.TripToN.AiResponse.entity.AiResponse;
import server.TripToN.AiResponse.service.AiResponseService;
import server.TripToN.commentLike.repository.CommentLikeRepository;
import server.TripToN.concern.dto.ConcernRequestDto;
import server.TripToN.concern.dto.ConcernUpdateRequestDto;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.entity.LuggageType;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcernServiceTest {

    @Mock
    private ConcernRepository concernRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AiResponseService aiResponseService;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @InjectMocks
    private ConcernService concernService;

    @Test
    void saveConcernAndGetAiResponse_aiSuccess_returnsAiResponseDto() {
        // given
        Member member = member(1L);
        ConcernRequestDto dto = concernRequest();
        AiResponse aiResponse = AiResponse.builder()
                .responseContent("좋은 여행이 될 거예요.")
                .createdAt(LocalDateTime.of(2026, 5, 10, 10, 0))
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(concernRepository.save(any(Concern.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(aiResponseService.getResponse(any(Concern.class))).thenReturn(aiResponse);

        // when
        AiResponseDto result = concernService.saveConcernAndGetAiResponse(dto, 1L);

        // then
        assertThat(result.getResponseContent()).isEqualTo("좋은 여행이 될 거예요.");
        assertThat(result.getConcernTitle()).isEqualTo("여행 고민");
        verify(concernRepository).save(any(Concern.class));
    }

    @Test
    void saveConcernAndGetAiResponse_aiReturnsNull_returnsFallbackDto() {
        // given
        Member member = member(1L);
        ConcernRequestDto dto = concernRequest();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(concernRepository.save(any(Concern.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(aiResponseService.getResponse(any(Concern.class))).thenReturn(null);

        // when
        AiResponseDto result = concernService.saveConcernAndGetAiResponse(dto, 1L);

        // then
        assertThat(result.getResponseContent()).isEqualTo("현재 AI 모델 문제로 응답할 수 없습니다.");
        assertThat(result.getConcernContent()).isEqualTo("어디로 갈지 모르겠어요.");
    }

    @Test
    void getConcernDetail_notFound_throwsConcernNotFound() {
        // given
        when(concernRepository.findConcernAndMemberAndCommentAndResponseByConcernId(1L))
                .thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> concernService.getConcernDetail(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.CONCERN_NOT_FOUND);
    }

    @Test
    void updateConcern_notOwner_throwsWrongAccessUpdate() {
        // given
        Concern concern = Concern.builder()
                .concernId(1L)
                .member(member(1L))
                .concernTitle("old")
                .concernContent("old")
                .luggageType(LuggageType.a)
                .build();
        ConcernUpdateRequestDto dto = ConcernUpdateRequestDto.builder()
                .concernTitle("new")
                .concernContent("new")
                .build();
        when(concernRepository.findByConcernIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(concern));

        // when / then
        assertThatThrownBy(() -> concernService.updateConcern(1L, 2L, dto))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.WRONG_ACCESS_UPDATE);
    }

    @Test
    void removeConcern_owner_callsSoftDelete() {
        // given
        Concern concern = Concern.builder()
                .concernId(1L)
                .member(member(1L))
                .concernTitle("title")
                .concernContent("content")
                .luggageType(LuggageType.a)
                .build();
        when(concernRepository.findByConcernIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(concern));

        // when
        concernService.removeConcern(1L, 1L);

        // then
        verify(concernRepository).softDeleteById(1L);
    }

    private Member member(Long id) {
        return Member.builder()
                .memberId(id)
                .memberEmail("user" + id + "@tripton.com")
                .memberNickname("traveler" + id)
                .memberLoginPassword("password")
                .build();
    }

    private ConcernRequestDto concernRequest() {
        return ConcernRequestDto.builder()
                .concernTitle("여행 고민")
                .concernContent("어디로 갈지 모르겠어요.")
                .luggageType(LuggageType.a)
                .build();
    }
}
