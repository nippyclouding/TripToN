package server.TripToN.concernLike.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.entity.LuggageType;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.concernLike.entity.ConcernLike;
import server.TripToN.concernLike.repository.ConcernLikeRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcernLikeServiceTest {

    @Mock
    private ConcernLikeRepository concernLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ConcernRepository concernRepository;

    @InjectMocks
    private ConcernLikeService concernLikeService;

    @Test
    void toggleLike_alreadyLiked_deletesLike() {
        // given
        when(concernLikeRepository.existsByMemberMemberIdAndConcernConcernId(1L, 1L)).thenReturn(true);

        // when
        concernLikeService.toggleLike(1L, 1L);

        // then
        verify(concernLikeRepository).deleteConcernLikeByConcernConcernIdAndMemberMemberId(1L, 1L);
    }

    @Test
    void toggleLike_notLiked_savesLike() {
        // given
        when(concernLikeRepository.existsByMemberMemberIdAndConcernConcernId(1L, 1L)).thenReturn(false);
        when(concernRepository.findByConcernIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(concern(1L)));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member(1L)));

        // when
        concernLikeService.toggleLike(1L, 1L);

        // then
        verify(concernLikeRepository).save(any(ConcernLike.class));
    }

    @Test
    void toggleLike_concernNotFound_throwsConcernNotFound() {
        // given
        when(concernLikeRepository.existsByMemberMemberIdAndConcernConcernId(1L, 1L)).thenReturn(false);
        when(concernRepository.findByConcernIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> concernLikeService.toggleLike(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.CONCERN_NOT_FOUND);
    }

    @Test
    void getLikeCount_returnsRepositoryCount() {
        // given
        when(concernLikeRepository.countByConcernConcernIdAndConcernDeletedAtIsNull(1L)).thenReturn(3L);

        // when
        long result = concernLikeService.getLikeCount(1L);

        // then
        assertThat(result).isEqualTo(3L);
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
