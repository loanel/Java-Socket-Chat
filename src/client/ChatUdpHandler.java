package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.logging.Logger;

class ChatUdpHandler extends Thread{
    private static Logger logger = Logger.getLogger("Chat Udp handler logger");
    private DatagramSocket datagramSocket;


    ChatUdpHandler(DatagramSocket datagramSocket) throws IOException {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run(){
        try{
            while(!datagramSocket.isClosed()){
                byte[] receiveBuffer = new byte[2048];
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                System.out.println("UDP message from " + msg);
            }
        } catch(IOException exception){
            logger.warning(exception.getMessage());
        }
    }
}
