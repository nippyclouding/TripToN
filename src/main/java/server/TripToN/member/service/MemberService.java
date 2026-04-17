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
import server.TripToN.member.dto.*;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ConcernRepository concernRepository;
    private final CommentRepository commentRepository;
    private final ConcernLikeRepository concernLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    public void signUp(SignUpRequestDto dto) {
        Member member = Member.builder()
                .memberEmail(dto.getMemberEmail())
                .memberLoginPassword(dto.getMemberLoginPassword())
                .memberNickName(dto.getMemberNickName())
                .build();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member signIn(SignInRequestDto dto) {
        Optional<Member> memberOpt = memberRepository.findByMemberEmail(dto.getMemberEmail());

        if (memberOpt.isEmpty()) return null;

        Member member = memberOpt.get();
        if (member.getMemberLoginPassword().equals(dto.getMemberLoginPassword())) return member;
        return null;
    }

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPage(Long memberId,
                                       int concernPage,
                                       int commentPage,
                                       int concernLikePage,
                                       int commentLikePage) {
        // 회원 정보
        // 회원이 작성한 고민 리스트
        // 회원이 작성한 댓글 리스트
        // 회원이 좋아요한 고민
        // 회원이 좋아요한 댓글

        Member member = memberRepository.findById(memberId).orElseThrow();

        Page<Concern> concerns = concernRepository.findByMemberMemberId(
                memberId, PageRequest.of(concernPage, 5, Sort.by("createdAt").descending()));

        Page<Comment> comments = commentRepository.findByMemberMemberId(
                memberId, PageRequest.of(commentPage, 5, Sort.by("createdAt").descending()));

        Page<ConcernLike> concernLikes = concernLikeRepository.findByMemberMemberIdWithConcern(
                memberId, PageRequest.of(concernLikePage, 5));

        Page<CommentLike> commentLikes = commentLikeRepository.findByMemberMemberIdWithComment(
                memberId, PageRequest.of(commentLikePage, 5));

        return MyPageResponseDto.builder()
                .memberEmail(member.getMemberEmail())
                .memberNickName(member.getMemberNickName())

                .concernDtos(concerns.getContent().stream()
                        .map(c -> MyPageConcernResponseDto.builder()
                                .concernId(c.getConcernId())
                                .concernTitle(c.getConcernTitle())
                                .isLocked(c.isLocked())
                                .luggageType(c.getLuggageType())
                                .createdAt(c.getCreatedAt())
                                .build())
                        .toList())
                .concernTotalPages(concerns.getTotalPages())

                .commentDtos(comments.getContent().stream()
                        .map(c -> MyPageCommentResponseDto.builder()
                                .commentId(c.getCommentId())
                                .commentContent(c.getCommentContent())
                                .createdAt(c.getCreatedAt())
                                .build())
                        .toList())
                .commentTotalPages(comments.getTotalPages())

                .concernLikeDtos(concernLikes.getContent().stream()
                        .map(cl -> MyPageConcernLikeDto.builder()
                                .concernLikeId(cl.getConcernLikeId())
                                .concernId(cl.getConcern().getConcernId())
                                .concernTitle(cl.getConcern().getConcernTitle())
                                .build())
                        .toList())
                .concernLikeTotalPages(concernLikes.getTotalPages())

                .commentLikeDtos(commentLikes.getContent().stream()
                        .map(cl -> MyPageCommentLikeDto.builder()
                                .commentLikeId(cl.getCommentLikeId())
                                .commentContent(cl.getComment().getCommentContent())
                                .build())
                        .toList())
                .commentLikeTotalPages(commentLikes.getTotalPages())

                .build();
    }
}
