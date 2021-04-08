import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class solution_2wordladders {
    public static void main(String[] args) {
        solver s = new solver();
        s.scan();
    }
}

class solver {

    public void scan() {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));

        int n = scanner.nextInt();
        int q = scanner.nextInt();

        // Innehåller alla start och slut noder i par
        NodePair[] sp = new NodePair[q];

        // Grafen
        Graph G = new Graph();

        // Lägger till alla orden som noder (inte kopplade än)
        for (int i = 0; i < n; i++) {
            String num = scanner.next();
            G.nodes.add(new node(num.toCharArray()));
        }

        // Hittar var nod i Graph och lägger till dem som par
        for (int i = 0; i < q; i++) {
            node n1 = G.getNodeByString(scanner.next());
            node n2 = G.getNodeByString(scanner.next());
            sp[i] = new NodePair(n1, n2);
        }

        // Kollar om någon annan nod innehåller node's fyra sista bokstäver
        for (int i = 0; i < G.nodes.size(); i++) {
            node node = G.nodes.get(i);

            // Går igenom alla andra nodes, i != j
            for (int j = 0; j < G.nodes.size(); j++) {
                if (i != j) {
                    // Den andra noden, ordet som noden innehåller och antalet korrekta bokstäver
                    // som den andra noden innehåller
                    node other = G.nodes.get(j);
                    String oWord = other.fullWord();
                    int correct = 0;

                    // Kollar för var bokstav i node (char c) om den finns i den andra nodens ord
                    for (int k = 1; k < node.text.length; k++) {
                        char c = node.text[k];
                        // Kollar om antalet gånger som c förekommer i noderna
                        // Den första är 1 till 4 medans den andra är 0 till 4
                        if (node.charCount(c, 1) > other.charCount(c, 0)) {
                            correct = 0;
                            break;
                        } else {
                            // Om den andra nodens ord innehåller c, dvs index != -1, räknar vi den. Annars
                            // avbryter vi direkt.
                            if (oWord.indexOf(c) != -1) {
                                correct++;
                            } else {
                                correct = 0;
                                break;
                            }
                        }
                    }
                    // Om other nodtexten innehöll lika många correkta som nodens text har i längd -
                    // 1 så lägger vi till den som en granne.
                    if (correct == node.text.length - 1) {
                        node.nbs.add(other);
                    }
                    // correct = 0;
                }
            }
        }

        // För var par gör vi bredden först sökning för att hitta kortast sträcka från
        // den ena noden till den andra.
        for (NodePair nodePair : sp) {
            BFS(G, nodePair.n1, nodePair.n2);
        }
    }

    private void BFS(Graph G, node s, node t) {
        // ordet vi vill nå
        String endWord = t.fullWord();

        // Om vi är på den rätta noden behöver vi inte gå någonstans och skriver ut 0
        // if (s.fullWord().equals(t.fullWord())) {
        if (s.Compare(endWord)) {
            System.out.println("0");
            return;
        }

        // Resetar alla värden
        for (node n : G.nodes) {
            n.pred = null;
            n.depth = 0;
        }
        
        LinkedList<node> q = new LinkedList<node>();
        // Lägger till startnoden
        q.add(s);

        while (q.size() > 0) {
            node nodeMain = q.poll();
            for (node grannNode : nodeMain.nbs) {
                // Om pred == null så har vi inte varit vid den noden innan
                if (grannNode.pred == null) {
                    // Kollar om g ord är samma som slutordet
                    if (endWord.equals(grannNode.fullWord())) {
                        // if (t.Compare(grannNode.fullWord())){
                        System.out.println(nodeMain.depth + 1);
                        return;
                    }
                    // Om det är fel nod går vi vidare
                    // Sätter den andra nodens djup som vår mains djup + 1
                    grannNode.depth = nodeMain.depth + 1;
                    // lägger till i kön
                    q.addLast(grannNode);
                    // sätter företrädarnoden till nodeMain i den grannoden
                    grannNode.pred = nodeMain;
                }
            }
        }
        // Hittar vi ingen väg så skriver vi ut
        System.out.println("Impossible");
    }
}

class Graph {
    // Lista med alla noder
    ArrayList<node> nodes = new ArrayList<>();

    // Hittar en nod från en string
    public node getNodeByString(String string) {
        for (node node : nodes) {
            if (node.Compare(string)) {
                return node;
            }
        }
        return null;
    }
}

class node {
    // Nodens egenskaper
    public char[] text;
    private String Stext;
    public ArrayList<node> nbs = new ArrayList<>();
    public node pred;
    public int depth = 0;

    public node(char[] t) {
        text = t;
        Stext = new String(t);
    }

    // räknar antalet gånger en karaktär förekommer i nodens string
    // Tar också in int f som är en förskjutning på forloopen
    public int charCount(char c, int f) {
        int count = 0;
        for (int i = f; i < text.length; i++)
            if (c == text[i])
                count++;
        return count;
    }

    // Returnerar hela ordet förutom den första bokstaven, dvs de fyra sista
    public String lastWord() {
        return fullWord().substring(1);
    }

    // Returnerar hela ordet som en sträng
    public String fullWord() {
        return Stext;
    }

    // Kollar om någon string s är samma som denna noden
    public boolean Compare(String s) {
        if (fullWord().equals(s))
            return true;
        return false;
    }

}

class NodePair {
    // Nodpar
    public node n1;
    public node n2;

    public NodePair(node n1, node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }
}