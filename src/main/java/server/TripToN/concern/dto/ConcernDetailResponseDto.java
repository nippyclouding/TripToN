package server.TripToN.concern.dto;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import server.TripToN.comment.dto.CommentResponseDto;
import server.TripToN.concern.entity.LuggageType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcernDetailResponseDto {
    // 고민 상세페이지에서 보여줄 내용

    // 고민 작성 회원 정보
    private Long memberId;
    private String memberNickName;

    // 고민 정보
    private String concernTitle;
    private String concernContent;
    private LuggageType luggageType;

    private LocalDateTime createdAt;

    // 가방 이미지 인덱스 (luggageType.ordinal() + 1)
    private int luggageTypeImageIndex;

    // ai 응답 정보
    private String responseContent;

    // 댓글 리스트 정보
    List<CommentResponseDto> dtos;
}
