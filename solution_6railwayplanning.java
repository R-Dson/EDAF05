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

        // HashMap<Integer, Node> nodes = new HashMap<>(N);
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

            Edge e1 = new Edge(nodeu, nodev, c, i);
            Edge e2 = new Edge(nodev, nodeu, c, i);

            nodeu.edges.put(v, e1);
            nodev.edges.put(u, e2);

            graph.AddToList(e1);
            graph.AddToList(e2);
        }

        Integer[] remove = new Integer[P];
        for (int i = 0; i < P; i++) {
            remove[i] = scanner.nextInt();
        }
        int lowest = Integer.MAX_VALUE;
        int count = 0;
        //graph.CreateRGraph();

        //tar bort kanter
        //for (int i = 1; i < 16; i++) {
            //graph.removeEdgei(remove[remove.length - i]);
        //}

        //int tempis1 = ford_fulkerson(graph, graph.nodes.get(0), graph.nodes.get(graph.nodes.size() - 1), C);
        //graph.CreateRGraph();
        //graph.removeEdgei(remove[remove.length - 1]);
        //int tempis = ford_fulkerson(graph, graph.nodes.get(0), graph.nodes.get(graph.nodes.size() - 1), C);

        for (int i = 0; i < remove.length; i++) {
            graph.CreateRGraph();
            graph.removeEdgei(remove[remove.length - i - 1]);
            int tempi = ford_fulkerson(graph, graph.nodes.get(0), graph.nodes.get(graph.nodes.size() - 1), C);

            if (tempi > -1) {
                lowest = tempi;
                count = i;
            } else {
                break;
            }

        }

        System.out.println(count + " " + lowest);
    }

    public static int ford_fulkerson(Graph G, Node s, Node t, int c) {
        G.SetToZero();
        // G.CreateRGraph();
        int maxflow = 0;
        while (BFS(G.rgraph, s, t)) {
            int minc = Integer.MAX_VALUE;
            Node temp = t;
            while (temp.pred != null) {
                int tempc = temp.pred.GetEdge(temp.value).cap;
                if (tempc < minc)
                    minc = tempc;
                temp = temp.pred;
            }

            temp = t;
            while (temp.pred != null) {
                Edge edge = temp.pred.GetEdge(temp.value);

                Node d = edge.d, srs = edge.s;
                d.GetEdge(srs.value).cap += minc;
                srs.GetEdge(d.value).cap -= minc;

                temp = temp.pred;
            }
            maxflow += minc;
            // kan använda t.pred till att hitta vägen tillbaka

        }
        return maxflow;
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
            nodeMain.visited = true;
            Iterator<Integer> it = nodeMain.edges.keySet().iterator();

            while (it.hasNext()) {
                Integer grannI = it.next();
                Node grannNode = G.graph.nodes.get(grannI);
                Edge edge = nodeMain.GetEdge(grannI);
                // Om pred == null så har vi inte varit vid den noden innan
                if (grannNode.pred == null && edge.cap > 0 && !grannNode.visited) {
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

    public Graph(int M, int N) {
        edges = new ArrayList<>(M);
        nodes = new HashMap<>(N);
        this.M = M;
    }

    public void CreateRGraph() {
        rgraph = new RGraph(M, this);
        rgraph.AddAll(edges);

        /*for (Edge edge : listEdgesDest) {
            edge.d.AddEdge(edge.source, edge);
        }
        for (Edge edge : listEdgesSource) {
            edge.s.AddEdge(edge.dest, edge);
        }*/
        //listEdgesDest.clear();
        //listEdgesSource.clear();
    }

    public void AddToList(Edge e) {
        edges.add(e);
    }

    public void AddToBoth(Edge e) {
        AddToList(e);
        rgraph.AddToList(e);
    }

    public void SetToZero() {
        edges.forEach(e -> e.flow = 0);

    }

    ArrayList<Edge> listEdgesDest = new ArrayList<>();
    ArrayList<Edge> listEdgesSource = new ArrayList<>();

    public void removeEdgei(int i) {
        Iterator<Edge> it = edges.iterator();
        while (it.hasNext()) {
            Edge e = it.next();
            if (e.index == i) {
                Node d = e.d;
                Node s = e.s;

                listEdgesDest.add(d.edges.remove(s.value));
                listEdgesSource.add(s.edges.remove(d.value));
            }
        }
    }

}

class RGraph {

    private ArrayList<Edge> RList;

    public Graph graph;

    public RGraph(int M, Graph G) {
        RList = new ArrayList<>(M);
        graph = G;
    }

    public void AddToList(Edge e) {
        RList.add(e);
    }

    public void AddAll(Collection<Edge> c) {
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

    public Edge(Node s, Node d, int c, int index) {
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

    boolean visited = false;

    HashMap<Integer, Edge> edges = new HashMap<>();

    public Node(int value) {
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
        if (e.source == i)
            return e.s;
        else
            return e.d;
    }
}