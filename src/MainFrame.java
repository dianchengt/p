import javax.swing.*;

public class MainFrame implements Runnable {

    private GraphicsPanel panel;
    public MainFrame(){
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 580);
        frame.setLocationRelativeTo(null);
        panel =  new GraphicsPanel();
        frame.add(panel);
        frame.setVisible((true));

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while(true){
            panel.repaint();
        }
    }
}
