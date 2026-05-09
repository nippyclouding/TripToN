package server.TripToN.member.dto.mypage;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    // 회원 정보
    private String memberEmail;
    private String memberNickName;
    // 회원이 작성한 고민 리스트
    private List<MyPageConcernResponseDto> concernDtos;
    private int concernTotalPages;
    // 회원이 작성한 댓글 리스트
    private List<MyPageCommentResponseDto> commentDtos;
    private int commentTotalPages;
    // 회원이 좋아요한 고민 리스트
    private List<MyPageConcernLikeDto> concernLikeDtos;
    private int concernLikeTotalPages;
    // 회원이 좋아요한 댓글 리스트
    private List<MyPageCommentLikeDto> commentLikeDtos;
    private int commentLikeTotalPages;



}
