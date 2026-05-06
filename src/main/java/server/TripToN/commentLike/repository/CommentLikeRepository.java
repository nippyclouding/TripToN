package server.TripToN.commentLike.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.TripToN.commentLike.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByMemberMemberIdAndCommentCommentId(Long memberId, Long commentId);

    void deleteCommentLikeByCommentCommentIdAndMemberMemberId(Long commentId, Long memberId);

    long countByCommentCommentId(Long commentId);
    @Query(value = "SELECT cl FROM CommentLike cl JOIN FETCH cl.comment WHERE cl.member.memberId = :memberId",
            countQuery = "SELECT COUNT(cl) FROM CommentLike cl WHERE cl.member.memberId = :memberId")
    Page<CommentLike> findByMemberMemberIdWithComment(@Param("memberId") Long memberId, Pageable pageable);
}