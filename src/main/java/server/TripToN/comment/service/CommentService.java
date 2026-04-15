package server.TripToN.comment.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.comment.dto.CommentCreateRequestDto;
import server.TripToN.comment.dto.CommentUpdateRequestDto;
import server.TripToN.comment.entity.Comment;
import server.TripToN.comment.repository.CommentRepository;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;



@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ConcernRepository concernRepository;
    private final MemberRepository memberRepository;

    public void createComment(CommentCreateRequestDto dto, Long writerId, Long concernId) {
        Concern concern = concernRepository.findById(concernId).orElseThrow();
        Member writer = memberRepository.findById(writerId).orElseThrow();
        Comment comment = Comment.builder()
                .commentContent(dto.getCommentContent())
                .concern(concern)
                .member(writer)
                .build();
        commentRepository.save(comment);
    }

    public void updateComment(CommentUpdateRequestDto dto, Long currentMemberId, Long commentId) {
        // 1. 작성자 일치 여부 조회
        Comment findComment = commentRepository.findCommentAndMemberById(commentId).orElseThrow();
        if (!findComment.getMember().getMemberId().equals(currentMemberId)) throw new BusinessException(ErrorCode.WRONG_ACCESS_UPDATE);

        // 2. update
        findComment.updateContent(dto.getCommentContent());
    }

    public void deleteComment(Long currentMemberId, Long commentId) {
        // 1. 작성자 일치 여부 조회
        Comment findComment = commentRepository.findCommentAndMemberById(commentId).orElseThrow();
        if (!findComment.getMember().getMemberId().equals(currentMemberId)) throw new BusinessException(ErrorCode.WRONG_ACCESS_DELETE);

        // 2. delete
        commentRepository.softDeleteById(commentId);
    }
}
