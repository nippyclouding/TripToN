package server.TripToN.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.AiResponse.repository.AiRequestLogRepository;
import server.TripToN.admin.dto.*;
import server.TripToN.concern.repository.ConcernRepository;
import server.TripToN.concern.repository.ConcernAdminProjection;
import server.TripToN.member.repository.MemberLoginLogRepository;
import server.TripToN.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;
    private final ConcernRepository concernRepository;
    private final MemberLoginLogRepository memberLoginLogRepository;
    private final AiRequestLogRepository aiRequestLogRepository;

    public TotalCountResponseDto getTotalCount() {
        long totalAiRequestCountTodayCount = aiRequestLogRepository.countToday();
        long totalMember = memberRepository.count();
        long totalConcern = concernRepository.count();
        // concern 엔티티의 @SQLRestriction("deleted_at IS NULL") 로 삭제된 것은 집계 x

        return TotalCountResponseDto.builder()
                .totalAiRequestCountTodayCount(totalAiRequestCountTodayCount)
                .totalMemberCount(totalMember)
                .totalConcernCount(totalConcern)
                .build();
    }

    public Page<AdminMemberResponseDto> getMembers(int page) {
        return memberRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(AdminMemberResponseDto::from);
    }

    public Page<AdminConcernResponseDto> getConcerns(int page) {
        Page<ConcernAdminProjection> concernPage =
                concernRepository.findAllForAdmin(PageRequest.of(page, 10));
        return concernPage.map(AdminConcernResponseDto::from);
    }

    public Page<AdminMemberLoginLogResponseDto> getMemberLoginLogs(int page) {
        return memberLoginLogRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(AdminMemberLoginLogResponseDto::from);
    }

    public Page<AdminGeminiRequestLogResponseDto> getGeminiRequestLogs(int page) {
        return aiRequestLogRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(AdminGeminiRequestLogResponseDto::from);
    }
}
