package com.unionman.verification.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.unionman.verification.dto.VerificationDTO;
import com.unionman.verification.utils.RedisUtils;
import com.unionman.verification.utils.VerifyImageUtils;
import com.unionman.verification.vo.ResponseVO;
import com.unionman.verification.vo.VerificationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("getPic")
    @ResponseBody
    public ResponseVO<VerificationVO> getPic(HttpServletRequest request) throws IOException {

        // 读取图库目录
        File imgCatalog = new File(ResourceUtils.getURL("classpath:").getPath() + "static\\img\\slider\\targets\\");
        File[] files = imgCatalog.listFiles();
        // 随机选择需要切的图
        int randNum = new Random().nextInt(files.length);
        File targetFile = files[randNum];
        // 随机选择剪切模版
        Random r = new Random();
        int num = r.nextInt(6) + 1;
        File tempImgFile = new File(ResourceUtils.getURL("classpath:").getPath() + "static\\img\\slider\\templates\\" + num
                + "-w.png");
        // 根据模板裁剪图片
        try {

            VerificationVO verificationVO = VerifyImageUtils.pictureTemplatesCut(tempImgFile, targetFile);
            // 生成流水号，这里就使用时间戳代替
            String lno =  String.valueOf(Calendar.getInstance().getTimeInMillis());

            redisUtils.set(lno, verificationVO.getXWidth(), 120);

            verificationVO.setCapcode(lno);

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

        ResponseVO responseVO;

        Object x = redisUtils.get(verificationDTO.getCapcode());

        if (x == null) {

            // 超期
            responseVO = ResponseVO.error(3, "验证码过时");
        } else if (verificationDTO.getXpos() - Integer.parseInt(x.toString()) > 5 || verificationDTO.getXpos() - Integer.parseInt(x.toString()) < -5) {

            // 验证失败
            responseVO = ResponseVO.error(2, "验证失败");
        } else {

            // 验证成功
            responseVO = ResponseVO.success();
        }

        redisUtils.del(verificationDTO.getCapcode());

        return responseVO;
    }



}
