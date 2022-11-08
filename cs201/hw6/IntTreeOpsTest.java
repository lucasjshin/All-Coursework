public class IntTreeOpsTest extends IntTreeOps  {

    // main method for testing the IntTree methods
    public static void main(String[] args) 
    {   // create a sample tree for testing
        IntTree lt = node(1, node(4, empty(), empty()),
                             node(2, node(5, empty(), empty()), empty()));
        IntTree rt = node(3, empty(), node(7, empty(), empty()));
        IntTree t = node(6, lt, rt);
        System.out.println("Test tree:");
        printTree(t);
        System.out.print("height of left subtree = " + height(lt));
        System.out.print(",  right subtree = " + height(rt));
        System.out.println(", full tree = " + height(t));
        System.out.print("sum of left subtree = " + treeSum(lt));
        System.out.print(", right subtree = " + treeSum(rt));
        System.out.println(", full tree = " + treeSum(t));
        System.out.print("nodes of left subtree = " + countNodes(lt));
        System.out.print(", right subtree = " + countNodes(rt));
        System.out.println(", full tree = " + countNodes(t));
        System.out.print("max of left subtree = " + maxInt(lt));
        System.out.print(", right subtree = " + maxInt(rt));
        System.out.println(", full tree = " + maxInt(t));
        System.out.println("Testing treeDouble:");
        printTree(treeDouble(t));
        System.out.println("Testing onesTree:");
        printTree(onesTree(t));
        System.out.println("Testing partialSumUp:");
        printTree(partialSumUp(t));
        System.out.println("Testing partialSumDown:");
        printTree(partialSumDown(t));
        System.out.println("Testing partialHeightsUp:");
        printTree(partialHeightsUp(t));
        System.out.println("Testing partialHeightsDown:");
        printTree(partialHeightsDown(t));
        System.out.print("preOrderWrite t : ");
        preOrderWrite(t);
        System.out.println();
        System.out.print("inOrderWrite t :  ");
        inOrderWrite(t);
        System.out.println();
        System.out.print("postOrderWrite t :");
        postOrderWrite(t);
        System.out.println();           
    }
}

                
