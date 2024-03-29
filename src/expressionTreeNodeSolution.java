import java.lang.Math.*;

class expressionTreeNodeSolution {
	private String value;
	private expressionTreeNodeSolution leftChild, rightChild, parent;

	expressionTreeNodeSolution() {
		value = null;
		leftChild = rightChild = parent = null;
	}

	/*
	 * Arguments: String s: Value to be stored in the node
	 * expressionTreeNodeSolution l, r, p: the left child, right child, and
	 * parent of the node to created Returns: the newly created
	 * expressionTreeNodeSolution
	 */
	expressionTreeNodeSolution(String s, expressionTreeNodeSolution l,
			expressionTreeNodeSolution r, expressionTreeNodeSolution p) {
		value = s;
		leftChild = l;
		rightChild = r;
		parent = p;
	}

	/* Basic access methods */
	String getValue() {
		return value;
	}

	expressionTreeNodeSolution getLeftChild() {
		return leftChild;
	}

	expressionTreeNodeSolution getRightChild() {
		return rightChild;
	}

	expressionTreeNodeSolution getParent() {
		return parent;
	}

	/* Basic setting methods */
	void setValue(String o) {
		value = o;
	}

	// sets the left child of this node to n
	void setLeftChild(expressionTreeNodeSolution n) {
		leftChild = n;
		n.parent = this;
	}

	// sets the right child of this node to n
	void setRightChild(expressionTreeNodeSolution n) {
		rightChild = n;
		n.parent = this;
	}

	// Returns the root of the tree describing the expression s
	// Watch out: it makes no validity checks whatsoever!
	expressionTreeNodeSolution(String s) {
		// check if s contains parentheses. If it doesn't, then it's a leaf
		if (s.indexOf("(") == -1)
			setValue(s);
		else { // it's not a leaf

			/*
			 * break the string into three parts: the operator, the left
			 * operand, and the right operand. **
			 */
			setValue(s.substring(0, s.indexOf("(")));
			// delimit the left operand 2008
			int left = s.indexOf("(") + 1;
			int i = left;
			int parCount = 0;
			// find the comma separating the two operands
			while (parCount >= 0 && !(s.charAt(i) == ',' && parCount == 0)) {
				if (s.charAt(i) == '(')
					parCount++;
				if (s.charAt(i) == ')')
					parCount--;
				i++;
			}
			int mid = i;
			if (parCount < 0)
				mid--;

			// recursively build the left subtree
			setLeftChild(new expressionTreeNodeSolution(s.substring(left, mid)));

			if (parCount == 0) {
				// it is a binary operator
				// find the end of the second operand.F13
				while (!(s.charAt(i) == ')' && parCount == 0)) {
					if (s.charAt(i) == '(')
						parCount++;
					if (s.charAt(i) == ')')
						parCount--;
					i++;
				}
				int right = i;
				setRightChild(new expressionTreeNodeSolution(s.substring(
						mid + 1, right)));
			}
		}
	}

	// Returns a copy of the subtree rooted at this node... 2013
	expressionTreeNodeSolution deepCopy() {
		expressionTreeNodeSolution n = new expressionTreeNodeSolution();
		n.setValue(getValue());
		if (getLeftChild() != null)
			n.setLeftChild(getLeftChild().deepCopy());
		if (getRightChild() != null)
			n.setRightChild(getRightChild().deepCopy());
		return n;
	}

	// Returns a String describing the subtree rooted at a certain node.
	public String toString() {
		String ret = value;
		if (getLeftChild() == null)
			return ret;
		else
			ret = ret + "(" + getLeftChild().toString();
		if (getRightChild() == null)
			return ret + ")";
		else
			ret = ret + "," + getRightChild().toString();
		ret = ret + ")";
		return ret;
	}

