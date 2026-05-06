package server.TripToN.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {
    // 총 회원 수, 총 고민 수 (삭제된 데이터는 배제) 조회 api
    // 회원 접속 로그 api 조회 (페이징 처리)
    // 일별 Gemini api 요청 횟수 조회

    private final AdminService adminService;

}
