/* 
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * A simple telnet-esque client that accepts a few special keywords from the server.
 * Connects to a pre-established muticast IP on local network to search for server address.
 * Server output to client is printed in its own thread.
 */

package Challenge268EASY_client;

import java.util.Timer;
import java.util.TimerTask;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class Main {
    private static final short SEARCH_TIMEOUT = 20000;
    private static final short MULTICAST_PORT = 4117;
    private static final String SERVER_MULTICAST_ADDR = "224.0.0.3";
    private static final short SERVER_PORT = 3003;
    
    private static TimerTask timeOut = new TimerTask() {
        public void run() {
            System.err.println("Server could not be found on network.");
            System.exit(1);
        }
    };
    
    private static void printUsage() {
        System.err.println("Usage:\tjava Challenge268EASY_client <server_ip>");
        System.exit(1);
    }
    
    private static String getServerIP() {
        String serverIpAddr = "";
        
        Timer timer = new Timer();
        byte[] buf = new byte[16];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        
        try {
            MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
            InetAddress address = InetAddress.getByName(SERVER_MULTICAST_ADDR);
            socket.joinGroup(address);

            System.out.println("Searching for server on network...");
            
            timer.schedule(timeOut, SEARCH_TIMEOUT);
            socket.receive(packet); // 20 seconds to try it. Server broadcasts every 10.
            timer.cancel();
        
            serverIpAddr = new String(packet.getData(), 0, packet.getLength());
        
            System.out.println("Received server address " + serverIpAddr + 
                "\nAttempting to connect.");
        } catch (UnknownHostException ex) {
            System.err.println("Invalid hostname exception caught. Are you connected to a network?");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Caught an IOException while searching for server. Terminating.");
            System.exit(1);
        }
        
        return serverIpAddr;
    }
    
    public static void main(String[] args) {
        String hostName = "";
        
        switch(args.length) {
            case 0:
                hostName = getServerIP();
                break;
            case 1:
                hostName = args[0];
                break;
            
            default:
                printUsage();
        }
        
        try (
            Socket server = new Socket(hostName, SERVER_PORT);
            PrintWriter out = new PrintWriter(
                                server.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                        server.getInputStream()));
            
            BufferedReader stdIn = new BufferedReader(
                                    new InputStreamReader(System.in));
        ) {
            (new LocalOutputThread(in, out)).start();
            
            String userInput;
            
            while(((userInput = stdIn.readLine()) != null) && !userInput.equals("quit"))
                    out.println(userInput);
            
            out.println("quit");
            System.exit(0);
        } catch (UnknownHostException ex) {
            System.err.println("Invalid hostname.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("There doesn't appear to be a server running at that address.");
            System.exit(1);
        }
    }
}

