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

5. Results
Workload 1 — Random Access
nDynamicArray avg time (ns)LinkedList avg time (ns)1001,054,50010,914,4201,000127,12018,556,28010,0004,253,680244,741,200100,000453,8802,622,807,820
DynamicArray maintains approximately constant-time indexed access, while LinkedList becomes substantially slower as n increases.
Workload 2 — Search
nDynamicArray avg time (ns)LinkedList avg time (ns)DynamicArray comparisonsLinkedList comparisons1002,430,9202,062,400100,000100,0001,0002,755,7604,202,8401,000,0001,000,00010,00023,743,36044,914,46010,000,00010,000,000100,000490,213,360735,975,000100,000,000100,000,000The number of comparisons grows linearly with n for both structures, which agrees with the theoretical O(n) search complexity.
Workload 3 — Insertion and Removal
nStructurePositionInsert (ns)Remove (ns)100DynamicArrayFront4,979,8805,621,220100LinkedListFront215,020101,360100DynamicArrayMiddle1,918,6401,966,360100LinkedListMiddle312,660234,4801,000DynamicArrayFront3,578,6003,586,2401,000LinkedListFront144,78093,3601,000DynamicArrayMiddle2,609,9402,614,2201,000LinkedListMiddle1,683,4401,696,70010,000DynamicArrayFront25,676,52025,031,20010,000LinkedListFront39,24025,44010,000DynamicArrayMiddle14,584,66014,682,94010,000LinkedListMiddle18,101,40017,252,740100,000DynamicArrayFront1,574,981,0401,766,355,880100,000LinkedListFront25,68013,260100,000DynamicArrayMiddle731,679,100679,223,140100,000LinkedListMiddle199,697,760264,210,160
The front position strongly favors LinkedList because insertion and removal at the head require constant work, while DynamicArray must shift many elements.
Workload 4 — Priority Processing
nInsert time (ns)ExtractMin time (ns)Insert comparisonsExtract comparisonsOrder correct100139,220201,852513,880100,000true1,000444,0602,2382,402,72015,001true10,0005,240,32022,6836,934,100216,600true100,00011,210,940228,142146,056,0202,831,649true




All extracted elements were in non-decreasing order, confirming that the heap maintained the required priority property.
6. Discussion
The experimental results generally agree with the theoretical analysis: DynamicArray get() remains effectively constant-time, while LinkedList get() becomes much slower as n increases. Search is O(n) for both structures, and the experiments show exactly the same number of comparisons, although their execution times differ because of implementation and memory-access overhead. Workload 3 also follows the expected behavior: LinkedList is much faster for insertion and removal at the front, while both structures require linear work for middle operations. Interestingly, the middle-position results show that similar O(n) behavior does not guarantee identical running times; at n = 100,000, for example, DynamicArray middle insertion took about 731.7 ms while LinkedList took about 199.7 ms. The Min-Heap results show increasing cost with larger inputs, while order_ok = true for every tested size confirms that extraction preserved non-decreasing order.
7. Design Recommendations
DynamicArray is appropriate for workloads requiring frequent indexed access because get(index) is O(1) and its contiguous storage provides efficient memory access. LinkedList is useful when frequent insertions and removals occur at the beginning of the structure. Min-Heap is appropriate for priority-based processing because it provides efficient minimum-element access and logarithmic insertion and extraction.
8. Conclusion
The experiments demonstrate that theoretical complexity is a useful predictor of performance, but it does not fully determine real execution time. The workloads showed clear differences between contiguous arrays, linked nodes, and heap-based organization. The results also demonstrated that two operations with the same asymptotic complexity can still have noticeably different practical running times because of implementation details and constant factors. Overall, the most suitable data structure depends on the operations performed most frequently.








