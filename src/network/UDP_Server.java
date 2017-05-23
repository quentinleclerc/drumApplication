package network;

import control.NoteChannel;

import java.io.IOException;
import java.net.*;
import java.nio.channels.InterruptibleChannel;
import java.time.Clock;

public class UDP_Server implements Runnable, InterruptibleChannel {

    private int PORT;
    private DatagramSocket socket;
    private NoteChannel listener;
    private Clock clock = Clock.systemUTC();

    public UDP_Server(int port) {
        this.PORT = port;
    }

    public void run() {

        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        System.out.println("Server receiving..");

        while(true) {
            try {
                receive();
            } catch (IOException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void receive() throws IOException {
        byte[] receivedData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);


        socket.receive(receivePacket);


        receivedData = receivePacket.getData();

        System.out.println("Receiving packet from " + receivePacket.getAddress()+":"+receivePacket.getPort());
        System.out.println("Receiced int : " + receivedData[0] + " " + receivedData[4]);

        listener.receivedNote(receivedData[0], receivedData[4], clock.millis());

    }

    @Override
    public boolean isOpen() {
        return !socket.isClosed();
    }

    public void close() throws IOException {
        socket.close();
    }


    public void setListener(NoteChannel listener) {
        this.listener = listener;
    }
}