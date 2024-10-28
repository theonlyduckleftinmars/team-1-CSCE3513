package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayActionScreen {

    private static final int TIME_REMAINING = 30;
    private JLabel countdownTimer;
    private Timer timer;
    private int timeRemaining = TIME_REMAINING;

    private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
    private static final Color LIGHT_TEXT = new Color(200, 200, 200);

    private List<Player> greenTeamPlayers;
    private List<Player> redTeamPlayers;

    private JTextArea actionLogArea;

    public PlayActionScreen(List<Player> greenTeamPlayers, List<Player> redTeamPlayers) {
        this.greenTeamPlayers = greenTeamPlayers;
        this.redTeamPlayers = redTeamPlayers;
    }

    public void display() {
        JFrame frame = new JFrame("Laser Tag - Play Action");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        countdownTimer = new JLabel("Countdown to laser mayhem: " + timeRemaining, SwingConstants.CENTER);
        countdownTimer.setFont(new Font("Arial", Font.BOLD, 24));
        countdownTimer.setForeground(LIGHT_TEXT);
        countdownTimer.setBackground(DARK_BACKGROUND);
        countdownTimer.setOpaque(true);
        frame.add(countdownTimer, BorderLayout.NORTH);

        JPanel teamPanel = new JPanel(new GridLayout(1, 3));

        JPanel greenTeamPanel = createTeamPanel("Green Team", greenTeamPlayers, Color.GREEN);
        JPanel redTeamPanel = createTeamPanel("Red Team", redTeamPlayers, Color.RED);
        JPanel actionLogPanel = createActionLogPanel();

        teamPanel.add(greenTeamPanel);
        teamPanel.add(actionLogPanel);
        teamPanel.add(redTeamPanel);

        frame.add(teamPanel, BorderLayout.CENTER);
        frame.getContentPane().setBackground(DARK_BACKGROUND);
        frame.setVisible(true);

        startCountdownTimer();
    }


    private JPanel createActionLogPanel() {
        JPanel actionLogPanel = new JPanel(new BorderLayout());
        actionLogPanel.setBackground(DARK_BACKGROUND);
        actionLogPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(LIGHT_TEXT), "Action Log"));

        actionLogArea = new JTextArea();
        actionLogArea.setEditable(false);
        actionLogArea.setBackground(DARK_BACKGROUND);
        actionLogArea.setForeground(LIGHT_TEXT);
        actionLogArea.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(actionLogArea);
        scrollPane.setBorder(null);
        actionLogPanel.add(scrollPane, BorderLayout.CENTER);

        return actionLogPanel;
    }

    private void startCountdownTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                countdownTimer.setText("Countdown to laser mayhem: " + timeRemaining);
                countdownTimer.setHorizontalAlignment(SwingConstants.CENTER);

                if (timeRemaining <= 0) {
                    timer.stop();
                    countdownTimer.setText("GAME STARTING");
                    countdownTimer.setHorizontalAlignment(SwingConstants.CENTER);
                    startGame();
                }
            }
        });
        timer.start();
    }

    private void startGame() {
        System.out.println("Game started!");
        logAction("Player1 shot Player2");
        logAction("Player3 shot Player4");
        // purely for testing rn!!!!
    }

    private void logAction(String action) {
        actionLogArea.append(action + "\n");
        actionLogArea.setCaretPosition(actionLogArea.getDocument().getLength()/2);
    }

    private JPanel createTeamPanel(String teamName, List<Player> players, Color teamColor) {
        JPanel teamPanel = new JPanel(new BorderLayout());
        teamPanel.setBorder(BorderFactory.createLineBorder(teamColor, 3));
        teamPanel.setBackground(DARK_BACKGROUND);

        JLabel teamLabel = new JLabel(teamName, SwingConstants.CENTER);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 24));
        teamLabel.setOpaque(true);
        teamLabel.setBackground(teamColor);
        teamLabel.setForeground(Color.WHITE);
        teamPanel.add(teamLabel, BorderLayout.NORTH);

        JPanel playerListPanel = new JPanel(new GridLayout(players.size(), 1, 5, 5));
        playerListPanel.setBackground(DARK_BACKGROUND);

        for (Player player : players) {
            JLabel playerLabel = new JLabel("ID: " + player.getId() + " | Codename: " + player.getCodeName());
            playerLabel.setForeground(LIGHT_TEXT);
            playerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            playerListPanel.add(playerLabel);
        }

        teamPanel.add(playerListPanel, BorderLayout.CENTER);

        return teamPanel;
    }
}
