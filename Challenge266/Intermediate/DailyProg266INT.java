// Solution to the problem posted here: https://www.reddit.com/r/dailyprogrammer/comments/4iut1x/20160511_challenge_266_intermediate_graph_radius/

import java.io.*;
import java.util.*;

class Node {
    public String label;
    public int distance;
    public ArrayList<Node> edges;

    public Node(String l) {
        label = l;
        distance = -1;
        edges = new ArrayList<>();
    }


    private Node getUnvisitedChild() {
        for(Node child : edges)
            if(child.distance == -1)
                return child;
        return null;
    }

    private void clearDistance() {
        if(this.distance > -1) {
            this.distance = -1;

            for(Node n : edges)
                n.clearDistance();
        }
    }

    public void addEdge(Node n) {
        edges.add(n);
    }

    public int degree() {
        return edges.size();
    }

    public int eccentricity() {
        Queue<Node> parent_nodes = new LinkedList<Node>();

        int ecc = 0;
        parent_nodes.add(this);
        this.distance = 0;

        while(!parent_nodes.isEmpty()) {
            Node n = parent_nodes.poll();
            Node child;

            while((child = n.getUnvisitedChild()) != null) {
                child.distance = n.distance + 1;
                parent_nodes.add(child);
            }

            if(n.distance > ecc)
                ecc = n.distance;
        }

        clearDistance();

        return ecc;
    }
}

class DailyProg266INT {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Node> node_reference = new HashMap<>();

        int rad = Integer.parseInt(scanner.nextLine()); // can't be higher than this, after all
        int diam = 0;  // can't be less than zero!

        while(scanner.hasNextLine()) {
            String[] input = scanner.nextLine().split("[ ]+");
            if(!node_reference.containsKey(input[0]))
                node_reference.put(input[0], new Node(input[0]));
            if(!node_reference.containsKey(input[1]))
                node_reference.put(input[1], new Node(input[1]));
            node_reference.get(input[0]).addEdge(node_reference.get(input[1]));
        }

        scanner.close();

        Iterator<Node> node_itr = node_reference.values().iterator();

        while(node_itr.hasNext()) {
            Node current_node = node_itr.next();

            if(current_node.degree() < 1) continue;

            int nodeEcc = current_node.eccentricity();

            if(rad > nodeEcc) rad = nodeEcc;
            if(diam < nodeEcc) diam = nodeEcc;
        }

        System.out.println("Radius: " + rad + "\nDiameter: " + diam);
    }
}
