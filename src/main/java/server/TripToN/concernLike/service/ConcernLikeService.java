package server.TripToN.concernLike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.concernLike.entity.ConcernLike;
import server.TripToN.concernLike.repository.ConcernLikeRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.member.repository.MemberRepository;


import static server.TripToN.global.error.ErrorCode.CONCERN_NOT_FOUND;
import static server.TripToN.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ConcernLikeService {

    private final ConcernLikeRepository concernLikeRepository;
    private final MemberRepository memberRepository;
    private final ConcernRepository concernRepository;

    private void addLike(Long memberId, Long concernId) {
        ConcernLike concernLike = ConcernLike.builder()
                .concern(concernRepository.findByConcernIdAndDeletedAtIsNull(concernId).orElseThrow(() -> new BusinessException(CONCERN_NOT_FOUND)))
                .member(memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND)))
                .build();
        concernLikeRepository.save(concernLike);
    }

    private void cancelLike(Long memberId, Long concernId) {
        concernLikeRepository.deleteConcernLikeByConcernConcernIdAndMemberMemberId(concernId, memberId);
    }

    @Transactional
    public void toggleLike(Long memberId, Long concernId) {
        if (concernLikeRepository.existsByMemberMemberIdAndConcernConcernId(memberId, concernId)) cancelLike(memberId, concernId);
        else addLike(memberId, concernId);
    }

    public boolean isLiked(Long memberId, Long concernId) {
        return concernLikeRepository.existsByMemberMemberIdAndConcernConcernId(memberId, concernId);
    }

    public long getLikeCount(Long concernId) {
        return concernLikeRepository.countByConcernConcernIdAndConcernDeletedAtIsNull(concernId);
    }
}
