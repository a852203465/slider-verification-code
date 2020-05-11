package cn.darkjrong.verification.controller;

import cn.darkjrong.verification.common.pojo.dto.VerificationDTO;
import cn.darkjrong.verification.common.utils.RedisUtils;
import cn.darkjrong.verification.common.utils.VerifyImageUtils;
import cn.darkjrong.verification.common.pojo.vo.ResponseVO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 滑块验证码控制层
 * @author Rong.Jia
 * @date 2019/12/11 22:03
 */
@Slf4j
@Controller
public class SliderController {

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("getPic/{account}")
    @ResponseBody
    public ResponseVO<VerificationVO> getPic(@PathVariable("account") String account, HttpServletRequest request) throws IOException {

        // 读取图库目录
        ClassPathResource classPathResource = new ClassPathResource("static/img/slider/targets");
        File imgCatalog = classPathResource.getFile();
        File[] files = imgCatalog.listFiles();
        // 随机选择需要切的图
        int randNum = new Random().nextInt(files.length);
        File targetFile = files[randNum];
        // 随机选择剪切模版
        Random r = new Random();
        int num = r.nextInt(6) + 1;
        ClassPathResource classPathResource1 = new ClassPathResource("static/img/slider/templates/" + num+ "-w.png");
        File tempImgFile = classPathResource1.getFile();

        // 根据模板裁剪图片
        try {

            VerificationVO verificationVO = VerifyImageUtils.pictureTemplatesCut(tempImgFile, targetFile);

            redisUtils.set(account, verificationVO.getXWidth(), 120);

            // 移除横坐标送前端
            verificationVO.setXWidth(null);

            return ResponseVO.success(verificationVO);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @PostMapping("checkcapcode")
    public ResponseVO checkCapcode(@RequestBody VerificationDTO verificationDTO) {

        log.info("checkCapcode {}", verificationDTO.toString());

        String username = verificationDTO.getUsername();
        String password = verificationDTO.getPassword();

        if (StrUtil.isBlank(username)) {

            log.error("账号为空");
            return ResponseVO.error(4, "账号为空");
        }

        if (!StrUtil.equals("admin", verificationDTO.getUsername())) {

            log.error("账号不正确");
            return ResponseVO.error(8, "账号不正确");
        }

        if (StrUtil.isBlank(password)) {

            log.error("密码为空");
            return ResponseVO.error(5, "密码为空");
        }

        if (!StrUtil.equals("123456", verificationDTO.getPassword())) {

            log.error("密码不正确");
            return ResponseVO.error(6, "密码不正确");
        }

        if (verificationDTO.getXpos() == null) {
             log.error("验证码为空");
            return ResponseVO.error(7, "验证码为空");
        }

        Object x = redisUtils.get(username);

        if (x == null) {

            // 超期
            return ResponseVO.error(3, "验证码过时");
        } else if (verificationDTO.getXpos() - Double.parseDouble(x.toString()) > 5 || verificationDTO.getXpos() - Double.parseDouble(x.toString()) < - 5) {

            // 验证失败
            return ResponseVO.error(2, "验证失败");
        } else {

            // 验证成功
            redisUtils.del(username);
            return ResponseVO.success();
        }

    }



}
