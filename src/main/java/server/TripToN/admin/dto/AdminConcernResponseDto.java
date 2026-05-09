package server.TripToN.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.TripToN.concern.repository.ConcernAdminProjection;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminConcernResponseDto {
    private Long concernId;
    private String concernTitle;
    private String concernContent;
    private String luggageType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long memberId;
    private String memberEmail;
    private String memberNickname;

    public static AdminConcernResponseDto from(ConcernAdminProjection projection) {
        return AdminConcernResponseDto.builder()
                .concernId(projection.getConcernId())
                .concernTitle(projection.getConcernTitle())
                .concernContent(projection.getConcernContent())
                .luggageType(projection.getLuggageType())
                .createdAt(projection.getCreatedAt())
                .updatedAt(projection.getUpdatedAt())
                .deletedAt(projection.getDeletedAt())
                .memberId(projection.getMemberId())
                .memberEmail(projection.getMemberEmail())
                .memberNickname(projection.getMemberNickname())
                .build();
    }
}
