package server.TripToN.concern.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import server.TripToN.concern.entity.Concern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcernRepository extends JpaRepository<Concern, Long> {
    @Query("SELECT c FROM Concern c JOIN FETCH c.member")
    Page<Concern> findAllWithMember(Pageable pageable);
}
