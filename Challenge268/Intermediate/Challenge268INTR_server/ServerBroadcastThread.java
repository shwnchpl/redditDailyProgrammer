/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * ServerBroadcastThread broadcasts the server's local network address over the network
 * to a pre-established mutlicast address.
 *
 */

package Challenge268INTR_server;

import java.util.Enumeration;

import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.DatagramPacket;

import java.io.IOException;


public class ServerBroadcastThread extends Thread {
    private DelayedPriorityBlockingQueue<String> messages;
    private InetAddress group;
    private static final short LOCAL_PORT = 4116;
    private static final short MULTICAST_PORT = 4117;
    private static final short BROADCAST_FREQ = 10000; // ten seconds
    private static final String MULTICAST_ADDR = "224.0.0.3"; // also hardcoded into client
    private DatagramSocket socket = null;
    
    public ServerBroadcastThread(DelayedPriorityBlockingQueue<String> m) throws IOException {
        super("ServerBroadcastThread");
        messages = m;
        socket = new DatagramSocket(LOCAL_PORT);
        group = InetAddress.getByName(MULTICAST_ADDR);
    }

    private String getLANaddr() {
        try {
            for(Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                for(Enumeration inetAddrs = ((NetworkInterface)ifaces.nextElement()).getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress)inetAddrs.nextElement();
                    if(!inetAddr.isLoopbackAddress())
                        if(inetAddr.isSiteLocalAddress())
                            return inetAddr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            messages.delayAdd("Error: unable to get LAN address.");
        }
        return "NOADDR";
    }
    
    @Override
    public void run() {
        String lanAddr;
        
        if((lanAddr = getLANaddr()) == "NOADDR") { // not very likely to happen
            messages.delayAdd("Unable to broadcast address over LAN.");
            return;
        }
        
        byte[] buf = new byte[16];
        buf = lanAddr.getBytes();
        
        while(true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length,
                    group, MULTICAST_PORT);
                socket.send(packet);
            } catch (IOException e) {
                messages.delayAdd("Error: unable to broadcast.");
            }
            try {
                Thread.sleep(BROADCAST_FREQ);
            } catch (InterruptedException e) {
                messages.delayAdd("Caught an InturruptedException when attempting " +
                                "to sleep in ServerBroadcastThread.");
            }
        }
    }
}
