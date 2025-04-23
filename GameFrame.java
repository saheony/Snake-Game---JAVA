import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake Game Pro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); //takes the JFrame and fits all the components sets in it
        this.setVisible(true);
        this.setLocationRelativeTo(null); //to put the window in the middle of the screen

        // Add icon (create a snake.png image in your resources)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon/snake.png"));
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }
        // Add game panel with border
        GamePanel panel = new GamePanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(panel);
        // Finalize window
        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
        //modern--
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }
}
