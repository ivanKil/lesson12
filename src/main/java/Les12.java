import java.util.*;

public class Les12 {
    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;
    static float[] arr = new float[SIZE];

    public static void main(String[] args) {
        transformArrayInSingleThread();
        System.out.println("");
        try {
            transformArrayInTwoThreads();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void transformArrayInSingleThread() {
        Arrays.fill(arr, 1);
        printArrayInfo(arr);

        long start = System.currentTimeMillis();
        transformArray(0, arr);
        System.out.println("Время выполнения в одном потоке: " + (System.currentTimeMillis() - start));

        printArrayInfo(arr);
    }

    private static void transformArrayInTwoThreads() throws InterruptedException {
        Arrays.fill(arr, 1);
        printArrayInfo(arr);

        long start = System.currentTimeMillis();
        float[] a1 = new float[HALF];
        float[] a2 = new float[HALF];
        System.arraycopy(arr, 0, a1, 0, HALF);
        System.arraycopy(arr, HALF, a2, 0, HALF);

        Thread t1 = new Thread(() -> transformArray(0, a1));
        Thread t2 = new Thread(() -> transformArray(HALF, a2));


        new Thread(() -> {
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
                System.arraycopy(a1, 0, arr, 0, HALF);
                System.arraycopy(a2, 0, arr, HALF, HALF);
                System.out.println("Время выполнения в двух потоках: " + (System.currentTimeMillis() - start));
                printArrayInfo(arr);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void transformArray(int delta, float array[]) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + (i + delta) / 5) * Math.cos(0.2f + (i + delta) / 5)
                    * Math.cos(0.4f + (i + delta) / 2));
        }
    }

    public static void printArrayInfo(float array[]) {
        System.out.println(new StringBuilder().append("Массив: size=").append(array.length).append(", ")
                .append(array[0]).append(", ").append(array[2]).append(", ").append(array[3]).append(", ")
                .append(array[array.length - 3]).append(", ").append(array[array.length - 2]).append(", ")
                .append(array[array.length - 1]));
    }
}