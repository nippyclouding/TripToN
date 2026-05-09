package server.TripToN.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "MEMBER_LOGIN_LOGS")
public class MemberLoginLog {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "member_login_log_id")
        private Long member_login_log_id;

        @CreatedDate
        @Column(updatable = false)
        private LocalDateTime createdAt;

        private Boolean loginStatus;

        private String loginFailureReason;

        private String loginTryId;

        private String loginMemberNickname;
}
