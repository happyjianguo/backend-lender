package com.yqg.common.sender;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Image verification code tool class
 * Created by gao on 2018/6/22.
 */
public class ImgCaptchaUtil {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //Max randomized character length
    private static final int CODE_NUM = 4;
    private static Font myFont = new Font("Arial", Font.BOLD, 16);
    private static char[] charSequence = "0123456789abcdefghigklmnopqrstuvwxyz"
            .toCharArray();

    private static Random random = new Random();

    // Generate image captcha
    public static String [] generateImgCode() throws IOException {
        // Specify the size of the graphic verification code image
        int width = 80, height = 25;
        // Generate new image
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // Draw content
        Graphics g = image.getGraphics();
        g.setColor(getRandomColor(200, 250));
        g.fillRect(1, 1, width - 1, height - 1);
        g.setColor(new Color(102, 102, 102));
        g.drawRect(0, 0, width - 1, height - 1);
        g.setFont(myFont);
        //Randomly generate lines #1 to make the picture look more messy
        g.setColor(getRandomColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width - 1);// x starting point
            int y = random.nextInt(height - 1);// y starting point
            int x1 = random.nextInt(6) + 1;// x offset
            int y1 = random.nextInt(12) + 1;// y offset
            g.drawLine(x, y, x + x1, y + y1);
        }
        // Randomly generate lines #2 to make the picture look more messy
        for (int i = 0; i < 70; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int x1 = random.nextInt(12) + 1;
            int y1 = random.nextInt(6) + 1;
            g.drawLine(x, y, x - x1, y - y1);
        }

        // Generate random string
        StringBuilder imgCaptcha = new StringBuilder(CODE_NUM);
        for (int i = 0; i < CODE_NUM; i++) {
            String tmp = getRandomChar();
            imgCaptcha.append(tmp);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random
                    .nextInt(110), 20 + random.nextInt(110)));
            g.drawString(tmp, 15 * i + 10, 20);
        }
        g.dispose();
        
        // Write image output
        ByteArrayOutputStream output= new ByteArrayOutputStream ();
        ImageIO.write(image, "JPEG", output);
        byte[] img = output.toByteArray();
        String imgBase64 = Base64.encodeBase64String(img).replaceAll("\n", "");

        String [] imgInfo={imgBase64,imgCaptcha.toString()};
        return imgInfo;
    }

    private static Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static String getRandomChar() {
        int index = random.nextInt(charSequence.length);
        return String.valueOf(charSequence[index]);
    }

}
