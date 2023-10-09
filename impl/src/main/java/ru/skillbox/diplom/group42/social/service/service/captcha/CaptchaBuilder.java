package ru.skillbox.diplom.group42.social.service.service.captcha;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Data
public class CaptchaBuilder {

    @Value("${vars.captchaCharStr}")
    private String elegibleChars = "0123456789";
    private Color backgroundColor = Color.white;

    private Color borderColor = Color.black;
    private Color textColor = Color.black;
    private Color circleColor = new Color(190, 160, 150);
    private Font textFont = new Font("Verdana", Font.BOLD, 20);
    private int charsToPrint = 4;
    private int width = 160;
    private int height = 50;
    private int circlesToDraw = 25;
    private float horizMargin = 10.0f;
    private double rotationRange = 0.7;
    private Graphics2D g;
    private String captchaString;
    private BufferedImage bufferedImage;

    public CaptchaBuilder setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
    public CaptchaBuilder setTextFont(Font textFont) {
        this.textFont = textFont;
        return this;
    }

    public BufferedImage build(){
        generateBackground();
        generateCaptcha();
        System.out.println(captchaString);
        return bufferedImage;
    }

    private void generateBackground(){
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
        g.setColor(circleColor);
        for (int i = 0; i < circlesToDraw; i++) {
            int L = (int) (Math.random() * height / 2.0);
            int X = (int) (Math.random() * width - L);
            int Y = (int) (Math.random() * height - L);
            g.draw3DRect(X, Y, L * 2, L * 2, true);
        }
    }
    private void generateCaptcha(){
        g.setColor(textColor);
        g.setFont(textFont);
        FontMetrics fontMetrics = g.getFontMetrics();
        int maxAdvance = fontMetrics.getMaxAdvance();
        int fontHeight = fontMetrics.getHeight();
        char[] chars = elegibleChars.toCharArray();
        float spaceForLetters = -horizMargin * 2 + width;
        float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);
        StringBuffer finalString = new StringBuffer();
        for (int i = 0; i < charsToPrint; i++) {
          processChar(i,finalString,chars,fontMetrics,maxAdvance,fontHeight,spacePerChar);
        }
        g.setColor(borderColor);
        g.drawRect(0, 0, width - 1, height - 1);
        g.dispose();
        captchaString = finalString.toString();
    }

    private void processChar(int i, StringBuffer finalString,char[] chars,FontMetrics fontMetrics
            ,int maxAdvance,int fontHeight,float spacePerChar){
        double randomValue = Math.random();
        int randomIndex = (int) Math.round(randomValue * (chars.length - 1));
        char characterToShow = chars[randomIndex];
        finalString.append(characterToShow);
        int charWidth = fontMetrics.charWidth(characterToShow);
        int charDim = Math.max(maxAdvance, fontHeight);
        int halfCharDim = (int) (charDim / 2);
        BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
        Graphics2D charGraphics = charImage.createGraphics();
        charGraphics.translate(halfCharDim, halfCharDim);
        double angle = (Math.random() - 0.5) * rotationRange;
        charGraphics.transform(AffineTransform.getRotateInstance(angle));
        charGraphics.translate(-halfCharDim, -halfCharDim);
        charGraphics.setColor(textColor);
        charGraphics.setFont(textFont);
        int charX = (int) (0.5 * charDim - 0.5 * charWidth);
        charGraphics.drawString("" + characterToShow, charX, (int) ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));
        float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
        int y = (int) ((height - charDim) / 2);
        g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
        charGraphics.dispose();
    }

}
