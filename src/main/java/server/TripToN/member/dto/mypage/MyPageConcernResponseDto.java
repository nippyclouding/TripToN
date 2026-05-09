package server.TripToN.member.dto.mypage;

import lombok.*;
import server.TripToN.concern.entity.LuggageType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageConcernResponseDto {
    private Long concernId;
    private String concernTitle;
    private LuggageType luggageType;
    private LocalDateTime createdAt;
}
