package server.TripToN.AiResponse.entity;

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
@NoArgsConstructor
@SuperBuilder
@Table(name = "GEMINI_API_REQUEST_LOGS")
public class AiRequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_response_log_id")
    private Long aiResponseLogId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Boolean requestStatus;

    private String requestFailureReason;

    private Long requestMemberId;

    private String requestMemberNickname;
}
