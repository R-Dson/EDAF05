import java.io.BufferedInputStream;
import java.util.ArrayList;
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
        for (int[] wls : women) {
            for (int i = 0; i < wls.length; i++)
                if (wls[i] < 0)
                    System.out.println(i);

        } 
    }

    private void GS(int[][] W, int[][] M) {
        ArrayList<ArrayList<Integer>> RemovedMen = new ArrayList<>();

        // lägger till alla män i en lista
        ArrayList<ArrayList<Integer>> p = new ArrayList<>();
        for (int[] is : M) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int s : is)
                list.add(s);
            p.add(list);
        }

        while (p.size() > 0) {
            // första mannen
            ArrayList<Integer> currentMan = p.get(0);
            p.remove(0);

            int[] ManMostPrefered = new int[1];
            // hittar prefered kvinna
            for (int i = 0; i < W.length; i++) {
                if (W[i][0] == currentMan.get(1)) {
                    ManMostPrefered = W[i];
                    break;
                }
            }

            // tar bort kvinnan så mannen inte kan välja henne igen
            currentMan.remove(1);

            // CurrentPartner är -4001 om det inte finns, detta är utanför det möjliga intervallet på N
            int CurrentPartner = -4001;

            for (int i = 1; i < ManMostPrefered.length; i++) {
                // CurrentPartner har negativt värde. sparar värdet
                if (ManMostPrefered[i] < 0) {
                    CurrentPartner = i;
                    break;
                }
            }

            //Om det inte finns en CurrentPartner
            if (CurrentPartner == -4001) {
                // lägger till i listan med borttagna män
                RemovedMen.add(currentMan);

                // gör värdet negativt om de är ett par
                ManMostPrefered[currentMan.get(0)] = -ManMostPrefered[currentMan.get(0)];

                // lägst värde blir bättre. Partnern har ett negativt värde
            } else if (-ManMostPrefered[CurrentPartner] > ManMostPrefered[currentMan.get(0)]) {
                // lägger till i listan med borttagna män
                RemovedMen.add(currentMan);

                // tar bort den gamla partnern
                ManMostPrefered[CurrentPartner] = -ManMostPrefered[CurrentPartner];
                
                // sätter in den nya partnern
                ManMostPrefered[currentMan.get(0)] = -ManMostPrefered[currentMan.get(0)];

                //lägger till det borttagna objektet i p igen
                for (int i = 0; i < RemovedMen.size(); i++) {
                    if (RemovedMen.get(i).get(0) == CurrentPartner) {
                        p.add(RemovedMen.remove(i));
                        break;
                    }
                }
            } else {
                //lägger tillbaka i listan
                p.add(currentMan);
            }
        }
    }
}
