import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;

     final int x[] = new int[GAME_UNITS]; //all the x coordinates the snake head
     final int y[] = new int[GAME_UNITS]; //all the y coordinates
    private int bodyParts = 6;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private final Random random;

    //colors
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color GRID_COLOR = new Color(50, 50, 50);
    private final Color SNAKE_HEAD_COLOR = new Color(100, 200, 100);
    private final Color SNAKE_BODY_COLOR = new Color(80, 180, 80);
    private final Color APPLE_COLOR = new Color(220, 50, 50);
    private final Color SCORE_COLOR = new Color(200, 200, 255);
    private final Color GAME_OVER_COLOR = new Color(220, 80, 80);

    GamePanel(){
      random = new Random();
      this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
      this.setBackground(BACKGROUND_COLOR);
      this.setFocusable(true);
      this.addKeyListener(new MyKeyAdapter());

      startGame();
    }

    public void startGame(){
        // Initialize snake position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = SCREEN_WIDTH/2 - i*UNIT_SIZE; // Start from center going right
            y[i] = SCREEN_HEIGHT/2;
        }
      newApple();
      running = true;
      timer = new Timer(DELAY,this);
      timer.start();
    }

    public void paintComponent(Graphics g){
     super.paintComponent(g);
     draw(g);
    }

    public void draw(Graphics g) {
     if(running){
        g.setColor(GRID_COLOR);
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //x axis
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); //y axis grid
        }
        g.setColor(APPLE_COLOR.darker());
        g.fillOval(appleX + 2, appleY + 2, UNIT_SIZE, UNIT_SIZE);
        g.setColor(APPLE_COLOR);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        //draw the snake with gradiant effect

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(SNAKE_HEAD_COLOR);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                //eyes
                g.setColor(Color.WHITE);
                if(direction == 'R' || direction == 'L'){
                    g.fillOval(x[i]+5, y[i]+5, 5, 5);
                    g.fillOval(x[i]+15, y[i]+5, 5,5);
                }else{
                    g.fillOval(x[i]+5, y[i]+5, 5, 5);
                    g.fillOval(x[i]+5,y[i]+15,5, 5);
                }
            } else {
                g.setColor(new Color(
                        SNAKE_BODY_COLOR.getRed(),
                        SNAKE_BODY_COLOR.getGreen(),
                        SNAKE_BODY_COLOR.getBlue(),
                        200 -i*2)); //transparency gradie
                g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE,8,8);
            }
        }
        //draw score with shadow
         g.setFont(new Font("Segoe UI",Font.BOLD,30));
         FontMetrics metrics = getFontMetrics(g.getFont());
          String scoreText = "Score: "+ applesEaten;

         g.setColor(Color.BLACK);
         g.drawString(scoreText,(SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2 + 2,
                 g.getFont().getSize() + 2);
         g.setColor(SCORE_COLOR);
         g.drawString(scoreText,
                 (SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2,
                 g.getFont().getSize());

     }else{
         gameOver(g);
     }
    }

    public void newApple(){
     //generate the coodinates of the new apple-anytime we begin the game, eat an apple or score...
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //appear some place along the x axis
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i>0; i--){ //shifting the body parts of the snake around
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
     if((x[0] == appleX) && (y[0] == appleY)){
         bodyParts++;
         applesEaten++;
         newApple();
         GameSounds.playEat();
     }
    }
    public void checkCollisions(){
    //check if the head of the snake collides with its body
        for(int i = bodyParts; i>0; i--){
            if((x[0] == x[i])&&(y[0] == y[i])){
             running = false;
                GameSounds.playGameOver();
            }
        }
    //check if the head of the snake touches left border
        if(x[0] < 0){
            running = false;
            GameSounds.playGameOver();
        }
    //check if head touches right border
       if(x[0] > SCREEN_WIDTH){
           running = false;
           GameSounds.playGameOver();
       }
    //check if head touches top border
       if(y[0] < 0){
           running = false;
           GameSounds.playGameOver();
       }
    //check if head touches bottom borders
        if(y[0] > SCREEN_HEIGHT){
            running = false;
            GameSounds.playGameOver();
        }
    //stop the timer if runnig is false
        if(!running){
            timer.stop();
            GameSounds.playGameOver();
        }
    }
    public void gameOver(Graphics g){
        //displays the score
        g.setColor(SCORE_COLOR);
        g.setFont(new Font("Segoe UI",Font.BOLD,30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH -metrics1.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());

    //set up game over text
        g.setFont(new Font("Segoe UI",Font.BOLD,60));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";

        g.setColor(Color.BLACK);
        g.drawString(gameOverText,
                (SCREEN_WIDTH - metrics2.stringWidth(gameOverText)) / 2 + 3,
                SCREEN_HEIGHT / 2 + 3);

        g.setColor(GAME_OVER_COLOR);
        g.drawString(gameOverText,
                (SCREEN_WIDTH - metrics2.stringWidth(gameOverText)) / 2,
                SCREEN_HEIGHT / 2);
        // Restart instruction
        g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        String restartText = "Press SPACE to restart";
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(restartText,
                (SCREEN_WIDTH - metrics3.stringWidth(restartText)) / 2,
                SCREEN_HEIGHT / 2 + 50);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        //TODO Auto-generated method stub
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
         switch(e.getKeyCode()){
             case  KeyEvent.VK_LEFT:
             if(direction != 'R'){
                 direction = 'L';
             }
             break;
             case  KeyEvent.VK_RIGHT:
                 if(direction != 'L'){
                     direction = 'R';
                 }
                 break;
             case  KeyEvent.VK_UP:
                 if(direction != 'D'){
                     direction = 'U';
                 }
                 break;
             case  KeyEvent.VK_DOWN:
                 if(direction != 'U'){
                     direction = 'D';
                 }
                 break;
             case KeyEvent.VK_SPACE:
                 if(!running) {
                     // Reset game
                     bodyParts = 6;
                     applesEaten = 0;
                     direction = 'R';
                     // Reset snake position
                     for (int i = 0; i < bodyParts; i++) {
                         x[i] = SCREEN_WIDTH/2 - i*UNIT_SIZE;
                         y[i] = SCREEN_HEIGHT/2;
                     }
                     newApple();
                     running = true;
                     timer = new Timer(DELAY, GamePanel.this);
                     timer.start();
                     repaint();
                 }
                 break;
            }
        }
    }
}
