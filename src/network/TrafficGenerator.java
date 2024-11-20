import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.ArrayList;

public class TrafficGenerator {

    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 7500;
    private static final int CLIENT_PORT = 7501;

    public static void main(String[] args) {
        try {

            System.out.println("This program will generate some test traffic players provided by user from UI inputs");

            ArrayList<Integer> redTeam = new ArrayList<Integer>();
            ArrayList<Integer> greenTeam = new ArrayList<Integer>();

            DatagramSocket receiveSocket = new DatagramSocket(SERVER_PORT);
            DatagramSocket sendSocket = new DatagramSocket();

            System.out.println("\nWaiting for start from game software");

            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            String receivedData = "";

            while (!receivedData.equals("202")) {
                receiveSocket.receive(receivePacket);
                receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from game software: " + receivedData);

                if (receivedData.contains(":")) {
                    String player[] = receivedData.split(":");
                    if (player.length == 2) {
                        if(player[1].equals("0")){
                            greenTeam.add(Integer.parseInt(player[0]));
                        }else if(player[1].equals("1")){
                            redTeam.add(Integer.parseInt(player[0]));
                        }else{
                            System.out.println("Player team ID does not match up with any of the teams " + receivedData);
                        }
                    }else{
                        System.out.println("One of the inputs was for a player of improper length " + receivedData);
                    }

                }

            }

            System.out.println("");

            Random random = new Random();
            int counter = 0;

            while (true) {
                int redPlayerIndex = random.nextInt(redTeam.size());
                int greenPlayerIndex = random.nextInt(greenTeam.size());
                String message;

                if (random.nextInt(2) == 0) {
                    message = redTeam.get(redPlayerIndex) + ":" + greenTeam.get(greenPlayerIndex);
                } else {
                    message = greenTeam.get(greenPlayerIndex) + ":" + redTeam.get(redPlayerIndex);
                }

                if (counter == 10) {
                    message = redTeam.get(redPlayerIndex) + ":43";
                }
                if (counter == 20) {
                    message = greenTeam.get(greenPlayerIndex) + ":53";
                }

                System.out.println("Transmitting to game: " + message);

                byte[] sendBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(SERVER_ADDRESS), CLIENT_PORT);
                sendSocket.send(sendPacket);

                receiveSocket.receive(receivePacket);
                receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from game software: " + receivedData);
                System.out.println("");

                counter++;
                if (receivedData.equals("221")) {
                    break;
                }

                Thread.sleep(random.nextInt(3) * 100);
            }

            System.out.println("Program complete");
            receiveSocket.close();
            sendSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
