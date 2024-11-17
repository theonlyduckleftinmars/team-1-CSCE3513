/*
Name: Will Taylor
Class: CSCE 35103
Assignment: Sprint 5

Docu:
This file is the class for the server sockets of the photon laser game and uses udp socket 22

TODO:
ASK ABOUT LOCAL HOST AND IF THAT NEEDS TO CHANGE
CHANGE PORTS BACK WHEN DONE TESTING
FIGURE OUT HOW TO ASSIGN CODES

*/

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class PhotonClientSocket{
    
    DatagramSocket ssin;
    DatagramSocket ssout;

    PhotonClientSocket() throws IOException{ //will return an exception if error instead of actual server socket class

        ssin = new DatagramSocket(7500);
        ssout = new DatagramSocket(7501);
        Scanner scan = new Scanner(System.in);

        String userInput;
        System.out.println("Connected type to send messages");
        while(true){
            userInput = scan.nextLine();
            if("exit".equalsIgnoreCase(userInput)){
                break;
            }
            System.out.println("Input: " + userInput + " Sent over port 7501 to address: " + InetAddress.getLocalHost());

            byte arr[] = userInput.getBytes();
    
            DatagramPacket packet = new DatagramPacket(arr, arr.length, InetAddress.getLocalHost(), 7501);


            ssout.send(packet);

        }

    }

    //testing
    public static void main(String args[]){

        try{
            PhotonClientSocket cs = new PhotonClientSocket();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
