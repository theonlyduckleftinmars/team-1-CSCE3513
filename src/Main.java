import view.PlayerEntryScreen;
import view.SplashScreen;
import network.PhotonServerSocket;
import database.PlayerManager;

public class Main {
    public static void main(String[] args) {
        PlayerManager playerManager = new PlayerManager();
        SplashScreen splashScreen = new SplashScreen();
        PhotonServerSocket pss = new PhotonServerSocket();
        PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();

        splashScreen.display();
        playerEntryScreen.display();

        playerManager.loadPlayers();
    }
}
