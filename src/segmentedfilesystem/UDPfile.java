package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.ArrayList;

public class UDPfile {
    public boolean hasHead;
    public boolean hasEnd;
    public int fileLength;
    public ArrayList<DatagramPacket> packetData = new ArrayList<DatagramPacket>();

    public void add(DatagramPacket packet, boolean isHead, boolean isEnd, int numOfPackets) {
       // System.out.println("Im in add");

        if(isHead) {
            this.hasHead = true;
        }

        if(isEnd) {
            this.hasEnd = true;
            this.fileLength = numOfPackets + 2;
        }

        this.packetData.add(packet);
        System.out.println(this.hasHead + " " + this.hasEnd + " " + this.fileLength + " " + this.isComplete());
       // System.out.println(packetData.size()+ " " + this.fileLength);
    }

    public boolean isComplete() {
        if (this.hasHead && this.hasEnd) {
            return (packetData.size() == this.fileLength);
        } else {
            return false;
        }
    }


}
