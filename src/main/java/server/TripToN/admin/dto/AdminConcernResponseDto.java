package server.TripToN.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.TripToN.concern.entity.Concern;

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

    public static AdminConcernResponseDto from(Concern concern) {
        return AdminConcernResponseDto.builder()
                .concernId(concern.getConcernId())
                .concernTitle(concern.getConcernTitle())
                .concernContent(concern.getConcernContent())
                .luggageType(concern.getLuggageType().name())
                .createdAt(concern.getCreatedAt())
                .updatedAt(concern.getUpdatedAt())
                .deletedAt(concern.getDeletedAt())
                .memberId(concern.getMember().getMemberId())
                .memberEmail(concern.getMember().getMemberEmail())
                .memberNickname(concern.getMember().getMemberNickname())
                .build();
    }
}
