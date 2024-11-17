// TrafficGenerator.java
package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

public class TrafficGenerator {

    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 7500;
    private static final int CLIENT_PORT = 7501;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("This program will generate some test traffic for 2 players on the red team as well as 2 players on the green team\n");

            System.out.print("Enter equipment id of red player 1 ==> ");
            String red1 = scanner.nextLine();
            System.out.print("Enter equipment id of red player 2 ==> ");
            String red2 = scanner.nextLine();
            System.out.print("Enter equipment id of green player 1 ==> ");
            String green1 = scanner.nextLine();
            System.out.print("Enter equipment id of green player 2 ==> ");
            String green2 = scanner.nextLine();

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
            }

            System.out.println("");

            Random random = new Random();
            int counter = 0;

            while (true) {
                String redPlayer = random.nextInt(2) == 0 ? red1 : red2;
                String greenPlayer = random.nextInt(2) == 0 ? green1 : green2;
                String message;

                if (random.nextInt(2) == 0) {
                    message = redPlayer + ":" + greenPlayer;
                } else {
                    message = greenPlayer + ":" + redPlayer;
                }

                if (counter == 10) {
                    message = redPlayer + ":43";
                }
                if (counter == 20) {
                    message = greenPlayer + ":53";
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

                Thread.sleep(random.nextInt(3) * 1000);
            }

            System.out.println("Program complete");
            receiveSocket.close();
            sendSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}