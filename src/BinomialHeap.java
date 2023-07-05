
import java.util.*;

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

    public int numOfTrees;

    /**
     *
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapItem.
     *
     */

    /**
     * Time complexity: O(logn)
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
            tempHeap.numOfTrees = 1;
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

    /**
     * Time complexity: O(logn)
     */
    public void deleteMin()
    {
        BinomialHeap childrenHeap = this.createChildrenHeap(this.min);
        BinomialHeap detachedHeap = this.detachNode(this.min);
        childrenHeap.meld(detachedHeap);
        this.copyHeap(childrenHeap);
    }

    /**
     * Time complexity: O(1)
     */

    public void copyHeap(BinomialHeap heap)
    {
        this.min = heap.min;
        this.last = heap.last;
        this.size = heap.size;
        this.numOfTrees = heap.numOfTrees;
    }

    /**
     * Time complexity: O(logn)
     */
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


    /**
     * Time complexity: O(logn)
     */
    public BinomialHeap detachNode(HeapNode node)
    {
        BinomialHeap newHeap = new BinomialHeap();
        int numOfTrees = this.numOfTrees;
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

    /**
     * Time complexity: O(1)
     */
    public HeapItem findMin()
    {
        if (this.min.item == null)
            return null;
        return this.min.item;
    }

    /**
     *
     * pre: 0<diff<item.key
     *
     * Decrease the key of item by diff and fix the heap.
     *
     */

    /**
     * Time complexity: O(logn)
     */
    public void decreaseKey(HeapItem item, int diff)
    {
        HeapNode current = item.node;
        item.key -= diff;
        while (current.parent != null)
        {
            if (item.key < current.parent.item.key) {
                current.item = current.parent.item;
                current.parent.item.node = current;
                current.parent.item = item;
                item.node = current.parent;
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

    /**
     * Time complexity: O(logn)
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

    /**
     * Time complexity: O(logn)
     */
    public void meld(BinomialHeap heap2)
    {
        // If heap2 is empty
        if (heap2.size == 0)
            return;
        if (this.size == 0)
            this.copyHeap(heap2);
        else {
            int unionUntilRank = this.merge(heap2);
            this.unionTrees(unionUntilRank);
        }
    }


    /**
     *
     * Return the number of elements in the heap
     *
     */

    /**
     * Time complexity: O(1)
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

    /**
     * Time complexity: O(1)
     */
    public boolean empty()
    {
        if (this.size == 0)
            return true;
        return false;
    }

    /**
     *
     * Return the number of trees in the heap.
     *
     */

    /**
     * Time complexity: O(1)
     */
    public int numTrees()
    {
        return this.numOfTrees;
    }


    /**
     * Time complexity: O(logn)
     */
    public int merge(BinomialHeap heap2) {

        BinomialHeap mergedHeap = new BinomialHeap();
        BinomialHeap smallRankHeap;
        BinomialHeap bigRankHeap;

        if(this.last.rank > heap2.last.rank){
            smallRankHeap = heap2;
            bigRankHeap = this;
        }
        else{
            smallRankHeap = this;
            bigRankHeap = heap2;
        }

        int newSize = smallRankHeap.size + bigRankHeap.size;
        int newNumOfTrees = smallRankHeap.numOfTrees + bigRankHeap.numOfTrees;
        int smallHeapCounter = 0;
        int smallRankHeapMaxTreeRank = smallRankHeap.last.rank;
        HeapNode currentSmallHeapTree = smallRankHeap.last.next;
        HeapNode currentBigHeapTree = bigRankHeap.last.next;

        while (smallHeapCounter < smallRankHeap.numTrees())
        {
            if(currentSmallHeapTree.rank <= currentBigHeapTree.rank){
                HeapNode nextSmallHeapTree = currentSmallHeapTree.next;
                mergedHeap.addHeapNode(currentSmallHeapTree);
                currentSmallHeapTree = nextSmallHeapTree;
                smallHeapCounter += 1;
            }
            else{
                HeapNode nextBigHeapTree = currentBigHeapTree.next;
                mergedHeap.addHeapNode(currentBigHeapTree);
                currentBigHeapTree = nextBigHeapTree;
            }

        }

        // now mergedHeap contains the merged start of big with all of small heap
        HeapNode newMin;
        if (mergedHeap.min.item.key <= bigRankHeap.min.item.key){
            newMin = mergedHeap.min;
        }
        else{
            newMin = bigRankHeap.min;
        }
        HeapNode firstMergedHeapNode = mergedHeap.last.next;
        mergedHeap.last.next = currentBigHeapTree;
        mergedHeap.last = bigRankHeap.last;
        mergedHeap.last.next = firstMergedHeapNode;
        this.last = mergedHeap.last;
        this.min = newMin;
        this.size = newSize;
        this.numOfTrees = newNumOfTrees;
        return smallRankHeapMaxTreeRank; // we will use this in unionTrees
    }

    /**
     * Time complexity: O(logn)
     */
    public void unionTrees(int unionUntilRank) {
        BinomialHeap unionedHeap = new BinomialHeap();
        int numTrees = this.numTrees();
        if (numTrees == 1)
            return;
        if (numTrees == 2) {
            if (this.last.rank != this.last.next.rank)
                return;
            else
            {
                HeapNode temp = HeapNode.link(this.last.next, this.last);
                unionedHeap.addHeapNode(temp);
                this.copyHeap(unionedHeap);
            }
            return;
        }

        HeapNode current = this.last.next;
        HeapNode next = current.next;
        while (((current.rank <= unionUntilRank) || (current.rank == next.rank)) && (current != this.last))
        {
            if (current.rank != next.rank)
            {
                unionedHeap.addHeapNode(current);
                current = next;
                next = next.next;
            }

            //there are 3 trees with the same rank
            else if (next != current && next.next != current && current.rank == next.next.rank && next != this.last)
            {
                //in this case we can add the current one and union the next two
                unionedHeap.addHeapNode(current);
                current = next;
                next = next.next;
            }
            //there are 2 trees with the same rank and the third one is of bigger rank, or they are the only trees.
            else if(next != current)
            {
                // if we are at the end of the heap list (there are only two trees left)
                if (next == this.last)
                {
                    BinomialHeap lastTreeHeap = new BinomialHeap();
                    lastTreeHeap.addHeapNode(HeapNode.link(current, next));
                    this.last = lastTreeHeap.last;
                    this.numOfTrees--;
                    current = this.last;
                    break;
                }
                // there are at least three trees in the heap, and current and next are of same rank
                else
                {
                    HeapNode nextNext = next.next; // save this because link changes next's!
                    current = HeapNode.link(current, next);
                    current.next = nextNext; // we do so that current will remain a valid start for the rest of the heap list.
                    next = current.next;
                    this.numOfTrees--;
                }
            }
        }
        // now concat unioned heap and the rest of this.heap (unionedHeap -> current -> this.Heap)
        if (unionedHeap.empty())
        {
            this.last.next = current;
            return;
        }
        HeapNode firstNode = unionedHeap.last.next;
        unionedHeap.last.next = current;
        this.last.next = firstNode;
    }

    /**
     * Time complexity: O(1)
     */
    // Inserting a new binomial tree as the last root to an ordered heap
    public void addHeapNode(HeapNode node)
    {
        if (this.empty())
        {
            this.min = node;
            this.last = node;
            this.size = (int)Math.pow(2, node.rank);
            this.numOfTrees = 1;
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
            this.numOfTrees++;
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

        public void printNodeList(){
            int maxNum = 20;
            int count =0;
            int curr_rank = 0;
            HeapNode curr = this;
            Set<HeapNode> seenNodes = new HashSet<>();
            do{
                System.out.print("b" + curr.rank + " -> ");
                seenNodes.add(curr);
                curr = curr.next;
                count++;
                if(count>maxNum){
                    System.out.println("error - more than " +  maxNum + " nodes");
                    return;
                }

                //for cases where the list is not really loop
            } while(!seenNodes.contains(curr));
            System.out.print("|");

            if(curr!=this)
            {
                System.out.println("error - did not finished on the starting node...");
            }
            System.out.println();
        }


        /**
         * Time complexity: O(1)
         */
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



    public static void main(String[] args) {


//        BinomialHeap heap1 = new BinomialHeap();
//        for (int i=0; i<128; i++) {
//            heap1.insert(10-i, String.valueOf(i));
//        }
//        heap1.print();
//        heap1.last.next.printNodeList();
//        System.out.println(heap1.numOfTrees);

        /*
        BinomialHeap heap2 = new BinomialHeap();
        for (int i=0; i<16; i++) {
            //System.out.println("inserting " + (i));
            heap2.insert(i, String.valueOf(i));
        }



         */

//        BinomialHeap heap3 = new BinomialHeap();
//        heap3.insert(3, "a");
//        heap3.insert(2, "a");
//        heap3.insert(1, "a");
//        heap3.insert(0, "a");
//        heap3.print();
//        System.out.println(heap3.last.next.item.key);

        int[] insertions = { 5, 6, 7, 8, 4, 3, 2, 11 };
        HashMap<Integer, BinomialHeap.HeapItem> keyToNode = new HashMap<>();
        BinomialHeap bh = new BinomialHeap();
        for (int i : insertions) {
            keyToNode.put(i, bh.insert(i, null));
        }

        bh.delete(keyToNode.get(4));
//        bh.print();
//        bh.last.next.printNodeList();
//        System.out.println(bh.numOfTrees);
        bh.delete(keyToNode.get(8));

        bh.print();
        bh.last.next.printNodeList();
        System.out.println(bh.numOfTrees);
    }
}
