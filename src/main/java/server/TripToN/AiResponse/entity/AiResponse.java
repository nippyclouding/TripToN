package server.TripToN.AiResponse.entity;

import server.TripToN.concern.entity.Concern;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "RESPONSES")
public class AiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long responseId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concern_id", nullable = false, unique = true)
    private Concern concern;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String responseContent;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
