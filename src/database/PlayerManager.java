package database;

import model.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class PlayerManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/photon";
    private static final String USERNAME = "student";
    private static final String PASSWORD = "student";

    public void insertPlayer(Player player) {
        String sql = "INSERT INTO players (id, codename) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setInt(1, player.getId());
            statement.setString(2, player.getCodeName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT id, codename FROM players";

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = con.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String codename = resultSet.getString("codename");
                players.add(new Player(id, codename));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
        // test
    }
}
