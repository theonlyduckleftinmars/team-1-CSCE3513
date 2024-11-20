import view.PlayerEntryScreen;
import view.SplashScreen;
import network.PhotonServerSocket;
import database.PlayerManager;

public class Main {
    public static void main(String[] args) {

        PhotonServerSocket pss = new PhotonServerSocket();
        PlayerManager playerManager = new PlayerManager();
        SplashScreen splashScreen = new SplashScreen();
        PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();

        splashScreen.display();
        playerEntryScreen.display();

        playerManager.loadPlayers();


    }
}
