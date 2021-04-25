import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
            NB nuAdd = new NB(nv, w);
            nu.nbs.add(nuAdd);

            NB nvAdd = new NB(nu, w);
            nv.nbs.add(nvAdd);

            Edge e = new Edge(u,v,w);
            graph.edges.add(e);
            nv.edges.add(e);
            nu.edges.add(e);
            
        }
        
        int total = prim(graph, graph.nodes.get(1));
        System.out.println(total);
    }

    //graf och rot noden
    static Integer prim(Graph G, Node r) {
        //alla tillgängliga kanter, sorterade enligt högst prioritet (lägst vikt)
        PriorityQueue<Edge> edges = new PriorityQueue<>();
        //lägger in alla kanter från rot noden
        edges.addAll(r.edges);

        // alla noder har ett tal 1-n, om de är true har vi besäkt tidigare (positionen blir n-1 så klart)
        boolean[] nodeAdded = new boolean[G.nodes.size()];

        //total vikt
        int tw = 0;

        // sätter value till rot värdet 
        int value = r.value;
        // sätter att vi besökt roten
        nodeAdded[value-1] = true;

        while (!edges.isEmpty()) {
            //hämtar kanten med högst prioritet
            Edge e = edges.poll();
            if (!e.visited) {
                tw += e.w;
                e.visited = true;
                
                // om värdet i nodeAdded för noden v är false så "färdas vi från u till v" annars "färdas vi från v till u". värdet på nodeAdded var om vi hade besökt noden
                // vi vill hitta noden vi "färdas till". value -1 är att det är en nod vi varit på till en nod som vi varit på
                if (!nodeAdded[e.v-1]) {
                    value = e.v;
                }
                else if (!nodeAdded[e.u-1]){
                    value = e.u;
                }
                else {
                    value = -1;
                }

                if (value != -1) {
                    Node n = G.nodes.get(value);
                    nodeAdded[value-1] = true;
                    for (Edge edge : n.edges) {
                        if (!edge.visited) {
                            edges.add(edge);
                        }
                    }
                }
                else{ // tar bort vikten om det är från en nod vi varit på och till en nod vi varit på, dvs både v och u är true i nodeAdded
                    tw -= e.w;
                }
            }
        }
        return tw;
    }

}

class Graph {
    public HashMap<Integer, Node> nodes = new HashMap<>();
    public ArrayList<Edge> edges = new ArrayList<>();

    public Node getNodeByNumber(int i) {
        return nodes.get(i);
    }

    public Edge get(int i){
        return null;
    }
}

class Node {
    int value;
    //grannoder
    PriorityQueue<NB> nbs = new PriorityQueue<>();

    HashSet<Edge> edges = new HashSet<>();

    public Node(int n) {
        value = n;
    }
}

class NB implements Comparator<NB>, Comparable<NB> {
    public int weight;
    public Node node;

    public NB(Node n, int w) {
        node = n;
        weight = w;
    }

    @Override
    public int compare(NB arg0, NB arg1) {
        return Integer.compare(arg0.weight, arg1.weight);
    }

    @Override
    public int compareTo(NB o) {
        return Integer.compare(this.weight, o.weight);
    }

}

class Edge implements Comparable<Edge>, Comparator<Edge>{

    int v;
    int u;
    int w;

    boolean visited = false;

    public Edge(int v, int u, int w){
        this.v = v;
        this.u = u;
        this.w = w;
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