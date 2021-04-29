import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class solution_4closestpair {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));
        // antalet personer
        int N = scanner.nextInt();
        Px[] pqPx = new Px[N];
        Py[] pqPy = new Py[N];

        for (int i = 0; i < N; i++) {
            Px px = new Px(scanner.nextDouble());

            // px.value = i;
            pqPx[i] = px;

            Py py = new Py(scanner.nextDouble());
            // px.value = i;
            pqPy[i] = py;

            Person p = new Person(px, py, i);
            px.person = p;
            py.person = p;

        }
        Arrays.sort(pqPx);
        Arrays.sort(pqPy);
        double d = closest(pqPx, pqPy, N);
        String s = String.format("%.6f", d).replace(',', '.');
        System.out.println(s);
    }

    public static double closest(Px[] px, Py[] py, int N) {
        if (N <= 3) {
            if (N == 2) {
                // return (float) Math.sqrt(Math.pow(py[1].y - py[0].y, 2) + Math.pow(px[1].x -
                // px[0].x, 2));
                return pyth(py[1].y - py[0].y, px[1].x - px[0].x);
            }
            if (N == 3) {
                double lowest = Integer.MAX_VALUE;

                for (int i = 0; i < py.length; i++) {
                    for (int j = 0; j < py.length; j++) {
                        if (i != j) {
                            double f = pyth(px[i].x - px[j].x, px[i].person.Py.y - px[j].person.Py.y);
                            if (f < lowest) {
                                lowest = f;
                            }
                        }

                    }
                }
                return lowest;
            }
            return 0;
        }

        int len = N;

        Px lx[] = Arrays.copyOfRange(px, 0, len / 2);
        Px rx[] = Arrays.copyOfRange(px, (len / 2), len);

        Py ly[] = new Py[lx.length];
        Py ry[] = new Py[rx.length];

        for (int i = 0; i < lx.length; i++) {
            ly[i] = lx[i].person.Py;
            ry[i] = rx[i].person.Py;
        }
        ry[rx.length - 1] = rx[rx.length - 1].person.Py;

        double dl = closest(lx, ly, lx.length);
        double dr = closest(rx, ry, rx.length);

        double delta = Math.min(dl, dr);

        double xAvg = (lx[lx.length - 1].x - rx[0].x) / 2;

        LinkedList<Py> Syl = new LinkedList<>();
        LinkedList<Py> Syr = new LinkedList<>();

        for (int i = 0; i < py.length; i++) {
            if (xAvg - delta < py[i].person.Px.x && py[i].person.Px.x < xAvg + delta) {
                if (i < len / 2)
                    Syl.add(py[i]);
                else
                    Syr.add(py[i]);
            }
        }

        double fl = delta;
        for (Py pyr : Syr) {
            for (Py pyl : Syl) {
                // float f = (float) Math.sqrt(Math.pow(pyr.y - pyl.y, 2) +
                // Math.pow(pyr.person.x.x - pyl.person.x.x, 2));
                double f = pyth(pyr.y - pyl.y, pyr.person.Px.x - pyl.person.Px.x);
                if (f < fl) {
                    fl = f;
                }
            }
        }
        return fl;
    }

    static double pyth(double x, double y) {
        return Math.sqrt(x*x + y*y);
    }
}

class Px implements Comparable<Px>, Comparator<Px> {

    Person person;
    double x;

    public Px(Double x) {
        this.x = x;
    }

    @Override
    public int compare(Px o1, Px o2) {
        return Double.compare(o1.x, o2.x);
    }

    @Override
    public int compareTo(Px o) {
        return Double.compare(x, o.x);
    }

}

class Py implements Comparable<Py>, Comparator<Py> {

    // int value;
    Person person;
    double y;

    public Py(Double y) {
        this.y = y;
    }

    @Override
    public int compare(Py o1, Py o2) {
        return Double.compare(o1.y, o2.y);
    }

    @Override
    public int compareTo(Py o) {
        return Double.compare(y, o.y);
    }
}

class Person {

    Px Px;
    Py Py;
    int ind;

    public Person(Px x, Py y, int ind) {
        this.Px = x;
        this.Py = y;
        this.ind = ind;
    }

}