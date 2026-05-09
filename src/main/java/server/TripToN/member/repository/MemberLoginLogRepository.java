package server.TripToN.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.TripToN.member.entity.MemberLoginLog;

public interface MemberLoginLogRepository extends JpaRepository<MemberLoginLog, Long> {
}
