import javax.swing.*;

public class InstructionsFrame {
    private InstructionsPanel panel;

    public InstructionsFrame() {
        JFrame frame = new JFrame("How to Play");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        panel = new InstructionsPanel(frame);
        frame.add(panel);
        frame.setVisible(true);
    }
}

