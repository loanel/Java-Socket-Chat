package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatTcpHandler extends Thread{
    private final Scanner tcpConnectionReader;

    ChatTcpHandler(Socket socket) throws IOException{
        this.tcpConnectionReader = new Scanner(socket.getInputStream());
    }

    @Override
    public void run(){
        while(tcpConnectionReader.hasNextLine()){
            System.out.println("TCP Message from " + tcpConnectionReader.nextLine());
        }
    }
}
