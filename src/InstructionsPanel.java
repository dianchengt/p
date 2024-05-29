import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InstructionsPanel extends JPanel implements ActionListener {

    private JButton backButton;
    private JFrame enclosingFrame;

    public InstructionsPanel(JFrame frame) {
        enclosingFrame = frame;
        backButton = new JButton("Back");
        add(backButton);
        backButton.addActionListener(this);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        backButton.setLocation(0, 0);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() instanceof JButton){
            JButton button = (JButton) e.getSource();
            if(button == backButton){
                WelcomeFrame f = new WelcomeFrame();
                enclosingFrame.setVisible(false);
            }
        }
    }

}
