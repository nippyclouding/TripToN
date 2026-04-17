package server.TripToN.concern.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.TripToN.concern.entity.Concern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcernRepository extends JpaRepository<Concern, Long> {
    @Query("SELECT c FROM Concern c JOIN FETCH c.member")
    Page<Concern> findAllWithMember(Pageable pageable);

    @Query("SELECT c FROM Concern c " +
            "JOIN FETCH c.member " +
            "LEFT JOIN FETCH c.comments " +
            "LEFT JOIN FETCH c.aiResponse " +
            "WHERE c.concernId = :concernId")
    Concern findConcernAndMemberAndCommentAndResponseByConcernId(@Param("concernId") Long concernId);

    @Modifying(clearAutomatically = true) // 해당 쿼리 실행 후 트랜잭션이 바로 종료되기 때문에 캐시 불일치 문제 가능성은 없다
    @Query("UPDATE Concern c set c.deletedAt = CURRENT_TIMESTAMP where c.concernId = :concernId")
    void softDeleteById(Long concernId);

    @Query("SELECT c FROM Concern c WHERE c.member.memberId = :memberId")
    Page<Concern> findByMemberMemberId(@Param("memberId") Long memberId, Pageable pageable);


}
