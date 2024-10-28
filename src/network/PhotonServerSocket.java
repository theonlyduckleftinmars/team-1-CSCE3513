/*
TODO: Figure out if laser tag machines are on local network or have own separate IPs

Docu:
PORTS:
    7500: Client sends data over this port for server to receive
    7501: Server sends data over this port for client to receive

PhotonServerSocket:
    Decodes messages received from client handler and also broadcasts codes out for client machines to be activated

ClientHandler:
    Deals with input data from client machines and passes off data received to PhotonServerSocket while running in a separate thread

*/

//UDP
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
//Threads
import java.util.concurrent.*;
//Utility
import java.nio.ByteBuffer;

public class PhotonServerSocket{

    private static final int IN_PORT = 7500;
    private static final int OUT_PORT = 7501;

    //managing the thread that listens for messages from clients
    private ThreadPoolExecutor exe = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.DAYS, new ArrayBlockingQueue<>(0), new ThreadPoolExecutor.AbortPolicy());

    private DatagramSocket sin;
    private DatagramSocket sout;
    private ClientHandler ch;

//--------------------------------------------------------------------------------------------------------------------------------

    PhotonServerSocket(){ //will return an exception if error instead of actual server socket class

        try{
        sin = new DatagramSocket(IN_PORT);    //port it should receive data from
        sout = new DatagramSocket(OUT_PORT);    //port it should send data to
        }catch(SocketException se){
            System.out.println("Error setting up sockets");
            se.printStackTrace();
        }

        AddClientHandler();

    }

    private void AddClientHandler(){ //should run constantly so it can check for new clients and add them to the server will also throw exception on error

        System.out.println("Client handler setup");

        ch = new ClientHandler(sin, this); //make a new handler which will be used to listen for inputs

        exe.submit(ch); //run this handler in a new thread so this can listen for what clients send

    }

//--------------------------------------------------------------------------------------------------------------------------------
//Server functions for server managing handlers (server to handler interactions)

    //use to send codes out to clients
    public void assignCode(int code){

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(code);
        byte byteArray[] = buffer.array();
        
        try{
            DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, InetAddress.getLocalHost(), OUT_PORT);
            sout.send(packet);
        }catch(Exception e){
            System.out.println("Error sending out a code");
            e.printStackTrace();
        }

    }

//--------------------------------------------------------------------------------------------------------------------------------
//Handler to server functions

    private void Decode(String code, ClientHandler ch){
        
        if(code.contains(":")){
            String players[] = code.split(":");
            if(players.length == 2)
                System.out.println("Player " + players[1] + " was hit");  //get everything to right of semicolon which is the code of hit person
        }else{
            System.out.println("Code recieved from client did not match currently compatible codes: " + code);
        }

    }

//--------------------------------------------------------------------------------------------------------------------------------
//Threads execution methods
    
    //TODO handle a harder shutdown of threads if cleanly doing it fails
    public void StopRunningThread(){

        try{
            exe.shutdown();
        }catch(Exception e){
            System.out.println("There was an exeception to cleanly shutting down a thread");
            e.printStackTrace();
        }

    }

    public void RestartThread(){

        try{
            exe.submit(ch);
        }catch(Exception e){
            System.out.println("There was an exception to restarting a thread");
            e.printStackTrace();
        }

    }

//--------------------------------------------------------------------------------------------------------------------------------
//CLIENT HANDLER

    private static class ClientHandler implements Runnable{ //inner class to help handle each client inside server

        private DatagramSocket sin;
        private PhotonServerSocket pss;
    
        private boolean keepRunning = true;

//--------------------------------------------------------------------------------------------------------------------------------
//Constructor

        public ClientHandler(DatagramSocket sin, PhotonServerSocket pss){ //instantiate with socket from accept in AddClient
            
            this.pss = pss;
            this.sin = sin;
    
        }

//--------------------------------------------------------------------------------------------------------------------------------

        public void run(){ //this is what runs when thread is executed and gets info from client as a thread separate from main so it can run concurrently waiting for data without blocking main thread
    
            //recieves data from the client
    
            while(keepRunning){
    
                try{

                    byte buffer[] = new byte[1024];

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    sin.receive(packet); //receiving
        
                    int length = packet.getLength();
                    byte[] data = packet.getData();
        
                    pss.Decode(new String(data, 0, length), this); //add handlling to make sure data recieved is string
                }catch(Exception e){

                    System.out.println("There was an error in the thread for receiving packets");
                    e.printStackTrace();

                }
    
            }
    
        }
    
        public void StopRunning(){  //stop the while loop
    
            keepRunning = false;
    
        }
    
    }

}

//--------------------------------------------------------------------------------------------------------------------------------
