package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPManager {

    private static final int BROADCAST_PORT = 7500;
    private static final int RECEIVE_PORT = 7501;

    public void broadcastHit(int equipmentId) {
        try (DatagramSocket socket = new DatagramSocket()) {

            byte[] buffer = String.valueOf(equipmentId).getBytes();
            InetAddress address = InetAddress.getByName("255.255.255.255"); // Broadcast address

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, BROADCAST_PORT);

            socket.send(packet);
            System.out.println("Broadcasted hit for equipment ID: " + equipmentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveHits() {
        try (DatagramSocket socket = new DatagramSocket(RECEIVE_PORT)) {
            byte[] buffer = new byte[256];

            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received hit data: " + received);

                String[] data = received.split(":");
                if (data.length == 2) {
                    int transmittingId = Integer.parseInt(data[0]);
                    int hitId = Integer.parseInt(data[1]);
                    System.out.println("Player with equipment ID " + transmittingId + " hit player with equipment ID " + hitId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
