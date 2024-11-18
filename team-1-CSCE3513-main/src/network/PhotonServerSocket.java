package network;

import java.net.*;
import java.util.concurrent.*;

public class PhotonServerSocket {

    private static final int IN_PORT = 7501;
    private static final int OUT_PORT = 7500;

    private ThreadPoolExecutor exe = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.DAYS,
            new ArrayBlockingQueue<>(1));

    private DatagramSocket sin;
    private DatagramSocket sout;
    private ClientHandler ch;

    private int baseHitterCode = -1;
    private boolean baseHitToggle = false;

    public PhotonServerSocket() {
        try {
            sin = new DatagramSocket(IN_PORT);
            sout = new DatagramSocket();
            System.out.println("Server input socket initialized: " + sin);
        } catch (SocketException se) {
            System.out.println("Error setting up sockets");
            se.printStackTrace();
        }

        addClientHandler();
    }

    private void addClientHandler() {
        System.out.println("Client handler setup");

        ch = new ClientHandler(sin, this);

        exe.submit(ch);
    }

    public void assignCode(int code) {
        String codeString = String.valueOf(code);
        byte[] byteArray = codeString.getBytes();

        try {
            DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, InetAddress.getLocalHost(),
                    OUT_PORT);
            sout.send(packet);
        } catch (Exception e) {
            System.out.println("Error sending out a code");
            e.printStackTrace();
        }
    }

    private void decode(String code, ClientHandler ch) {
        System.out.println("Current base hitter: " + baseHitterCode + "\nCurrent toggle on base hits: "
                + baseHitToggle);
        System.out.println("Code received was: " + code);

        String response = null;
        if (code.contains(":")) {
            String players[] = code.split(":");
            if (players.length == 2) {
                String shooter = players[0];
                String target = players[1];
                System.out.println("Player " + target + " was hit by Player " + shooter);

                if (Integer.parseInt(target) == 43) {
                    System.out.println("Player " + shooter + " has hit the base");
                    if (!baseHitToggle && baseHitterCode == -1) {
                        setBaseHitter(Integer.parseInt(shooter));
                    }
                    response = "Base hit by Player " + shooter;
                } else if (Integer.parseInt(target) == 53) {
                    response = "Green base hit by Player " + shooter;
                } else {
                    response = "Player " + shooter + " hit Player " + target;
                }
            }
        } else if (code.equals("202")) {
            response = "Game started!";
        } else if (code.equals("221")) {
            response = "Game ended!";
        } else {
            response = "Invalid code received: " + code;
        }
        if (response != null) {
            sendResponse(response, ch);
        }
    }

    private void sendResponse(String message, ClientHandler ch) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),
                    OUT_PORT);
            sout.send(responsePacket);
            System.out.println("Response sent: " + message);
        } catch (Exception e) {
            System.out.println("Error sending response: " + message);
            e.printStackTrace();
        }
    }

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

    public void RemoveBaseHitter() {
        baseHitToggle = false;
        baseHitterCode = -1;
    }

    private void setBaseToggle(boolean toggle) {
        baseHitToggle = toggle;
    }

    private void setBaseHitter(int code) {
        if (code != 43) {
            baseHitterCode = code;
            setBaseToggle(true);
        } else {
            System.out.println("Base cannot hit another base code wasn't acted upon");
        }
    }

    public int getHitterCode() {
        return baseHitterCode;
    }

    private static class ClientHandler implements Runnable {

        private DatagramSocket sin;
        private PhotonServerSocket pss;

        private boolean keepRunning = true;

        public ClientHandler(DatagramSocket sin, PhotonServerSocket pss) {
            this.pss = pss;
            this.sin = sin;
        }

        @Override
        public void run() {
            while (keepRunning) {
                try {
                    byte buffer[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    sin.receive(packet);

                    int length = packet.getLength();
                    byte[] data = packet.getData();

                    pss.decode(new String(data, 0, length), this);
                } catch (Exception e) {
                    System.out.println("There was an error in the thread for receiving packets");
                    e.printStackTrace();
                }
            }
        }

        public void StopRunning() {
            keepRunning = false;
        }
    }
}
