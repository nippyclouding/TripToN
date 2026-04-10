package server.TripToN.concern.dto;

import server.TripToN.concern.entity.LuggageType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcernRequestDto {
    // 2 페이지에서 화면에서 받아올 데이터
    private String concernTitle;
    private String concernContent;
    private boolean isLocked;
    private LuggageType luggageType;
}
