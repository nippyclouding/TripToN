package server.TripToN.AiResponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.TripToN.AiResponse.entity.AiRequestLog;

public interface AiRequestLogRepository extends JpaRepository<AiRequestLog, Long> {
    @Query("SELECT count(a) FROM AiRequestLog a WHERE a.createdAt >= CURRENT_DATE")
    long countToday();
}
