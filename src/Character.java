import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character implements methods{

    private int health;
    private int domainBar;
    private BufferedImage charImage;
    private double xCoord;
    private double yCoord;
    private boolean facingRight;
    private final double DISTANCE = 0.5;
    public Character(double xCoord, double yCoord, String file, boolean facingRight){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.facingRight = facingRight;
        health = 10;
        domainBar = 0;
        try{
            charImage = ImageIO.read(new File(file));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public BufferedImage getImage(){
        return charImage;
    }

    public double getXCoord(){
        return xCoord;
    }

    public double getYCoord(){
        return yCoord;
    }

    public int getHealth(){
        return health;
    }

    public int getDomainBar(){
        return domainBar;
    }

    public boolean isFacingRight(){
        return facingRight;
    }

    public void loseHealth(){
        health --;
    }

    public void chargeDomain(){
        domainBar ++;
    }

    public void resetDomainBar(){
        domainBar = 0;
    }

    public void moveLeft(){
        if(!(xCoord < 0)){
            xCoord -= DISTANCE;
        }
    }

    public void moveRight(){
        if(!(xCoord > 850)){
            xCoord += DISTANCE;
        }
    }

    public void moveUp(){
        if(!(yCoord < 0)){
            yCoord -= DISTANCE;
        }
    }

    public void moveDown() {
        if (!(yCoord > 435)) {
            yCoord += DISTANCE;
        }
    }

    public void changefacingDirection(){
        facingRight = !facingRight;
    }

    public Rectangle rect() {
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}

