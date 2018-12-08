package segmentedfilesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    
    public static void main(String[] args) throws IOException {

        DatagramSocket socket;
        final InetAddress address;
        HashMap<Byte, UDPfile> fileMap = new HashMap<Byte, UDPfile>();
        ArrayList<Byte> fileIDs = new ArrayList<Byte>();

        socket = new DatagramSocket();
        address = InetAddress.getByName(args[0]);

        initConnection(address, socket);
        receivePackets(socket, fileMap, fileIDs);
        sortPackets(fileMap, fileIDs);
        writeFiles(fileIDs, fileMap);

        socket.close();
    }

    // Start connection with server
    public static void initConnection(InetAddress address, DatagramSocket socket) throws IOException {
        byte[] buf = new byte[0];
        DatagramPacket emptyPacket;

        emptyPacket = new DatagramPacket(buf, buf.length, address, 6014);
        socket.send(emptyPacket);
    }

    public static void writeFiles(ArrayList<Byte> fileIDs, HashMap<Byte, UDPfile> fileMap) throws IOException {
        // Write out data to corresponding files
        for(int i = 0; i < fileIDs.size(); i++) {
            DatagramPacket headerPacket = getFileFromMap(fileIDs, fileMap, i).headerPacket;
            FileOutputStream output = new FileOutputStream(fileMap.get(fileIDs.get(i)).fileName(headerPacket));
            for(int j = 0; j < fileMap.get(fileIDs.get(i)).packetData.size(); j++) {
                output.write(getFileFromMap(fileIDs, fileMap, i).getPacketData(getFileFromMap(fileIDs, fileMap, i).packetData.get(j)));
            }
            System.out.println(fileMap.get(fileIDs.get(i)).fileName(headerPacket) + " received");
            output.close();
        }
    }

    // Sort the packets in each of the files
    public static void sortPackets(HashMap<Byte, UDPfile> fileMap, ArrayList<Byte> fileIDs) {
        for(int i = 0; i < fileIDs.size(); i++) {
            fileMap.get(fileIDs.get(i)).sort();
        }
    }

    public static UDPfile getFileFromMap(ArrayList<Byte> fileIDs, HashMap<Byte, UDPfile> fileMap, int mapIndex) {
        return fileMap.get(fileIDs.get(mapIndex));
    }

    // Calculate the size of the packet
    public static int calculateSize(int largeInt, int smallInt) {
        if (largeInt < 0) {
            largeInt = largeInt + 256;
        }
        if (smallInt < 0) {
            smallInt = smallInt + 256;
        }
        return largeInt * 256 + smallInt;
    }

    // Check to see if all of the packets have been received in each file
    public static boolean filesDone(ArrayList<Byte> fileIDs, HashMap<Byte, UDPfile> fileMap) {
        for (int i = 0; i < fileIDs.size(); i++) {
            if(!(fileMap.get(fileIDs.get(i)).isComplete())) {
                return false;
            }
        }

        return (fileIDs.size() == 3);
    }

    // Read packets from server and filter them to their corresponding files
    public static void receivePackets(DatagramSocket socket, HashMap<Byte, UDPfile> fileMap, ArrayList<Byte> fileIDs ) throws IOException {

        while(!filesDone(fileIDs, fileMap)) {
            byte[] buf = new byte[1028];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            if (buf[0] % 2 == 0) {
                if(fileMap.get(buf[1]) == null) {
                    fileIDs.add(buf[1]);
                    fileMap.put(buf[1], new UDPfile());
                }

                fileMap.get(buf[1]).add(packet, true, false, 0);
            }

            if(buf[0] % 2 != 0) {
                if (buf[0] == (3 % 4)) {
                    if(fileMap.get(buf[1]) == null) {
                        fileIDs.add(buf[1]);
                        fileMap.put(buf[1], new UDPfile());
                    }

                    fileMap.get(buf[1]).add(packet, false, true, calculateSize(buf[2], buf[3]));
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
