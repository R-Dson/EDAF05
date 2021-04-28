import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class solution_4closestpair {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));

        HashMap<Integer, Person> Persons = new HashMap<>();


        //PriorityQueue<Px> pqPx = new PriorityQueue<>();
        //PriorityQueue<Py> pqPy = new PriorityQueue<>();

        //antalet personer
        int N = scanner.nextInt();
        Px[] pqPx = new Px[N];
        Py[] pqPy = new Py[N];
        
        for (int i = 0; i < N; i++) {
            Px px = new Px(scanner.nextInt());
            pqPx[i] = px;

            Py py = new Py(scanner.nextInt());
            pqPy[i] = py;
            
            Person p = new Person(px, py, i);
            
            Persons.put(i, p);
        }
        Arrays.sort(pqPx);
        Arrays.sort(pqPy);
        closest(pqPx, pqPy, N);
    }

    public static void closest(Px[] px, Py[] py, int N){
        
    }
}

class Px implements Comparable<Px>, Comparator<Px>{

    int x;

    public Px(int x){
        this.x = x;
    }

    @Override
    public int compare(Px o1, Px o2) {
        return Integer.compare(o1.x, o2.x);
    }

    @Override
    public int compareTo(Px o) {
        return Integer.compare(x, o.x);
    }

}
class Py implements Comparable<Py>, Comparator<Py>{
    int y;

    public Py(int y){
        this.y = y;
    }
    
    @Override
    public int compare(Py o1, Py o2) {
        return Integer.compare(o1.y, o2.y);
    }

    @Override
    public int compareTo(Py o) {
        return Integer.compare(y, o.y);
    }
}

class Person{

    Px x;
    Py y;
    int ind;

    public Person(Px x, Py y, int ind){
        this.x = x;
        this.y = y;
        this.ind = ind;
    }
    
}