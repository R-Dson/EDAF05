import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class solution_5gorilla {

    static Integer[][] connection;
    static Integer[][] opt;
    static HashMap<Character, Letter> lettersC = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));

        LinkedList<Query> queries = new LinkedList<>();
        int count = 0;

        while (scanner.hasNext()) {
            if (scanner.hasNextInt())
                break;

            String s = scanner.next();

            Letter l = new Letter(s.charAt(0));
            l.v = count;
            lettersC.put(s.charAt(0), l);
            count++;
        }

        connection = new Integer[lettersC.size()][lettersC.size()];

        for (int y = 0; y < lettersC.size(); y++) {
            for (int x = 0; x < lettersC.size(); x++) {
                connection[y][x] = scanner.nextInt();
            }
        }

        int q = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < q; i++) {
            String s = scanner.nextLine();

            String[] split = s.split(" ");
            char[] chars1 = split[0].toCharArray();
            char[] chars2 = split[1].toCharArray();

            ArrayList<Character> string1 = new ArrayList<Character>(chars1.length);
            for (Character character : chars1) {
                string1.add(character);
            }

            ArrayList<Character> string2 = new ArrayList<Character>(chars2.length);
            for (Character character : chars2) {
                string2.add(character);
            }

            Query query = new Query(string1, string2);
            queries.add(query);
        }
        scanner.close();

        align(queries);
    }

    static private void align(LinkedList<Query> queries) {
        Iterator<Query> it = queries.iterator();
        while (it.hasNext()) {
            Query query = it.next();
            int q1Length = query.query1.size();
            int q2Length = query.query2.size();

            opt = new Integer[q1Length+1][q2Length+1];

            opt(q1Length, q2Length, query);

            //System.out.println(Arrays.deepToString(optvals).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

            int longest = Math.max(query.query2.size(), query.query1.size());

            int i = query.query1.size();
            int j = query.query2.size();
            StringBuilder sb = new StringBuilder(longest);
            StringBuilder sb2 = new StringBuilder(longest);

            while (i > 0 && j > 0) {
                Character ci = query.query1.get(i-1);
                Letter li = lettersC.get(ci);

                Character cj = query.query2.get(j-1);
                Letter lj = lettersC.get(cj);
                
                if (i > 0 && j > 0 && opt[i][j] == opt[i-1][j-1] + connection[li.v][lj.v]) {
                    sb.append(query.query1.get(--i));
                    sb2.append(query.query2.get(--j));
                }
                else if (i > 0 && opt[i][j] + 4  == opt[i - 1][j]) {
                    sb.append(query.query1.get(i-1));
                    sb2.append('*');
                    i--;
                }
                else if (j > 0 && opt[i][j] + 4 == opt[i][j-1]) {
                    sb2.append(query.query2.get(j-1));
                    sb.append('*');
                    j--;
                }
            }

            while (i > 0) {
                sb.append(query.query1.get(i));
                sb2.append('*');
                i--;                
            }
            while (j > 0) {
                sb2.append(query.query2.get(j));
                sb.append('*');
                i--;                
            }

            String st = sb.reverse().toString();
            String st2 = sb2.reverse().toString();
            
            System.out.println(st + " " + st2);
        }
    }

    static int opt(int i, int j, Query query) {
        int delta = -4;
        if (i == 0 && j == 0) {
            opt[i][j] = 0;
            return 0;
        }
        if (i == 0) {
            opt[0][j] = j * delta;
            return j * delta;
        } else if (j == 0) {
            opt[i][0] = i * delta;
            return i * delta;
        }

        Character ci = query.query1.get(i-1);
        Letter li = lettersC.get(ci);

        Character cj = query.query2.get(j-1);
        Letter lj = lettersC.get(cj);

        int connopt = connection[li.v][lj.v] + opt(i - 1, j - 1, query);

        int dopt1 = delta + opt(i, j - 1, query);
        int dopt2 = delta + opt(i - 1, j, query);

        opt[i][j] = Math.max(Math.max(dopt1, dopt2), connopt);
        return opt[i][j];
    }

}

class Query {
    ArrayList<Character> query1;
    ArrayList<Character> query2;

    public Query(ArrayList<Character> query1, ArrayList<Character> query2) {
        this.query1 = query1;
        this.query2 = query2;
    }
}

class Letter {
    char c;
    int v;

    public Letter(char c) {
        this.c = c;
    }
}

/*static void optAlt(Query q) {
        int delta = -4;
        for (int i = 0; i <= q.query1.size(); i++) {
            opt[i][0] = i * delta;
        }
        for (int i = 0; i <= q.query2.size(); i++) {
            opt[0][i] = i * delta;
        }
        for (int i = 1; i <= q.query1.size(); i++) {
            for (int j = 1; j <= q.query2.size(); j++) {
                int dopt2 = delta + opt[i - 1][j];
                int dopt1 = delta + opt[i][j - 1];

                Character ci = q.query1.get(i-1);
                Letter li = lettersC.get(ci);

                Character cj = q.query2.get(j-1);
                Letter lj = lettersC.get(cj);

                int connopt = connection[li.v][lj.v] + opt[i - 1][j - 1];
                opt[i][j] = Math.min(Math.min(dopt1, dopt2), connopt);
            }
        }
    }*/
