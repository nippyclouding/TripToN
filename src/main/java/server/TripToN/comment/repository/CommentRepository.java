package server.TripToN.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.TripToN.comment.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.member m where c.commentId =:commentId")
    Optional<Comment> findCommentAndMemberById(@Param("commentId") Long commentId);

    @Modifying(clearAutomatically = true) // 해당 쿼리 실행 후 트랜잭션이 바로 종료되기 때문에 캐시 불일치 문제 가능성은 없다
    @Query("UPDATE Comment c set c.deletedAt = CURRENT_TIMESTAMP where c.commentId = :commentId")
    void softDeleteById(@Param("commentId") Long commentId);
}
