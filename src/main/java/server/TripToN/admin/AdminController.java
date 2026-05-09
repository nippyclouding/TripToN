package server.TripToN.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import server.TripToN.admin.dto.TotalCountResponseDto;

@RequiredArgsConstructor
@RequestMapping("/admin-console")
@Controller
public class AdminController {
    /*
    관리자 페이지 항목
    count - 총 회원 수, 총 고민 수 (삭제된 데이터는 배제) 조회 api
    count - 일별 Gemini api 요청 횟수 조회 - ai_api_request_log_id 테이블

    전체 회원, 전체 고민 (페이징 처리)

    회원 접속 로그 조회 (페이징 처리) - login_log 테이블
    gemini 요청 로그 조회 (페이징 처리) - ai request log 테이블
     */


    private final AdminService adminService;

    @GetMapping
    public String admin() { return "admin"; }

    // 총 회원 수, 총 고민 수 (삭제된 데이터는 배제), 일별 Gemini api 요청 횟수 조회 api
    @GetMapping("/totalMember-totalConcern-Count")
    public ResponseEntity<TotalCountResponseDto> getTotalCount() {
        return ResponseEntity.ok(adminService.getTotalCount());
    }

    // 회원 목록 조회
    @GetMapping("/members")
    public ResponseEntity<?> getMembers(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(adminService.getMembers(page));
    }

    @GetMapping("/concerns")
    public ResponseEntity<?> getConcerns(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(adminService.getConcerns(page));
    }

    @GetMapping("/member-login-logs")
    public ResponseEntity<?> getMemberLoginLogs(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(adminService.getMemberLoginLogs(page));
    }

    @GetMapping("/gemini-request-logs")
    public ResponseEntity<?> getGeminiRequestLogs(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(adminService.getGeminiRequestLogs(page));
    }

}
