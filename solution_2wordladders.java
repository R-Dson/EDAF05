import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class solution {
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
        NodePair[] sp = new NodePair[q];

        Graph G = new Graph();

        for (int i = 0; i < n; i++) {
            String num = scanner.next();
            G.nodes.add(new node(num.toCharArray()));
        }

        for (int i = 0; i < q; i++) {
            node n1 = G.getNodeByString(scanner.next());
            node n2 = G.getNodeByString(scanner.next());
            sp[i] = new NodePair(n1, n2);
        }

        for (int i = 0; i < G.nodes.size(); i++) {
            node node = G.nodes.get(i);

            for (int j = 0; j < G.nodes.size(); j++) {
                if (i != j) {
                    node other = G.nodes.get(j);
                    String sword = other.fullWord();
                    int correct = 0;

                    for (int k = 1; k < node.text.length; k++) {
                        char c = node.text[k];
                        if (node.charCount(c, 1) > other.charCount(c, 0)) {
                            correct = 0;
                            break;
                        } else {
                            if (sword.indexOf(c) != -1) {
                                correct++;
                            } else {
                                correct = 0;
                                break;
                            }
                        }
                    }
                    if (correct == node.text.length - 1) {
                        node.nbs.add(other);
                    }
                    correct = 0;
                }
            }
        }

        for (NodePair nodePair : sp) {
            BFS(G, nodePair.n1, nodePair.n2);
        }
    }

    private void BFS(Graph G, node s, node t) {
        if (s.fullWord().equals(t.fullWord())){
            System.out.println("0");
            return;
        }
        for (node n : G.nodes){
            n.pred = null;
            n.depth = 0;
        }

        String tWord = t.fullWord();

        ArrayList<node> q = new ArrayList<node>();
        q.add(s);

        while (q.size() > 0) {
            node nodeMain = q.remove(0);
            for (node otherNode : nodeMain.nbs) {
                if (otherNode.pred == null) {
                    if (tWord.equals(otherNode.fullWord())) {
                        System.out.println(nodeMain.depth + 1);
                        return;
                    }
                    otherNode.depth = nodeMain.depth + 1;
                    q.add(otherNode);
                    otherNode.pred = nodeMain;
                }
            }
        }
        System.out.println("Impossible");
    }
}

class Graph {
    ArrayList<node> nodes = new ArrayList<>();

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
    public char[] text;
    public ArrayList<node> nbs = new ArrayList<>();
    public node pred;
    public int depth = 0;

    public node(char[] t) {
        text = t;
    }

    public String lastWord() {
        return fullWord().substring(1);
    }

    public int charCount(char c, int f) {
        int count = 0;
        for (int i = f; i < text.length; i++)
            if (c == text[i])
                count++;
        return count;
    }

    public String fullWord() {
        return new String(text);
    }

    public boolean Compare(String s) {
        if (fullWord().equals(s))
            return true;
        return false;
    }

}

class NodePair{
    public node n1;
    public node n2;

    public NodePair(node n1, node n2){
        this.n1 = n1;
        this.n2 = n2;
    }
}