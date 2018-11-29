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

    public static void receivePackets(DatagramSocket socket, HashMap<Byte, UDPfile> fileMap) throws IOException {
        int headerCount = 0;
        int endCount = 0;
        boolean filesDone = false;
        UDPfile file1 = new UDPfile();
        UDPfile file2 = new UDPfile();
        UDPfile file3 = new UDPfile();

        while(!(file1.isComplete()) && !(file2.isComplete()) && !(file3.isComplete())) {
            byte[] buf = new byte[1028];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            if (buf[0] % 2 == 0) {
                headerCount++;
                if(fileMap.get(buf[1]) == null) {
                    fileMap.put(buf[1], new UDPfile());
                }
                fileMap.get(buf[1]).add(packet, true, false, 0);
            }

            if(buf[0] % 2 != 0) {
                if (buf[0] == (3 % 4)) {
                    endCount++;
                }
            }

        }
    }

}
