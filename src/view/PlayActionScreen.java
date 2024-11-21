package view;

import model.Player;
import network.PhotonServerSocket;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PlayActionScreen {

	private static final int GAME_TIMER = 360;
	private int gameTimer = GAME_TIMER;
	private static final int TIME_REMAINING = 30;
	private JLabel countdownTimer;
	private JLabel greenTeamScoreLabel;
	private JLabel redTeamScoreLabel;
	private Timer timer;
	private int timeRemaining = TIME_REMAINING;

	private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
	private static final Color LIGHT_TEXT = new Color(200, 200, 200);

	private PlayerEntryScreen pes;

	private List<Player> greenTeamPlayers;
	private List<Player> redTeamPlayers;
	private Map<Integer, String> equipmentMap;
	private Map<Integer, Integer> playerScores;

	private JPanel greenTeamPlayerListPanel;
	private JPanel redTeamPlayerListPanel;

	private int greenTeamScore = 0;
	private int redTeamScore = 0;

	private JTextArea actionLogArea;

    
	private Clip musicClip;

	public PlayActionScreen(List<Player> greenTeamPlayers, List<Player> redTeamPlayers, Map<Integer, String> equipmentMap) {
		this.greenTeamPlayers = greenTeamPlayers;
		this.redTeamPlayers = redTeamPlayers;
		this.equipmentMap = equipmentMap;

		this.playerScores = new HashMap<>();
		for (Player player : greenTeamPlayers) {
			playerScores.put(player.getId(), 100);
		}
		for (Player player : redTeamPlayers) {
			playerScores.put(player.getId(), 100);
		}
	}

	public void display() {
		JFrame frame = new JFrame("Laser Tag - Play Action");
		frame.setSize(1000, 800);
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
		
		JPanel scorePanel = new JPanel(new GridLayout(2,1));
		scorePanel.setBackground(DARK_BACKGROUND);
		
		//Display green team total score
		int greenScore = scoreTotal(greenTeamPlayers);
		greenTeamScoreLabel = new JLabel("Green team Score: " + greenScore, SwingConstants.CENTER);
		greenTeamScoreLabel.setForeground(Color.GREEN);
		greenTeamScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
		//Display red team total score
		int redScore = scoreTotal(redTeamPlayers);
		redTeamScoreLabel = new JLabel("Red team Score: " + redScore, SwingConstants.CENTER);
		redTeamScoreLabel.setForeground(Color.RED);
		redTeamScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
		//Add score panels to the screen
		scorePanel.add(greenTeamScoreLabel);
		scorePanel.add(redTeamScoreLabel);

		teamPanel.add(greenTeamPanel);
		teamPanel.add(actionLogPanel);
		teamPanel.add(redTeamPanel);

		frame.add(scorePanel, BorderLayout.SOUTH);
		frame.add(teamPanel, BorderLayout.CENTER);
		frame.getContentPane().setBackground(DARK_BACKGROUND);
		frame.setVisible(true);

		updateTeamScores();
		startCountdownTimer();
	}
	
	//Find total score for all players on a team
	    public int scoreTotal(List<Player> team)
    {
		int totalScore = 0;
		
		for(Player player: team)
		{
			totalScore += player.getScore();
		}
		return totalScore;
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
	private JPanel createTeamPanel(String teamName, List<Player> players, Color teamColor) {
		JPanel teamPanel = new JPanel(new BorderLayout());
		teamPanel.setBorder(BorderFactory.createLineBorder(teamColor, 3));
		teamPanel.setBackground(DARK_BACKGROUND);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(DARK_BACKGROUND);

		JLabel teamLabel = new JLabel(teamName, SwingConstants.CENTER);
		teamLabel.setFont(new Font("Arial", Font.BOLD, 24));
		teamLabel.setForeground(LIGHT_TEXT);
		headerPanel.add(teamLabel, BorderLayout.CENTER);



		teamPanel.add(headerPanel, BorderLayout.NORTH);

		JPanel playerListPanel = new JPanel(new GridLayout(players.size(), 1, 5, 5));
		playerListPanel.setBackground(DARK_BACKGROUND);

		for (Player player : players) {
			JPanel playerPanel = createPlayerPanel(player);
			
			playerListPanel.add(playerPanel);
		}

		teamPanel.add(playerListPanel, BorderLayout.CENTER);

		if (teamName.equals("Green Team")) {
			greenTeamPlayerListPanel = playerListPanel;
		} else {
			redTeamPlayerListPanel = playerListPanel;
		}

		return teamPanel;
	}

	private JPanel createPlayerPanel(Player player){
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new FlowLayout());
		playerPanel.setBackground(DARK_BACKGROUND);

		JLabel playerLabel = new JLabel(
					"ID: " + player.getId() + " | Codename: " + player.getCodeName() + " | Score: 100");
		playerLabel.setForeground(LIGHT_TEXT);
		playerLabel.setFont(new Font("Arial", Font.PLAIN, 18));

		playerPanel.add(playerLabel);

		return playerPanel;
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
		}else{
			System.out.println("No music files were found");
		}
	}
	private void stopMusic() {
		if (musicClip != null && musicClip.isRunning()) {
			musicClip.stop();
			musicClip.close();
		}
	}

	private void startCountdownTimer() {
		timer = new Timer(1000, e -> {
			timeRemaining--;
			countdownTimer.setText("Countdown to laser mayhem: " + timeRemaining);
			countdownTimer.setHorizontalAlignment(SwingConstants.CENTER);

			if (timeRemaining == 15) {
				playMusic();
			} else if (timeRemaining == 0) {
				countdownTimer.setText("GAME STARTING");
				countdownTimer.setHorizontalAlignment(SwingConstants.CENTER);
				timer.stop();
				Timer gameStartingTimer = new Timer(10000, event -> startGame());
				gameStartingTimer.setRepeats(false);
				gameStartingTimer.start();
			}
		});
		timer.start();
	}

	private void startGame() {
		System.out.println("Game started!");
		
		logAction("Game started!");

		//sending out player info as string PlayerID:TeamID with TeamID 0 being green and 1 being red

		new Thread(() -> {

			for(int i = 0; i < greenTeamPlayers.size(); i++){	//sending out green team
				
				Player greenPlayer = greenTeamPlayers.get(i);
				PhotonServerSocket.sendPlayer(Integer.parseInt(equipmentMap.get(greenPlayer.getId())), 0);
			
			}
				
			for(int i = 0; i < redTeamPlayers.size(); i++){		//sending out red team
				
				Player redPlayer = redTeamPlayers.get(i);
				PhotonServerSocket.sendPlayer(Integer.parseInt(equipmentMap.get(redPlayer.getId())), 1);

			}

			PhotonServerSocket.assignCode(202);	//send out code when done getting and assigning players

			//simulating 10 seconds of game time can be changed instructions received at the exact end of the game aren't processed

			try{
				Thread.sleep(10000);
			}catch(Exception e){
				e.printStackTrace();
			}

			PhotonServerSocket.assignCode(221);

			while(PhotonServerSocket.currentlyInExecution()){	//let last instruction finish before game ends
				try{
					Thread.sleep(100);
				}catch(Exception e){
					System.out.println("Exception thrown while waiting for last instruction to execute");
				}
			}

			endGame();

		}).start();

	}
	private void endGame() {

		System.out.println("Green Team total score: " + greenTeamScore + "\nRed Team total score: " + redTeamScore + "\nPlayer Summary:");

		for(int i = 0; i < greenTeamPlayers.size(); i++){
			System.out.println("Green Player " + greenTeamPlayers.get(i).getCodeName() + " score: " + playerScores.get(greenTeamPlayers.get(i).getId()));
		}

		for(int i = 0; i < redTeamPlayers.size(); i++){
			System.out.println("Red Player " + redTeamPlayers.get(i).getCodeName() + " score: " + playerScores.get(redTeamPlayers.get(i).getId()));
		}

		greenTeamPlayers.clear();
		redTeamPlayers.clear();

		stopMusic();
		PhotonServerSocket.RemoveBaseHitter();

		SwingUtilities.invokeLater(() -> {
			PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();
			playerEntryScreen.display();
		});

		Window currentWindow = SwingUtilities.getWindowAncestor(countdownTimer);
		if (currentWindow != null) {
			currentWindow.dispose();
		}
	}


	public void updateScores(int HardwarehitterId, int HardwarehitId, boolean isBaseHit) {

		int hitterScoreChange = isBaseHit ? 20 : 10;

		int hitterId;
		int hitId;

		if(HardwarehitId != 43 && HardwarehitId != 53){
			hitterId = findKeyByValue(equipmentMap, String.valueOf(HardwarehitterId));
			hitId = findKeyByValue(equipmentMap, String.valueOf(HardwarehitId));
			playerScores.put(hitId, playerScores.get(hitId) + -10);	//-10 points for being hit
		}else{
			hitterId = findKeyByValue(equipmentMap, String.valueOf(HardwarehitterId));
		}

		playerScores.put(hitterId, playerScores.get(hitterId) + hitterScoreChange);

		updateTeamScores();
		updatePlayerPanels();

	}

	public Integer findKeyByValue(Map<Integer, String> map, String value) {

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }

        return null; // Return null if no match is found
    }

	private void updateTeamScores() {
		greenTeamScore = greenTeamPlayers.stream().mapToInt(player -> playerScores.get(player.getId())).sum();
		redTeamScore = redTeamPlayers.stream().mapToInt(player -> playerScores.get(player.getId())).sum();

		if (greenTeamScoreLabel != null) {
			greenTeamScoreLabel.setText("Green Team Score: " + greenTeamScore);
		}
		if (redTeamScoreLabel != null) {
			redTeamScoreLabel.setText("Red Team Score: " + redTeamScore);
		}
	}

	private void updatePlayerPanels() {

		for (int i = 0; i < greenTeamPlayers.size(); i++) {
			Player player = greenTeamPlayers.get(i);
			int score = playerScores.get(player.getId());

			for(Component c : greenTeamPlayerListPanel.getComponents()){
				JPanel playerPanel = (JPanel) c;
				for(Component d : playerPanel.getComponents()){
					if(d instanceof JLabel){
						JLabel playerLabel = (JLabel) d;
						playerLabel.setText("ID: " + player.getId() + " | Codename: " + player.getCodeName() + " | Score: " + score);
					}
				}
			}
		}
		for (int i = 0; i < redTeamPlayers.size(); i++) {
			Player player = redTeamPlayers.get(i);
			int score = playerScores.get(player.getId());
			
			for(Component c : redTeamPlayerListPanel.getComponents()){
				JPanel playerPanel = (JPanel) c;
				for(Component d : playerPanel.getComponents()){
					if(d instanceof JLabel){
						JLabel playerLabel = (JLabel) d;
						playerLabel.setText("ID: " + player.getId() + " | Codename: " + player.getCodeName() + " | Score: " + score);
					}
				}
			}
		}
	}

	public void addB(int team, int ID){	//team 0: green team 1: red

		System.out.println("Someone hit the base looking to add stylized B");

		int PlayerID = findKeyByValue(equipmentMap, String.valueOf(ID));

		String filePath = "assets/B.png";
		File file = new File(filePath);

		if(file.exists()){

			JLabel image = new JLabel(new ImageIcon(filePath));

			if(team == 0){

				JPanel playerPanel = searchForPlayer(greenTeamPlayerListPanel, PlayerID);
				playerPanel.add(image, 0);
				greenTeamPlayerListPanel.revalidate();
				greenTeamPlayerListPanel.repaint();
	
			}else if(team == 1){
	
				JPanel playerPanel = searchForPlayer(redTeamPlayerListPanel, PlayerID);
				playerPanel.add(image, 0);
				redTeamPlayerListPanel.revalidate();
				redTeamPlayerListPanel.repaint();
	
			}else{
	
				System.out.println("Trying to add bold B to a player that isn't on a recognized team");
	
			}

		}else{

			System.out.println("Was unable to find file for stylize B using file path " + filePath);

		}

	}

	private JPanel searchForPlayer(JPanel playerListPanel, int ID){

		for(Component c : playerListPanel.getComponents()){
			if(c instanceof JPanel){

				JPanel playerPanel = (JPanel) c;

				for(Component innerc : playerPanel.getComponents()){
					if(innerc instanceof JLabel){
						JLabel player = (JLabel) innerc;
						if(player.getText().contains("ID: " + ID)){
							return playerPanel;
						}
					}
				}
			}
		}
		return null;
	}

	private boolean isNumeric(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void logAction(String action) {
		actionLogArea.append(action + "\n");
		actionLogArea.setCaretPosition(actionLogArea.getDocument().getLength());
	}

}
