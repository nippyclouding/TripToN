package server.TripToN.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @GetMapping
    public String main() {
        return "1_main";
    }
    @GetMapping("/story")
    public String story() { return "2_introduce"; }
    @GetMapping("/choose")
    public String choose() { return "3_select"; }

    @GetMapping("/signup")
    public String signup() { return "signup"; }

    @GetMapping("/result")
    public String result() { return "6_result"; }
}
