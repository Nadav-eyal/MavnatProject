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
//    public HeapItem insert(int key, String info) {
//        return;
//    } // should be replaced by student code


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
                mergedHeap.addHeapNode(currentHeap1);
                currentHeap1 = currentHeap1.next;
                count1 += 1;
            }
            else
            {
                mergedHeap.addHeapNode(currentHeap2);
                currentHeap2 = currentHeap2.next;
                count2 += 1;
            }
        }
        while (count1 < numOfTrees1)
        {
            mergedHeap.addHeapNode(currentHeap1);
            currentHeap1 = currentHeap1.next;
            count1 += 1;
        }
        while (count2 < numOfTrees2)
        {
            mergedHeap.addHeapNode(currentHeap2);
            currentHeap2 = currentHeap2.next;
            count2 += 1;
        }
        return mergedHeap;
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

        public HeapNode link(HeapNode x, HeapNode y)
        {
            if (x.item.key >= y.item.key)
            {
                HeapNode temp = x;
                x = y;
                y = temp;
            }
            y.next = x.child.next;
            x.child.next = y;
            y.parent = x;
            x.child = y;
            // When linking two Bk's binomial trees the size increases by 1
            x.rank += 1;
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
        System.out.println(heap1.size + " " + heap1.last.item.key + " " + heap1.min.item.info);
        System.out.println(heap2.size + " " + heap2.last.item.key + " " + heap2.min.item.info);
        BinomialHeap mergedHeap = heap2.merge(heap1);
        System.out.println(mergedHeap.size + " " + mergedHeap.last.item.key + " " + mergedHeap.min.item.info);

    }
}
