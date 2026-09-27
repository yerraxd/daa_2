public class Tests {
    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean condition) {
        if (condition) passed++;
        else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    public static void main(String[] args) {
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
}