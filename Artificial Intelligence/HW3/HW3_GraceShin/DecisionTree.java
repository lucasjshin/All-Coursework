// Lucas Shin & Sam Grace
// Honor Code: I have neither given nor received any unauthorized aid on this assignment
import java.util.ArrayList;

public class DecisionTree {
	private TreeNode root = null; //stores the root of the decision tree 
	
	public void train(ArrayList<Example> examples) {
		int numFeatures = 0; 
		if(examples.size()>0) //get the number of featuers in these examples
			numFeatures = examples.get(0).getNumFeatures(); 

		//initialize empty positive and negative lists  
		ArrayList<Example> pos = new ArrayList<Example>(); 
		ArrayList<Example> neg = new ArrayList<Example>(); 
		
		//paritition examples into positive and negative ones 
		for(Example e: examples) { 
			if (e.getLabel())
				pos.add(e);
			else
				neg.add(e);
		}
		
		//create the root node of the tree
		root = new TreeNode(null, pos, neg, numFeatures);
		
		//call recursive train()  on the root node
		train(root, numFeatures);
	}
 
	
	/**
	 * The recursive train method that builds a tree at TreeNode node
	 * @param node: current node to train
	 * @param numFeatures: total number of features
	 */
	private void train(TreeNode node, int numFeatures) {

		ArrayList<Example> pos;
		ArrayList<Example> neg;
		double highestInfoGain;
		int bestFeature;
		double entropy;

		// check if we have no more features
		boolean noFeatures = true;
		// j is the feature we're checking
		for (int j = 0; j < numFeatures; j++) {
			if (!node.featureUsed(j)) {
				noFeatures = false;
				break;
			}
		}

		// Base Case 2 (If no more examples at this node)
		if(node.pos.isEmpty() && node.neg.isEmpty()) {
			node.isLeaf = true;
			node.decision = node.parent.pos.size() >= node.parent.neg.size();
		}
		// Base Case 1 (If all remaining examples at this node have the same label L)
		else if (node.pos.isEmpty() || node.neg.isEmpty()) {
			node.isLeaf = true;
			node.decision = node.pos.size() > node.neg.size();
		}
		// Base Case 3 (If no more features left)
		else if (noFeatures) {
			node.isLeaf = true;
			node.decision = node.pos.size() > node.neg.size();
			
		// Recursive Step
		} else {
			// get the positive and negative examples for this node
			pos = new ArrayList<Example>();
			neg = new ArrayList<Example>();
			pos = node.pos;
			neg = node.neg;

			highestInfoGain = 0;
			bestFeature = -1;

			// use helper function to calculate entropy
			entropy = getEntropy(pos.size(), neg.size());
			
			// iterate through features and calculate information gain to determine best feature to split on
			for (int i = 0; i < numFeatures; i++) {
				if (node.featureUsed(i)) {
					continue;
				}
				// calculate information gain at each feature
				double gain = entropy - getRemainingEntropy(i, node);
				// store highest information gain and best feature
				if(gain >= highestInfoGain) {
					highestInfoGain = gain;
					bestFeature = i;
				}
			}
		
			// set best feature
			node.setSplitFeature(bestFeature);

			createChildren(node, numFeatures);
			train(node.trueChild, numFeatures);
			train(node.falseChild, numFeatures);
		}
	}

			
	
	/**
	 * Creates the true and false children of TreeNode node
	 * @param node: node at which to create children
	 * @param numFeatures: total number of features
	 */
	private void createChildren(TreeNode node, int numFeatures){
		ArrayList<Example> posTrue = new ArrayList<Example>();
		ArrayList<Example> posFalse = new ArrayList<Example>();
		ArrayList<Example> negTrue = new ArrayList<Example>();
		ArrayList<Example> negFalse = new ArrayList<Example>();

		for(Example e : node.pos) {
			// store positive true examples and positive false examples to respective arrayList
			if(e.getFeatureValue(node.getSplitFeature()) == true) {
				posTrue.add(e);
			} else {
				posFalse.add(e);
			}
		}

		for(Example e : node.neg) {
			// store negative true examples and negative false examples to respective arrayList
			if(e.getFeatureValue(node.getSplitFeature()) == true) {
				negTrue.add(e);
			} else {
				negFalse.add(e);
			}
		} 

		// create false children and true children
		node.falseChild = new TreeNode(node, posFalse, negFalse, numFeatures);
		node.trueChild = new TreeNode(node, posTrue, negTrue, numFeatures);
	}
	
