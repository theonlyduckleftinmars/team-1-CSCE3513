import view.PlayerEntryScreen;
import view.SplashScreen;
import network.*;

public class Main {
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();
        PhotonServerSocket pss = new PhotonServerSocket();
        PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();

        splashScreen.display();
        playerEntryScreen.display();
        udpManager.receiveHits();
    }
}
