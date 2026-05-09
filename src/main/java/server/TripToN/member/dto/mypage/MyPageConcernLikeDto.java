package server.TripToN.member.dto.mypage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageConcernLikeDto {
    private Long concernLikeId;
    private Long concernId;
    private String concernTitle;
}
