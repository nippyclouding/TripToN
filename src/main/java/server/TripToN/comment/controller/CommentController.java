package server.TripToN.comment.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.TripToN.comment.dto.CommentCreateRequestDto;
import server.TripToN.comment.dto.CommentUpdateRequestDto;
import server.TripToN.comment.service.CommentService;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.util.Const;

import static server.TripToN.global.error.ErrorCode.SESSION_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{concernId}")
    public ResponseEntity<Void> postComment(@Valid @ModelAttribute CommentCreateRequestDto dto, HttpSession session, @PathVariable Long concernId) {

        Long writerId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (writerId == null) throw new BusinessException(SESSION_NOT_FOUND);

        commentService.createComment(dto, writerId, concernId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@Valid @ModelAttribute CommentUpdateRequestDto dto, HttpSession session, @PathVariable Long commentId) {
        Long currentMemberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (currentMemberId == null) throw new BusinessException(SESSION_NOT_FOUND);
        commentService.updateComment(dto, currentMemberId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(HttpSession session, @PathVariable Long commentId) {
        Long currentMemberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (currentMemberId == null) throw new BusinessException(SESSION_NOT_FOUND);
        commentService.deleteComment(currentMemberId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
