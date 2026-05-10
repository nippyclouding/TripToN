package server.TripToN.concern.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import server.TripToN.AiResponse.service.AiResponseService;
import server.TripToN.comment.dto.CommentResponseDto;
import server.TripToN.comment.entity.Comment;
import server.TripToN.commentLike.repository.CommentLikeRepository;
import server.TripToN.concern.dto.*;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;
import server.TripToN.AiResponse.dto.AiResponseDto;
import server.TripToN.AiResponse.entity.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcernService {
    private final ConcernRepository concernRepository;
    private final MemberRepository memberRepository;
    private final AiResponseService aiResponseService;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public AiResponseDto saveConcernAndGetAiResponse(ConcernRequestDto dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 1. DB에 고민 저장
        Concern concern = Concern.builder()
                .concernTitle(dto.getConcernTitle())
                .concernContent(dto.getConcernContent())
                .luggageType(dto.getLuggageType())
                .member(member)
                .build();
        concernRepository.save(concern);

        // 2. AI 응답 생성, 리턴
        AiResponse aiResponse = aiResponseService.getResponse(concern); // requires new
        // 응답 오류
        if (aiResponse == null) {
            return AiResponseDto.builder()
                    .responseContent("현재 AI 모델 문제로 응답할 수 없습니다.")
                    .createdAt(LocalDateTime.now())
                    .concernTitle(dto.getConcernTitle())
                    .concernContent(dto.getConcernContent())
                    .build();
        }
        // 정상 응답
        else return AiResponseDto.builder()
                .responseContent(aiResponse.getResponseContent())
                .createdAt(aiResponse.getCreatedAt())
                .concernTitle(dto.getConcernTitle())
                .concernContent(dto.getConcernContent())
                .build();
    }

    public Page<ConcernResponseDto> getConcerns(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        Page<Concern> concernPage = concernRepository.findAllWithMember(pageable);
        return concernPage.map(c -> ConcernResponseDto.from(c, c.getMember()));
    }

    public ConcernDetailResponseDto getConcernDetail(Long concernId, Long loginMemberId) {
        Concern concern = concernRepository.findConcernAndMemberAndCommentAndResponseByConcernId(concernId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONCERN_NOT_FOUND));

        return ConcernDetailResponseDto.builder()
                .memberId(concern.getMember().getMemberId())
                .memberNickName(concern.getMember().getMemberNickname())
                .concernTitle(concern.getConcernTitle())
                .concernContent(concern.getConcernContent())
                .luggageType(concern.getLuggageType())
                .createdAt(concern.getCreatedAt())
                .luggageTypeImageIndex(concern.getLuggageType().ordinal() + 1)
                .responseContent(concern.getAiResponse() != null
                        ? concern.getAiResponse().getResponseContent()
                        : "AI 응답 없음")
                .dtos(concern.getComments().stream()
                        .map(c -> {
                            CommentResponseDto dto = c.toDto();
                            dto.setLikeCount(commentLikeRepository.countByCommentCommentId(c.getCommentId()));
                            dto.setIsLiked(loginMemberId != null &&
                                    commentLikeRepository.existsByMemberMemberIdAndCommentCommentId(loginMemberId, c.getCommentId()));
                            return dto;
                        })
                        .toList())
                .build();

    }

    @Transactional
    public void updateConcern(Long concernId, Long memberId, ConcernUpdateRequestDto dto) {
        Concern findConcern = concernRepository.findByConcernIdAndDeletedAtIsNull(concernId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONCERN_NOT_FOUND));

        if (!memberId.equals(findConcern.getMember().getMemberId()))
            throw new BusinessException(ErrorCode.WRONG_ACCESS_UPDATE);

        findConcern.updateConcern(dto);
    }

    @Transactional
    public void removeConcern(Long concernId, Long memberId) {
        Concern findConcern = concernRepository.findByConcernIdAndDeletedAtIsNull(concernId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONCERN_NOT_FOUND));

        if (!memberId.equals(findConcern.getMember().getMemberId()))
            throw new BusinessException(ErrorCode.WRONG_ACCESS_DELETE);

        concernRepository.softDeleteById(concernId);
    }
}
