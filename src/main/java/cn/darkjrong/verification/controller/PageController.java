package cn.darkjrong.verification.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Slf4j
@Controller
public class PageController {

    @RequestMapping("/slider")
    public String slider() {
        return "slider";
    }

    @GetMapping("/arit")
    public String arit() {
        return "arit";
    }




}
