package server.TripToN.concernLike.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.TripToN.concernLike.entity.ConcernLike;

public interface ConcernLikeRepository extends JpaRepository<ConcernLike, Long> {
    void deleteConcernLikeByConcernConcernIdAndMemberMemberId(Long concernId, Long memberId);

    boolean existsByMemberMemberIdAndConcernConcernId(Long memberId, Long concernId);

    long countByConcernConcernId(Long concernId);

    @Query(value = "SELECT cl FROM ConcernLike cl JOIN FETCH cl.concern WHERE cl.member.memberId = :memberId",
            countQuery = "SELECT COUNT(cl) FROM ConcernLike cl WHERE cl.member.memberId = :memberId")
    Page<ConcernLike> findByMemberMemberIdWithConcern(@Param("memberId") Long memberId, Pageable pageable);
}
