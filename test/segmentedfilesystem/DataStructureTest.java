package segmentedfilesystem;

import static org.junit.Assert.*;
import static segmentedfilesystem.Main.initConnection;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is just a stub test file. You should rename it to
 * something meaningful in your context and populate it with
 * useful tests.
 */
public class DataStructureTest {
    DatagramSocket socket;
    HashMap<Byte, UDPfile> fileMap;
    ArrayList<Byte> fileIDs;

    @Before
    public void start() throws IOException{
        final InetAddress address;
        this.fileMap = new HashMap<Byte, UDPfile>();
        this.fileIDs = new ArrayList<Byte>();

        this.socket = new DatagramSocket();
        address = InetAddress.getByName("heartofgold.morris.umn.edu");
        initConnection(address, socket);
    }



    @Test
    public void calculateSize() {
        assertEquals(Main.calculateSize(0,0), 0);
        assertEquals(Main.calculateSize(0,-125), 131);
        assertEquals(Main.calculateSize(54,-96), 13984);
        assertEquals(Main.calculateSize(-25,-102), 59290);
    }

    @Test
    public void getFileTest() {
        byte id = 0;
        fileIDs.add(id);
        fileMap.put(id, new UDPfile());

        assertEquals(Main.getFileFromMap(fileIDs, fileMap, 0), fileMap.get(fileIDs.get(0)));
    }

    @Test
    public void mapTest() throws IOException{
        Main.receivePackets(socket, fileMap, fileIDs);
        assertEquals(fileMap.size(), 3);
        assertEquals(fileIDs.size(), 3);
    }

    @Test
    public void filesDoneTest() throws IOException {
        assertEquals(Main.filesDone(fileIDs, fileMap), false);
        Main.receivePackets(socket, fileMap, fileIDs);
        assertEquals(Main.filesDone(fileIDs, fileMap), true);
    }
}
