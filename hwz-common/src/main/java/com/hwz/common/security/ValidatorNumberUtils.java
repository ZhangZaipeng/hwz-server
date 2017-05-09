/**
 * @(#)ValidatorNumberUtils.java Copyright 2011 jointown, Inc. All rights reserved.
 */
package com.hwz.common.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import org.apache.commons.lang3.StringUtils;

import com.hwz.platform.YvanUtil;

/**
 * 验证码创建、验证工具<br/>
 * 创建：将验证码生成图片，同时将验证码sha-1摘要写入会话cookie中<br/>
 * 验证：验证验证码时根据会话cookie的摘要进行验证<br/>
 *
 * @author jianguo.xu
 * @version 1.0, 2008-6-23
 */
public class ValidatorNumberUtils {

    /**
     * 验证码位数
     */
    private static final int NUMBER_COUNT = 4;
    private static final String VALIDATOR_COOKIE_NAME = "validator_number";
    /**
     * COOKIE过期时间
     */
    private static final long COOKIE_EXPIRE_TIME = 1000 * 60 * 20;
    private static final Random RANDOM = new Random();

    /**
     * 创建验证码<br/>
     * 会把验证码写入cookie中
     *
     * @param response
     * @throws java.io.IOException
     * @author jianguo.xu
     */
    public static final void create(HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        // 在内存中创建图象
        int width = 60, height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // 设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        // 随机产生55条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 55; i++) {
            int x = RANDOM.nextInt(width);
            int y = RANDOM.nextInt(height);
            int xl = RANDOM.nextInt(12);
            int yl = RANDOM.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String randomValue = getRandomValue(g);
        writeCookie(randomValue);

        // 图象生效
        g.dispose();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
        ImageIO.write(image, "PNG", response.getOutputStream());
        imageOut.close();
    }

    private static String getRandomValue(Graphics g) {
        String randomValue = "";
        // 取随机产生的认证码(4位数字)
        for (int i = 0; i < NUMBER_COUNT; i++) {
            String rand = String.valueOf(RANDOM.nextInt(10));
            randomValue += rand;
            // 将认证码显示到图象中
            g.setColor(new Color(20 + RANDOM.nextInt(110), 20 + RANDOM.nextInt(110), 20 + RANDOM.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(rand, 13 * i + 6, 16);
        }
        return randomValue;
    }

    private static void writeCookie(String randomValue) {
        YvanUtil.removeCookie(VALIDATOR_COOKIE_NAME, "/");
        Date date = new Date();
        String dateString = String.valueOf(date.getTime());
        String key = randomValue + dateString;
        String shaKey = YvanUtil.encodeBase64(DigestUtils.shaHex(key));
        Cookie cookie = new Cookie(VALIDATOR_COOKIE_NAME, shaKey + "_" + dateString);
        cookie.setPath("/");
        YvanUtil.writeCookie(cookie);
    }

    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 验证验证码<br/>
     * 从cookie中获取验证码并进行验证
     *
     * @param validatorCode
     * @return
     * @author jianguo.xu
     */
    public static final boolean validator(String validatorCode) {
        Cookie cookie = YvanUtil.getCookie(VALIDATOR_COOKIE_NAME);
        if (cookie == null || cookie.getValue() == null) return false;
        String cookieValue = cookie.getValue();
        String[] array = cookieValue.split("_");
        if (array.length != 2) return false;
        String cookieShaKey = array[0];
        String dateString = array[1];
        if (StringUtils.isEmpty(cookieShaKey) || StringUtils.isEmpty(dateString)) return false;
        long cookeDate = Long.parseLong(dateString);
        long nowDate = new Date().getTime();
        if (nowDate - cookeDate > COOKIE_EXPIRE_TIME) return false;
        if (StringUtils.isEmpty(validatorCode)) return false;
        String shaKey = YvanUtil.encodeBase64(DigestUtils.shaHex(validatorCode + dateString));
        return cookieShaKey.equals(shaKey) ? true : false;
    }
}
