// import java.util.Random;

// public class PiEstimator {
//     //number of iterations the algorithm, a larger number here should result in a more accurate estimate
//     //public static int numberOfDarts = 1_000_000_000;
//     public static void main(String[] args) throws InterruptedException {
//         //how many darts lie within the quarter circle region

//         //Task task1 = new TaskEvent()

//         TotalWithin totalCount = new TotalWithin();

//         throwDarts thread1 = new throwDarts(totalCount);

//         throwDarts thread2 = new throwDarts(totalCount);

//         throwDarts thread3 = new throwDarts(totalCount);

//         throwDarts thread4 = new throwDarts(totalCount);

//         thread1.start();

//         thread2.join();

//         thread3.join();

//         thread4.join();

//     }

//     static class throwDarts extends Thread {

//         private TotalWithin t;

//         throwDarts(TotalWithin t){
//             this.t =t;
//         }

//         public void run(){

//             Random r = new Random();

//             double numberOfDarts = 1_000_000_000;

//             int within = 0;
//             int i=0;
//             for(int z=0; z<1000000;z++){
//                 synchronized (t){
//                     //get x and y coordinates for the darts
//                     double x = r.nextDouble();
//                     double y = r.nextDouble();
//                     //calculate the distance from the origin (0, 0) darts with a distance less than 1 are within the
//                     //quarter circle so add these to within
//                     double dist = Math.sqrt((x * x) + (y * y));
//                     if (dist < 1) {
//                         t.DartsWithin++;
//                     }
//                 }
//                 if(t.DartsWithin==0){
//                     System.out.println("The t.DartsWithin value is 0");
//                 }else{
//                     System.out.println("Total number of darts in the circle is "+ t.DartsWithin+" out of 1,000,000,000");
//                     System.out.println("The estimate is: " + (4*(double)(t.DartsWithin)/1000000));
//                 }

//             }
//         }
//     }

//     static class TotalWithin{

//         int DartsWithin = 0;

//         public void checkDart(double dist){
//             if(dist<1){
//                 DartsWithin++;
//             }
//         }
//     }
// }



















import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PiEstimator {
    public static void main(String[] args) throws InterruptedException {
        int numThreads = 4;
        long totalDarts = 1_000_000_000;
        long dartsPerThread = totalDarts / numThreads;
        TotalWithin totalWithin = new TotalWithin();
        DartThrower[] dartThrowers = new DartThrower[numThreads];
        for (int i = 0; i < numThreads; i++) {
            dartThrowers[i] = new DartThrower(totalWithin, dartsPerThread);
            dartThrowers[i].start();
        }
        for (DartThrower dartThrower : dartThrowers) {
            dartThrower.join();
        }
        double piEstimate = 4.0 * totalWithin.getCount() / totalDarts;
        System.out.println("\nTotal darts thrown: " + totalDarts);
        System.out.println("Total darts within the circle: " + totalWithin.getCount());
        System.out.println("Pi estimate: " + piEstimate);
    }
}

class TotalWithin {
    private AtomicLong count = new AtomicLong(0);
    public void increment() {
        count.incrementAndGet();
    }
    public long getCount() {
        return count.get();
    }
}
class DartThrower extends Thread {
    private final TotalWithin totalWithin;
    private final long darts;
    public DartThrower(TotalWithin totalWithin, long darts) {
        this.totalWithin = totalWithin;
        this.darts = darts;
    }
    @Override
    public void run() {
        Random random = new Random();
        for (long i = 0; i < darts; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1) {
                totalWithin.increment();
            }
        }
    }
}