	/**
	 * Computes and returns the remaining entropy if feature is chosen
	 * at node.
	 * @param feature: the feature number
	 * @param node: node at which to find remaining entropy
	 * @return remaining entropy at node
	 */
	private double getRemainingEntropy(int feature, TreeNode node){

		ArrayList<Example> examples = new ArrayList<Example>();
		examples.addAll(node.pos);
		examples.addAll(node.neg);

		int posTrue = 0; // pos of true child
		int posFalse = 0; // pos of false child
		int negTrue = 0; // neg of true child
		int negFalse = 0; // neg of false child

		for(Example e : examples) {
			// iterate through examples and increment respective arrays depending on the decision of the label
			if(e.getFeatureValue(feature) == true) {
				if(e.getLabel() == true) {
					posTrue++;
				} else {
					posFalse++;
				}
			} else {
				if(e.getLabel() == true) {
					negTrue++;
				} else {
					negFalse++;
				}
			}
		}
		
		// below to calculate fractions for weighted entropy
		int totTrue = posTrue + posFalse;
		int totFalse = negTrue + negFalse;
		int total = totTrue + totFalse;
		double trueDivTotal = (double) totTrue/total;
		double falseDivTotal = (double) totFalse/total;
		
		// calculate fractions multiplied by their respective entropies
		double trueChildEnt = trueDivTotal*getEntropy(posTrue, posFalse);
		double falseChildEnt = falseDivTotal*getEntropy(negTrue, negFalse);

		return trueChildEnt + falseChildEnt;
	}
	
	/**
	 * Computes the entropy of a node given the number of positive and negative examples it has
	 * @param numPos: number of positive examples
	 * @param numNeg: number of negative examples
	 * @return - entropy
	 */
	private double getEntropy(int numPos, int numNeg){
		// if statement to make sure there are never scenarios where the denominator of the fraction is 0
		if(numPos == 0 || numNeg ==0){
			return 0;
		} else {
			double total = numPos + numNeg;

			// P(Yes) / Total
			double numPosDivTotal = numPos/total;
			// P(No) / Total
			double numNegDivTotal = numNeg/total;

			// calculate entropy
			double entropy = -1*(numPosDivTotal)*log2(numPosDivTotal) - (numNegDivTotal)*log2(numNegDivTotal);

			return entropy;
		}
	}
	
	/**	
	 * Computes log_2(d) (To be used by the getEntropy() method)
	 * @param d - value
	 * @return log_2(d)
	 */
	private double log2(double d){
		if(d==0){
			return 0;
		} else{
			return Math.log(d)/Math.log(2);
		}
	}
	
	/** 
	 * TODO: complete this method
	 * Classifies example e using the learned decision tree
	 * @param e: example
	 * @return true if e is predicted to be  positive,  false otherwise
	 */
	public boolean classify(Example e) {
		// calls helper function so that we can use recursion, starting at the root
		return classifier(e, root);
	}

	// recursive helper function for classify
	public boolean classifier(Example e, TreeNode treeNode) {
		// stop recursion if current node is at leaf
		if (treeNode.isLeaf == true) {
			return treeNode.decision;
		} else {
			// if example e has value v for f, recurse on node's child
			if (e.getFeatureValue(treeNode.getSplitFeature()) == false) {
				return classifier(e, treeNode.falseChild);
			} else {
				return classifier(e, treeNode.trueChild);
			}
		}

	}
	
	
	//----------DO NOT MODIFY CODE BELOW------------------
	public void print(){
		printTree(root, 0);
	}
	
	private void printTree(TreeNode node, int indent){
		if(node== null)
			return;
		if(node.isLeaf){
			if(node.decision)
				System.out.println("Positive");
			else
				System.out.println("Negative");
		}
		else{
			System.out.println();
			doIndents(indent);
			System.out.print("Feature "+node.getSplitFeature() + " = True:" );
			printTree(node.trueChild, indent+1);
			doIndents(indent);
			System.out.print("Feature "+node.getSplitFeature() + " = False:" );//+  "( " + node.falseChild.pos.size() + ", " + node.falseChild.neg.size() + ")");
			printTree(node.falseChild, indent+1);
		}
	}
	
	private void doIndents(int indent){
		for(int i=0; i<indent; i++)
			System.out.print("\t");
	}
}