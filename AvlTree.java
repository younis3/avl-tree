package oop.ex4.data_structures;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * An implementation of the AVL tree data structure.
 */
public class AvlTree implements Iterable<Integer> {

    private Node root;  // the root of the tree
    private int nodesSum;  // the total number of nodes in the tree

    private final int RIGHT_VIOLATION = -2;  // the right violation a node has
    private final int LEFT_VIOLATION = 2;  // the left violation a node has



    /**
     * The default constructor.
     */
    public AvlTree(){

    }


    /**
     * A copy constructor that creates a deep copy of the given AvlTree.
     * @param tree - The AVL tree to be copied.
     */
    public AvlTree(AvlTree tree){
        Iterator<Integer> iterator = tree.iterator();
        while (iterator.hasNext()){
            add(iterator.next());
        }
    }


    /**
     * A constructor that builds a new AVL tree containing all unique values in the input array.
     * @param data - the values to add to tree.
     */
    public AvlTree(int[] data){
        if (data == null){
            return;
        }
        for (int node : data) {
            add(node);
        }
    }



    /**
     * iterator in interface Iterable<Integer>
     * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
     * ascending order, and does NOT implement the remove() method.
     */
    @Override
    public Iterator<Integer> iterator() {
        final LinkedList<Integer> list = nodesList();
        return new Iterator<Integer>() {
            int index = 0;

            /**
             * @return true if the iterator has next, false otherwise.
             */
            @Override
            public boolean hasNext() {
                return index < list.size();
            }

            /**
             * @return the next Value of the iterator.
             */
            @Override
            public Integer next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                Integer value = list.get(index);
                index++;
                return value;
            }

            /**
             * Removal operation are not supported. throwing UnsupportedOperationException.
             */
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Operations of removals are not supported");
            }
        };

    }


    /**
     * Add a new node with the given key to the tree.
     * @param newValue - the value of the new node to add.
     * @return true if the value to add is not already in the tree and it was successfully added, false otherwise.
     */
    public boolean add(int newValue){
        if (contains(newValue) != -1)  // check if already in the tree
            return false;

        nodesSum++;
        Node newNode = new Node(newValue);

        if (root == null){  // if tree is empty, make newNode the root
            root = newNode;
            return true;
        }

        addNode(newNode, root);  // add the new node

        Node current = newNode.getParent();

        // find the unbalanced node
        while (current != null && (current.balanceCheck() != LEFT_VIOLATION && current.balanceCheck() != RIGHT_VIOLATION)){
            current = current.getParent();
        }
        addRebalance(current);

        return true;
    }

    /**
     * a helper method for add method
     */
    private void addNode(Node toAdd, Node currentRoot){
        if (toAdd.getValue() > currentRoot.getValue()) {
            if (currentRoot.getRight() == null) {
                currentRoot.setRight(toAdd);
            }
            addNode(toAdd, currentRoot.getRight());
        }
        if (toAdd.getValue() < currentRoot.getValue()) {
            if (currentRoot.getLeft() == null) {
                currentRoot.setLeft(toAdd);
            }
            addNode(toAdd, currentRoot.getLeft());
        }
    }

    /**
     * Check whether the tree contains the given input value.
     * @param searchVal the value to search for.
     * @return the depth of the node (0 for the root) with the given value if it was found in the tree, −1
     * otherwise.
     */
    public int contains(int searchVal){
        if (root == null) {
            return -1;
        }
        return root.contains(searchVal);
    }




    /**
     * @return the number of nodes in the tree
     */
    public int size(){
        return nodesSum;
    }


    /**
     * returns a linked list of the tree nodes
     */
    private LinkedList<Integer> nodesList(){
        LinkedList<Integer> list = new LinkedList<Integer>();
        Node current = root.findMinVal();
        list.add(current.getValue());
        while (current.successor() != null){
            current = current.successor();
            list.add(current.getValue());
        }
        return list;
    }


    /**
     * Calculates the minimum number of nodes in an AVL tree of height h.
     * @param h the height of the tree (a non−negative number) in question.
     * @return the minimum number of nodes in an AVL tree of the given height.
     */
    public static int findMinNodes(int h){
        if (h == 0) {
            return 1;
        }
        if (h == 1) {
            return 2;
        }
        int sum = 0;
        int cur = 1;
        int next = 2;
        for (int i = 2; i <= h; i++){
            sum = cur + next + 1;
            cur = next;
            next = sum;
        }
        return sum;
    }



    /**
     * This method rotates the given node right to make the tree balance
     */
    private void rotateRight(Node node){
        Node child = node.getLeft();
        node.setLeft(child.getRight());
        if (child.getRight() != null) {
            child.getRight().setParent(node);
        }
        child.setParent(node.getParent());
        if (node.getParent() == null) {
            root = child;
        }
        else if (node == node.getParent().getRight()) {
            node.getParent().setRight(child);
        }
        else {
            node.getParent().setLeft(child);
        }
        child.setRight(node);
        node.setParent(child);
    }


    /**
     * This method rotates the given node left to make the tree balance
     */
    private void rotateLeft(Node node){
        Node child = node.getRight();
        node.setRight(child.getLeft());
        if (child.getLeft() != null) {
            child.getLeft().setParent(node);
        }
        child.setParent(node.getParent());
        if (node.getParent() == null) {
            root = child;
        }
        else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(child);
        }
        else {
            node.getParent().setRight(child);
        }
        child.setLeft(node);
        node.setParent(child);
    }



    /**
     * Rebalancing the tree after a new value was added
     */
    private void addRebalance(Node currentNode){
        if (currentNode == null) {
            return;
        }
        if (currentNode.balanceCheck() == RIGHT_VIOLATION){
            if (currentNode.getRight().balanceCheck() == -1){
                // right-right unbalance
                rotateLeft(currentNode);
            }
            else if (currentNode.getRight().balanceCheck() == 1) {
                // right-left unbalance
                rotateRight(currentNode.getRight());
                rotateLeft(currentNode);
            }
        }
        else {
            if (currentNode.getLeft().balanceCheck() == -1){
                // left-right unbalance
                rotateLeft(currentNode.getLeft());
                rotateRight(currentNode);
            }
            else if (currentNode.getLeft().balanceCheck() == 1) {
                // left-left unbalance
                rotateRight(currentNode);
            }

        }

    }




    /**
     * Removes the node with the given value from the tree, if it exists.
     * @param toDelete the value to remove from the tree.
     * @return true if the given value was found and deleted, false otherwise.
     */
    public boolean delete(int toDelete){
        if (contains(toDelete) == -1) {
            return false;
        }
        // delete the node and get the node that may has an unbalance
        Node currentNode = deleteNode(toDelete);
        while (currentNode != null){
            if (currentNode.balanceCheck() == LEFT_VIOLATION || currentNode.balanceCheck() == RIGHT_VIOLATION) {
                // reBalance and get the node that may has an unbalance
                currentNode = deleteRebalance(currentNode);
            }
            else{
                currentNode = currentNode.getParent();
            }
        }

        nodesSum--;

        return true;

    }


    /**
     * a helper method for Delete
     */
    private Node deleteNode(int toDelete){
        Node nodeToDelete = root.searchInt(toDelete);
        if (nodeToDelete.isLeaf()){  // the node doesn't have any children
            if (nodeToDelete == root) {
                root = null;
            }
            else if (nodeToDelete == nodeToDelete.getParent().getRight()) {
                nodeToDelete.getParent().setRight(null);
            }
            else {
                nodeToDelete.getParent().setLeft(null);
            }
        }

        else if (nodeToDelete.getRight() == null){  // the node has only a left child
            if (nodeToDelete == root) {
                root = nodeToDelete.getLeft();
            }
            else if (nodeToDelete == nodeToDelete.getParent().getRight()) {
                nodeToDelete.getParent().setRight(nodeToDelete.getLeft());
            }
            else {
                nodeToDelete.getParent().setLeft(nodeToDelete.getLeft());
            }
        }

        else if (nodeToDelete.getLeft() == null){  // the node has only a right child
            if (nodeToDelete == root) {
                root = nodeToDelete.getRight();
            }

            else if (nodeToDelete == nodeToDelete.getParent().getRight()) {
                nodeToDelete.getParent().setRight(nodeToDelete.getRight());
            }
            else {
                nodeToDelete.getParent().setLeft(nodeToDelete.getRight());
            }
        }

        else{  // the node have both right and left children
            Node toReplace = nodeToDelete.successor();
            Node toRebalance = toReplace.getParent() == nodeToDelete ? toReplace : toReplace.getParent();
            if (toReplace.getValue() > toReplace.getParent().getValue()) {
                toReplace.getParent().setRight(toReplace.getRight());
            }
            else {
                toReplace.getParent().setLeft(toReplace.getRight());
            }
            toReplace.setParent(null);
            if (nodeToDelete.getParent() == null) {
                root = toReplace;
            }

            else if (toReplace.getValue() > nodeToDelete.getParent().getValue()) {
                nodeToDelete.getParent().setRight(toReplace);
            }
            else {
                nodeToDelete.getParent().setLeft(toReplace);
            }
            toReplace.setLeft(nodeToDelete.getLeft());
            toReplace.setRight(nodeToDelete.getRight());
            return toRebalance;
        }
        return nodeToDelete.getParent();
    }



    /**
    * ReBalance the tree after a deletion
    * @return a Node that its parents may need balancing as well
    */
    private Node deleteRebalance(Node current){
        if (current == null) {
            return null;
        }
        Node rootNew;
        if (current.balanceCheck() == RIGHT_VIOLATION) {
            if (current.getRight().balanceCheck() == 1) {
                // right-left unbalance
                rootNew = current.getRight().getLeft();
                rotateRight(current.getRight());
                rotateLeft(current);
            }
            else {
                // right-right unbalance
                rootNew = current.getRight();
                rotateLeft(current);
            }
        }
        else{
            if (current.getLeft().balanceCheck() == -1){
                // left-right unbalance
                rootNew = current.getLeft().getRight();
                rotateLeft(current.getLeft());
                rotateRight(current);
            }
            else {
                // left-left unbalance
                rootNew = current.getLeft();
                rotateRight(current);
            }
        }
        return rootNew;
    }




}