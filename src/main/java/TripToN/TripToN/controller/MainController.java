package TripToN.TripToN.controller;

import TripToN.TripToN.domain.Concern;
import TripToN.TripToN.domain.luggages.*;
import TripToN.TripToN.repository.LuggageRepository;
import TripToN.TripToN.service.LuggageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class MainController {

    private final LuggageService luggageService;
    private final LuggageRepository luggageRepository;

    @GetMapping
    public String page1(){
        return "1_main";
    }

    @GetMapping("/introduce")
    public String introduce(){
        return "introduce/introduce";
    }

    @GetMapping("/select")
    public String select(){
        return "2_select";
    }

    @PostMapping("/select")
    public String selectItem(@RequestParam String luggage, HttpSession session, Model model){
        session.setAttribute("selectedLuggageType", luggage);
        return "3_concern";
    }

    //3_concern에서 submit을 누르면 요청받는 컨트롤러
    @PostMapping("/concern")
    public String concern(@ModelAttribute Concern concern,HttpSession session, Model model) {

        String luggageType = (String) session.getAttribute("selectedLuggageType");

        //concern 검증
        if(concern.getUserName() == null || concern.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름이 필요합니다");
        }
        if(concern.getConcern() == null || concern.getConcern().trim().isEmpty()) {
            throw new IllegalArgumentException("고민이 필요합니다");
        }
        if(concern.getPassword() == null || concern.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 필요합니다");
        }

        String userName = concern.getUserName();
        String password = concern.getPassword();
        String problem = concern.getConcern();

        if("LuggageA".equals(luggageType)){
            LuggageA luggageA = new LuggageA(userName, problem, password);

            luggageService.put(luggageA);
        }
        else if("LuggageB".equals(luggageType)){
            LuggageB luggageB = new LuggageB(userName, problem, password);

            luggageService.put(luggageB);
        }
        else if("LuggageC".equals(luggageType)){
            LuggageC luggageC = new LuggageC(userName, problem, password);

            luggageService.put(luggageC);
        }

        return "4_transform";
    }

    @GetMapping("/result")
    public String result(HttpSession session, Model model) {
        // 세션에서 가방 타입 가져오기
        String luggageType = (String) session.getAttribute("selectedLuggageType");

        if (luggageType == null) {
            throw new IllegalArgumentException("세션이 만료되었습니다. 처음부터 다시 시작해주세요.");
        }

        // 가장 최근에 저장된 해당 타입의 Luggage 찾기
        List<Luggage> allLuggages = luggageService.findAll();
        Luggage userLuggage = null;

        for (int i = allLuggages.size() - 1; i >= 0; i--) {
            Luggage luggage = allLuggages.get(i);

            if ("LuggageA".equals(luggageType) && luggage instanceof LuggageA) {
                userLuggage = luggage;
                break;
            } else if ("LuggageB".equals(luggageType) && luggage instanceof LuggageB) {
                userLuggage = luggage;
                break;
            } else if ("LuggageC".equals(luggageType) && luggage instanceof LuggageC) {
                userLuggage = luggage;
                break;
            }
        }

        // 모델에 데이터 추가
        if (userLuggage != null) {
            model.addAttribute("luggage", userLuggage);
            model.addAttribute("answer", userLuggage.getAnswer());
            model.addAttribute("userName", userLuggage.getUserName());
            model.addAttribute("concern", userLuggage.getConcern());
        } else {
            model.addAttribute("error", "결과를 찾을 수 없습니다.");
        }

        return "5_result";
    }


    @GetMapping("/find")
    public String find(Model model) {
        // 모든 luggage 조회
        List<Luggage> allLuggages = luggageService.findAll();
        model.addAttribute("luggages", allLuggages);
        return "6_find";
    }

    @PostMapping("/verify-password")
    public String verifyPassword(@RequestParam Long luggageId,
                                 @RequestParam String inputPassword,
                                 HttpSession session,
                                 Model model) {

        try {
            Luggage luggage = luggageService.findById(luggageId);

            // 비밀번호 검증
            if (luggage.getPassword().equals(inputPassword)) {
                // 성공: 세션에 luggage ID 저장하고 myData로 이동
                session.setAttribute("verifiedLuggageId", luggageId);
                return "redirect:/myData";
            } else {
                // 실패: 에러 메시지와 함께 다시 6_find로
                List<Luggage> allLuggages = luggageService.findAll();
                model.addAttribute("luggages", allLuggages);
                model.addAttribute("error", "틀렸습니다");
                model.addAttribute("failedLuggageId", luggageId); // 실패한 luggage 표시용
                return "6_find";
            }
        } catch (Exception e) {
            model.addAttribute("error", "데이터를 찾을 수 없습니다");
            return "6_find";
        }
    }

    @GetMapping("/myData")
    public String myData(HttpSession session, Model model) {
        Long luggageId = (Long) session.getAttribute("verifiedLuggageId");

        if (luggageId == null) {
            return "redirect:/find"; // 인증되지 않으면 find로 돌려보냄
        }

        try {
            Luggage luggage = luggageService.findById(luggageId);
            model.addAttribute("luggage", luggage);
            return "myData"; // myData.html 페이지
        } catch (Exception e) {
            model.addAttribute("error", "데이터를 불러올 수 없습니다");
            return "6_find";
        }
    }


}
