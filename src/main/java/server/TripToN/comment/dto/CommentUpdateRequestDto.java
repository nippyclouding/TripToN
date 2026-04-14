package server.TripToN.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDto {
    @NotNull
    private String commentContent;
}
