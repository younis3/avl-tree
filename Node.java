package oop.ex4.data_structures;


/**
 * This class represents a Node in the AVL Tree
 */
public class Node{

    private int data;  // the data (integer) of the node
    private Node right;  // the right child
    private Node left;  // the left child
    private Node parent;  // the parent of the node



    /**
     * Creates a new node
     * @param value the data the node holds.
     */
    public Node(int value){
        this.data = value;
    }


    /**
     * @return the value of the node.
     */
    public int getValue(){
        return this.data;
    }

    /**
     * @return the right child of the node
     */
    public Node getRight(){
        return this.right;
    }

    /**
     * Setting a new right node
     * @param newRight the node to become the new right
     */
    public void setRight(Node newRight){
        this.right = newRight;
        if (newRight != null)
            newRight.setParent(this);
    }

    /**
     * @return the left child of the node
     */
    public Node getLeft(){
        return this.left;
    }

    /**
     * Setting a new left node
     * @param newLeft the node to become the new left
     */
    public void setLeft(Node newLeft){
        this.left = newLeft;
        if (newLeft != null) {
            newLeft.setParent(this);
        }
    }

    /**
     * @return parent of the node
     */
    public Node getParent(){
        return this.parent;
    }

    /**
     * Set a new parent node
     * @param father the new parent
     */
    public void setParent(Node father){
        parent = father;
    }

    /**
     * @return  the height of the right subtree
     */
    private int rightHeight(){
        return height(this.right);
    }

    /**
     * @return  the height of the left subtree
     */
    private int leftHeight(){
        return this.height(this.left);
    }


    /**
     * @param root the root of the tree.
     * @return the height of the tree.
     */
    public int height(Node root){
        if (root == null) { // if empty
            return -1;  // the height of an empty tree
        }
        return Math.max(height(root.left), height(root.right)) + 1;
    }


    /**
     * the nodes of the left must be not greater than one in height, same for the right
     * in order for the tree to stay balanced
     * @return the difference
     */
    public int balanceCheck(){
        return leftHeight() - rightHeight();
    }




    /**
     * checks if a value is in the tree
     * @param searchVal the value to search for
     * @return the depth of the node if searchVal is in the subtree of node, -1 otherwise
     */
    public int contains(int searchVal){
        Node node = this;
        for (int i = 0; node != null; i++){
            if (searchVal == node.data) {
                return i;
            }
            if (searchVal > node.data) {
                node = node.right;
            }
            else if (searchVal < node.data) {
                node = node.left;
            }
        }
        return -1;
    }

    /**
     * @return the Node with the minimum value in the tree
     */
    public Node findMinVal(){
        return findMinValHelper(this);
    }

    /**
     * helper method for FindMin
     */
    private Node findMinValHelper(Node node){
        if (node == null || node.left == null) {
            return node;
        }
        return findMinValHelper(node.left);
    }


    /**
     * check if the node has children or not
     * @return true if node is a leaf, false otherwise.
     */
    public boolean isLeaf(){
        return right == null & left == null;
    }



    /**
     * @param val the value to search for
     * @return the node that has the same value as val
     */
    public Node searchInt(int val){
        return searchIntHelper(val, this);
    }


    /**
     * a recursive helper method for searchInt()
     */
    private Node searchIntHelper(int searchVal, Node node){
        if (node == null) {
            return null;
        }
        if (searchVal == node.data) {
            return node;
        }
        if (searchVal > node.data) {
            return searchIntHelper(searchVal, node.right);
        }
        else {
            return searchIntHelper(searchVal, node.left);
        }
    }



    /**
     * @return the successor of node
     */
    public Node successor(){
        return successorHelper(this);
    }


    /**
     * helper method for successor method
     */
    private Node successorHelper(Node node){
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            return findMinValHelper(node.right);
        }
        Node father = node.parent;
        Node child = node;
        while (father != null && child == father.right){
            child = father;
            father = father.parent;
        }
        return father;
    }



}