package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.ArrayList;

public class UDPfile {
    public boolean hasHead;
    public boolean hasEnd;
    public int fileLength;
    public ArrayList<DatagramPacket> packetData = new ArrayList<DatagramPacket>();

    public void add(DatagramPacket packet, boolean isHead, boolean isEnd, int numOfPackets) {
        if(isHead) {
            this.hasHead = true;
        }

        if(isEnd) {
            this.hasEnd = true;
            this.fileLength = numOfPackets;
        }

        this.packetData.add(packet);
    }

    public boolean isComplete() {
        if (this.hasHead && this.hasEnd) {
            return (packetData.size() == this.fileLength);
        } else {
            return false;
        }
    }


}
