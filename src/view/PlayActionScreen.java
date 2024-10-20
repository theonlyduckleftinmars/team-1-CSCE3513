package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;
//Import event listener stuff for countdown timer
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayActionScreen {

	//Define the time (30 seconds)
	private static final int TIME_REMAINING = 30;
	private JLabel countdownTimer;
	private Timer timer;
	private int timeRemaining = TIME_REMAINING;
	
	
    private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
    private static final Color LIGHT_TEXT = new Color(200, 200, 200);

    private List<Player> greenTeamPlayers;
    private List<Player> redTeamPlayers;

    public PlayActionScreen(List<Player> greenTeamPlayers, List<Player> redTeamPlayers) {
        this.greenTeamPlayers = greenTeamPlayers;
        this.redTeamPlayers = redTeamPlayers;
    }

    public void display() {
        JFrame frame = new JFrame("Laser Tag - Play Action");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        //Create the countdown timer visual (top of the screen)
        countdownTimer = new JLabel("Countdown to laser mayhem: " + timeRemaining, SwingConstants.CENTER);
        countdownTimer.setFont(new Font("Arial", Font.BOLD, 24));
        countdownTimer.setForeground(LIGHT_TEXT);
        countdownTimer.setBackground(DARK_BACKGROUND);
        countdownTimer.setOpaque(true);
        frame.add(countdownTimer, BorderLayout.NORTH);
        

        JPanel teamPanel = new JPanel(new GridLayout(1, 2));

        JPanel greenTeamPanel = createTeamPanel("Green Team", greenTeamPlayers, Color.GREEN);
        JPanel redTeamPanel = createTeamPanel("Red Team", redTeamPlayers, Color.RED);

        teamPanel.add(greenTeamPanel);
        teamPanel.add(redTeamPanel);

        frame.add(teamPanel, BorderLayout.CENTER);
        frame.getContentPane().setBackground(DARK_BACKGROUND);
        frame.setVisible(true);
        //Start the timer
        startCountdownTimer();
    }
    
    private void startCountdownTimer()
    {
		//Create the timer (uses milliseconds, so 1000 = 1 second)
		timer = new Timer(1000, new ActionListener(){
			@Override
			//A second has passed!
			public void actionPerformed(ActionEvent e){
				//Decrease the timer
				timeRemaining--;
				//Display updated countdown
				countdownTimer.setText("Countdown to laser mayhem: " + timeRemaining);
				//Make sure that bad boy stays in the middle of the screen
				countdownTimer.setHorizontalAlignment(SwingConstants.CENTER);
				//The timer hit 0! Stop the timer, start the game
				if(timeRemaining <= 0)
				{
					timer.stop();
					startGame();
				}
			}
		});
		
		timer.start();
	}
	
	//Timer has hit 0! Start the game
	private void startGame()
	{
		//Don't have to start the game yet. Just display a message to the terminal for now!
		System.out.print("Game started!");
		//Call udpManager.startGame() here to transmit the 202 signal?
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
