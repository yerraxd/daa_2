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
    }

    static void workload2Search() throws IOException {
    }

    static void workload3InsertRemove() throws IOException {
    }

    static void runInsertRemove(PrintWriter out, int n, String posLabel, int index) {
    }

    static void workload4Heap() throws IOException {
    }
}