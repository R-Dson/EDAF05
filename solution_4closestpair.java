import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;

public class solution_4closestpair {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedInputStream(System.in));
        // antalet personer
        int N = scanner.nextInt();
        X[] Px = new X[N];
        Y[] Py = new Y[N];


        for (int i = 0; i < N; i++) {
            X px = new X(scanner.nextDouble());
            Px[i] = px;

            Y py = new Y(scanner.nextDouble());
            Py[i] = py;

            Point p = new Point(px, py, i);
            px.person = p;
            py.person = p;
        }

        Arrays.sort(Px);
        Arrays.sort(Py);

        double d = closest(Px, Py, N);

        String s = String.format("%.6f", d).replace(',', '.');

        System.out.println(s);
    }

    public static double closest(X[] px, Y[] py, int N) {
        if (N <= 3) {
            double lowest = Integer.MAX_VALUE;
            for (int i = 0; i < py.length; i++) {
                for (int j = i + 1; j < py.length; j++) {
                    double f = pyth(py[i].y - py[j].y, py[i].person.Px.x - py[j].person.Px.x);
                    if (f < lowest) {
                        lowest = f;
                    }

                }
            }
            return lowest;
        }

        int len = N;

        X lx[] = Arrays.copyOfRange(px, 0, len / 2);
        X rx[] = Arrays.copyOfRange(px, (len / 2), len);

        Y ly[] = new Y[lx.length];
        Y ry[] = new Y[rx.length];

        for (int i = 0; i < lx.length; i++) {
            ly[i] = lx[i].person.Py;
            ry[i] = rx[i].person.Py;
        }

        if (ry[ry.length - 1] == null)
            ry[ry.length - 1] = rx[rx.length - 1].person.Py;

        double dl = closest(lx, ly, lx.length);
        double dr = closest(rx, ry, rx.length);

        double delta = Math.min(dl, dr);

        double xAvg = px[(px.length)/ 2].x ;

        ArrayList<Y> Sy = new ArrayList<>();

        for (int i = 0; i < py.length; i++) {
             if (Math.abs(xAvg - py[i].person.Px.x) < delta) {
                Sy.add(py[i]);
            }
        }

        double fl = delta;

        int c = 15;
        for (int i = 0; i < Sy.size(); i++) {
            Y pyi = Sy.get(i);
            for (int j = i + 1; j < i + c; j++) {
                if (j < Sy.size()) {
                    Y pyj = Sy.get(j);
                    double f = pyth(pyi.y - pyj.y, pyi.person.Px.x - pyj.person.Px.x);
                    if (f < fl) {
                        fl = f;
                    }
                }
            }

        }

        return fl;
    }

    static double pyth(double x, double y) {
        return Math.hypot(x, y);
    }
}

class X implements Comparable<X>, Comparator<X> {

    Point person;
    double x;

    public X(Double x) {
        this.x = x;
    }

    @Override
    public int compare(X o1, X o2) {
        return Double.compare(o1.x, o2.x);
    }

    @Override
    public int compareTo(X o) {
        return Double.compare(x, o.x);
    }

}

class Y implements Comparable<Y>, Comparator<Y> {

    // int value;
    Point person;
    double y;

    public Y(Double y) {
        this.y = y;
    }

    @Override
    public int compare(Y o1, Y o2) {
        return Double.compare(o1.y, o2.y);
    }

    @Override
    public int compareTo(Y o) {
        return Double.compare(y, o.y);
    }
}

class Point {

    X Px;
    Y Py;
    int ind;

    public Point(X x, Y y, int ind) {
        this.Px = x;
        this.Py = y;
        this.ind = ind;
    }

}