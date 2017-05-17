package network;

import java.io.*;
import java.net.*;

class UDP_Client {

    public static void main(String args[]) throws Exception {

        Integer PORT = 5678;
        InetAddress IP_ADRESS;IP_ADRESS = InetAddress.getByName("127.0.0.1");

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();

        byte[] sendData = new byte[1024];

        System.out.println("Client open.. [CTRL+C to quit]");

        while(true) {
        	System.out.println("Message to send :");

            String message = inFromUser.readLine();
            sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IP_ADRESS, PORT);

            clientSocket.send(sendPacket);
        }

        // clientSocket.close();
    }

}