package dggs.organizing_work_boot.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mainController {
    @GetMapping("/")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
