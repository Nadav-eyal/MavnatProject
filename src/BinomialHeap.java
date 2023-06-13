import java.util.HashSet;
import java.util.Set;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{


    public int size;
    public HeapNode last;
    public HeapNode min;

    /**
     *
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapItem.
     *
     */
    public HeapItem insert(int key, String info) {
        HeapItem item = new HeapItem(key, info);
        HeapNode node = new HeapNode();
        node.item = item;
        item.node = node;
        if (this.size == 0) {
            this.addHeapNode(node);
        }
        else {
            BinomialHeap tempHeap = new BinomialHeap();
            tempHeap.addHeapNode(node);
            this.meld(tempHeap);
        }
        return item;
    } // should be replaced by student code


    /**
     *
     * Delete the minimal item
     *
     */
    public void deleteMin()
    {
        BinomialHeap childrenHeap = this.createChildrenHeap(this.min);
        BinomialHeap detachedHeap = this.detachNode(this.min);
        childrenHeap.meld(detachedHeap);
        this.copyHeap(childrenHeap);
    }

    public void copyHeap(BinomialHeap heap)
    {
        this.min = heap.min;
        this.last = heap.last;
        this.size = heap.size;
    }

    public BinomialHeap createChildrenHeap(HeapNode node)
    {
        BinomialHeap childrenHeap = new BinomialHeap();
        // We don't have any children
        if (node.rank == 0)
            return new BinomialHeap();
        HeapNode current = node.child.next;
        HeapNode currentNext;

        int count = 0;
        while (count < node.rank)
        {
            currentNext = current.next;
            current.parent = null;
            childrenHeap.addHeapNode(current);
            current = currentNext;
            count ++;
        }

        return childrenHeap;
    }

    public BinomialHeap detachNode(HeapNode node)
    {
        BinomialHeap newHeap = new BinomialHeap();
        int numOfTrees = this.numTrees();
        int count = 0;
        HeapNode current = this.last.next;
        HeapNode currentNext;
        while (count < numOfTrees - 1)
        {
            currentNext = current.next;
            if (current != node)
            {
                newHeap.addHeapNode(current);
                count ++;
            }
            current = currentNext;
        }
        return newHeap;
    }

    /**
     *
     * Return the minimal HeapItem
     *
     */
    public HeapItem findMin()
    {
        return this.min.item;
    }

    /**
     *
     * pre: 0<diff<item.key
     *
     * Decrease the key of item by diff and fix the heap.
     *
     */
    public void decreaseKey(HeapItem item, int diff)
    {
        HeapNode current = item.node;
        item.key -= diff;
        while (current.parent != null)
        {
            if (item.key < current.parent.item.key) {
                current.item = current.parent.item;
                current.parent.item = item;
                current = current.parent;
                continue;
            }
            break;
        }
        // Updating the minimum
        if (item.key < this.min.item.key)
            this.min = item.node;
    }

    /**
     *
     * Delete the item from the heap.
     *
     */
    public void delete(HeapItem item)
    {
        int reduce = item.key - this.min.item.key + 1;
        this.decreaseKey(item, reduce);
        this.deleteMin();
    }

    /**
     *
     * Meld the heap with heap2
     *
     */
    public void meld(BinomialHeap heap2)
    {
        // If heap2 is empty
        if (heap2.size == 0)
            return;
        if (this.size == 0)
            this.copyHeap(heap2);
        else {
            BinomialHeap merged_heap = this.merge(heap2);
            BinomialHeap final_heap = merged_heap.unionTrees();
            this.copyHeap(final_heap);
        }
    }

    /**
     *
     * Return the number of elements in the heap
     *
     */
    public int size()
    {
        return this.size;
    }

    /**
     *
     * The method returns true if and only if the heap
     * is empty.
     *
     */
    public boolean empty()
    {
        if (this.size == 0)
            return true;
        return false; // should be replaced by student code
    }

    /**
     *
     * Return the number of trees in the heap.
     *
     */
    public int numTrees()
    {
        if (this.size == 0)
            return 0;

        int numOfTrees = 1;
        HeapNode current = this.last.next;
        while (current != this.last)
        {
            numOfTrees += 1;
            current = current.next;
        }
        return numOfTrees;
    }

    public BinomialHeap merge(BinomialHeap heap2) {
        BinomialHeap mergedHeap = new BinomialHeap();
        HeapNode currentHeap1 = this.last.next, currentHeap2 = heap2.last.next;
        int count1 = 0, count2 = 0;
        int numOfTrees1 = this.numTrees(), numOfTrees2 = heap2.numTrees();

        while (count1 < numOfTrees1 && count2 < numOfTrees2)
        {
            if (currentHeap1.rank <= currentHeap2.rank) {
                HeapNode nextHeap1 = currentHeap1.next;
                mergedHeap.addHeapNode(currentHeap1);
                currentHeap1 = nextHeap1;
                count1 += 1;
            }
            else
            {
                HeapNode nextHeap2 = currentHeap2.next;
                mergedHeap.addHeapNode(currentHeap2);
                currentHeap2 = nextHeap2;
                count2 += 1;
            }
        }
        while (count1 < numOfTrees1)
        {
            HeapNode nextHeap1 = currentHeap1.next;
            mergedHeap.addHeapNode(currentHeap1);
            currentHeap1 = nextHeap1;
            count1 += 1;
        }
        while (count2 < numOfTrees2)
        {
            HeapNode nextHeap2 = currentHeap2.next;
            mergedHeap.addHeapNode(currentHeap2);
            currentHeap2 = nextHeap2;
            count2 += 1;
        }
        return mergedHeap;
    }

    public BinomialHeap unionTrees() {
        BinomialHeap finalHeap = new BinomialHeap();

        int numTrees = this.numTrees();
        if (numTrees == 1)
            return this;
        if (numTrees == 2) {
            if (this.last.rank != this.last.next.rank)
                return this;
            else
            {
                HeapNode temp = HeapNode.link(this.last.next, this.last);
                finalHeap.addHeapNode(temp);
            }
        }
        HeapNode current = this.last.next;
        HeapNode next = current.next;

        while (finalHeap.size != this.size)
        {
            if (current.rank != next.rank)
            {
                finalHeap.addHeapNode(current);
                current = next;
                next = next.next;
            }

            else
            {
                if (current.rank == next.next.rank && next != this.last) {
                    finalHeap.addHeapNode(current);
                    current = next;
                    next = next.next;
                }
                else {
                    HeapNode nextNext = next.next;
                    HeapNode temp = HeapNode.link(current, next);
                    current = temp;
                    next = nextNext;
                }

            }
        }
        return finalHeap;
    }

        // Inserting a new binomial tree as the last root to an ordered heap
        public void addHeapNode(HeapNode node)
    {
        if (this.empty())
        {
            this.min = node;
            this.last = node;
            this.size = (int)Math.pow(2, node.rank);
            node.next = node;
        }
        else
        {
            if (node.item.key < this.min.item.key)
                this.min = node;
            node.next = this.last.next;
            this.last.next = node;
            this.last = node;
            this.size += (int)Math.pow(2, node.rank);
        }
    }

    /**
     * Class implementing a node in a Binomial Heap.
     *
     */
    public class HeapNode {
        public HeapItem item;
        public HeapNode child;
        public HeapNode next;
        public HeapNode parent;
        public int rank;

        public HeapNode() {
            this.item = null;
            this.child = null;
            this.next = null;
            this.parent = null;
            this.rank = 0;
        }

        public static HeapNode link(HeapNode x, HeapNode y)
        {
            if (x.item.key >= y.item.key)
            {
                HeapNode temp = x;
                x = y;
                y = temp;
            }
            if (x.rank == 0)
            {
                x.child = y;
                y.parent = x;
                x.rank += 1;
                x.next = x;
                y.next = y;
            }
            else
            {
                y.next = x.child.next;
                x.child.next = y;
                y.parent = x;
                x.child = y;
                // When linking two Bk's binomial trees the size increases by 1
                x.rank += 1;
            }
            return x;
        }
    }



    /**
     * Class implementing an item in a Binomial Heap.
     *
     */
    public class HeapItem{
        public HeapNode node;
        public int key;
        public String info;

        public HeapItem(int key, String info)
        {
            this.key = key;
            this.info = info;
            this.node = null;
        }
    }

    public void print() {
        System.out.println("Binomial Heap:");
        System.out.println("Size: " + size);

        if (min != null) {
            System.out.println("Minimum Node: " + min.item.key);
        } else {
            System.out.println("No minimum node.");
        }

        System.out.println("Heap Nodes:");
        if (last != null) {
            Set<HeapNode> visited = new HashSet<>();
            printHeapNode(last, 0, visited);
        } else {
            System.out.println("No heap nodes.");
        }
    }

    private void printHeapNode(HeapNode node, int indentLevel, Set<HeapNode> visited) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("    ");
        }

        System.out.println(indent + "Key: " + node.item.key);
        System.out.println(indent + "Info: " + node.item.info);
        System.out.println(indent + "Rank: " + node.rank);

        visited.add(node);

        if (node.child != null && !visited.contains(node.child)) {
            System.out.println(indent + "Child:");
            printHeapNode(node.child, indentLevel + 1, visited);
        }

        if (node.next != null && !visited.contains(node.next)) {
            System.out.println(indent + "Sibling:");
            printHeapNode(node.next, indentLevel, visited);
        }
    }



    public static void main(String[] args){
        BinomialHeap heap1 = new BinomialHeap();
        for (int i=0; i<1; i++) {
            heap1.insert(i, String.valueOf(i));
        }
        heap1.delete(heap1.last.item);

        heap1.print();
        System.out.println("NUM OF TREES " + heap1.numTrees());
    }
}
