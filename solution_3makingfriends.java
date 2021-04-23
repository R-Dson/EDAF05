import java.io.BufferedInputStream;
import java.util.HashMap;
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
                nu =  new Node(u);
                graph.nodes.put(u, nu);
            }
            if (nv == null) {
                nv =  new Node(v);
                graph.nodes.put(v, nv);
            }

            //lägger till varandra i varandras listor så de pekar på varandra
            nu.nbs.put(v, w);
            nv.nbs.put(u, w);
        }
        String temp = "";
    }

}

class Graph {
    //public ArrayList<Edge> edges = new ArrayList<>();
    public HashMap<Integer, Node> nodes = new HashMap<>();

    public Node getNodeByNumber(int i){
        return nodes.get(i);
    }
}

class Node{
    int n;
    //värdet och vikten
    HashMap<Integer, Integer> nbs = new HashMap<>();

    public Node(int n){
        this.n = n;
    }
}

/*class Edge {
    // ena personen
    public int u;
    // andra personen
    public int v;
    // vikten w
    public int w;

    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }
}*/