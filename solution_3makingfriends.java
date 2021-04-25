import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
            nu.nbsHash.put(nv.value, nuAdd);

            NB nvAdd = new NB(nu, w);
            nv.nbs.add(nvAdd);
            nv.nbsHash.put(nu.value, nvAdd);

            Edge e = new Edge(u,v,w);
            graph.edges.add(e);

            nv.edges.add(e);
            nv.pEdges.add(e);

            nu.edges.add(e);
            nu.pEdges.add(e);
            
        }
        /*HashMap<Node, Integer> T = prim(graph, graph.nodes.get(1));
        int total = 0;
        for (Integer i : T.values()) {
            total += i;
        }*/
        int total = prim(graph, graph.nodes.get(1));
        System.out.println(total);
    }

    

    //graf och rot noden
    static Integer prim(Graph G, Node r) {
        //LinkedList<NB> T = new LinkedList<>();
        //HashMap<Node, Integer> T = new HashMap<>();
        //int weights = 0;

        //HashMap<Integer, Node> Q = new HashMap<>(G.nodes);
        //Node ne = Q.remove(r.value);
        //T.put(ne, 0);
        

        PriorityQueue<Edge> edges = new PriorityQueue<>();
        edges.addAll(r.edges);

        ArrayList<Edge> visited = new ArrayList<>();

        boolean[] nodeAdded = new boolean[G.nodes.size()];

        HashMap<Integer, Node> nMap = new HashMap<>();
        //nMap.put(r.value, r);

        int tw = 0;
        //int last = r.value;
        int next = r.value;
        //Node n = r;
        nodeAdded[next-1] = true;

        while (!edges.isEmpty()) {
            Edge e = edges.poll();
            if (!e.visited) {
                tw += e.w;
                e.visited = true;
                
                if (!nodeAdded[e.v-1]) {
                    next = e.v;
                }
                else if (!nodeAdded[e.u-1]){
                    next = e.u;
                }
                else {
                    next = -1;
                }

                if (next != -1) {
                    Node n = G.nodes.get(next);
                    nodeAdded[next-1] = true;
                    for (Edge edge : n.edges) {
                        if (!edge.visited) {
                            edges.add(edge);
                        }
                    }
                }
            }
        }
        return tw;
/*
        while (Q.size() > 0) {
            Node node = null;
            NB closestNB = null;
            int NBweight = Integer.MAX_VALUE;

            Iterator<Node> nIterator = T.keySet().iterator();

            while (nIterator.hasNext()) {
                Node next = nIterator.next();
                LinkedList<NB> list = new LinkedList<>();

                while (next.nbs.size() > 0) {
                    NB closest = next.nbs.poll();
                    list.add(closest);

                    if (Q.containsKey(closest.node.value) && closest.weight < NBweight){
                        closestNB = closest;
                        NBweight = closest.weight;
                        node = next;
                        break;
                    }
                }

                next.nbs.addAll(list);

            }


            Node closest = null;
            int weight = Integer.MAX_VALUE;
            for (Node Qnode : Q.values()) {

                for (Node Tnode : T.keySet()) {

                    NB nb = Tnode.nbsHash.get(Qnode.value);

                    if(nb != null){
                        int w = nb.weight;

                        if (w < weight) {
                            weight = w;
                            closest = Qnode;
                        }
                    }
                }
            }
            
            //T.put(Q.remove(closest.value), weight);
            node.nbs.remove(closestNB);
            Node newnode = Q.remove(closestNB.node.value);
            T.put(newnode, NBweight);
            closestNB = null;
            NBweight = Integer.MAX_VALUE;
        }
        return T;*/
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
    HashMap<Integer, NB> nbsHash = new HashMap<>();
    PriorityQueue<NB> nbs = new PriorityQueue<>();

    HashSet<Edge> edges = new HashSet<>();
    PriorityQueue<Edge> pEdges = new PriorityQueue<>();

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