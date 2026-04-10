package server.TripToN.concern.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import server.TripToN.AiResponse.service.AiResponseService;
import server.TripToN.concern.dto.ConcernRequestDto;
import server.TripToN.concern.dto.ConcernResponseDto;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.global.security.CustomUserPrincipal;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;
import server.TripToN.AiResponse.dto.AiResponseDto;
import server.TripToN.AiResponse.entity.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConcernService {
    private final ConcernRepository concernRepository;
    private final MemberRepository memberRepository;
    private final AiResponseService aiResponseService;

    @Transactional
    public AiResponseDto saveConcernAndGetAiResponse(ConcernRequestDto dto) {
        // 회원 조회
        CustomUserPrincipal principal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = principal.getId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUNT_ERROR));

        // 1. DB에 고민 저장
        Concern concern = Concern.builder()
                .concernTitle(dto.getConcernTitle())
                .concernContent(dto.getConcernContent())
                .isLocked(dto.isLocked())
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
                    .build();
        }
        // 정상 응답
        else return AiResponseDto.builder()
                .responseContent(aiResponse.getResponseContent())
                .createdAt(aiResponse.getCreatedAt())
                .build();
    }

    public List<ConcernResponseDto> getConcerns(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        Page<Concern> concernPage = concernRepository.findAllWithMember(pageable);
        return concernPage.getContent().stream()
                .map(c -> ConcernResponseDto.from(c, c.getMember()))
                .toList();
    }
}
