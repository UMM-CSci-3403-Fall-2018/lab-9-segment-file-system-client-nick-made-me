package segmentedfilesystem;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class Main {
    
    public static void main(String[] args) throws IOException {
        DatagramSocket socket;
        final InetAddress address;
        HashMap<Byte, UDPfile> fileMap = new HashMap<Byte, UDPfile>();

        socket = new DatagramSocket();
        address = InetAddress.getByName(args[0]);

        initConnection(address, socket);
        receivePackets(socket, fileMap);

    }

    public static void initConnection(InetAddress address, DatagramSocket socket) throws IOException {
        byte[] buf = new byte[0];
        DatagramPacket emptyPacket;

        emptyPacket = new DatagramPacket(buf, buf.length, address, 6014);
        socket.send(emptyPacket);
    }

    public static boolean receivePackets(DatagramSocket socket, HashMap<Byte, UDPfile> fileMap) throws IOException {
        int headerCount = 0;
        int endCount = 0;
        boolean filesDone = false;

        while(headerCount != 3 && endCount != 3 && !filesDone) {
            byte[] buf = new byte[1028];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            if (buf[0] % 2 == 0) {
                headerCount++;
            }

            if(buf[0] % 2 != 0) {
                if (buf[1] % 2 != 0) {
                    endCount++;
                }
            }





        }



    }

}
