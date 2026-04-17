package server.TripToN.member.dto;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageCommentLikeDto {
    private Long commentLikeId;
    private String commentContent;
}
