package server.TripToN.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalCountResponseDto {
    private Long totalMemberCount;
    private Long totalConcernCount;
    private Long totalAiRequestCountTodayCount;
}
