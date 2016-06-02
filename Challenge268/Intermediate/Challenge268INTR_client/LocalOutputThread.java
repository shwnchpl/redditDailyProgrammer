/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * Output thread listens for output from the server and prints it to stdout.
 * Recieves and interprets special signals from the server.
 *
 */

package Challenge268INTR_client;

import java.io.BufferedReader;
import java.io.PrintWriter;

import java.io.IOException;

public class LocalOutputThread extends Thread {
    BufferedReader fromServer;
    PrintWriter toServer;
    
    public LocalOutputThread(BufferedReader servOut, PrintWriter toServ) {
        super("LocalOutputThread");
        fromServer = servOut;
        toServer = toServ;
    }
    
    @Override
    public void run() {
        try {
            String received;
            
            while((received = fromServer.readLine()) != null) {
                switch(received) {
                    case "GRT":
                        System.out.print("Please enter a username: ");
                        break;
                    case "UNNF":
                        System.out.print("Username taken/invalid. Please enter a username: ");
                        break;
                    case "WLK":
                        System.out.println("Connected!");
                        break;
                    case "CONCHECK":
                        toServer.println("CON");
                        break;
                    case "SCT":
                        System.out.println("Server has terminated the connection.");
                        System.exit(1);
                        break;
                    default:
                        System.out.println("> " + received);
                }
            }
        } catch (IOException e) {
            System.err.println("Caught an IO Exception while attempting "
                                + "to read from server. Terminating.");
            System.exit(1);
        }
    }
}
