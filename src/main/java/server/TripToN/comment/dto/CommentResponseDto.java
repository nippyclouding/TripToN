package server.TripToN.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String commentContent;
    private String commentMemberNickname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
