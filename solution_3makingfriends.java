import java.io.BufferedInputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class solution_3makingfriends {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));

        Graph graph = new Graph();

        // antalet personer på eventet
        int N = scanner.nextInt();
        // antalet par som är förväntat att det tar för att bli vänner
        int M = scanner.nextInt();

        graph.numEdges = M;

        for (int i = 0; i < M; i++) {
            //
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            // vikten
            int w = scanner.nextInt();

            Node nu = graph.nodes.get(u);
            Node nv = graph.nodes.get(v);

            if (nu == null) {
                nu = new Node(u);
                graph.nodes.put(u, nu);
            }
            if (nv == null) {
                nv = new Node(v);
                graph.nodes.put(v, nv);
            }

            // lägger till varandra i varandras listor så de pekar på varandra
            Edge e = new Edge(u, v, w, i);
            nv.edges.add(e);
            nu.edges.add(e);
        }

        int total = prim(graph, graph.nodes.get(1));
        System.out.println(total);
    }

    // graf och rot noden
    static Integer prim(Graph G, Node r) {
        // alla tillgängliga kanter, sorterade enligt högst prioritet (lägst vikt)
        PriorityQueue<Edge> availableEdges = new PriorityQueue<>();
        // lägger in alla kanter från rot noden
        availableEdges.addAll(r.edges);
        
        // alla noder har ett tal mellan 1 och n, om de är true har vi besäkt tidigare
        // (positionen blir n-1 så klart)
        boolean[] nodeAdded = new boolean[G.nodes.size()];

        // edgesAdded håller ett värde så att vi inte lägger en edge två gånger
        boolean[] edgesAdded = new boolean[G.numEdges];

        for (Edge edge : availableEdges) {
            edgesAdded[edge.val] = true;
        }

        // total vikt
        int tw = 0;

        // sätter att vi besökt roten
        nodeAdded[r.value - 1] = true;

        while (!availableEdges.isEmpty()) {
            // hämtar kanten med högst prioritet
            Edge e = availableEdges.poll();
            if (!e.visited) {
                e.visited = true;
                int value = -1;
                // Om värdet i nodeAdded för noden v är false så "färdas vi från u till v"
                // annars "färdas vi från v till u". värdet på nodeAdded var om vi hade besökt
                // noden.
                // Vi vill hitta noden vi "färdas till". value -1 är att det är en nod vi varit
                // på till en nod som vi varit på
                if (!nodeAdded[e.v - 1]) {
                    value = e.v;
                } else if (!nodeAdded[e.u - 1]) {
                    value = e.u;
                }

                // går bara vidare om vi inte varit på nåbon av noderna innan
                if (value != -1) {
                    // adderar vikten
                    tw += e.w;
                    // hämtar noden som tillhör värdet på noden vi inte besökt
                    Node n = G.nodes.get(value);
                    // markerar att vi besökt noden
                    nodeAdded[value - 1] = true;
                    // lägger till alla nya kanter från den nya noden

                    Iterator<Edge> it = n.edges.iterator();
                    while (it.hasNext()) {
                        Edge edge = it.next();
                        if (!edge.visited && !edgesAdded[edge.val]) {
                            availableEdges.add(edge);
                            edgesAdded[edge.val] = true;
                        }
                    }
                }
            }
        }
        return tw;
    }

}

class Graph {
    int numEdges = 0;
    // alla värdne och respektive nod
    public HashMap<Integer, Node> nodes = new HashMap<>();
}

class Node {
    int value;
    // kanter
    LinkedList<Edge> edges = new LinkedList<>();

    public Node(int n) {
        value = n;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node))
            return false;
        if(value == ((Node)obj).value)
            return true;
        return false;
    }

}

class Edge implements Comparable<Edge>, Comparator<Edge> {

    int val;
    int v;
    int u;
    int w;

    boolean visited = false;

    public Edge(int v, int u, int w, int val) {
        this.v = v;
        this.u = u;
        this.w = w;
        this.val = val;
    }

    @Override
    public int compare(Edge arg0, Edge arg1) {
        return Integer.compare(arg0.w, arg1.w);
    }

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(this.w, o.w);
    }

    
}
