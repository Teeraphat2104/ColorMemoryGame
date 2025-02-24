import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ColorMemoryGame {
    private JFrame frame;
    private JPanel panel;
    private JLabel roundLabel, scoreLabel;
    private JButton redButton, greenButton, blueButton, yellowButton;
    private ArrayList<Color> sequence;
    private ArrayList<Color> userInput;
    private int currentIndex, score;
    private boolean userTurn;
    private final ArrayList<Color> COLORS;
    private final Random random = new Random();

    public ColorMemoryGame() {
        frame = new JFrame("Color Memory Game");
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        
        scoreLabel = new JLabel("Score: 0", SwingConstants.LEFT);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        topPanel.add(scoreLabel, BorderLayout.WEST);
        
        roundLabel = new JLabel("Round: 1", SwingConstants.CENTER);
        roundLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        topPanel.add(roundLabel, BorderLayout.CENTER);
        
        frame.add(topPanel, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        redButton = createColorButton(Color.RED);
        greenButton = createColorButton(Color.GREEN);
        blueButton = createColorButton(Color.BLUE);
        yellowButton = createColorButton(Color.YELLOW);

        panel.add(redButton);
        panel.add(greenButton);
        panel.add(yellowButton);
        panel.add(blueButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(400, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        COLORS = new ArrayList<Color>();
        COLORS.add(Color.RED);
        COLORS.add(Color.GREEN);
        COLORS.add(Color.BLUE);
        COLORS.add(Color.YELLOW);
        
        sequence = new ArrayList<Color>();
        userInput = new ArrayList<Color>();
        score = 0;
        resetButtons();
        startNewRound();
    }

    private JButton createColorButton(final Color color) {
        JButton button = new JButton();
        button.setBackground(new Color(50, 50, 50));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 150));
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userTurn) {
                    handleUserInput(color);
                }
            }
        });
        return button;
    }

    private void handleUserInput(Color color) {
        if (!userTurn) return;
        userInput.add(color);
        if (!userInput.get(userInput.size() - 1).equals(sequence.get(userInput.size() - 1))) {
            JOptionPane.showMessageDialog(frame, "Game Over! Score: " + score);
            sequence.clear();
            score = 0;
            scoreLabel.setText("Score: " + score);
            resetButtons();
            startNewRound();
            return;
        }
        if (userInput.size() == sequence.size()) {
            score++;
            scoreLabel.setText("Score: " + score);
            Timer timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startNewRound();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void startNewRound() {
        userTurn = false;
        userInput.clear();
        Collections.shuffle(COLORS);
        sequence.add(COLORS.get(random.nextInt(COLORS.size())));
        roundLabel.setText("Round: " + sequence.size());
        showSequence();
    }

    private void showSequence() {
        userTurn = false;
        currentIndex = 0;
        Timer timer = new Timer(600, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < sequence.size()) {
                    highlightButton(sequence.get(currentIndex));
                    currentIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                    resetButtons();
                    userTurn = true;
                }
            }
        });
        timer.start();
    }

    private void highlightButton(final Color color) {
        final JButton buttonToHighlight;
        if (color == Color.RED) buttonToHighlight = redButton;
        else if (color == Color.GREEN) buttonToHighlight = greenButton;
        else if (color == Color.BLUE) buttonToHighlight = blueButton;
        else buttonToHighlight = yellowButton;

        buttonToHighlight.setBackground(color);
        Timer revertTimer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonToHighlight.setBackground(new Color(50, 50, 50));
            }
        });
        revertTimer.setRepeats(false);
        revertTimer.start();
    }

    private void resetButtons() {
        redButton.setBackground(new Color(50, 50, 50));
        greenButton.setBackground(new Color(50, 50, 50));
        blueButton.setBackground(new Color(50, 50, 50));
        yellowButton.setBackground(new Color(50, 50, 50));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ColorMemoryGame();
            }
        });
    }
}
