import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class Character implements methods{

    private int health;
    private int domainBar;
    private BufferedImage charImage, charLeft, charRight;
    private double xCoord;
    private double yCoord;
    private boolean facingRight;
    private final double DISTANCE = 0.4;
    private long startTime;


    public Character(double xCoord, double yCoord, String file, String leftFile, String rightFile, boolean facingRight) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.facingRight = facingRight;
        health = 10;
        domainBar = 0;
        try {
            charImage = ImageIO.read(new File(file));
            charLeft = ImageIO.read(new File(leftFile));
            charRight = ImageIO.read(new File(rightFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public BufferedImage getImage() {
        return charImage;
    }

    public BufferedImage getLeft() {
        return charLeft;
    }

    public BufferedImage getRight() {
        return charRight;
    }

    public double getXCoord() {
        return xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }

    public int getHealth() {
        return health;
    }

    public int getDomainBar() {
        return domainBar;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void loseHealth() {
        health--;
    }

    public void chargeDomain() {
        domainBar++;
    }

    public void resetDomainBar() {
        domainBar = 0;
    }

    public void moveLeft() {
        if (!(xCoord < 0)) {
            xCoord -= DISTANCE;
        }
    }

    public void moveRight() {
        if (!(xCoord > 885)) {
            xCoord += DISTANCE;
        }
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public Rectangle rect() {
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }


    public boolean hasLanded(){
        return yCoord >= 300;
    }

    public void setYCoord(double yCoord){
        this.yCoord = yCoord;
    }

}

