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

    public void add(DatagramPacket packet, boolean isHead, boolean isEnd, int numOfPackets) {
       // System.out.println("Im in add");

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
        //System.out.println(this.hasHead + " " + this.hasEnd + " " + this.fileLength + " " + this.isComplete());
       // System.out.println(packetData.size()+ " " + this.fileLength);
    }

    public boolean isComplete() {
        if (this.hasHead && this.hasEnd) {
            return (packetData.size() == this.fileLength);
        } else {
            return false;
        }
    }

    public int calculateSize(int largeInt, int smallInt) {
        //System.out.println(largeInt + " " + smallInt);
        if (largeInt < 0) {
            largeInt = largeInt + 256;
        }
        if (smallInt < 0) {
            smallInt = smallInt + 256;
        }
        return largeInt * 256 + smallInt;
    }

    public void sort() {
        Collections.sort(packetData, new SortByFileID());
    }

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
