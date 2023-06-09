import java.awt.event.WindowFocusListener;

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
        BinomialHeap tempHeap = new BinomialHeap();
        tempHeap.addHeapNode(node);
        this.meld(tempHeap);

        return item;
    } // should be replaced by student code


    /**
     *
     * Delete the minimal item
     *
     */
    public void deleteMin()
    {
        return; // should be replaced by student code

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
        return; // should be replaced by student code
    }

    /**
     *
     * Delete the item from the heap.
     *
     */
    public void delete(HeapItem item)
    {
        return; // should be replaced by student code
    }

    /**
     *
     * Meld the heap with heap2
     *
     */
    public void meld(BinomialHeap heap2)
    {
        BinomialHeap merged_heap = this.merge(heap2);
        BinomialHeap final_heap = merged_heap.unionTrees();
        this.last =  final_heap.last;
        this.size =  final_heap.size;
        this.min =  final_heap.min;


        return; // should be replaced by student code
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


    public static void main(String[] args){
        BinomialHeap heap1 = new BinomialHeap();
        BinomialHeap heap2 = new BinomialHeap();
        HeapNode node1 = heap1.new HeapNode();
        HeapItem item1 = heap1.new HeapItem(1, "hello");
        node1.item = item1;
        item1.node = node1;
        HeapNode node2 = heap2.new HeapNode();
        HeapItem item2 = heap2.new HeapItem(2, "saamak");
        node2.item = item2;
        item2.node = node2;
        heap1.addHeapNode(node1);
        heap2.addHeapNode(node2);

        BinomialHeap heap3 = new BinomialHeap();
        HeapNode node3 = heap3.new HeapNode();
        HeapItem item3 = heap1.new HeapItem(3, "zayen");
        node3.item = item3;
        item3.node = node3;
        heap3.addHeapNode(node3);


        heap1.meld(heap2);
        heap1.insert(6, "hi");
        heap1.insert(7, "hipify");
        //heap3.meld(heap1);
        //System.out.println(heap3.size + " " + heap3.min.item.key + " " + heap3.last.child.parent.item.key);

        heap3.insert(0, "failure");
        heap3.insert(4, "another");
        heap3.insert(5, "boom");
        heap3.insert(8, "p");
        heap3.insert(9, "f");
        heap3.insert(10, "d");
        heap3.insert(-1, "d");



        System.out.println(heap3.size + " " + heap3.min.item.key + " " + heap3.last.child.item.key + " " + heap3.numTrees());




    }
}
