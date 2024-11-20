package network;

//UDP
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
//Threads
import java.util.concurrent.*;
//Utility
import java.nio.ByteBuffer;

public class PhotonServerSocket {

    private static final int IN_PORT = 7501;
    private static final int OUT_PORT = 7500;

    private static int greenBaseHitterCode = -1;    //when -1 base hasn't been hit else it will be the code of the player who hit the base
    private static boolean greenBaseHitToggle = false; //when false base hasn't been hit yet else the base has been hit
    private static int redBaseHitterCode = -1;
    private static boolean redBaseHitToggle = false;

    //managing the thread that listens for messages from clients
    private ThreadPoolExecutor exe = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.DAYS, new ArrayBlockingQueue<>(1));

    private ClientHandler ch;

    public PhotonServerSocket() { //will return an exception if error instead of actual server socket class

        AddClientHandler();

    }

    private void AddClientHandler() { //should run constantly so it can check for new clients and add them to the server will also throw exception on error

        ch = new ClientHandler(this); //make a new handler which will be used to listen for inputs

        exe.submit(ch); //run this handler in a new thread so this can listen for what clients send
    }

    //Server functions for server managing handlers (server to handler interactions)
    //use to send codes out to clients
    public static void assignCode(int code) {

        String codeString = String.valueOf(code);
        byte[] byteArray = codeString.getBytes();

        try {
            DatagramSocket sout = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, InetAddress.getLocalHost(), OUT_PORT);
            sout.send(packet);
            sout.close();
        } catch (Exception e) {
            System.out.println("Error sending out a code");
            e.printStackTrace();
        }

    }

    public static void sendPlayer(int ID, int teamID){

        String codeString = String.valueOf(ID) + ":" + String.valueOf(teamID);
        byte[] byteArray = codeString.getBytes();

        try {
            DatagramSocket sout = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, InetAddress.getLocalHost(), OUT_PORT);
            sout.send(packet);
            sout.close();
        } catch (Exception e) {
            System.out.println("Error sending out a code");
            e.printStackTrace();
        }

    }

    //Handler to server functions
    private void Decode(String code, ClientHandler ch) {
        //System.out.println("Current Red base hitter: " + redBaseHitterCode + "\nCurrent toggle on red base hits: " + redBaseHitToggle);
        //System.out.println("Current Green base hitter: " + greenBaseHitterCode + "\nCurrent toggle on green base hits: " + greenBaseHitToggle);
        //System.out.println("Code received was: " + code);

        String response = null;
        if (code.contains(":")) {
            String players[] = code.split(":");
            if (players.length == 2) {
                String shooter = players[0];
                String target = players[1];

                // Base hit logic
                if (Integer.parseInt(target) == 43) {
                    if (!greenBaseHitToggle && greenBaseHitterCode == -1) {
                        System.out.println("Player " + shooter + " has hit the green base");
                        SetGreenBaseHitter(Integer.parseInt(shooter));
                        response = "Green base hit by Player " + shooter;
                    }
                } else if (Integer.parseInt(target) == 53) {
                    if (!redBaseHitToggle && redBaseHitterCode == -1) {
                        System.out.println("Player " + shooter + " has hit the red base");
                        SetGreenBaseHitter(Integer.parseInt(shooter));
                        response = "Green base hit by Player " + shooter;
                    }
                } else {
                    response = "Player " + shooter + " hit Player " + target;
                }
            }
        } else {
            response = "Invalid code received: " + code;
        }
        // Send response back to the client
        if (response != null) {
            sendResponse(response);
        }
    }

    private void sendResponse(String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramSocket sout = new DatagramSocket();
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), OUT_PORT);
            sout.send(responsePacket);
            sout.close();
        } catch (Exception e) {
            System.out.println("Error sending response: " + message);
            e.printStackTrace();
        }
    }

    //Threads execution methods
    public void StopRunningThread() {
        try {
            exe.shutdown();
        } catch (Exception e) {
            System.out.println("There was an exception to cleanly shutting down a thread");
            e.printStackTrace();
        }
    }

    public void RestartThread() {
        try {
            exe.submit(ch);
        } catch (Exception e) {
            System.out.println("There was an exception to restarting a thread");
            e.printStackTrace();
        }
    }

    //Getters and setters
    public static void RemoveBaseHitter() {
        greenBaseHitToggle = false;
        greenBaseHitterCode = -1;
        redBaseHitToggle = false;
        redBaseHitterCode = -1;
    }

    private void SetGreenBaseToggle(boolean toggle) {
        greenBaseHitToggle = toggle;
    }

    private void SetRedBaseToggle(boolean toggle) {
        redBaseHitToggle = toggle;
    }

    private void SetGreenBaseHitter(int code) {
        if (code != 43 && code != 53) {
            greenBaseHitterCode = code;
            SetGreenBaseToggle(true);
        } else {
            System.out.println("Base cannot hit another base code wasn't acted upon");
        }
    }

    private void SetRedBaseHitter(int code) {
        if (code != 43 && code != 53) {
            redBaseHitterCode = code;
            SetRedBaseToggle(true);
        } else {
            System.out.println("Base cannot hit another base code wasn't acted upon");
        }
    }

    //CLIENT HANDLER
    private static class ClientHandler implements Runnable { //inner class to help handle each client inside server

        private PhotonServerSocket pss;

        private boolean keepRunning = true;

        //Constructor
        public ClientHandler(PhotonServerSocket pss) { //instantiate with socket from accept in AddClient

            this.pss = pss;

        }

        public void run() { //this is what runs when thread is executed and gets info from client as a thread separate from main so it can run concurrently waiting for data without blocking main thread
            //receives data from the client

            while (keepRunning) {
                try {
                    byte buffer[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    DatagramSocket in = new DatagramSocket(IN_PORT);
                    in.receive(packet); //receiving
                    System.out.println("Packet was received");

                    int length = packet.getLength();
                    byte[] data = packet.getData();

                    pss.Decode(new String(data, 0, length), this); //add handling to make sure data received is string
                    in.close();
                } catch (Exception e) {
                    System.out.println("There was an error in the thread for receiving packets");
                    e.printStackTrace();
                }
            }
                
        }

        public void StopRunning() {  //stop the while loop
            keepRunning = false;
        }

    }
}
