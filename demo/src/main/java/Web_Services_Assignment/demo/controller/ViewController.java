package Web_Services_Assignment.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/customer")
    public String customerView() {
        return "customer";
    }

    @GetMapping("/operator")
    public String operatorView() {
        return "operator";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
