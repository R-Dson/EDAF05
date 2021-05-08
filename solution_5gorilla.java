import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class solution_5gorilla{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));
        HashMap<Integer, Letter> letters = new HashMap<>();

        int count = 0;

        while (scanner.hasNext()) {
            if (scanner.hasNextInt())
                break;

            String s = scanner.next();
            Letter l = new Letter(s.charAt(0));
            letters.put(count, l);
            count++;
        }

        System.out.println(letters.size());
        Integer [][] connection = new Integer[letters.size()][letters.size()];
        
        for (int y = 0; y < letters.size(); y++) {
            for (int x = 0; x < letters.size(); x++) {
                int inInt = scanner.nextInt();
                connection[y][x] = inInt;
            }
        }

        int q = scanner.nextInt();
        scanner.nextLine();
        LinkedList<Query> queries = new LinkedList<>();
        for (int i = 0; i < q; i++) {
            String s = scanner.nextLine();
            String[] split = s.split(" ");
            Query query = new Query(split[0].toCharArray(), split[1].toCharArray());
            queries.add(query);
        }
    }
}

class Query{
    char[] query1;
    char[] query2;

    public Query(char[] query1, char[] query2){
        this.query1 = query1;
        this.query2 = query2;
    }
}

class Letter{
    char c;
    int v;

    public Letter(char c){
        this.c = c;
    }
}