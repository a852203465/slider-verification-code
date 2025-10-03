package cn.darkjrong.verification.service.impl;

import cn.darkjrong.verification.common.exception.SliderVerificationCodeException;
import cn.darkjrong.verification.common.map.TimedMap;
import cn.darkjrong.verification.common.pojo.dto.ArithmeticValidateDTO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.darkjrong.verification.service.ArithmeticService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 算术服务实现类
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Slf4j
@Service
public class ArithmeticServiceImpl implements ArithmeticService {

    private static final String[] OPERATORS = {"+", "-", "*", "/"};

    private final TimedMap<String, String> captchaStorage = new TimedMap<>();

    @Override
    public VerificationVO getCaptcha() {
        MathExpression mathExpression = genMathExpression();
        String imageBase64 = generateCaptchaImage(mathExpression.getExpression());

        String captchaId = IdUtil.fastSimpleUUID();
        captchaStorage.put(captchaId, Convert.toStr(mathExpression.getResult()), 1800, TimeUnit.SECONDS);

        VerificationVO verificationVO = new VerificationVO();
        verificationVO.setCaptchaId(captchaId);
        verificationVO.setOriginalImage(imageBase64);
        return verificationVO;
    }

    @Override
    public void validate(ArithmeticValidateDTO arithmeticValidateDTO) {
        String captchaId = arithmeticValidateDTO.getCaptchaId();
        String userAnswer = arithmeticValidateDTO.getUserAnswer();

        String correctAnswer = captchaStorage.get(captchaId);
        if (StrUtil.isBlank(correctAnswer)) {
            log.error("验证码ID【{}】的答案为空", captchaId);
            throw new SliderVerificationCodeException(500, "验证码已过期,请重新获取");
        }

        if (!StrUtil.equals(userAnswer, correctAnswer)) {
            log.error("验证码ID【{}】输入【{}】答案【{}】不一致", captchaId, userAnswer, correctAnswer);
            throw new SliderVerificationCodeException(500, "验证码不正确,请重新输入");
        }
    }

    /**
     * 生成随机数学表达式和答案
     * @return MathExpression 包含表达式和答案的对象
     */
    private static MathExpression genMathExpression() {
        int a = RandomUtil.randomInt(20) + 1; // 1-20
        int b = RandomUtil.randomInt(20) + 1; // 1-20

        ExpressionParser parser = new SpelExpressionParser();

        String operator = RandomUtil.randomEle(OPERATORS);
        String result = null;
        if (operator.equals("/")) {
            result = parser.parseExpression(a + operator + Convert.toDouble(b)).getValue(String.class);
        } else {
            result = parser.parseExpression(a + operator + b).getValue(String.class);
        }
        String expression = a + " " + operator + " " + b + " = ?";
        log.info("表达式【{}】计算结果【{}】", expression, result);
        return new MathExpression(expression, result);
    }

    /**
     * 生成验证码图片
     * @param expression 数学表达式
     * @return Base64编码的图片字符串
     */
    private static String generateCaptchaImage(String expression) {
        int width = 120;
        int height = 40;

        // 创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // 绘制干扰线（让验证码更难被机器识别）
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制表达式文字
        g.setColor(Color.BLACK);
        g.drawString(expression, 10, 25);

        g.dispose();

        byte[] bytes = ImgUtil.toBytes(image, ImgUtil.IMAGE_TYPE_PNG);
        return "data:image/png;base64," + Base64.encode(bytes);
    }

    /**
     * 内部类用于存储表达式和结果
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MathExpression {
        private String expression;
        private String result;
    }










}
