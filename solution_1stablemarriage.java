import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class solution{
    public static void main(String[] args) {
        solver s = new solver();
        s.scan();
    }
}

class solver {

    public void scan() {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));
        // tar in antalet kvinnor/män
        int n = scanner.nextInt();

        int[][] men = new int[n][n + 1];
        int[][] women = new int[n][n + 1];
        int currentMan = 0;
        int w = 0;
        boolean exist = false;

        for (int count = 0; count < 2*(n+1)*n; count++) {
            Integer num = scanner.nextInt();

            if (count % (n + 1) == 0) {
                // kollar om det finns en kvinna med talet annars lägger till som man
                if (women[num-1][0] == num) {
                    men[currentMan][0] = num;
                    currentMan++;
                    exist = true;
                } else {
                    women[num-1][0] = num;
                    w = num-1;
                    exist = false;
                }
            } else {
                if (exist) {
                    int index = count % (n + 1);
                    men[currentMan - 1][index] = num;
                } else {
                    int index = count % (n + 1);
                    women[w][num] = index;
                }
            }
        }
        
        GS(women, men);
    }

    private void GS(int[][] W, int[][] M) {

        HashMap<Integer, LinkedList<Integer>> pairs = new HashMap<>(); 

        // lägger till alla män i en lista
        LinkedList<LinkedList<Integer>> p = new LinkedList<>();
        for (int[] is : M) {
            LinkedList<Integer> list = new LinkedList<>();
            for (int s : is)
                list.add(s);
            p.add(list);
        }

        while (p.size() > 0) {
            // första mannen
            LinkedList<Integer> currentMan = p.poll();
            
            int man = currentMan.poll();
            int preferedWoman = currentMan.poll();
            currentMan.addFirst(man);

            boolean inPair = pairs.containsKey(preferedWoman);

            if (!inPair) {
                pairs.put(preferedWoman, currentMan);
            }
            else if(W[preferedWoman-1][pairs.get(preferedWoman).peek()] > W[preferedWoman-1][man]){
                LinkedList<Integer> oldMan = pairs.get(preferedWoman);
                pairs.replace(preferedWoman, currentMan);
                p.add(oldMan);
            }
            else{
                p.add(currentMan);
            }
        }
        for(int i = 1; i < pairs.size()+1; i++){
            System.out.println(pairs.get(i).peek());
        }
    }
}
