package server.TripToN.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String commentContent;
}
