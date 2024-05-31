import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class GraphicsPanel extends JPanel implements KeyListener, ActionListener {

    private BufferedImage background, gojoIcon, sukunaIcon;
    private Character playerOne, playerTwo;
    private boolean[] pressedKeys;
    private Timer timer;
    private int time;
    private Clip gojoClip, sukunaClip, bothClip;
    private boolean playGojoDomain, playSukunaDomain, playBothDomain;
    private boolean gojoFirstLoop;
    private boolean rightPurpleHollowActive, leftPurpleHollowActive;
    private Projectile purpleHollow;
    private static int gojoIncrementer = 1;

    public GraphicsPanel() {
        playerOne = new Character(20, 380, "assets/gojo.png", "assets/gojo.png", "assets/gojo.png", true);
        playerTwo = new Character(825, 380, "assets/sukuna.png", "assets/sukunaleft.png", "assets/sukunaright.png", false);
        addKeyListener(this);
        pressedKeys = new boolean[128];
        setFocusable(true);
        time = 0;
        timer = new Timer(1000, this);
        playGojoDomain = true;
        playSukunaDomain = true;
        playBothDomain = true;
        try {
            background = ImageIO.read(new File("assets/background.png"));
            gojoIcon = ImageIO.read(new File("assets/gojoicon.png"));
            sukunaIcon = ImageIO.read(new File("assets/sukunaicon.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        gojoFirstLoop = true;
        rightPurpleHollowActive = false;
        leftPurpleHollowActive = false;
        purpleHollow = new Projectile(playerOne.getXCoord(), playerOne.getYCoord(), "assets/purplehollow.png");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // setting up the background, stats, and characters
        g.drawImage(background, 0, 0, null);
        if (playerOne.isFacingRight()) {
            g.drawImage(playerOne.getRight(), (int) playerOne.getXCoord(), (int) playerOne.getYCoord(), null);
        } else {
            g.drawImage(playerOne.getLeft(), (int) playerOne.getXCoord(), (int) playerOne.getYCoord(), null);
        }
        if (playerTwo.isFacingRight()) {
            g.drawImage(playerTwo.getRight(), (int) playerTwo.getXCoord(), (int) playerTwo.getYCoord(), null);
        } else {
            g.drawImage(playerTwo.getLeft(), (int) playerTwo.getXCoord(), (int) playerTwo.getYCoord(), null);
        }
        g.drawImage(gojoIcon, 10, 20, null);
        g.drawImage(sukunaIcon, 875, 20, null);
        g.setFont(new Font("Courier New", Font.ITALIC, 24));

        // writing names
        g.setColor(Color.BLUE);
        g.drawString("Gojo Satoru", 100, 40);
        g.drawRect(10, 20, 75, 73);
        g.drawRect(9, 19, 76, 74);
        g.drawRect(10, 20, 76, 74);
        g.setColor(Color.RED);
        g.drawString("Ryomen Sukuna", 680, 40);
        g.drawRect(875, 20, 75, 76);
        g.drawRect(874, 19, 76, 77);
        g.drawRect(875, 20, 76, 77);

        // drawing health bars
        g.setColor(Color.BLACK);
        g.drawRect(100, 50, 200, 20);
        g.drawRect(660, 50, 200, 20);
        g.setColor(Color.RED);
        int startXOne = 100;
        for (int i = 0; i < playerOne.getHealth(); i++) {
            g.fillRect(startXOne, 50, 20, 20);
            startXOne += 20;
        }
        int startXTwo = 840;
        for (int i = 0; i < playerTwo.getHealth(); i++) {
            g.fillRect(startXTwo, 50, 20, 20);
            startXTwo -= 20;
        }

        // drawing domain bars
        g.setColor(Color.BLACK);
        g.drawRect(100, 75, 200, 20);
        g.setColor(Color.YELLOW);
        startXOne = 100;
        for (int i = 0; i < playerOne.getDomainBar(); i++) {
            g.fillRect(startXOne, 75, 65, 20);
            startXOne += 45;
        }
        g.setColor(Color.BLACK);
        g.drawRect(660, 75, 200, 20);
        g.setColor(Color.YELLOW);
        startXTwo = 795;
        for (int i = 0; i < playerTwo.getDomainBar(); i++) {
            g.fillRect(startXTwo, 75, 65, 20);
            startXTwo -= 45;
        }

        // playerOne movement keys
        if (pressedKeys[65]) {
            playerOne.faceLeft();
            playerOne.moveLeft();
        }
        if (pressedKeys[68]) {
            playerOne.faceRight();
            playerOne.moveRight();
        }
        if (pressedKeys[87]) {
            playerOne.moveUp();
        }
        if (pressedKeys[83]) {
            playerOne.moveDown();
        }

        // playerTwo movement keys
        if (pressedKeys[37]) {
            playerTwo.faceLeft();
            playerTwo.moveLeft();
        }
        if (pressedKeys[39]) {
            playerTwo.faceRight();
            playerTwo.moveRight();
        }
        if (pressedKeys[38]) {
            playerTwo.moveUp();
        }
        if (pressedKeys[40]) {
            playerTwo.moveDown();
        }

        // player one attack key
        if (pressedKeys[69]) {
            rightPurpleHollowActive = true;
        }
        if (rightPurpleHollowActive) {
            if (gojoFirstLoop) {
                purpleHollow.setXCoord(playerOne.getXCoord());
                purpleHollow.setYCoord(playerOne.getYCoord());
                gojoFirstLoop = false;
            }
            purpleHollow.setYCoord(purpleHollow.getYCoord());
            g.drawImage(purpleHollow.getImage(), (int) purpleHollow.getXCoord() + gojoIncrementer, (int) purpleHollow.getYCoord(), null);
            gojoIncrementer += 1;
            if (purpleHollow.rect().intersects(playerTwo.rect())) {
                rightPurpleHollowActive = false;
                gojoIncrementer = 1;
                playerTwo.loseHealth();
                if (playerOne.getDomainBar() < 4) {
                    playerOne.chargeDomain();
                }
                gojoFirstLoop = true;
            } else if (purpleHollow.getXCoord() + gojoIncrementer > 850) {
                rightPurpleHollowActive = false;
                gojoIncrementer = 1;
                gojoFirstLoop = true;
                purpleHollow.setXCoord(0);
                purpleHollow.setYCoord(0);
            }
        }

        
    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() instanceof Timer) {
            time++;
        }
    }

    private void playGojoMusic(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/gojodomainmusic.wav").getAbsoluteFile());
            gojoClip = AudioSystem.getClip();
            gojoClip.open(audioInputStream);
            gojoClip.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void playSukunaMusic(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/sukunadomainmusic.wav").getAbsoluteFile());
            sukunaClip = AudioSystem.getClip();
            sukunaClip.open(audioInputStream);
            sukunaClip.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void playBothMusic(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/bothdomainmusic.wav").getAbsoluteFile());
            bothClip = AudioSystem.getClip();
            bothClip.open(audioInputStream);
            bothClip.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static int getGojoIncrementer(){
            return gojoIncrementer;
    }

}
