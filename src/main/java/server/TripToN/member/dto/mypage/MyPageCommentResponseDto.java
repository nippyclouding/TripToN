package server.TripToN.member.dto.mypage;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageCommentResponseDto {
    private Long commentId;
    private String commentContent;
    private LocalDateTime createdAt;
}