	// Returns the value of the expression rooted at a given node
	// when x has a certain value
	double evaluate(double x) {
		// WRITE YOUR CODE HERE
		expressionTreeNodeSolution left, right;
		String node = this.value;
		left = this.getLeftChild();
		right = this.getRightChild();

		if (node.equals("add")) {
			return left.evaluate(x) + right.evaluate(x);
		} else if (node.equals("minus")) {
			return left.evaluate(x) - right.evaluate(x);
		} else if (node.equals("mult")) {
			return left.evaluate(x) * right.evaluate(x);
		} else if (node.equals("cos")) {
			return Math.cos(left.evaluate(x));
		} else if (node.equals("sin")) {
			return Math.sin(left.evaluate(x));
		} else if (node.equals("exp")) {
			return Math.pow(left.evaluate(x), right.evaluate(x));
		} else if (node.charAt(0) == 'x')
			return x;
		else
			return Double.parseDouble(node);
	}

	/*
	 * returns the root of a new expression tree representing the derivative of
	 * the original expression
	 */
	expressionTreeNodeSolution differentiate() {
		expressionTreeNodeSolution left, right;
		
		left = this.getLeftChild();		
		right = this.getRightChild();
		expressionTreeNodeSolution copy = deepCopy();
		

		if (copy.value.charAt(0) == 'x')
			copy.value = ""+1;
		else if ('0' <= copy.value.charAt(0) && copy.value.charAt(0) <= '9')
			copy.value = ""+0;
		else {
			if (value.equals("add")) {
				copy.setLeftChild(left.differentiate());
				copy.setRightChild(right.differentiate());
			} else if (value.equals("minus")) {
				copy.setLeftChild(left.differentiate());
				copy.setRightChild(right.differentiate());
			} else if (value.equals("mult")) {
				expressionTreeNodeSolution secCopy = deepCopy();
				expressionTreeNodeSolution thirdCopy = deepCopy();
				expressionTreeNodeSolution fourthCopy = deepCopy();
				expressionTreeNodeSolution fifthCopy = deepCopy();
				expressionTreeNodeSolution sixthCopy = deepCopy();
				
				copy.value = "add";
				
				copy.setLeftChild(secCopy.getLeftChild());
				copy.leftChild.value = "mult";
				copy.setRightChild(secCopy.getRightChild());
				copy.rightChild.value = "mult";
				
				copy.leftChild.setLeftChild(thirdCopy);
				copy.leftChild.setRightChild(fourthCopy);
				
				copy.rightChild.setLeftChild(fifthCopy);
				copy.rightChild.setRightChild(sixthCopy);
				
				copy.leftChild.leftChild = left.differentiate();
				copy.leftChild.rightChild = right;
				copy.rightChild.leftChild = left;
				copy.rightChild.rightChild = right.differentiate();
			} else if (value.equals("cos")) {
				expressionTreeNodeSolution aCopy = deepCopy();
				expressionTreeNodeSolution secCopy = deepCopy();
				
				copy.value = "minus";
				
				copy.leftChild = new expressionTreeNodeSolution("0",null,null,this.parent);
				copy.rightChild = new expressionTreeNodeSolution("mult",aCopy.leftChild.differentiate(),null,this.parent);
				copy.rightChild.setRightChild(new expressionTreeNodeSolution("sin",secCopy.leftChild,null,this.parent));
				
			} else if (value.equals("sin")) {
				expressionTreeNodeSolution aCopy = deepCopy();
				expressionTreeNodeSolution secCopy = deepCopy();
				
				copy = new expressionTreeNodeSolution("mult",aCopy.leftChild.differentiate(),null,this.parent);
				copy.rightChild = new expressionTreeNodeSolution("cos",secCopy.leftChild,null,this.parent);
			} else if (value.equals("exp")) {
				expressionTreeNodeSolution aCopy = deepCopy();
				expressionTreeNodeSolution secCopy = deepCopy();
				
				copy = new expressionTreeNodeSolution("mult",aCopy.leftChild.differentiate(),null,this.parent);
				copy.rightChild = new expressionTreeNodeSolution("exp",secCopy.leftChild,null,this.parent);
			}
		}
		return copy;
	}

	public static void main(String args[]) {
		expressionTreeNodeSolution e = new expressionTreeNodeSolution(
				"sin(cos(x))");
		System.out.println(e);
		//System.out.println(e.evaluate(1));
		System.out.println(e.differentiate());

	}
}