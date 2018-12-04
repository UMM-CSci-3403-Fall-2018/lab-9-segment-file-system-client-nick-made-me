package segmentedfilesystem;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
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

        System.out.println("done with packets, closing socket");

        socket.close();
    }

    public static void initConnection(InetAddress address, DatagramSocket socket) throws IOException {
        byte[] buf = new byte[0];
        DatagramPacket emptyPacket;

        emptyPacket = new DatagramPacket(buf, buf.length, address, 6014);
        socket.send(emptyPacket);
    }

    public static int calculateSize(int largeInt, int smallInt) {
        System.out.println(largeInt + " " + smallInt);
        if (largeInt < 0) {
            largeInt = largeInt + 256;
        }
        if (smallInt < 0) {
            smallInt = smallInt + 256;
        }
        return largeInt * 256 + smallInt;
    }

    public static boolean filesDone(ArrayList<Byte> fileIDs, HashMap<Byte, UDPfile> fileMap) {
        for (int i = 0; i < fileIDs.size(); i++) {
            if(!(fileMap.get(fileIDs.get(i)).isComplete())) {
                return false;
            }
        }

        return (fileIDs.size() == 3);
    }

    public static void receivePackets(DatagramSocket socket, HashMap<Byte, UDPfile> fileMap) throws IOException {
        int headerCount = 0;
        int endCount = 0;
        boolean filesDone = false;
        ArrayList<Byte> fileIDs = new ArrayList<Byte>();

        while(!filesDone(fileIDs, fileMap)) {
          //  System.out.println("Im in the loop");
            byte[] buf = new byte[1028];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
           // System.out.println("The size of the map is " + fileMap.size());

            if (buf[0] % 2 == 0) {
                //System.out.println("Im in the header if");
                headerCount++;
                if(fileMap.get(buf[1]) == null) {
                    fileIDs.add(buf[1]);
                    fileMap.put(buf[1], new UDPfile());
                }
                fileMap.get(buf[1]).add(packet, true, false, 0);
            }

            if(buf[0] % 2 != 0) {
              //  System.out.println("Im in the data if");
                if (buf[0] == (3 % 4)) {
                    System.out.println("Im in the ender if");
                    if(fileMap.get(buf[1]) == null) {
                        fileIDs.add(buf[1]);
                        fileMap.put(buf[1], new UDPfile());
                    }
                    fileMap.get(buf[1]).add(packet, false, true, calculateSize(buf[2], buf[3]));
                    endCount++;
                } else {
                    if (fileMap.get(buf[1]) == null) {
                        fileIDs.add(buf[1]);
                        fileMap.put(buf[1], new UDPfile());
                    }
                    fileMap.get(buf[1]).add(packet, false, false, 0);
                }
            }
        }


    }
}
