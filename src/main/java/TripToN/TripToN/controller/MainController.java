package TripToN.TripToN.controller;

import TripToN.TripToN.domain.Concern;
import TripToN.TripToN.domain.Luggage;
import TripToN.TripToN.domain.LuggageType;
import TripToN.TripToN.service.LuggageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class MainController {

    private final LuggageService luggageService;


    //메인 페이지
    @GetMapping
    public String page1(){
        return "1_main";
    }

    //소개 페이지
    @GetMapping("/introduce")
    public String introduce(){
        return "2_introduce";
    }

    //상품 선택 페이지 조회
    @GetMapping("/select")
    public String select(){
        return "3_select";
    }

    //상품 선택 페이지에서 데이터 받아 처리
    @PostMapping("/concern")
    public String selectItem(@RequestParam String luggageType,
                             @RequestParam String concern,
                             @RequestParam String userName,
                             @RequestParam String password,
                             HttpSession session, Model model){
        try {
            //입력값 검증
            if (concern == null || concern.trim().isEmpty()) {
                model.addAttribute("error", "고민을 입력해주세요.");
                return "3_select";
            }
            if (userName == null || userName.trim().isEmpty()) {
                model.addAttribute("error", "사용자 이름을 입력해주세요.");
                return "3_select";
            }
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "비밀번호를 입력해주세요.");
                return "3_select";
            }

            //DDD 관점에서는 컨트롤러에서 도메인을 생성하면 안되지만, 단순한 프로젝트이기에 컨트롤러에서 도메인 객체 생성
            LuggageType convertluggageType = convertToLuggageType(luggageType); // luggageType 데이터 조회
            Concern requestedConcern = new Concern(userName, concern, password); // 고민 객체 생성

            //응답 생성 (gemini, default 방식 등..)
            String response = luggageService.generateResponse(requestedConcern);
            requestedConcern.assignResponse(response);

            //DB와 세션에 luggage 저장
            Luggage luggage = new Luggage(requestedConcern, convertluggageType);
            Luggage saveLuggage = luggageService.saveLuggage(luggage);

            //세션에 사용자 luggage 저장, model에 사용자 고민 담아 프론트로 전달
            session.setAttribute("saveLuggage", saveLuggage);
            model.addAttribute("concern", saveLuggage.getConcern());

            return "5_info";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "유효하지 않은 가방 타입입니다.");
            return "3_select";
        }
    }
    private LuggageType convertToLuggageType(String luggageString) {
        switch (luggageString) {
            case "LuggageA": return LuggageType.LUGGAGE;
            case "LuggageB": return LuggageType.CART;
            case "LuggageC": return LuggageType.BAG;
            default:
                throw new IllegalArgumentException("Unknown luggage type: " + luggageString);
        }
    }

    // 입력한 고민 페이지 조회
    @GetMapping("/info")
    public String showInfo(HttpSession session, Model model){
        Luggage luggage = (Luggage) session.getAttribute("saveLuggage");

        if (luggage == null || luggage.getConcern() == null) {
            return "redirect:/select";
        }

        model.addAttribute("concern", luggage.getConcern());
        return "5_info";
    }

    //결과 페이지 조회 (첫 페이지만 서버사이드 렌더링)
    @GetMapping("/result")
    public String result(Model model){
        org.springframework.data.domain.Page<Luggage> page = luggageService.findAllPaged(0, 5);
        model.addAttribute("luggageList", page.getContent());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "6_result";
    }

    //페이징 API (AJAX용)
    @GetMapping("/api/luggage")
    @ResponseBody
    public Map<String, Object> getLuggagePaged(@RequestParam(defaultValue = "0") int page) {
        org.springframework.data.domain.Page<Luggage> luggagePage = luggageService.findAllPaged(page, 5);
        List<Map<String, Object>> items = luggagePage.getContent().stream().map(l -> {
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("lid", l.getLID());
            item.put("username", l.getConcern().getUserName());
            item.put("luggageTypeOrdinal", l.getLuggageType().ordinal() + 1);
            item.put("luggageType", l.getLuggageType().name());
            return item;
        }).toList();

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("content", items);
        result.put("currentPage", luggagePage.getNumber());
        result.put("totalPages", luggagePage.getTotalPages());
        result.put("totalElements", luggagePage.getTotalElements());
        return result;
    }

    @GetMapping("/{lid}")
    public String getLuggageDetail(@PathVariable Long lid, Model model) {
        try {
            Luggage luggage = luggageService.findById(lid);
            model.addAttribute("luggage", luggage);
            return "luggage_detail";
        } catch (IllegalArgumentException e) {
            return "redirect:/result";
        }
    }

    //비밀번호 AJAX로 검증
    @PostMapping("/verify-password")
    @ResponseBody
    public Map<String, Boolean> verifyPassword(@RequestBody Map<String, String> request) {
        try {
            Long lid = Long.parseLong(request.get("lid"));
            String password = request.get("password");

            Luggage luggage = luggageService.findById(lid);
            boolean isValid = luggage.getConcern().matchPassword(password);

            return Map.of("success", isValid);
        } catch (Exception e) {
            return Map.of("success", false);
        }
    }

    // Luggage의 Concern response 반환
    @GetMapping("/api/luggage/{lid}/response")
    @ResponseBody
    public String getLuggageResponse(@PathVariable Long lid) {
        Luggage luggage = luggageService.findById(lid);
        if (luggage != null && luggage.getConcern() != null) {
            return luggage.getConcern().getResponse();
        }
        return "응답을 찾을 수 없습니다.";
    }
}
