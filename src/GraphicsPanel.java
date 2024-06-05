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


    private BufferedImage background, gojoIcon, sukunaIcon, backupBackground, gojoDomain, sukunaDomain, domainClash;
    private Character playerOne, playerTwo;
    private boolean[] pressedKeys;
    private Timer timer;
    private int time;
    private Clip gojoClip, sukunaClip, bothClip;
    private boolean playGojoDomain, playSukunaDomain, playBothDomain;
    private boolean gojoFirstLoop, sukunaFirstLoop;
    private boolean rightPurpleHollowActive, leftPurpleHollowActive, rightDismantleActive, leftDismantleActive;
    private Projectile purpleHollow, rightDismantle, leftDismantle;
    private static int gojoIncrementer = 0;
    private static int sukunaIncrementer = 0;
    private boolean sukunaDomainActive, gojoDomainActive;

    private boolean isGojoJumping, isSukunaJumping;
    private boolean isGojoFalling, isSukunaFalling;
    private long gojoJumpStartTime, sukunaJumpStartTime;

    private long gojoSlowJumpStartTime, sukunaSlowJumpStartTime;
    private boolean gojoSlowJump, sukunaSlowJump;
    private boolean gojoFirstTimeJump, sukunaFirstTimeJump;
    private double gojoStartJump, sukunaStartJump;

    private long gojoSlowFallStartTime, sukunaSlowFallStartTime;
    private boolean gojoSlowFall, sukunaSlowFall;
    private boolean gojoFirstTimeFall, sukunaFirstTimeFall;
    private double gojoStartFall, sukunaStartFall;


    public GraphicsPanel() {
        playerOne = new Character(20, 350, "assets/gojo.png", "assets/gojo.png", "assets/gojo.png", true);
        playerTwo = new Character(825, 325, "assets/sukuna.png", "assets/sukunaleft.png", "assets/sukunaright.png", false);
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
            backupBackground = ImageIO.read(new File("assets/background.png"));
            gojoIcon = ImageIO.read(new File("assets/gojoicon.png"));
            sukunaIcon = ImageIO.read(new File("assets/sukunaicon.png"));
            gojoDomain = ImageIO.read(new File("assets/unlimitedvoid.png"));
            sukunaDomain = ImageIO.read(new File("assets/malevolentkitchen.png"));
            domainClash = ImageIO.read(new File("assets/domainclash.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        gojoFirstLoop = true;
        rightPurpleHollowActive = false;
        leftPurpleHollowActive = false;
        purpleHollow = new Projectile(playerOne.getXCoord(), playerOne.getYCoord(), "assets/purplehollow.png");

        sukunaFirstLoop = true;
        rightDismantleActive = false;
        leftDismantleActive = false;
        leftDismantle = new Projectile(playerTwo.getXCoord(), playerTwo.getYCoord(), "assets/dismantleleft.png");
        rightDismantle = new Projectile(playerTwo.getXCoord(), playerTwo.getYCoord(), "assets/dismantleright.png");

        isGojoJumping = false;
        isGojoFalling = false;
        gojoJumpStartTime = 0;
        gojoSlowJumpStartTime = 0;
        gojoSlowJump = false;
        gojoFirstTimeJump = true;
        gojoStartJump = 0;
        gojoSlowFallStartTime = 0;
        gojoSlowFall = false;
        gojoFirstTimeFall = true;
        gojoStartFall = 0;

        isSukunaJumping = false;
        isSukunaFalling = false;
        sukunaJumpStartTime = 0;
        sukunaSlowJumpStartTime = 0;
        sukunaSlowJump = false;
        sukunaFirstTimeJump = true;
        sukunaStartJump = 0;
        sukunaSlowFallStartTime = 0;
        sukunaSlowFall = false;
        sukunaFirstTimeFall = true;
        sukunaStartFall = 0;
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
        // a
        if (pressedKeys[65]) {
            playerOne.faceLeft();
            playerOne.moveLeft();
        }
        // d
        if (pressedKeys[68]) {
            playerOne.faceRight();
            playerOne.moveRight();
        }

        // w key: jump
        if (pressedKeys[87] && !isGojoJumping && !isGojoFalling) {
            gojoSlowJump = true;
            gojoJumpStartTime = System.currentTimeMillis();
            gojoSlowJumpStartTime = gojoJumpStartTime;
        }
        if(gojoSlowJump){
            long slowCurrentTime = System.currentTimeMillis();
            if(gojoFirstTimeJump){
                gojoStartJump = playerOne.getYCoord();
                gojoFirstTimeJump = false;
            }
            if(playerOne.getYCoord() + 200 == gojoStartJump){
                gojoFirstTimeJump = true;
                gojoSlowJump = false;
                isGojoJumping = true;
            }else if(slowCurrentTime - gojoSlowJumpStartTime >= 50){
                playerOne.setYCoord(playerOne.getYCoord() - 25);
                gojoSlowJumpStartTime = slowCurrentTime;
            }
        }
        if (isGojoJumping) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - gojoJumpStartTime >= 500) {
                isGojoJumping = false;
                gojoSlowFall = true;
            }
        }
        if(gojoSlowFall){
            long slowCurrentTime = System.currentTimeMillis();
            if(gojoFirstTimeFall){
                gojoStartFall = playerOne.getYCoord();
                gojoFirstTimeFall = false;
            }
            if(playerOne.getYCoord() - 200 == gojoStartFall){
                gojoFirstTimeFall = true;
                gojoSlowFall = false;
                isGojoFalling = true;
            }else if(slowCurrentTime - gojoSlowFallStartTime >= 50){
                playerOne.setYCoord(playerOne.getYCoord() + 25);
                gojoSlowFallStartTime = slowCurrentTime;
            }
        }
        if (isGojoFalling) {
            if (playerOne.hasLanded()) {
                isGojoFalling = false;
            }
        }

        // s key: crouch
        if (pressedKeys[83]) {

        }


        // playerTwo movement keys
        if (pressedKeys[74]) {
            playerTwo.faceLeft();
            playerTwo.moveLeft();
        }
        if (pressedKeys[76]) {
            playerTwo.faceRight();
            playerTwo.moveRight();
        }

        // i key: jump
        if (pressedKeys[73] && !isSukunaJumping && !isSukunaFalling) {
            sukunaSlowJump = true;
            sukunaJumpStartTime = System.currentTimeMillis();
            sukunaSlowJumpStartTime = sukunaJumpStartTime;
        }
        if(sukunaSlowJump){
            long slowCurrentTime = System.currentTimeMillis();
            if(sukunaFirstTimeJump){
                sukunaStartJump = playerTwo.getYCoord();
                sukunaFirstTimeJump = false;
            }
            if(playerTwo.getYCoord() + 200 == sukunaStartJump){
                sukunaFirstTimeJump = true;
                sukunaSlowJump = false;
                isSukunaJumping = true;
            }else if(slowCurrentTime - sukunaSlowJumpStartTime >= 50){
                playerTwo.setYCoord(playerTwo.getYCoord() - 25);
                sukunaSlowJumpStartTime = slowCurrentTime;
            }
        }
        if (isSukunaJumping) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - sukunaJumpStartTime >= 500) {
                isSukunaJumping = false;
                sukunaSlowFall = true;
            }
        }
        if(sukunaSlowFall){
            long slowCurrentTime = System.currentTimeMillis();
            if(sukunaFirstTimeFall){
                sukunaStartFall = playerTwo.getYCoord();
                sukunaFirstTimeFall = false;
            }
            if(playerTwo.getYCoord() - 200 == sukunaStartFall){
                sukunaFirstTimeFall = true;
                sukunaSlowFall = false;
                isSukunaFalling = true;
            }else if(slowCurrentTime - sukunaSlowFallStartTime >= 50){
                playerTwo.setYCoord(playerTwo.getYCoord() + 25);
                sukunaSlowFallStartTime = slowCurrentTime;
            }
        }
        if (isSukunaFalling) {
            if (playerTwo.hasLanded()) {
                isSukunaFalling = false;
            }
        }


        // k key: crouch
        if (pressedKeys[75]) {

        }

        // player one attack key
        if (pressedKeys[69] && playerOne.isFacingRight()) {
            rightPurpleHollowActive = true;
        }else if(pressedKeys[69] && !playerOne.isFacingRight()){
            leftPurpleHollowActive = true;
        }

        //player one shooting to the left
        if(!rightPurpleHollowActive) {
            if (leftPurpleHollowActive) {
                if (gojoFirstLoop) {
                    purpleHollow.setXCoord(playerOne.getXCoord());
                    purpleHollow.setYCoord(playerOne.getYCoord());
                    gojoFirstLoop = false;
                }
                purpleHollow.setYCoord(purpleHollow.getYCoord());
                g.drawImage(purpleHollow.getImage(), (int) purpleHollow.getXCoord() + gojoIncrementer, (int) purpleHollow.getYCoord(), null);
                gojoIncrementer -= 1;
                if (purpleHollow.rectOne().intersects(playerTwo.rect())) {
                    leftPurpleHollowActive = false;
                    gojoIncrementer = 0;
                    playerTwo.loseHealth();
                    if (playerOne.getDomainBar() < 4) {
                        playerOne.chargeDomain();
                    }
                    gojoFirstLoop = true;
                    purpleHollow.setXCoord(0);
                    purpleHollow.setYCoord(0);
                } else if (purpleHollow.getXCoord() + gojoIncrementer < 10) {
                    leftPurpleHollowActive = false;
                    gojoIncrementer = 0;
                    gojoFirstLoop = true;
                    purpleHollow.setXCoord(0);
                    purpleHollow.setYCoord(0);
                }
            }
        }

        // player one shooting to the right
        if(!leftPurpleHollowActive){
            if (rightPurpleHollowActive) {
                if (gojoFirstLoop) {
                    purpleHollow.setXCoord(playerOne.getXCoord());
                    purpleHollow.setYCoord(playerOne.getYCoord());
                    gojoFirstLoop = false;
                }
                purpleHollow.setYCoord(purpleHollow.getYCoord());
                g.drawImage(purpleHollow.getImage(), (int) purpleHollow.getXCoord() + gojoIncrementer, (int) purpleHollow.getYCoord(), null);
                gojoIncrementer += 1;
                if (purpleHollow.rectOne().intersects(playerTwo.rect())) {
                    rightPurpleHollowActive = false;
                    gojoIncrementer = 0;
                    playerTwo.loseHealth();
                    if (playerOne.getDomainBar() < 4) {
                        playerOne.chargeDomain();
                    }
                    gojoFirstLoop = true;
                    purpleHollow.setXCoord(0);
                    purpleHollow.setYCoord(0);
                } else if (purpleHollow.getXCoord() + gojoIncrementer > 850) {
                    rightPurpleHollowActive = false;
                    gojoIncrementer = 0;
                    gojoFirstLoop = true;
                    purpleHollow.setXCoord(0);
                    purpleHollow.setYCoord(0);
                }
            }
        }


        // player two attack key
        if(pressedKeys[79] && !playerTwo.isFacingRight()){
            leftDismantleActive = true;
        }else if(pressedKeys[79] && playerTwo.isFacingRight()) {
            rightDismantleActive = true;
        }

        // player two shooting to the right
        if(!leftDismantleActive) {
            if (rightDismantleActive) {
                if (sukunaFirstLoop) {
                    rightDismantle.setXCoord(playerTwo.getXCoord());
                    rightDismantle.setYCoord(playerTwo.getYCoord());
                    sukunaFirstLoop = false;
                }
                rightDismantle.setYCoord(rightDismantle.getYCoord());
                g.drawImage(rightDismantle.getImage(), (int) rightDismantle.getXCoord() + sukunaIncrementer, (int) rightDismantle.getYCoord(), null);
                sukunaIncrementer += 1;
                if (rightDismantle.rectTwo().intersects(playerOne.rect())) {
                    rightDismantleActive = false;
                    sukunaIncrementer = 0;
                    playerOne.loseHealth();
                    if (playerTwo.getDomainBar() < 4) {
                        playerTwo.chargeDomain();
                    }
                    sukunaFirstLoop = true;
                    rightDismantle.setXCoord(0);
                    rightDismantle.setYCoord(0);
                } else if (rightDismantle.getXCoord() + sukunaIncrementer > 850) {
                    rightDismantleActive = false;
                    sukunaIncrementer = 0;
                    sukunaFirstLoop = true;
                    rightDismantle.setXCoord(0);
                    rightDismantle.setYCoord(0);
                }
            }
        }

        // player two shooting to the left
        if(!rightDismantleActive) {
            if (leftDismantleActive) {
                if (sukunaFirstLoop) {
                    leftDismantle.setXCoord(playerTwo.getXCoord());
                    leftDismantle.setYCoord(playerTwo.getYCoord());
                    sukunaFirstLoop = false;
                }
                leftDismantle.setYCoord(leftDismantle.getYCoord());
                g.drawImage(leftDismantle.getImage(), (int) leftDismantle.getXCoord() + sukunaIncrementer, (int) leftDismantle.getYCoord(), null);
                sukunaIncrementer -= 1;
                if (leftDismantle.rectTwo().intersects(playerOne.rect())) {
                    leftDismantleActive = false;
                    sukunaIncrementer = 0;
                    playerOne.loseHealth();
                    if (playerTwo.getDomainBar() < 4) {
                        playerTwo.chargeDomain();
                    }
                    sukunaFirstLoop = true;
                    leftDismantle.setXCoord(0);
                    leftDismantle.setYCoord(0);
                } else if (leftDismantle.getXCoord() + sukunaIncrementer < 10) {
                    leftDismantleActive = false;
                    sukunaIncrementer = 0;
                    sukunaFirstLoop = true;
                    leftDismantle.setXCoord(0);
                    leftDismantle.setYCoord(0);
                }
            }
        }

        // playerOne domain keys
        if (pressedKeys[81] && playerOne.getDomainBar() == 4) {
            gojoDomainActive = true;
        }

        // playerTwo domain keys
        if (pressedKeys[85] && playerTwo.getDomainBar() == 4) {
            sukunaDomainActive = true;
        }

        if(gojoDomainActive){
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


    public static int getGojoIncrementer(){
        return gojoIncrementer;
    }


    public static int getSukunaIncrementer(){
        return sukunaIncrementer;
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

