import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {

    static final int[] SIZES = {100, 1000, 10000, 100000};
    static final int REPEATS = 5;
    static final long SEED = 42;
    static final String OUT_DIR = "results/csv/";

    public static void main(String[] args) throws IOException {
        new java.io.File(OUT_DIR).mkdirs();

        workload1RandomAccess();
        workload2Search();
        workload3InsertRemove();
        workload4Heap();

        System.out.println("Done. CSV files in " + OUT_DIR);
    }

    static void workload1RandomAccess() throws IOException {
        PrintWriter out = new PrintWriter(
                new FileWriter(OUT_DIR + "workload1_random_access.csv")
        );

        out.println("structure,n,avg_time_ns,accesses");

        for (int n : SIZES) {
            double arrTime = 0;
            double listTime = 0;

            for (int rep = 0; rep < REPEATS; rep++) {

                Random dataRnd = new Random(SEED);

                DynamicArray arr = new DynamicArray();
                LinkedList list = new LinkedList();

                for (int i = 0; i < n; i++) {
                    int v = dataRnd.nextInt();

                    arr.add(v);
                    list.add(v);
                }

                Random idxRnd = new Random(SEED + rep + 1);

                int[] indices = new int[10000];

                for (int i = 0; i < indices.length; i++) {
                    indices[i] = idxRnd.nextInt(n);
                }

                long t0 = System.nanoTime();

                for (int idx : indices) {
                    arr.get(idx);
                }

                long t1 = System.nanoTime();

                arrTime += (t1 - t0);

                long t2 = System.nanoTime();

                for (int idx : indices) {
                    list.get(idx);
                }

                long t3 = System.nanoTime();

                listTime += (t3 - t2);
            }

            out.printf(
                    "DynamicArray,%d,%.1f,%d%n",
                    n,
                    arrTime / REPEATS,
                    10000
            );

            out.printf(
                    "LinkedList,%d,%.1f,%d%n",
                    n,
                    listTime / REPEATS,
                    10000
            );

            System.out.println("W1 n=" + n + " done");
        }

        out.close();
    }

    static void workload2Search() throws IOException {
        PrintWriter out = new PrintWriter(
                new FileWriter(OUT_DIR + "workload2_search.csv")
        );

        out.println("structure,n,avg_time_ns,avg_comparisons");

        for (int n : SIZES) {
            double arrTime = 0;
            double listTime = 0;

            long arrComp = 0;
            long listComp = 0;

            for (int rep = 0; rep < REPEATS; rep++) {

                Random dataRnd = new Random(SEED);

                DynamicArray arr = new DynamicArray();
                LinkedList list = new LinkedList();

                for (int i = 0; i < n; i++) {
                    int v = dataRnd.nextInt();

                    arr.add(v);
                    list.add(v);
                }

                Random valRnd = new Random(SEED + rep + 1);

                int[] values = new int[1000];

                for (int i = 0; i < values.length; i++) {
                    values[i] = valRnd.nextInt();
                }

                arr.resetCounters();

                long t0 = System.nanoTime();

                for (int v : values) {
                    arr.contains(v);
                }

                long t1 = System.nanoTime();

                arrTime += (t1 - t0);
                arrComp += arr.comparisons;


                list.resetCounters();

                long t2 = System.nanoTime();

                for (int v : values) {
                    list.contains(v);
                }

                long t3 = System.nanoTime();

                listTime += (t3 - t2);
                listComp += list.comparisons;
            }

            out.printf(
                    "DynamicArray,%d,%.1f,%d%n",
                    n,
                    arrTime / REPEATS,
                    arrComp / REPEATS
            );

            out.printf(
                    "LinkedList,%d,%.1f,%d%n",
                    n,
                    listTime / REPEATS,
                    listComp / REPEATS
            );

            System.out.println("W2 n=" + n + " done");
        }

        out.close();
    }

    static void workload3InsertRemove() throws IOException {
        PrintWriter out = new PrintWriter(
                new FileWriter(OUT_DIR + "workload3_insert_remove.csv")
        );

        out.println(
                "structure,n,position,operation,avg_time_ns,avg_movements"
        );

        for (int n : SIZES) {

            runInsertRemove(out, n, "front", 0);
            runInsertRemove(out, n, "middle", n / 2);

            System.out.println("W3 n=" + n + " done");
        }

        out.close();
    }

    static void runInsertRemove(
            PrintWriter out,
            int n,
            String posLabel,
            int index
    ) {
        double arrInsTime = 0;
        double arrRemTime = 0;

        double listInsTime = 0;
        double listRemTime = 0;

        long arrInsMove = 0;
        long arrRemMove = 0;

        long listInsMove = 0;
        long listRemMove = 0;

        for (int rep = 0; rep < REPEATS; rep++) {

            Random dataRnd = new Random(SEED);

            DynamicArray arr = new DynamicArray();
            LinkedList list = new LinkedList();

            for (int i = 0; i < n; i++) {
                int v = dataRnd.nextInt();

                arr.add(v);
                list.add(v);
            }

            int idx = index;

            arr.resetCounters();

            long t0 = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                arr.add(idx, i);
            }

            long t1 = System.nanoTime();

            arrInsTime += (t1 - t0);
            arrInsMove += arr.movements;


            list.resetCounters();

            long t2 = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                list.add(idx, i);
            }

            long t3 = System.nanoTime();

            listInsTime += (t3 - t2);
            listInsMove += list.movements;

            arr.resetCounters();

            long t4 = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                arr.remove(idx);
            }

            long t5 = System.nanoTime();

            arrRemTime += (t5 - t4);
            arrRemMove += arr.movements;

            list.resetCounters();

            long t6 = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                list.remove(idx);
            }

            long t7 = System.nanoTime();

            listRemTime += (t7 - t6);
            listRemMove += list.movements;
        }

        out.printf(
                "DynamicArray,%d,%s,insert,%.1f,%d%n",
                n,
                posLabel,
                arrInsTime / REPEATS,
                arrInsMove / REPEATS
        );

        out.printf(
                "DynamicArray,%d,%s,remove,%.1f,%d%n",
                n,
                posLabel,
                arrRemTime / REPEATS,
                arrRemMove / REPEATS
        );

        out.printf(
                "LinkedList,%d,%s,insert,%.1f,%d%n",
                n,
                posLabel,
                listInsTime / REPEATS,
                listInsMove / REPEATS
        );

        out.printf(
                "LinkedList,%d,%s,remove,%.1f,%d%n",
                n,
                posLabel,
                listRemTime / REPEATS,
                listRemMove / REPEATS
        );
    }

    static void workload4Heap() throws IOException {
        PrintWriter out = new PrintWriter(
                new FileWriter(OUT_DIR + "workload4_heap.csv")
        );

        out.println(
                "n,avg_insert_time_ns,avg_extract_time_ns," +
                        "avg_insert_comparisons,avg_extract_comparisons,order_ok"
        );

        for (int n : SIZES) {

            double insTime = 0;
            double extTime = 0;

            long insComp = 0;
            long extComp = 0;

            boolean orderOk = true;

            for (int rep = 0; rep < REPEATS; rep++) {

                Random dataRnd = new Random(SEED + rep);

                int[] values = new int[n];

                for (int i = 0; i < n; i++) {
                    values[i] = dataRnd.nextInt();
                }

                MinHeap heap = new MinHeap();

                heap.resetCounters();

                long t0 = System.nanoTime();

                for (int v : values) {
                    heap.insert(v);
                }

                long t1 = System.nanoTime();

                insTime += (t1 - t0);
                insComp += heap.comparisons;


                heap.resetCounters();

                int prev = Integer.MIN_VALUE;

                long t2 = System.nanoTime();

                for (int i = 0; i < n; i++) {

                    int m = (Integer) heap.extractMin();

                    if (m < prev) {
                        orderOk = false;
                    }

                    prev = m;
                }

                long t3 = System.nanoTime();

                extTime += (t3 - t2);
                extComp += heap.comparisons;
            }

            out.printf(
                    "%d,%.1f,%.1f,%d,%d,%b%n",
                    n,
                    insTime / REPEATS,
                    extTime / REPEATS,
                    insComp / REPEATS,
                    extComp / REPEATS,
                    orderOk
            );

            System.out.println("W4 n=" + n + " done");
        }

        out.close();
    }
}