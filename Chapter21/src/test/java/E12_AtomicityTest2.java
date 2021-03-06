import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author ZhangGJ
 * @Date 2019/10/06
 */
class AtomicityTest2 implements Runnable {
    private int i;

    public synchronized int getValue() {
        return i;
    }

    private synchronized void evenIncrement() {
        i++;
        i++;
    }

    public void run() {
        while (true)
            evenIncrement();
    }
}


public class E12_AtomicityTest2 {
    public static void main(String[] args) {
        System.out.println("Press Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        AtomicityTest2 at = new AtomicityTest2();
        exec.execute(at);
        while (true) {
            int val = at.getValue();
            if (val % 2 != 0) {
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
