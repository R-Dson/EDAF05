import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class solution_6railwayplanning {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N, M, C, P;

        N = scanner.nextInt(); // antalet noder
        M = scanner.nextInt(); // antalet kanter
        C = scanner.nextInt(); // antalet personer
        P = scanner.nextInt(); // antalet vägar

        Node[] g = new Node[N];

        // deklarerar variablerna node
        for (int i = 0; i < N; i++) {
            g[i] = new Node();
            g[i].ind = i;
        }

        int[][] paths = new int[M][3];

        for (int i = 0; i < M; i++) {

            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int c = scanner.nextInt();

            // bygger upp vägarna mellan noderna
            paths[i][0] = u; // nod 1
            paths[i][1] = v; // nod 2
            paths[i][2] = c; // kapacitansen
            Edge e = new Edge(c, 0);

            g[u].neighbours.put(g[v], e);
            g[v].neighbours.put(g[u], e);

        }

        // tar bort alla vägarna som ska tas bort
        int[] removed = new int[P];
        for (int i = 0; i < P; i++) {
            removed[i] = scanner.nextInt();

            // hittar vägen
            int[] TempPath = paths[removed[i]];
            // hittar noderna till vägen
            int nodeNum1 = TempPath[0];
            int nodeNum2 = TempPath[1];

            // tar bort vägen i var nods neighbours lista
            // (neighbours är hashmap med värdena <Node, Edge>)
            g[nodeNum1].neighbours.remove(g[nodeNum2]);
            g[nodeNum2].neighbours.remove(g[nodeNum1]);

        }
        scanner.close();

        // totala flödet
        int totFlow = 0;
        // vi lägger nu tillbaka en åt gången tills
        // vi når den lägsta gränsen för C
        for (int j = removed.length - 1; j >= 0; j--) {

            // hittar vägen, nodernas värden och kapaciteten
            int[] TempPath = paths[removed[j]];
            int nodeNum1 = TempPath[0];
            int nodeNum2 = TempPath[1];
            int c = TempPath[2];

            // sätter in två nya vägar som vi tog bort tidigare
            g[nodeNum1].neighbours.put(g[nodeNum2], new Edge(c, 0));
            g[nodeNum2].neighbours.put(g[nodeNum1], new Edge(c, 0));

            // kollar om vi kan få ett flöde >= C med ford Fulkerson
            int maxflow = Integer.MAX_VALUE;
            while (maxflow != 0) {
                maxflow = fordFulkerson(g, 0, N - 1);
                totFlow += maxflow;
            }

            if (totFlow >= C) {
                System.out.println(j + " " + totFlow);
                return;
            }
        }
    }

    public static int fordFulkerson(Node[] g, int s, int t) {

        int total = 0;
        // Om vi hittar en väg från s till t får vi true från BFS
        while (BFS(g, s, t)) {

            // delta blir den lägsta kapacitet längst vägen
            int delta = Integer.MAX_VALUE;
            Node NbrNode = g[t];

            // går igenom vägen från t till s för att hitta lägsta kapaciteten
            while (NbrNode.ind != s) {
                // hämtar kanten mellan noderna
                Edge edge = NbrNode.pred.neighbours.get(NbrNode);
                // tillgängligt flöde
                int diff = edge.c - edge.f;
                //
                delta = Math.min(diff, delta);
                NbrNode = NbrNode.pred;
            }
            //adderar den lägsta kapacitansen till totala vägens flöde
            total += delta;

            //ökar flödet längs kanterna
            NbrNode = g[t];
            while (NbrNode.ind != s) {
                Edge e1 = NbrNode.pred.neighbours.get(NbrNode);
                e1.f = e1.f + delta;
                NbrNode = NbrNode.pred;
            }
        }
        
        return total;
    }

    public static boolean BFS(Node[] g, int s, int t) {

        // om vi är på rätt nod gör vi inget
        if (t == s) {
            return true;
        }

        // sätter alla noder till obesökta
        for (Node node : g) {
            node.visited = false;
        }

        g[s].visited = true;

        LinkedList<Node> q = new LinkedList<>();
        // Lägger till start noden i q
        q.add(g[s]);

        while (!q.isEmpty()) {
            // hämtar första elementet i q
            Node v = q.poll();

            // går igenom alla grannoder
            for (Node NbrNode : v.neighbours.keySet()) {
                Edge e = v.neighbours.get(NbrNode);

                // begär att vi inte besökt noden innan och att kanten
                // mellan har en kapacitet större än flödet
                if (!NbrNode.visited && e.c > e.f) {
                    NbrNode.visited = true;

                    // pred ger oss vägen tillbaks till start noden
                    NbrNode.pred = v;

                    if (NbrNode.ind == t) {
                        return true;
                    }

                    // har vi fel nod lägger vi till den i q och
                    // går igenom dens grannar
                    q.add(NbrNode);
                }
            }
        }

        // väg saknas
        return false;
    }
}

class Node {
    HashMap<Node, Edge> neighbours = new HashMap<Node, Edge>();
    Node pred;
    boolean visited;
    int ind;
}

class Edge {
    int c;
    int f;

    public Edge(int c, int f) {
        this.c = c;
        this.f = f;
    }
}
