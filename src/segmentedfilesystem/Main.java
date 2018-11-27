package segmentedfilesystem;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class Main {
    
    public static void main(String[] args) {
        DatagramSocket socket;
        final InetAddress address;

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(args[0]);
            initConnection(address, socket);
        } catch (UnknownHostException e) {
            System.out.println("Die");
        } catch (SocketException e) {
            System.out.println("Die");
        }

    }

    public static void initConnection(InetAddress address, DatagramSocket socket) {
        byte[] buf = new byte[0];
        DatagramPacket emptyPacket;

        try {
            emptyPacket = new DatagramPacket(buf, buf.length, address, 6014);
            socket.send(emptyPacket);
        } catch (IOException e) {
            System.out.println("Die");
        }
    }

    public static boolean receivePackets(DatagramSocket socket) {
        return false;
    }

}
