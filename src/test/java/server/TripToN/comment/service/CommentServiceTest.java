package server.TripToN.comment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.TripToN.comment.dto.CommentCreateRequestDto;
import server.TripToN.comment.dto.CommentUpdateRequestDto;
import server.TripToN.comment.entity.Comment;
import server.TripToN.comment.repository.CommentRepository;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.entity.LuggageType;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ConcernRepository concernRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_success_savesComment() {
        // given
        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .commentContent("응원합니다.")
                .build();
        when(concernRepository.findByConcernIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(concern(1L)));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member(1L)));

        // when
        commentService.createComment(dto, 1L, 1L);

        // then
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_concernNotFound_throwsConcernNotFound() {
        // given
        CommentCreateRequestDto dto = CommentCreateRequestDto.builder()
                .commentContent("응원합니다.")
                .build();
        when(concernRepository.findByConcernIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> commentService.createComment(dto, 1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.CONCERN_NOT_FOUND);
    }

    @Test
    void updateComment_notOwner_throwsWrongAccessUpdate() {
        // given
        CommentUpdateRequestDto dto = CommentUpdateRequestDto.builder()
                .commentContent("수정")
                .build();
        Comment comment = Comment.builder()
                .commentId(1L)
                .member(member(1L))
                .commentContent("원본")
                .build();
        when(commentRepository.findCommentAndMemberById(1L)).thenReturn(Optional.of(comment));

        // when / then
        assertThatThrownBy(() -> commentService.updateComment(dto, 2L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.WRONG_ACCESS_UPDATE);
    }

    @Test
    void deleteComment_owner_callsSoftDelete() {
        // given
        Comment comment = Comment.builder()
                .commentId(1L)
                .member(member(1L))
                .commentContent("원본")
                .build();
        when(commentRepository.findCommentAndMemberById(1L)).thenReturn(Optional.of(comment));

        // when
        commentService.deleteComment(1L, 1L);

        // then
        verify(commentRepository).softDeleteById(1L);
    }

    private Member member(Long id) {
        return Member.builder()
                .memberId(id)
                .memberEmail("user" + id + "@tripton.com")
                .memberNickname("traveler" + id)
                .memberLoginPassword("password")
                .build();
    }

    private Concern concern(Long id) {
        return Concern.builder()
                .concernId(id)
                .member(member(1L))
                .concernTitle("title")
                .concernContent("content")
                .luggageType(LuggageType.a)
                .build();
    }
}
