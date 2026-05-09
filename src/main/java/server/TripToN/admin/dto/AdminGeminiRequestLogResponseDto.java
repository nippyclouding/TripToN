package server.TripToN.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.TripToN.AiResponse.entity.AiRequestLog;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminGeminiRequestLogResponseDto {
    private Long aiRequestLogId;
    private LocalDateTime createdAt;
    private Boolean requestStatus;
    private String requestFailureReason;
    private Long requestMemberId;
    private String requestMemberNickname;

    public static AdminGeminiRequestLogResponseDto from(AiRequestLog log) {
        return AdminGeminiRequestLogResponseDto.builder()
                .aiRequestLogId(log.getAiResponseLogId())
                .createdAt(log.getCreatedAt())
                .requestStatus(log.getRequestStatus())
                .requestFailureReason(log.getRequestFailureReason())
                .requestMemberId(log.getRequestMemberId())
                .requestMemberNickname(log.getRequestMemberNickname())
                .build();
    }
}
