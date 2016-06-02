/*
 * Code by Shawn M. Chapla, 27 May 2016.
 *
 * A simple Synchronized wrapper around an ArrayList of ClientConnections.
 * Also keeps a hashmap of usernames->ClientConnections and includes some 
 * helper methods relating to username and IP address look-ups.
 *
 */

package Challenge268INTR_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import java.io.IOException;

public class SynchronizedConnectionList extends ArrayList<ClientConnection> {
    protected ArrayList<ClientConnection> connections;
    private HashMap<String, ClientConnection> userNameREF;
    
    public SynchronizedConnectionList() {
        connections = new ArrayList<>();
        userNameREF = new HashMap<>();
    }
    
    public ClientConnection getByUsername(String userName) {
        return userNameREF.get(userName);
    }
    
    public boolean userNameFree(String userName) {
        if(userName.replaceAll("\\s+", "").length() > 0)
            return !userNameREF.containsKey(userName);
        else
            return false;
    }
    
    public Set<String> usernameSet() {
        return userNameREF.keySet();
    }
    
    @Override
    public synchronized boolean add(ClientConnection connection) {
        userNameREF.put(connection.userName, connection);
        return super.add(connection);
    }

    @Override
    public synchronized boolean remove(Object connection) {
        userNameREF.remove(((ClientConnection)connection).userName);
        ((ClientConnection)connection).goDown();
        try {
            ((ClientConnection)connection).connectionSocket.close();
        } catch (IOException e) { } // either way, we're done with it
        return super.remove(connection);
    }
    
    @Override
    public synchronized ClientConnection get(int index) {
        return super.get(index);
    }
    
    @Override
    public synchronized Iterator<ClientConnection> iterator() {
        return super.iterator();
    }
    
    @Override
    public synchronized ClientConnection[] toArray() {  // this may be precarious
        return (ClientConnection[])super.toArray();
    }
}
