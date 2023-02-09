import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        int taskCount = 8;
        submitTasksInto(pool, taskCount);
        System.out.println("   ");
        pool.shutdown();
        measure(pool);
    }

    private static void submitTasksInto(ExecutorService pool, int taskCount) {
        System.out.println("Creating tasks...");
        IntStream.rangeClosed(1, taskCount)
                .mapToObj(Main::makeTask)
                .forEach(pool::submit);
    }

    private static Runnable makeTask(int taskId) {
        int temp = new Random().nextInt(20000) + 10000;
        int taskTime = (int) TimeUnit.MILLISECONDS.toSeconds(temp);
        return () -> heavyTask(taskId, taskTime);
    }

    private static void heavyTask(int taskId, int taskTime) {
        System.out.printf("The task %s will take %s seconds", taskId, taskTime);
        try {
            Thread.sleep(taskTime * 1000L);
            System.out.printf("The task %s has been completed in %s seconds\n", taskId, taskTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void measure(ExecutorService pool) {
        long start = System.nanoTime();
        try {
            pool.awaitTermination(600, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long delta = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("Completion of the task has taken %s milliseconds\n", delta);
    }
}
