package view;

import model.Player;
import database.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;


public class PlayerEntryScreen {

    private static final int NUM_PLAYERS = 15;
    private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
    private static final Color DARKER_BACKGROUND = new Color(30, 30, 30);
    private static final Color LIGHT_TEXT = new Color(200, 200, 200);

    private JTextField[][] greenTeamFields = new JTextField[NUM_PLAYERS][3];
    private JTextField[][] redTeamFields = new JTextField[NUM_PLAYERS][3];
    private JFrame frame;

    public void display() {
        JFrame frame = new JFrame("Laser Tag - Photon");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel greenTeamPanel = createTeamPanel("Green Team", Color.GREEN, greenTeamFields);
        JPanel redTeamPanel = createTeamPanel("Red Team", Color.RED, redTeamFields);

        JPanel teamsPanel = new JPanel(new GridLayout(1, 2));
        teamsPanel.add(greenTeamPanel);
        teamsPanel.add(redTeamPanel);
        teamsPanel.setBackground(DARK_BACKGROUND);

        JButton enterNewPlayerButton = new JButton("Enter New Player");
        JButton submitPlayersButton = new JButton("Submit Players");

        enterNewPlayerButton.setBackground(DARKER_BACKGROUND);
        enterNewPlayerButton.setForeground(LIGHT_TEXT);
        enterNewPlayerButton.setFocusPainted(false);
        enterNewPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPlayerForm(frame);
            }
        });

        submitPlayersButton.setBackground(DARKER_BACKGROUND);
        submitPlayersButton.setForeground(LIGHT_TEXT);
        submitPlayersButton.setFocusPainted(false);
        submitPlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPlayers();
            }
        });

        // Create an instructional label
        JLabel instructionLabel = new JLabel("<F5> to submit players and <F12> to clear players");
        instructionLabel.setForeground(LIGHT_TEXT);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(enterNewPlayerButton);
        buttonPanel.add(submitPlayersButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(instructionLabel, BorderLayout.SOUTH);
        bottomPanel.setBackground(DARK_BACKGROUND);

        setupKeyBindings(frame.getRootPane());

        frame.add(teamsPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.requestFocusInWindow();
        frame.getContentPane().setBackground(DARK_BACKGROUND);
        frame.setVisible(true);
    }


    private void setupKeyBindings(JComponent component) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("F12"), "clearPlayers");
        inputMap.put(KeyStroke.getKeyStroke("F5"), "submitPlayers");
        actionMap.put("clearPlayers", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPlayers();
            }
        });
        actionMap.put("submitPlayers", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPlayers();
            }
        });
    }

    public void clearPlayers() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            for (int j = 0; j < 3; j++) {
                greenTeamFields[i][j].setText("");
                redTeamFields[i][j].setText("");
            }
        }
        JOptionPane.showMessageDialog(frame, "All players have been cleared!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }


    private JPanel createTeamPanel(String teamName, Color teamColor, JTextField[][] playerFields) {
        JPanel teamPanel = new JPanel(new BorderLayout());
        teamPanel.setBorder(BorderFactory.createLineBorder(teamColor, 3));
        teamPanel.setBackground(DARK_BACKGROUND);

        JLabel teamLabel = new JLabel(teamName, SwingConstants.CENTER);
        teamLabel.setFont(new Font("Arial", Font.BOLD, 24));
        teamLabel.setOpaque(true);
        teamLabel.setBackground(teamColor);
        teamLabel.setForeground(Color.WHITE);
        teamPanel.add(teamLabel, BorderLayout.NORTH);

        JPanel playerInputPanel = new JPanel(new GridLayout(NUM_PLAYERS, 1, 5, 5));
        playerInputPanel.setBackground(DARK_BACKGROUND);

        for (int i = 0; i < NUM_PLAYERS; i++) {
            JPanel row = new JPanel(new GridLayout(1, 3, 5, 5));
            row.setBackground(DARK_BACKGROUND);

            JTextField playerIdField = createInputField("Player ID", true);
            JTextField codeNameField = createInputField("Code Name", false);
            JTextField equipmentIdField = createInputField("Equipment ID", true);

            row.add(playerIdField);
            row.add(codeNameField);
            row.add(equipmentIdField);

            playerFields[i][0] = playerIdField;
            playerFields[i][1] = codeNameField;
            playerFields[i][2] = equipmentIdField;

            playerInputPanel.add(row);
        }

        teamPanel.add(playerInputPanel, BorderLayout.CENTER);

        return teamPanel;
    }

    private JTextField createInputField(String title, boolean isNumeric) {
        JTextField textField = new JTextField();
        textField.setBackground(DARKER_BACKGROUND);
        textField.setForeground(LIGHT_TEXT);
        textField.setCaretColor(LIGHT_TEXT);
        textField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(LIGHT_TEXT, 1), title, 0, 0, null, LIGHT_TEXT));

        if (isNumeric) {
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != '\b') {
                        e.consume();
                    }
                }
            });
        }

        return textField;
    }

    private void openPlayerForm(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "New Player Form", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2));

        JLabel playerIdLabel = new JLabel("Player ID:");
        JTextField playerIdField = new JTextField();
        playerIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });

        JLabel codeNameLabel = new JLabel("Code Name:");
        JTextField codeNameField = new JTextField();

        JLabel equipmentIdLabel = new JLabel("Equipment ID:");
        JTextField equipmentIdField = new JTextField();
        equipmentIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });

        JLabel teamLabel = new JLabel("Team:");
        String[] teams = {"Green", "Red"};
        JComboBox<String> teamSelector = new JComboBox<>(teams);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerId = playerIdField.getText();
                String codeName = codeNameField.getText();
                String equipmentId = equipmentIdField.getText();
                String team = (String) teamSelector.getSelectedItem();

                if (!playerId.isEmpty() && !codeName.isEmpty()) {
                    System.out.println("Equipment ID for UDP: " + equipmentId);
                    updatePlayerEntry(playerId, codeName, equipmentId, team);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.add(playerIdLabel);
        dialog.add(playerIdField);
        dialog.add(codeNameLabel);
        dialog.add(codeNameField);
        dialog.add(equipmentIdLabel);
        dialog.add(equipmentIdField);
        dialog.add(teamLabel);
        dialog.add(teamSelector);
        dialog.add(new JLabel());
        dialog.add(submitButton);

        dialog.setVisible(true);
    }

    private void updatePlayerEntry(String playerId, String codeName, String equipmentId, String team) {
        JTextField[][] playerFields = team.equals("Green") ? greenTeamFields : redTeamFields;

	    //Adjust the font size so nothing get's cut off. Declare a font!
        Font customFont = new Font("Arial", Font.PLAIN, 9);

       	for (int i = 0; i < NUM_PLAYERS; i++) {
            if (playerFields[i][0].getText().isEmpty()) {
                playerFields[i][0].setText(playerId);
                playerFields[i][1].setText(codeName);
                playerFields[i][2].setText(equipmentId);
               
		//Added to set font sizes
		playerFields[i][0].setFont(customFont);
		playerFields[i][1].setFont(customFont);
		playerFields[i][2].setFont(customFont);		
		break;
            }
        }
    }

    private void submitPlayers() {
        PlayerManager playerManager = new PlayerManager();
        List<Player> greenTeamPlayers = new ArrayList<>();
        List<Player> redTeamPlayers = new ArrayList<>();

        for (int i = 0; i < NUM_PLAYERS; i++) {
            if (!greenTeamFields[i][0].getText().isEmpty()) {
                Player greenPlayer = new Player(Integer.parseInt(greenTeamFields[i][0].getText()), greenTeamFields[i][1].getText());
                greenTeamPlayers.add(greenPlayer);
                playerManager.insertPlayer(greenPlayer);
            }
            if (!redTeamFields[i][0].getText().isEmpty()) {
                Player redPlayer = new Player(Integer.parseInt(redTeamFields[i][0].getText()), redTeamFields[i][1].getText());
                redTeamPlayers.add(redPlayer);
                playerManager.insertPlayer(redPlayer);
            }
        }
        JDialog dialog = new JDialog(frame, "ERROR SUBMITTING PLAYERS", true);

        if(greenTeamPlayers.isEmpty() && redTeamPlayers.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Players empty!", "Error", JOptionPane.ERROR_MESSAGE);

        } else {
            PlayActionScreen playActionScreen = new PlayActionScreen(greenTeamPlayers, redTeamPlayers);
            playActionScreen.display();
        }
    }

}
