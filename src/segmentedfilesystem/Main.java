package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main {
    
    public static void main(String[] args) {
        byte[] buf = new byte[0];
        DatagramSocket socket;
        DatagramPacket emptyPacket;
        final InetAddress address;

        try {
            address = InetAddress.getByName(args[0]);
            socket = new DatagramSocket();
            emptyPacket = new DatagramPacket(buf, buf.length, address, 6014);
            socket.send(emptyPacket);
        } catch (SocketException e) {
            System.out.println("Die");
        } catch (IOException e) {
            System.out.println("Die");
        }

    }

}
