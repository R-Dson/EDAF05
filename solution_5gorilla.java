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

        // tar in alla bokstäver
        while (scanner.hasNext()) {
            if (scanner.hasNextInt())
                break;

            String s = scanner.next();

            Letter l = new Letter(s.charAt(0));
            l.v = count;
            lettersC.put(s.charAt(0), l);
            count++;
        }

        // skapar matris för värdena
        connection = new Integer[lettersC.size()][lettersC.size()];

        // fyller i matrisen
        for (int y = 0; y < lettersC.size(); y++) {
            for (int x = 0; x < lettersC.size(); x++) {
                connection[y][x] = scanner.nextInt();
            }
        }

        // antalet par som ska jämföras
        int q = scanner.nextInt();
        // Den kör inte korrekt utan denna... ??
        scanner.nextLine();

        for (int i = 0; i < q; i++) {
            String s = scanner.nextLine();

            // delar upp par strängen, går fortare än .split
            int ind = s.indexOf(' ');
            char[] chars1 = s.substring(0, ind).toCharArray();
            char[] chars2 = s.substring(ind + 1, s.length()).toCharArray();

            /*
             * String[] split = s.split(" "); char[] chars1 = split[0].toCharArray(); char[]
             * chars2 = split[1].toCharArray();
             */

            // bygger upp en Querty som innehåller strängarnas bokstäver
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

        // algoritmen
        align(queries);
    }

    static private void align(LinkedList<Query> queries) {
        // går igenom alla par
        Iterator<Query> it = queries.iterator();
        while (it.hasNext()) {
            Query query = it.next();

            int q1Length = query.query1.size();
            int q2Length = query.query2.size();

            // Skapar en optimerad matris som är rätt storlek för paret
            opt = new Integer[q1Length + 1][q2Length + 1];
            // Fyller i matrisen genom
            optFun(query);

            // System.out.println(Arrays.deepToString(optvals).replace("], ",
            // "]\n").replace("[[", "[").replace("]]", "]"));

            int i = query.query1.size();
            int j = query.query2.size();
            String s1 = "";
            String s2 = "";

            // slut elementen som ska ha * inkluderas i denna delen
            while (i > 0 && j > 0) {
                Character ci = query.query1.get(i - 1);
                Letter li = lettersC.get(ci);

                Character cj = query.query2.get(j - 1);
                Letter lj = lettersC.get(cj);

                // back trackar vägen vi gick tills vi byggt upp den korrekta stängen

                // kollar om diagonalelementet (upp till vänster) är samma, dvs att vi har en
                // matchning mellan strängarna, då lägger vi till bokstaven
                if (opt[i][j] == opt[i - 1][j - 1] + connection[li.v][lj.v]) {
                    s1 = query.query1.get(--i) + s1;
                    s2 = query.query2.get(--j) + s2;
                }
                // Om det är elementet över den nuvarande positionen så har vi att strängen i
                // query2 fattas en bokstav, sätter in en * då, s1 bygger på existerande sträng
                // här är 4 delta
                else if (opt[i][j] == opt[i - 1][j] - 4) {
                    s1 = query.query1.get(--i) + s1;
                    s2 = '*' + s2;
                }
                // Om det är elementet till vänster är det query1 som fattas en bokstav och
                // sätter in * i s1, bygger på sträng 2
                // här är 4 delta
                else if (opt[i][j] == opt[i][j - 1] - 4) {
                    s1 = '*' + s1;
                    s2 = query.query2.get(--j) + s2;
                }
            }

            // om vi har 'i' värden över så betyder det att vi har matchat alla element i s2
            // med s1 och s1 var länge än s2, då får vi tomrum i början och fyller på med *
            while (i > 0) {
                s1 = query.query1.get(--i) + s1;
                s2 = '*' + s2;
            }
            // samma sak gäller för 'j' fast för motsat sträng
            while (j > 0) {
                s1 = '*' + s1;
                s2 = query.query2.get(--j) + s2;
            }

            System.out.println(s1 + " " + s2);
        }
    }

    static void optFun(Query q) {
        int delta = -4;

        // fyller matrisens kanter med i * delta
        for (int i = 0; i <= q.query1.size(); i++) {
            opt[i][0] = i * delta;
        }
        for (int i = 0; i <= q.query2.size(); i++) {
            opt[0][i] = i * delta;
        }

        // börjar på 1, 1 i matrisen (0, 0 har vi precis beräknat, blev 0 såklart)
        // beräknar 
        for (int i = 1; i <= q.query1.size(); i++) {
            for (int j = 1; j <= q.query2.size(); j++) {
                // värdena här blir "bestraffade" med ett konstant värde -4 om de inte matchar
                // beräknar för elementet över, detta är om fattas ett element i querty2
                int dopt2 = delta + opt[i - 1][j];
                // beräknar för elementet till vänster, detta är om fattas ett element i querty1
                int dopt1 = delta + opt[i][j - 1];

                // hämtar elementets värde för bokstäverna
                Character ci = q.query1.get(i - 1);
                Letter li = lettersC.get(ci);

                Character cj = q.query2.get(j - 1);
                Letter lj = lettersC.get(cj);

                // beräknar för diagonalelementet en matchning
                int connopt = connection[li.v][lj.v] + opt[i - 1][j - 1];
                // hittar det största av värdena vilket blir det optimala värdet,
                opt[i][j] = Math.max(Math.max(dopt1, dopt2), connopt);
            }
        }
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

/*
 * static int opt(int i, int j, Query query) { int delta = -4; if (i == 0 && j
 * == 0) { opt[i][j] = 0; return 0; } if (i == 0) { opt[0][j] = j * delta;
 * return j * delta; } else if (j == 0) { opt[i][0] = i * delta; return i *
 * delta; }
 * 
 * Character ci = query.query1.get(i-1); Letter li = lettersC.get(ci);
 * 
 * Character cj = query.query2.get(j-1); Letter lj = lettersC.get(cj);
 * 
 * int con = connection[li.v][lj.v]; int copt = opt[i-1][j-1]; int connopt = con
 * + copt;
 * 
 * int dopt1 = delta + opt[i][j-1]; int dopt2 = delta + opt[i-1][j];
 * 
 * opt[i][j] = Math.max(Math.max(dopt1, dopt2), connopt); return opt[i][j]; }
 */