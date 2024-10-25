import view.PlayerEntryScreen;
import view.SplashScreen;
import network.*;

public class Main {
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();
        PhotonServerSocket = new PhotonServerSocket();
        PhotonClientSocket = new PhotonClientSocket();
        PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();

        splashScreen.display();
        playerEntryScreen.display();
        udpManager.receiveHits();
    }
}
