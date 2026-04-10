package TripToN.TripToN.concern.service;

import TripToN.TripToN.AiResponse.service.AiResponseService;
import TripToN.TripToN.concern.dto.ConcernRequestDto;
import TripToN.TripToN.concern.entity.Concern;
import TripToN.TripToN.concern.repository.ConcernRepository;
import TripToN.TripToN.global.error.BusinessException;
import TripToN.TripToN.global.error.ErrorCode;
import TripToN.TripToN.global.security.CustomUserPrincipal;
import TripToN.TripToN.member.entity.Member;
import TripToN.TripToN.member.repository.MemberRepository;
import TripToN.TripToN.AiResponse.dto.AiResponseDto;
import TripToN.TripToN.AiResponse.entity.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
}
