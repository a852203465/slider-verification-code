package cn.darkjrong.verification.service.impl;

import cn.darkjrong.verification.common.exception.SliderVerificationCodeException;
import cn.darkjrong.verification.common.map.TimedMap;
import cn.darkjrong.verification.common.pojo.dto.SliderValidateDTO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.darkjrong.verification.common.utils.SliderImageUtils;
import cn.darkjrong.verification.service.SliderService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SliderServiceImpl implements SliderService {

    private final TimedMap<String, String> captchaStorage = new TimedMap<>();

    @Override
    public VerificationVO getCaptcha() {
        File imgCatalog = this.getFile("static/img/slider/targets");
        ArrayList<File> files = CollectionUtil.newArrayList(imgCatalog.listFiles());
        if (CollectionUtil.isEmpty(files)) {
            log.error("滑块验证码原始图片为空");
            throw new SliderVerificationCodeException(500, "滑块验证码原始图片为空");
        }

        int randNum = new Random().nextInt(files.size());
        File targetFile = files.get(randNum);

        String uuid = IdUtil.fastSimpleUUID();
        File tempImgFile = getFile("static/img/slider/templates/" + (new Random().nextInt(6) + 1) + "-w.png");
        try {
            VerificationVO verificationVO = SliderImageUtils.pictureTemplatesCut(tempImgFile, targetFile);
            verificationVO.setCaptchaId(uuid);
            captchaStorage.put(uuid, Convert.toStr(verificationVO.getXWidth()), 1800, TimeUnit.SECONDS);
            verificationVO.setXWidth(null);
            return verificationVO;
        }catch (Exception e) {
            log.error(String.format("ID【%s】滑块验证生成异常,【%s】", uuid, e.getMessage()), e);
            throw new SliderVerificationCodeException(500, "滑块验证生成异常");
        }
    }

    @Override
    public void validate(SliderValidateDTO sliderValidateDTO) {
        Double xPos = sliderValidateDTO.getXPos();
        String captchaId = sliderValidateDTO.getCaptchaId();

        String value = captchaStorage.get(captchaId);
        if (StrUtil.isBlank(value)) {
            log.error("验证码ID【{}】不存在答案", captchaId);
            throw new SliderVerificationCodeException(500, "验证码已过期,请重新获取");
        }
        Double vCode = Double.parseDouble(value);
        if (xPos - vCode > 5 || xPos - vCode < - 5) {
            log.error("验证码ID【{}】输入【{}】答案【{}】验证不通过", captchaId, xPos, vCode);
            throw new SliderVerificationCodeException(500, "验证码不正确,请重试");
        }
    }


    /**
     *  获取文件
     * @param dir 文件目录
     * @return 文件
     */
    private File getFile(String dir) {
        ClassPathResource classPathResource = new ClassPathResource(dir);
        return classPathResource.getFile();
    }









}
