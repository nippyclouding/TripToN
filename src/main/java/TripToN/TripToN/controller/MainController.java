package TripToN.TripToN.controller;

import TripToN.TripToN.domain.Color;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return "2_introduce";
    }

    @GetMapping("/select")
    public String select(){
        return "3_select";
    }

    @PostMapping("/select")
    public String selectItem(@RequestParam String luggage, HttpSession session, Model model){
        session.setAttribute("selectedLuggageType", luggage);
        return "4_concern";
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

        return "5_weight";
    }
    //DB에 고객이 선택한 가방을 넣은 뒤 무게 측정 페이지
    //무게 측정 : 일단 보류

    @GetMapping("/color")
    public String color(HttpSession session, Model model){

        return "6_color";
    }

    @PostMapping("/color")
    public String colorFix(@RequestParam("selectedColor") String selectedColor, HttpSession session, Model model) {

        // 세션에서 가방 타입 가져오기
        String luggageType = (String) session.getAttribute("selectedLuggageType");

        if (luggageType == null) {
            throw new IllegalArgumentException("세션이 만료되었습니다.");
        }

        // 문자열을 Color enum으로 변환
        Color color = convertStringToColor(selectedColor);

        // 가장 최근에 저장된 해당 타입의 Luggage 찾아서 색깔 설정
        List<Luggage> allLuggages = luggageService.findAll();

        for (int i = allLuggages.size() - 1; i >= 0; i--) {
            Luggage luggage = allLuggages.get(i);

            if ("LuggageA".equals(luggageType) && luggage instanceof LuggageA) {
                luggageService.updateColorA((LuggageA) luggage, color);
                break;
            } else if ("LuggageB".equals(luggageType) && luggage instanceof LuggageB) {
                luggageService.updateColorB((LuggageB) luggage, color);
                break;
            } else if ("LuggageC".equals(luggageType) && luggage instanceof LuggageC) {
                luggageService.updateColorC((LuggageC) luggage, color);
                break;
            }
        }

        model.addAttribute("selectedColor", selectedColor);
        return "9_transform";
    }
    // 색깔 변환 유틸리티 메서드
    private Color convertStringToColor(String colorString) {
        switch (colorString.toLowerCase()) {
            case "red":
                return Color.Red;
            case "green":
                return Color.Green;
            case "blue":
                return Color.Blue;
            default:
                throw new IllegalArgumentException("올바르지 않은 색깔입니다: " + colorString);
        }
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

        // 모델에 데이터 추가 (개인 결과만)
        if (userLuggage != null) {
            model.addAttribute("luggage", userLuggage);
            model.addAttribute("answer", userLuggage.getAnswer());
            model.addAttribute("userName", userLuggage.getUserName());
            model.addAttribute("concern", userLuggage.getConcern());
        } else {
            model.addAttribute("error", "결과를 찾을 수 없습니다.");
        }

        // allLuggages 모델 추가 제거됨 (전체 결과는 별도 페이지에서 처리)

        return "7_result";
    }


    @GetMapping("/find")
    public String find(Model model) {
        List<Luggage> allLuggages = luggageService.findAll();
        model.addAttribute("allLuggages", allLuggages);  // ← "luggages"를 "allLuggages"로 변경
        return "8_find";
    }



    @PostMapping("/verify-password-ajax")
    @ResponseBody
    public Map<String, Object> verifyPasswordAjax(@RequestParam Long luggageId,
                                                  @RequestParam String inputPassword) {
        Map<String, Object> response = new HashMap<>();

        try {
            Luggage luggage = luggageService.findById(luggageId);

            if (luggage.getPassword().equals(inputPassword)) {
                response.put("success", true);
                response.put("message", "비밀번호 인증 성공");
            } else {
                response.put("success", false);
                response.put("message", "비밀번호가 틀렸습니다");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "데이터를 찾을 수 없습니다");
        }

        return response;
    }


}
