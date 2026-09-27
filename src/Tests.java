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

    static void testMinHeapBasic() {
        MinHeap h = new MinHeap();
        check("mh empty isEmpty", h.isEmpty());

        boolean threw = false;
        try { h.peekMin(); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("mh empty peekMin throws", threw);

        threw = false;
        try { h.extractMin(); } catch (IndexOutOfBoundsException e) { threw = true; }
        check("mh empty extractMin throws", threw);

        h.insert(5);
        check("mh one peekMin", h.peekMin().equals(5));
        check("mh one extractMin", h.extractMin().equals(5));
        check("mh after extract isEmpty", h.isEmpty());

        int[] values = {5, 3, 8, 1, 9, 1, 3, -4, 7, 0};
        MinHeap h2 = new MinHeap();
        for (int v : values) h2.insert(v);

        int prev = Integer.MIN_VALUE;
        boolean nonDecreasing = true;
        int count = 0;
        while (!h2.isEmpty()) {
            int m = (Integer) h2.extractMin();
            if (m < prev) nonDecreasing = false;
            prev = m;
            count++;
        }
        check("mh extractMin non-decreasing", nonDecreasing);
        check("mh extractMin returned all", count == values.length);
    }

    static void testMinHeapLarge() {
        MinHeap big = new MinHeap();
        java.util.Random rnd = new java.util.Random(42);
        for (int i = 0; i < 20000; i++) big.insert(rnd.nextInt());
        int last = Integer.MIN_VALUE;
        boolean ok = true;
        for (int i = 0; i < 20000; i++) {
            int m = (Integer) big.extractMin();
            if (m < last) ok = false;
            last = m;
        }
        check("mh large extract non-decreasing", ok);
    }
    static void crossValidateDynamicArray() {
        java.util.Random rnd = new java.util.Random(42);
        DynamicArray da = new DynamicArray();
        java.util.ArrayList<Integer> ref = new java.util.ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            int op = rnd.nextInt(4);
            if (op == 0 || ref.isEmpty()) {
                int v = rnd.nextInt(1000);
                da.add(v);
                ref.add(v);
            } else if (op == 1) {
                int idx = rnd.nextInt(ref.size() + 1);
                int v = rnd.nextInt(1000);
                da.add(idx, v);
                ref.add(idx, v);
            } else if (op == 2 && !ref.isEmpty()) {
                int idx = rnd.nextInt(ref.size());
                da.remove(idx);
                ref.remove(idx);
            }
        }
        boolean allMatch = true;
        for (int i = 0; i < ref.size(); i++) {
            if (!da.get(i).equals(ref.get(i))) allMatch = false;
        }
        check("da matches ArrayList", allMatch);
    }

    static void crossValidateLinkedList() {
        java.util.Random rnd = new java.util.Random(42);
        LinkedList la = new LinkedList();
        java.util.LinkedList<Integer> lref = new java.util.LinkedList<>();
        for (int i = 0; i < 5000; i++) {
            int op = rnd.nextInt(4);
            if (op == 0 || lref.isEmpty()) {
                int v = rnd.nextInt(1000);
                la.add(v);
                lref.add(v);
            } else if (op == 1) {
                int idx = rnd.nextInt(lref.size() + 1);
                int v = rnd.nextInt(1000);
                la.add(idx, v);
                lref.add(idx, v);
            } else if (op == 2 && !lref.isEmpty()) {
                int idx = rnd.nextInt(lref.size());
                la.remove(idx);
                lref.remove(idx);
            }
        }
        boolean allMatch = true;
        for (int i = 0; i < lref.size(); i++) {
            if (!la.get(i).equals(lref.get(i))) allMatch = false;
        }
        check("ll matches java.util.LinkedList", allMatch);
    }

    static void crossValidateMinHeap() {
        java.util.Random rnd = new java.util.Random(42);
        MinHeap heap = new MinHeap();
        java.util.PriorityQueue<Integer> pq = new java.util.PriorityQueue<>();
        for (int i = 0; i < 5000; i++) {
            int v = rnd.nextInt(100000);
            heap.insert(v);
            pq.add(v);
        }
        boolean sequenceMatches = true;
        while (!pq.isEmpty()) {
            int expected = pq.poll();
            int actual = (Integer) heap.extractMin();
            if (expected != actual) sequenceMatches = false;
        }
        check("mh matches PriorityQueue", sequenceMatches);
    }

    public static void main(String[] args) {
        testDynamicArrayBasic();
        testDynamicArrayLarge();
        testLinkedListBasic();
        testLinkedListLarge();
        testMinHeapBasic();
        testMinHeapLarge();
        crossValidateDynamicArray();
        crossValidateLinkedList();
        crossValidateMinHeap();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
}