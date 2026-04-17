package server.TripToN.member.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageConcernLikeDto {
    private Long concernLikeId;
    private Long concernId;
    private String concernTitle;
}
