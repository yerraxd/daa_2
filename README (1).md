# Assignment 2 - Dynamic Array, Linked List, Min-Heap

## 1. Overview

This project implements three data structures in Java: DynamicArray,
LinkedList (singly linked), and MinHeap (binary min-heap). Each structure
implements the core operations (add, add(index,x), remove(index),
get(index), contains(x) for the first two; insert, peekMin, extractMin
for the heap). Public counters comparisons and movements were added to
each class to measure algorithmic work directly, not just wall-clock
time, so the measured cost can be compared against the theoretical
complexity.

## 2. Complexity Analysis

| Structure | Operation | Best | Average | Worst | Aux. space |
|---|---|---|---|---|---|
| DynamicArray | add(x) | O(1) | O(1) amortized | O(n) on resize | O(1) amortized |
| DynamicArray | add(index,x) | O(1) at index = size | O(n) | O(n) at index = 0 | O(1) |
| DynamicArray | remove(index) | O(1) at index = size-1 | O(n) | O(n) at index = 0 | O(1) |
| DynamicArray | get(index) | O(1) | O(1) | O(1) | O(1) |
| DynamicArray | contains(x) | O(1) | O(n) | O(n) | O(1) |
| LinkedList | add(x) append | O(1) | O(1) | O(1) | O(1) |
| LinkedList | add(index,x) | O(1) at index = 0 | O(n) | O(n) at index = size | O(1) |
| LinkedList | remove(index) | O(1) at index = 0 | O(n) | O(n) | O(1) |
| LinkedList | get(index) | O(1) at index = 0 | O(n) | O(n) | O(1) |
| LinkedList | contains(x) | O(1) | O(n) | O(n) | O(1) |
| MinHeap | insert(x) | O(1) | O(log n) | O(log n) | O(1) |
| MinHeap | peekMin() | O(1) | O(1) | O(1) | O(1) |
| MinHeap | extractMin() | O(1) | O(log n) | O(log n) | O(1) |

Explanation:

get(index) on DynamicArray is always O(1) because it is a direct array
access, no case makes it slower.

add(index,x) and remove(index) on DynamicArray shift every element after
index by one position, so the cost equals size - index. Best case is at
the end (0 shifts), worst case is at index 0 (n shifts).

LinkedList is the opposite: get/add/remove by index requires walking
links from the head, so it costs O(index). add(x) at the end is O(1)
because of the tail pointer.

contains(x) is a linear scan for both structures, O(n) in both average
and worst case, but with a different constant factor (see Discussion).

insert/extractMin on MinHeap touch at most one root to leaf path in a
tree of height log(n), which gives O(log n).

## 3. Correctness, loop invariant proofs

### 3.1 DynamicArray.add(int index, Object data)

```java
for(int i = size; i>index;i--){
    array[i] = array[i-1];
}
array[index] = data;
```

Invariant: before each loop iteration, positions index through i-1 still
hold their original values, and positions i+1 through size already hold
the value that used to be one position to their left (already shifted).

Initialization: before the first iteration i = size. The range i+1..size
is empty, so that part holds trivially. The range index..size-1 is
untouched, which matches the invariant since the loop has not run yet.

Maintenance: assume the invariant holds at the start of an iteration with
i > index. The line array[i] = array[i-1] copies the value at position
i-1 (untouched by the invariant) into position i. After that, position i
holds the shifted value and i decreases by one, so the invariant holds
again for the new i.

Termination: the loop stops when i = index. At that point, by the
invariant, positions index+1 through size hold exactly the original
values from index through size-1, shifted right by one. Position index
itself was never touched by the loop, so array[index] = data places the
new element there without losing any data. This is exactly what a
correct insertion at index requires.

### 3.2 MinHeap siftDown, used inside extractMin

```java
private void siftDown(int index){
    while (true) {
        int left = index * 2 + 1;
        int right = index * 2 + 2;
        int smallest = index;
        if (left < size && compare(array[left], array[smallest]) < 0) smallest = left;
        if (right < size && compare(array[right], array[smallest]) < 0) smallest = right;
        if (smallest == index) break;
        swap(index, smallest);
        index = smallest;
    }
}
```

Invariant: before each iteration, every subtree except the one rooted at
the current index already satisfies the heap property (parent <= both
children). The only place the property can still be violated is between
index and its own children.

Initialization: before the first iteration index = 0, the root. The root
has no parent, so the invariant holds trivially, the only place the heap
property was broken after removing the old root is exactly at index 0.

Maintenance: the loop finds smallest among index and its up to two
children. If smallest == index, the loop breaks, meaning index already
satisfies the property. Otherwise swap(index, smallest) moves the smaller
child up into index (now correct) and moves the larger value down into
smallest, which becomes the new index for the next iteration. Every other
subtree is untouched by the swap, so it stays valid by the invariant.

Termination: index strictly increases while moving down the tree, and the
tree height is log(size), so the loop must stop within O(log n) steps,
either because it reached a leaf or because smallest == index earlier. At
that point, by the maintenance argument, the whole array satisfies the
min-heap property, which is exactly the postcondition extractMin needs.

## 4. Experimental Setup

n (initial number of elements): 100, 1000, 10000, 100000

m (operations per timed section): 10000 get() calls for Workload 1, 1000
contains() calls for Workload 2, 1000 insertions and 1000 removals at two
positions for Workload 3, n insert/extractMin calls for Workload 4.

Repetitions: 5 runs per data point, average reported.

Timing method: System.nanoTime() around the timed section only, input
generation happens before timing starts and is excluded.

Random seed: new Random(42) for the base dataset.

Workloads: as defined in the assignment brief (Random Access, Search,
Insertion/Removal at front and middle, Priority Processing), implemented
in src/Benchmark.java.

To run:
```
cd src
javac *.java
java Tests
java Benchmark
```
Benchmark writes CSV files to results/csv/.

## 5. Results

[PASTE YOUR TABLES HERE, one per workload, columns: n, structure, avg
time, metric (accesses/comparisons/movements), theoretical complexity]

[PASTE PLOTS HERE: time vs n, and operations vs n, for each workload]

## 6. Discussion

Random Access (W1) should match theory closely: DynamicArray.get() stays
flat across n because it is O(1), LinkedList.get() grows close to
linearly because it is O(n).

Search (W2) comparison counts should match exactly, since both structures
scan every element when the searched value is not present. Wall-clock
time still differs because of constant factors (see below).

Insertion/removal (W3) is the clearest case where two structures with the
same Big-O can behave very differently: at the middle index both are
Theta(n), but DynamicArray tends to be faster in practice because it
shifts contiguous memory (cache friendly), while LinkedList follows
pointers to separately allocated nodes (cache misses). Same complexity
class, different constant factor.

MinHeap (W4): comparisons should scale with n * log(n) overall, and the
extracted sequence must always be non-decreasing, matching what Tests.java
already validated.

[FILL IN YOUR ACTUAL NUMBERS AND OBSERVATIONS HERE ONCE YOU HAVE THE
BENCHMARK OUTPUT]

## 7. Design Recommendations

DynamicArray is better when random access by index dominates, or most
insertions/removals happen near the end.

LinkedList is useful when insertions/removals happen mostly at the front,
and indexed access is rare.

A heap is appropriate for priority based processing because it gives
O(log n) insert and O(log n) extract-min without needing a full sort,
which matches how a priority queue is actually used.

The right structure depends on the workload shape, not just the data.

## 8. Conclusion

[SUMMARIZE YOUR ACTUAL RESULTS HERE: which theoretical predictions were
confirmed, where the middle-insert case showed same Big-O but different
real speed, and what that means about constant factors in practice]
