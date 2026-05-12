package server.TripToN.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.comment.entity.Comment;
import server.TripToN.comment.repository.CommentRepository;
import server.TripToN.commentLike.entity.CommentLike;
import server.TripToN.commentLike.repository.CommentLikeRepository;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.concernLike.entity.ConcernLike;
import server.TripToN.concernLike.repository.ConcernLikeRepository;
import server.TripToN.member.dto.mypage.*;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ConcernRepository concernRepository;
    private final CommentRepository commentRepository;
    private final ConcernLikeRepository concernLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    public MyPageProfileResponseDto getMyPageProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return MyPageProfileResponseDto.builder()
                .memberEmail(member.getMemberEmail())
                .memberNickName(member.getMemberNickname())
                .build();
    }

    public Page<MyPageConcernResponseDto> getMyConcerns(Long memberId, int page) {
        return concernRepository.findByMemberMemberId(
                        memberId, PageRequest.of(page, 5, Sort.by("createdAt").descending()))
                .map(c -> MyPageConcernResponseDto.builder()
                        .concernId(c.getConcernId())
                        .concernTitle(c.getConcernTitle())
                        .luggageType(c.getLuggageType())
                        .createdAt(c.getCreatedAt())
                        .build());
    }

    public Page<MyPageCommentResponseDto> getMyComments(Long memberId, int page) {
        return commentRepository.findByMemberMemberId(
                        memberId, PageRequest.of(page, 5, Sort.by("createdAt").descending()))
                .map(c -> MyPageCommentResponseDto.builder()
                        .commentId(c.getCommentId())
                        .commentContent(c.getCommentContent())
                        .createdAt(c.getCreatedAt())
                        .build());
    }

    public Page<MyPageConcernLikeDto> getMyConcernLikes(Long memberId, int page) {
        return concernLikeRepository.findByMemberMemberIdWithConcern(
                        memberId, PageRequest.of(page, 5))
                .map(cl -> MyPageConcernLikeDto.builder()
                        .concernLikeId(cl.getConcernLikeId())
                        .concernId(cl.getConcern().getConcernId())
                        .concernTitle(cl.getConcern().getConcernTitle())
                        .build());
    }

    public Page<MyPageCommentLikeDto> getMyCommentLikes(Long memberId, int page) {
        return commentLikeRepository.findByMemberMemberIdWithComment(
                        memberId, PageRequest.of(page, 5))
                .map(cl -> MyPageCommentLikeDto.builder()
                        .commentLikeId(cl.getCommentLikeId())
                        .commentContent(cl.getComment().getCommentContent())
                        .build());
    }
}
