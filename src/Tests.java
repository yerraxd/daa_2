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
    static void testDynamicArrayBasic() {
        DynamicArray a = new DynamicArray();
        check("da empty isEmpty", a.isEmpty());
        check("da empty contains", !a.contains(1));

        boolean threw = false;
        try { a.get(0); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("da empty get throws", threw);

        threw = false;
        try { a.remove(0); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("da empty remove throws", threw);

        a.add(42);
        check("da one get", a.get(0).equals(42));
        check("da one contains", a.contains(42));

        threw = false;
        try { a.get(-1); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("da get -1 throws", threw);

        threw = false;
        try { a.get(1); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("da get size throws", threw);

        DynamicArray b = new DynamicArray();
        for (int i = 0; i < 10; i++) b.add(i % 3);
        check("da contains duplicate", b.contains(2));
        check("da contains missing", !b.contains(5));

        b.add(0, -1);
        check("da insert front", b.get(0).equals(-1));

        b.add(11, 100);
        check("da insert end", b.get(11).equals(100));

        b.remove(0);
        check("da remove front", b.get(0).equals(0));
    }

    static void testDynamicArrayLarge() {
        DynamicArray big = new DynamicArray();
        java.util.Random rnd = new java.util.Random(42);
        for (int i = 0; i < 20000; i++) big.add(rnd.nextInt());
        check("da large get not null", big.get(10000) != null);
        for (int i = 0; i < 19999; i++) big.remove(0);
        check("da shrink not empty yet", !big.isEmpty());
        big.remove(0);
        check("da all removed", big.isEmpty());
    }

    static void testLinkedListBasic() {
        LinkedList a = new LinkedList();
        check("ll empty isEmpty", a.isEmpty());
        check("ll empty contains", !a.contains(1));

        boolean threw = false;
        try { a.get(0); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("ll empty get throws", threw);

        threw = false;
        try { a.remove(0); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("ll empty remove throws", threw);

        a.add(7);
        check("ll one get", a.get(0).equals(7));

        threw = false;
        try { a.get(-1); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("ll get -1 throws", threw);

        threw = false;
        try { a.get(1); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("ll get size throws", threw);

        LinkedList b = new LinkedList();
        for (int i = 0; i < 10; i++) b.add(i % 3);
        check("ll contains duplicate", b.contains(2));
        check("ll contains missing", !b.contains(5));

        b.add(0, -1);
        check("ll insert front", b.get(0).equals(-1));

        b.add(11, 100);
        check("ll insert end", b.get(11).equals(100));

        b.remove(0);
        check("ll remove front", b.get(0).equals(0));
    }
    
    static void testLinkedListLarge() {
        LinkedList big = new LinkedList();
        java.util.Random rnd = new java.util.Random(42);
        for (int i = 0; i < 20000; i++) big.add(rnd.nextInt());
        check("ll large get not null", big.get(10000) != null);
    }

    public static void main(String[] args) {
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
}