package server.TripToN.global.util;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @GetMapping
    public String main(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute(Const.MEMBER_SESSION_KEY) != null);
        return "1_main";
    }
    @GetMapping("/introduce")
    public String story() { return "2_introduce"; }
    @GetMapping("/choose")
    public String choose() { return "3_select"; }

    @GetMapping("/signup")
    public String signup() { return "signup"; }

    @GetMapping("/result")
    public String result() { return "redirect:/concern"; }
}
