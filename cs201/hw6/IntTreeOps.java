public class IntTreeOps {
        
    // Operations built on top of the IntTree class
                
    // Accumulators
        
    public static int height(IntTree t) {
        // returns the height of the tree t
        if (isEmpty(t)) {
            return 0;
        } else {
            return 1 + Math.max(height(left(t)), height(right(t)));
        }
    }
        
    public static int treeSum(IntTree t) {
        // returns the sum of all the integers stored in the tree t
        if (isEmpty(t)) {
            return 0;
        } else {
            return value(t) + treeSum(left(t)) + treeSum(right(t));
        }
    }
        
    public static IntTree treeDouble(IntTree t) {
        // returns a new tree that is the same size and shape as t,
        // but with all the integers in the tree doubled
        if (isEmpty(t)) {
            return empty();
        } else {
            return node (2 * value(t), treeDouble(left(t)),
                                       treeDouble(right(t)));
        }
    }
        
    public static void preOrderWrite(IntTree t) {
        // prints out the contents of the tree t using preOrder
        // traversal of the nodes
        if (!(isEmpty(t))) {
            System.out.print("  " + value(t));
            preOrderWrite(left(t));
            preOrderWrite(right(t));
        }
    }
        
    public static void inOrderWrite(IntTree t) {
        // prints out the contents of the tree t using inOrder
        // traversal of the nodes
        if (!(isEmpty(t))) {
            inOrderWrite(left(t));
            System.out.print("  " + value(t));
            inOrderWrite(right(t));
        }
    }
        
    public static void postOrderWrite(IntTree t) {
        // prints out the contents of the tree t using postOrder
        // traversal of the nodes
        if (!(isEmpty(t))) {            
            postOrderWrite(left(t));
            postOrderWrite(right(t));
            System.out.print("  " + value(t));
        }
    }
        
    public static int countNodes(IntTree t) {
        // returns the number of nodes stored in the tree t
        if (isEmpty(t)) {
            return 0;
        } else {
            return 1 + countNodes(left(t)) + countNodes(right(t));
        }
    }
        
    public static int maxInt(IntTree t) {
        // returns the maximum integer stored in the tree t
        if (isEmpty(t)) {
            return Integer.MIN_VALUE; // very large negative number
        } else {
            return Math.max(value(t), Math.max(maxInt(left(t)),
                                               maxInt(right(t))));
        }
    }
        
    public static IntTree onesTree(IntTree t) {
        // returns a new tree that is the same size and shape as t,
        // but with all the nodes in the tree containing the integer 1
        if (isEmpty(t)) {
            return empty();
        } else {
            return node(1, onesTree(left(t)), onesTree(right(t)));
        }
    }
        
    public static IntTree partialSumUp(IntTree t) {
        // returns a new tree that is the same size and shape as t, in
        // which the value in each node of the new tree is the sum of
        // all the values in the tree rooted at the corresponding node
        // of the old tree
        if (isEmpty(t)) {
            return empty();
        } else {
            return node(treeSum(t), partialSumUp(left(t)),
                                    partialSumUp(right(t)));
        }
    }
        
    public static IntTree partialSumUp2(IntTree t) {
        // same as above, but more efficient
        if (isEmpty(t)) {
            return empty();
        } else {
            IntTree tl = partialSumUp2(left(t));
            IntTree tr = partialSumUp2(right(t));
            int vl = 0;
            if (!isEmpty(tl))
                vl = value(tl);
            int vr = 0;
            if (!isEmpty(tr))
                vr = value(tr);
            return node(value(t) + vl + vr,
                        tl,
                        tr);
        }
    }
        
    public static IntTree partialHeightsUp(IntTree t) {
        // returns a new tree that is the same size and shape as t, in
        // which the value in each node of the new tree is the height
        // of the tree rooted at the corresponding node of the old tree
        if (isEmpty(t)) {
            return empty();
        } else {
            return node(height(t), partialHeightsUp(left(t)),
                                   partialHeightsUp(right(t)));
        }
    }
        
    public static IntTree partialSumDown(IntTree t) {
        // returns a new tree that is the same size and shape as t, in
        // which the value in each node of the new tree is the sum of
        // all the values in the path from the corresponding node of
        // the old tree up to the root (inclusive)
        return sumDown(t, 0);
    }
        
    public static IntTree sumDown(IntTree t, int sum) {
        // used by partialSumDown (above)
        int new_sum;
        if (isEmpty(t)) {
            return empty();
        } else {
            new_sum = sum + value(t);
            return node(new_sum, sumDown(left(t), new_sum),
                                 sumDown(right(t), new_sum));
        }
    }
        
    public static IntTree partialHeightsDown(IntTree t) {
        // returns a new tree that is the same size and shape as t, in
        // which the value in each node of the new tree is the length of the
        // path from the corresponding node of the old tree up to the root
        return heightsDown(t, 0);
    }
        
    public static IntTree heightsDown(IntTree t, int height) {
        // used by partialHeightsDown (above)
        int new_height;
        if (isEmpty(t)) {
            return empty();
        } else {
            new_height = 1 + height;
            return node(new_height, heightsDown(left(t), new_height),
                                    heightsDown(right(t), new_height));
        }
    }
        
    // Local abbreviations
        
    public static IntTree empty() {
        return IntTree.empty();
    }
        
    public static boolean isEmpty(IntTree T) {
        return IntTree.isEmpty(T);
    }
        
    public static boolean isLeaf(IntTree T) {
        return IntTree.isLeaf(T);
    }
        
    public static IntTree node(int val, IntTree lt, IntTree rt) {
        return IntTree.node(val, lt, rt);
    }
        
    public static IntTree leaf(int val) {
        return IntTree.leaf(val);
    }
        
    public static int value(IntTree T) {
        return IntTree.value(T);
    }
        
    public static IntTree left(IntTree T) {
        return IntTree.left(T);
    }
        
    public static IntTree right(IntTree T) {
        return IntTree.right(T);
    }
        
    public static void setValue(IntTree T, int newValue) {
        IntTree.setValue(T, newValue);
    }
        
    public static void setLeft(IntTree T, IntTree newLeft) {
        IntTree.setLeft(T, newLeft);
    }
        
    public static void setRight(IntTree T, IntTree newRight) {
        IntTree.setRight(T, newRight);
    }

    public static void printTree(IntTree T) {
        IntTree.printTree(T);
    }
        
}
