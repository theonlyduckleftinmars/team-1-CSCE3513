import view.PlayerEntryScreen;
import view.SplashScreen;
import network.PhotonServerSocket;

public class Main {
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();
        PhotonServerSocket photonServerSocket = new PhotonServerSocket();
        PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();

        splashScreen.display();
        playerEntryScreen.display();

    }
}
