package view;

import model.Player;

import network.TrafficGenerator;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.*;

//Added for sound
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PlayActionScreen {

	private static final int GAME_TIMER = 360;
	private int gameTimer = GAME_TIMER;
	private static final int TIME_REMAINING = 30;
	private JLabel countdownTimer;
	private Timer timer;
	private int timeRemaining = TIME_REMAINING;

	private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
	private static final Color LIGHT_TEXT = new Color(200, 200, 200);

	private List<Player> greenTeamPlayers;
	private List<Player> redTeamPlayers;

	private JTextArea actionLogArea;
	private Clip musicClip;
	private TrafficGenerator trafficGenerator;

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

		JLabel titleLabel = new JLabel("Action Log");
		titleLabel.setForeground(Color.YELLOW);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		actionLogPanel.add(titleLabel, BorderLayout.NORTH);

		actionLogArea = new JTextArea();
		actionLogArea.setEditable(false);
		actionLogArea.setBackground(DARK_BACKGROUND);
		actionLogArea.setForeground(LIGHT_TEXT);
		actionLogArea.setFont(new Font("Arial", Font.PLAIN, 16));

		JScrollPane scrollPane = new JScrollPane(actionLogArea);
		scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_TEXT));
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

				if (timeRemaining == 15) {
					playMusic();
				} else if (timeRemaining == 0) {
					countdownTimer.setText("GAME STARTING");
					countdownTimer.setHorizontalAlignment(SwingConstants.CENTER);
					timer.stop();
					Timer gameStartingTimer = new Timer(10000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							startGame();
						}
					});
					gameStartingTimer.setRepeats(false);
					gameStartingTimer.start();
				}
			}
		});
		timer.start();
	}

	private void startGame() {
		System.out.print("Game started!");
		logAction("Player1 shot Player2");
		logAction("Player3 shot Player4");

		// Initialize and start the TrafficGenerator
		List<String> redPlayers = redTeamPlayers.stream().map(player -> String.valueOf(player.getId())).collect(Collectors.toList());
		List<String> greenPlayers = greenTeamPlayers.stream().map(player -> String.valueOf(player.getId())).collect(Collectors.toList());

		trafficGenerator = new TrafficGenerator(redPlayers, greenPlayers);
		new Thread(() -> trafficGenerator.start()).start();
	}

	private void stopGame() {
		stopMusic();
		System.out.print("Game stopped");
	}

	private void playMusic() {
		System.out.println("Music should be starting");

		File musicFolder = new File("view/music/");
		File[] musicFiles = musicFolder.listFiles((dir, name) -> name.endsWith(".wav"));

		if (musicFiles != null && musicFiles.length > 0) {
			Random random = new Random();
			File randomTrack = musicFiles[random.nextInt(musicFiles.length)];

			try {
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(randomTrack);
				musicClip = AudioSystem.getClip();
				musicClip.open(audioStream);
				musicClip.start();
				musicClip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopMusic() {
		if (musicClip != null && musicClip.isRunning()) {
			musicClip.stop();
			musicClip.close();
		}
	}

	private void logAction(String action) {
		actionLogArea.append(action + "\n");
		actionLogArea.setCaretPosition(actionLogArea.getDocument().getLength() / 2);
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