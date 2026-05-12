package server.TripToN.member.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageProfileResponseDto {
    private String memberEmail;
    private String memberNickName;
}
