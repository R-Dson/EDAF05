import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

class solution_6railwayplanning {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));
        int N, M, C, P;
        N = scanner.nextInt(); // antalet noder
        M = scanner.nextInt(); // antalet kanter
        C = scanner.nextInt(); // antalet personer
        P = scanner.nextInt(); // antalet vägar

        //HashMap<Integer, Node> nodes = new HashMap<>(N);
        Graph graph = new Graph(M, N);

        for (int i = 0; i < M; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int c = scanner.nextInt();
            
            Node nodeu = graph.nodes.get(u);
            Node nodev = graph.nodes.get(v);
            
            if (nodeu == null) {
                nodeu = new Node(u);
                graph.nodes.put(u, nodeu);
            }
            if (nodev == null) {
                nodev = new Node(v);
                graph.nodes.put(v, nodev);
            }

            Edge e = new Edge(nodeu, nodev, c, i);

            nodeu.edges.put(v, e);
            nodev.edges.put(u, e);

            graph.AddToList(e);
        }
        Integer[] remove = new Integer[P];
        for (int i = 0; i < P; i++) {
            remove[i] = scanner.nextInt();
        }
        ford_fulkerson(graph, graph.nodes.get(0), graph.nodes.get(graph.nodes.size()-1), C);
    }

    public static void ford_fulkerson(Graph G, Node s, Node t, int c){
        G.SetToZero();
        G.CreateRGraph();
        while (BFS(G.rgraph, s, t)) {
            // kan använda t.pred till att hitta vägen tillbaka
            System.out.println("x");
        }
        //String s = "";
    }

    private static boolean BFS(RGraph G, Node s, Node t) {
        // talet vi vill nå
        int i = t.value;

        // Om vi är på den rätta noden behöver vi inte gå någonstans
        if (s.value == t.value) {
            return true;
        }

        // Resetar alla värden
        Iterator<Node> itn = G.graph.nodes.values().iterator();
        while (itn.hasNext()) {
            Node n = itn.next();
            n.pred = null;
        }
        
        LinkedList<Node> q = new LinkedList<Node>();
        // Lägger till startnoden
        q.add(s);

        while (q.size() > 0) {
            Node nodeMain = q.poll();
            Iterator<Integer> it = nodeMain.edges.keySet().iterator();

            while (it.hasNext()) {
                Integer grannI = it.next();
                Node grannNode = G.graph.nodes.get(grannI);
                Edge edge = nodeMain.GetEdge(grannI);
                // Om pred == null så har vi inte varit vid den noden innan
                if (grannNode.pred == null) {
                    // Kollar om g ord är samma som slutordet
                    if (i == grannNode.value) {
                        grannNode.pred = nodeMain;
                        return true;
                    }
                    // Om det är fel nod går vi vidare
                    // lägger till i kön
                    q.addLast(grannNode);
                    // sätter företrädarnoden till nodeMain i den grannoden
                    grannNode.pred = nodeMain;
                }
            }
        }
        return false;
    }

}

class Graph {
    private int M;
    private ArrayList<Edge> edges;
    public HashMap<Integer, Node> nodes;

    public RGraph rgraph;
    public Graph (int M, int N)
    {
        edges = new ArrayList<>(M);
        nodes = new HashMap<>(N);
        this.M = M;
    }

    public void CreateRGraph(){
        if (rgraph == null){
            rgraph = new RGraph(M, this);
            rgraph.AddAll(edges);
        }
    }

    public void AddToList(Edge e){
        edges.add(e);
    }

    public void AddToBoth(Edge e){
        AddToList(e);
        rgraph.AddToList(e);
    }

    public void SetToZero(){
        edges.forEach(e -> e.flow = 0);
        
    }

    private ArrayList<Edge> getEdges(){
        return edges;
    }
    
}

class RGraph {

    private ArrayList<Edge> RList;

    public Graph graph;
    
    public RGraph(int M, Graph G) {
        RList = new ArrayList<>(M);
        graph = G;
    }

    public void AddToList(Edge e){
        RList.add(e);
    }

    public void AddAll(Collection<Edge> c){
        RList.addAll(c);
    }

}

class Edge {
    int source;
    int dest;
    int cap;
    int index;
    int flow;

    Node s;
    Node d;

    public boolean IsBackwards = false;

    public Edge (Node s, Node d, int c, int index) {
        this.s = s;
        source = s.value;
        this.d = d;
        dest = d.value;
        cap = c;
        this.index = index;
        flow = 0;
    }
}

class Node {
    int value;
    Node pred;

    HashMap<Integer, Edge> edges = new HashMap<>();
    public Node(int value){
        this.value = value;
    }

    public void AddEdge(int i, Edge e) {
        edges.put(i, e);
    }

    public Edge GetEdge(int i) {
        return edges.get(i);
    }

    public Node GetNodeFromEdge(int i) {
        if (i == value)
            return this;
        
        Edge e = edges.get(i);
        if(e.source == i)
            return e.s;
        else
            return e.d;
    }
}