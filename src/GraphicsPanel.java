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
import java.util.ArrayList;
import javax.sound.sampled.*;

public class GraphicsPanel extends JPanel implements KeyListener, ActionListener {

    private BufferedImage background, backupBackground, gojoIcon, sukunaIcon, gojoDomain, sukunaDomain, domainClash;
    private Character playerOne, playerTwo;
    private boolean[] pressedKeys;
    private boolean leftPurpleHollowActive, rightPurpleHollowActive, leftDismantleActive, rightDismantleActive;
    private Projectile leftPurpleHollow, rightPurpleHollow, rightDismantle, leftDismantle;
    private double oneLeftIncrementer, oneRightIncrementer, twoRightIncrementer, twoLeftIncrementer;
    private ArrayList<Point> oneLeftList, oneRightList, twoLeftList, twoRightList;
    private boolean gojoDomainActive, sukunaDomainActive;
    private Timer timer;
    private int time;
    private Clip gojoClip, sukunaClip, bothClip;
    private boolean playGojoDomain, playSukunaDomain, playBothDomain;

    public GraphicsPanel(){
        playerOne = new Character(20, 380, "assets/gojo.png", true);
        playerTwo = new Character(825, 380, "assets/sukuna.png", false);
        leftPurpleHollow = new Projectile(0, 0, "assets/purplehollow.png");
        rightPurpleHollow = new Projectile(0, 0, "assets/purplehollow.png");
        leftDismantle = new Projectile(0, 0, "assets/dismantleleft.png");
        rightDismantle = new Projectile(0, 0, "assets/dismantleright.png");
        addKeyListener(this);
        pressedKeys = new boolean[128];
        setFocusable(true);
        leftPurpleHollowActive = false;
        rightPurpleHollowActive = false;
        leftDismantleActive = false;
        rightDismantleActive = false;
        oneLeftIncrementer = 15;
        oneRightIncrementer = 15;
        twoRightIncrementer = 15;
        twoLeftIncrementer = 15;
        oneLeftList = new ArrayList<>();
        oneRightList = new ArrayList<>();
        twoLeftList = new ArrayList<>();
        twoRightList = new ArrayList<>();
        gojoDomainActive = false;
        sukunaDomainActive = false;
        time = 0;
        timer = new Timer(1000, this);
        playGojoDomain = true;
        playSukunaDomain = true;
        playBothDomain = true;
        try{
            background = ImageIO.read(new File("assets/background.png"));
            backupBackground = ImageIO.read(new File("assets/background.png"));
            gojoIcon = ImageIO.read(new File("assets/gojoicon.png"));
            sukunaIcon = ImageIO.read(new File("assets/sukunaicon.png"));
            gojoDomain = ImageIO.read(new File("assets/unlimitedvoid.png"));
            sukunaDomain = ImageIO.read(new File("assets/malevolentkitchen.png"));
            domainClash = ImageIO.read(new File("assets/domainclash.png"));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        oneLeftList.add(new Point((int) playerOne.getXCoord(), (int) playerOne.getYCoord()));
        oneRightList.add(new Point((int) playerOne.getXCoord(), (int) playerOne.getYCoord()));
        twoRightList.add(new Point((int) playerTwo.getXCoord(), (int) playerTwo.getYCoord()));
        twoLeftList.add(new Point((int) playerTwo.getXCoord(), (int) playerTwo.getYCoord()));
        g.drawImage(background, 0, 0, null);
        g.drawImage(playerOne.getImage(), (int) playerOne.getXCoord(), (int) playerOne.getYCoord(), null);
        g.drawImage(playerTwo.getImage(), (int) playerTwo.getXCoord(), (int) playerTwo.getYCoord(), null);
        g.drawImage(gojoIcon, 10, 20, null);
        g.drawImage(sukunaIcon, 875, 20, null);
        g.setFont(new Font("Courier New", Font.ITALIC, 24));

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
        if (!sukunaDomainActive) {
            if (pressedKeys[65]) {
                playerOne.moveLeft();
                playerOne.changefacingDirection();
            }
            if (pressedKeys[68]) {
                playerOne.moveRight();
            }
            if (pressedKeys[87]) {
                playerOne.moveUp();
            }
            if (pressedKeys[83]) {
                playerOne.moveDown();
            }
        }

        // playerTwo movement keys
        if (!gojoDomainActive) {
            if (pressedKeys[37]) {
                playerTwo.moveLeft();
                playerTwo.changefacingDirection();
            }
            if (pressedKeys[39]) {
                playerTwo.moveRight();
            }
            if (pressedKeys[38]) {
                playerTwo.moveUp();
            }
            if (pressedKeys[40]) {
                playerTwo.moveDown();
            }
        } else {
            if (pressedKeys[37]) {
                playerTwo.moveRight();
                playerTwo.changefacingDirection();
            }
            if (pressedKeys[39]) {
                playerTwo.moveLeft();
            }
            if (pressedKeys[38]) {
                playerTwo.moveDown();
            }
            if (pressedKeys[40]) {
                playerTwo.moveUp();
            }
        }

        // playerOne attack keys
        if (!leftPurpleHollowActive) {
            if (pressedKeys[81]) {
                rightPurpleHollowActive = true;
            }
            if (rightPurpleHollowActive) {
                rightPurpleHollow.setXCoord(oneRightList.get(0).getX() + oneRightIncrementer);
                rightPurpleHollow.setYCoord(oneRightList.get(0).getY());
                g.drawImage(rightPurpleHollow.getImage(), (int) rightPurpleHollow.getXCoord(), (int) rightPurpleHollow.getYCoord(), null);
                oneRightIncrementer += 1;
                if (rightPurpleHollow.getXCoord() > 850) {
                    rightPurpleHollowActive = false;
                    oneRightList.clear();
                    oneRightIncrementer = 15;
                } else if (rightPurpleHollow.rect().intersects(playerTwo.rect())) {
                    rightPurpleHollowActive = false;
                    oneRightList.clear();
                    oneRightIncrementer = 15;
                    playerTwo.loseHealth();
                    if (!sukunaDomainActive) {
                        if (playerOne.getDomainBar() < 4) {
                            playerOne.chargeDomain();
                        }
                    }
                }
            }
        }
        if (!rightPurpleHollowActive) {
            if (pressedKeys[69]) {
                leftPurpleHollowActive = true;
            }
            if (leftPurpleHollowActive) {
                leftPurpleHollow.setXCoord(oneLeftList.get(0).getX() + oneLeftIncrementer);
                leftPurpleHollow.setYCoord(oneLeftList.get(0).getY());
                g.drawImage(leftPurpleHollow.getImage(), (int) leftPurpleHollow.getXCoord(), (int) leftPurpleHollow.getYCoord(), null);
                oneLeftIncrementer -= 1;
                if (leftPurpleHollow.getXCoord() < 10) {
                    leftPurpleHollowActive = false;
                    oneLeftList.clear();
                    oneLeftIncrementer = 15;
                } else if (leftPurpleHollow.rect().intersects(playerTwo.rect())) {
                    leftPurpleHollowActive = false;
                    oneLeftList.clear();
                    oneLeftIncrementer = 15;
                    playerTwo.loseHealth();
                    if (!sukunaDomainActive) {
                        if (playerOne.getDomainBar() < 4) {
                            playerOne.chargeDomain();
                        }
                    }
                }
            }
        }

        // playerTwo attack keys
        if (!gojoDomainActive) {
            if (!rightDismantleActive) {
                if (pressedKeys[74]) {
                    leftDismantleActive = true;
                }
                if (leftDismantleActive) {
                    leftDismantle.setXCoord(twoLeftList.get(0).getX() + twoLeftIncrementer);
                    leftDismantle.setYCoord(twoLeftList.get(0).getY());
                    g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord(), (int) leftDismantle.getYCoord(), null);
                    twoLeftIncrementer -= 1;
                    if (leftDismantle.getXCoord() < 10) {
                        leftDismantleActive = false;
                        twoLeftList.clear();
                        twoLeftIncrementer = 15;
                    } else if (leftDismantle.rect().intersects(playerOne.rect())) {
                        leftDismantleActive = false;
                        twoLeftList.clear();
                        twoLeftIncrementer = 15;
                        playerOne.loseHealth();
                        if (playerTwo.getDomainBar() < 4) {
                            playerTwo.chargeDomain();
                        }
                    }
                }
            }
            if (!leftDismantleActive) {
                if (pressedKeys[75]) {
                    rightDismantleActive = true;
                }
                if (rightDismantleActive) {
                    rightDismantle.setXCoord(twoRightList.get(0).getX() + twoRightIncrementer);
                    rightDismantle.setYCoord(twoRightList.get(0).getY());
                    g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord(), (int) rightDismantle.getYCoord(), null);
                    twoRightIncrementer += 1;
                    if (rightDismantle.getXCoord() > 850) {
                        rightDismantleActive = false;
                        twoRightList.clear();
                        twoRightIncrementer = 15;
                    } else if (rightDismantle.rect().intersects(playerOne.rect())) {
                        rightDismantleActive = false;
                        twoRightList.clear();
                        twoRightIncrementer = 15;
                        playerOne.loseHealth();
                        if (playerTwo.getDomainBar() < 4) {
                            playerTwo.chargeDomain();
                        }
                    }
                }
            }
        } else {
            if (!rightDismantleActive) {
                if (pressedKeys[75]) {
                    leftDismantleActive = true;
                }
                if (leftDismantleActive) {
                    leftDismantle.setXCoord(twoLeftList.get(0).getX() + twoLeftIncrementer);
                    leftDismantle.setYCoord(twoLeftList.get(0).getY());
                    g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord(), (int) leftDismantle.getYCoord(), null);
                    twoLeftIncrementer -= 1;
                    if (leftDismantle.getXCoord() < 10) {
                        leftDismantleActive = false;
                        twoLeftList.clear();
                        twoLeftIncrementer = 15;
                    } else if (leftDismantle.rect().intersects(playerOne.rect())) {
                        leftDismantleActive = false;
                        twoLeftList.clear();
                        twoLeftIncrementer = 15;
                        playerOne.loseHealth();
                        if (!gojoDomainActive) {
                            if (playerTwo.getDomainBar() < 4) {
                                playerTwo.chargeDomain();
                            }
                        }
                    }
                }
            }
            if (!leftDismantleActive) {
                if (pressedKeys[74]) {
                    rightDismantleActive = true;
                }
                if (rightDismantleActive) {
                    rightDismantle.setXCoord(twoRightList.get(0).getX() + twoRightIncrementer);
                    rightDismantle.setYCoord(twoRightList.get(0).getY());
                    g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord(), (int) rightDismantle.getYCoord(), null);
                    twoRightIncrementer += 1;
                    if (rightDismantle.getXCoord() > 850) {
                        rightDismantleActive = false;
                        twoRightList.clear();
                        twoRightIncrementer = 15;
                    } else if (rightDismantle.rect().intersects(playerOne.rect())) {
                        rightDismantleActive = false;
                        twoRightList.clear();
                        twoRightIncrementer = 15;
                        playerOne.loseHealth();
                        if (!gojoDomainActive) {
                            if (playerTwo.getDomainBar() < 4) {
                                playerTwo.chargeDomain();
                            }
                        }
                    }
                }
            }
        }

        // playerOne domain keys

        if (pressedKeys[70] && playerOne.getDomainBar() == 4) {
            gojoDomainActive = true;
        }

        // playerTwo domain keys
        if (pressedKeys[77] && playerTwo.getDomainBar() == 4) {
            sukunaDomainActive = true;
        }
        if(gojoDomainActive && sukunaDomainActive){
            if (playBothDomain) {
                playBothMusic();
                playBothDomain = false;
            }
            background = domainClash;
            playerOne.resetDomainBar();
            playerTwo.resetDomainBar();
            timer.start();
            if (time == 6) {
                background = backupBackground;
                timer.stop();
                time = 0;
                gojoDomainActive = false;
                sukunaDomainActive = false;
                playBothDomain = true;
            }
        }else if(gojoDomainActive){
            if(playGojoDomain){
                playGojoMusic();
                playGojoDomain = false;
            }
            background = gojoDomain;
            playerOne.resetDomainBar();
            timer.start();
            if(time == 7){
                timer.stop();
                time = 0;
                background = backupBackground;
                gojoDomainActive = false;
                playGojoDomain = true;
            }
        }else if(sukunaDomainActive){
            if(playSukunaDomain){
                playSukunaMusic();
                playSukunaDomain = false;
            }
            background = sukunaDomain;
            playerTwo.resetDomainBar();
            timer.start();
            if(time == 3){
                timer.stop();
                time = 0;
                background = backupBackground;
                sukunaDomainActive = false;
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


}
