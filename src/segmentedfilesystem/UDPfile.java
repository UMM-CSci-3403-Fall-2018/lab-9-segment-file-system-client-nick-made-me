package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UDPfile {
    public boolean hasHead;
    public boolean hasEnd;
    public int fileLength;
    public DatagramPacket headerPacket;
    public ArrayList<DatagramPacket> packetData = new ArrayList<DatagramPacket>();

    // Add packets to the array
    public void add(DatagramPacket packet, boolean isHead, boolean isEnd, int numOfPackets) {

        if(isHead) {
            this.hasHead = true;
            this.headerPacket = packet;
            return;
        }

        if(isEnd) {
            this.hasEnd = true;
            this.fileLength = numOfPackets + 1;
        }

        this.packetData.add(packet);
    }

    // Boolean to check if all packets have been received
    public boolean isComplete() {
        if (this.hasHead && this.hasEnd) {
            return (packetData.size() == this.fileLength);
        } else {
            return false;
        }
    }

    // Calculates the filename of the file based off of the header packet
    public String fileName(DatagramPacket packet) {
        byte[] data = packet.getData();
        byte[] filename = new byte[packet.getLength() - 2];
        for(int i = 2; i < packet.getLength(); i++){
            filename[i - 2] = data[i];
        }
        return new String(filename);
    }

    public int calculateSize(int largeInt, int smallInt) {
        if (largeInt < 0) {
            largeInt = largeInt + 256;
        }
        if (smallInt < 0) {
            smallInt = smallInt + 256;
        }
        return largeInt * 256 + smallInt;
    }

    // Sorts the data based off of the comparator below
    public void sort() {
        Collections.sort(packetData, new SortByFileID());
       // packetData.add(0, headerPacket);
    }

    public byte[] getPacketData(DatagramPacket packet) {
        byte[] packetData = packet.getData();
        byte[] data = new byte[packet.getLength() - 4];

        for(int i = 4; i < packet.getLength(); i++) {
            data[i - 4] = packetData[i];
        }

        return data;
    }

    // Comparator for sorting
    public class SortByFileID implements Comparator<DatagramPacket> {
        public int compare(DatagramPacket a, DatagramPacket b) {
            byte[] byteArrA = a.getData();
            byte[] byteArrB = b.getData();

            int packetSizeA = calculateSize(byteArrA[2], byteArrA[3]);
            int packetSizeB = calculateSize(byteArrB[2], byteArrB[3]);

            return packetSizeA - packetSizeB;
        }
    }


}
