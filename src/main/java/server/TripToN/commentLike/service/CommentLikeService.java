package server.TripToN.commentLike.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.comment.repository.CommentRepository;
import server.TripToN.commentLike.entity.CommentLike;
import server.TripToN.commentLike.repository.CommentLikeRepository;
import server.TripToN.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void toggleLike(Long commentId, Long memberId) {
        if (commentLikeRepository.existsByMemberMemberIdAndCommentCommentId(memberId, commentId)) cancelLike(commentId, memberId);
        else addLike(commentId, memberId);
    }

    private void addLike(Long commentId, Long memberId) {
        commentLikeRepository.save(
                CommentLike.builder()
                        .comment(commentRepository.findById(commentId).orElseThrow())
                        .member(memberRepository.findById(memberId).orElseThrow())
                        .build()
        );
    }

    private void cancelLike(Long commentId, Long memberId) {
        commentLikeRepository.deleteCommentLikeByCommentCommentIdAndMemberMemberId(commentId, memberId);
    }
}
