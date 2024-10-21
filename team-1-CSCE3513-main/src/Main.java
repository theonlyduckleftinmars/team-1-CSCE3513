import view.PlayerEntryScreen;
import view.SplashScreen;
import network.*;

public class Main {
    public static void main(String[] args) {
        SplashScreen splashScreen = new SplashScreen();
        UDPManager udpManager = new UDPManager();
        PlayerEntryScreen playerEntryScreen = new PlayerEntryScreen();

        splashScreen.display();
        playerEntryScreen.display();
        udpManager.receiveHits();
    }
}
