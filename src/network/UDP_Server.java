package network;

import player.CustomSynthesizer;
import player.Drummer;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.InterruptibleChannel;
import java.time.Clock;

public class UDP_Server implements Runnable, InterruptibleChannel {

    private int PORT;
    private DatagramSocket socket;
    private byte[] receivedData;
    private Drummer drummer;

    public UDP_Server(int port) {
        this.PORT = port;

        CustomSynthesizer cs = new CustomSynthesizer();
        URL resource = getClass().getResource("/soundbanks/sdb.sf2");
        String fileName = resource.getFile();
        File file = new File(fileName);

        cs.loadSoundbank(file);

        this.drummer = new Drummer(cs);
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
        this.receivedData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);


        socket.receive(receivePacket);


        receivedData = receivePacket.getData();

        //String message = new String(receivePacket.getData());
        System.out.println("Receiving packet from " + receivePacket.getAddress()+":"+receivePacket.getPort());
        //System.out.println("Data: "+ message+"\n");
        // System.out.println("Receiced int : " + receivedData[0] + " " + receivedData[4]);


        drummer.noteOn(receivedData[0], receivedData[4]);
        Clock clock = Clock.systemUTC();
        System.out.println(clock.millis());

    }

    @Override
    public boolean isOpen() {
        return !socket.isClosed();
    }

    public void close() throws IOException {
        socket.close();
    }


}