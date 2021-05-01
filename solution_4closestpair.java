import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.Collections;
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
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();

            X px = new X(x, y);
            Px[i] = px;
        }

        Arrays.sort(Px);

        for (int i = 0; i < N; i++) {
            Y py = new Y(Px[i].x, Px[i].y);
            Py[i] = py;
        }

        double d = closest(Px, Py, N);

        String s = String.format("%.6f", d).replace(',', '.');

        System.out.println(s);
    }

    public static double closest(X[] px, Y[] py, int N) {
        if (N <= 3) {
            double lowest = Integer.MAX_VALUE;
            for (int i = 0; i < py.length; i++) {
                for (int j = i + 1; j < py.length; j++) {
                    double f = pyth(py[i].y - py[j].y, py[i].x - py[j].x);
                    if (f < lowest) {
                        lowest = f;
                    }
                }
            }
            return lowest;
        }

        X lx[] = Arrays.copyOfRange(px, 0, N / 2);
        X rx[] = Arrays.copyOfRange(px, (N / 2), N);

        Y ly[] = Arrays.copyOfRange(py, 0, N / 2);
        Y ry[] = Arrays.copyOfRange(py, (N / 2), N);

        double dl = closest(lx, ly, lx.length);
        double dr = closest(rx, ry, rx.length);

        double delta = Math.min(dl, dr);

        double xAvg = px[px.length / 2].x;

        ArrayList<Y> Sy = new ArrayList<>();

        for (int i = 0; i < py.length; i++) {
            if (Math.abs(xAvg - py[i].x) < delta) {
                Sy.add(py[i]);
            }
        }

        Collections.sort(Sy);
        double shortest = delta;

        int c = 15;
        for (int i = 0; i < Sy.size(); i++) {
            Y pyi = Sy.get(i);
            for (int j = i + 1; j < i + c; j++) {
                if (j < Sy.size()) {
                    Y pyj = Sy.get(j);
                    double dist = pyth(pyi.y - pyj.y, pyi.x - pyj.x);
                    if (dist < shortest) {
                        shortest = dist;
                    }
                } else {
                    break;
                }
            }
        }
        return shortest;
    }

    static double pyth(double x, double y) {
        return Math.hypot(x, y);
    }
}

class Coord {
    double x;
    double y;

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double value() {
        return -1;
    }

    public double opp() {
        return -1;
    }

}

class X extends Coord implements Comparable<X> {

    public X(double x, double y) {
        super(x, y);
    }

    @Override
    public int compareTo(X o) {
        return Double.compare(x, o.x);
    }

    @Override
    public double value() {
        return x;
    }

    @Override
    public double opp() {
        return y;
    }
}

class Y extends Coord implements Comparable<Y>{

    public Y(double x, double y) {
        super(x, y);
    }

    @Override
    public int compareTo(Y o) {
        return Double.compare(y, o.y);
    }

    @Override
    public double value() {
        return y;
    }

    @Override
    public double opp() {
        return x;
    }
}