import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/******************************************************************************
 *  Compilation:  javac SplayBST.java
 *  Execution:    java SplayBST
 *  Dependencies: none
 *
 *  Splay tree. Supports splay-insert, -search, and -delete.
 *  Splays on every operation, regardless of the presence of the associated
 *  key prior to that operation.
 *
 *  Written by Josh Israel.
 *
 *  Modified by Sanath Jayasena
 *
 ******************************************************************************/


public class SplayBST<Key extends Comparable<Key>, Value> {

    private Node root;   // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;            // key
        private Value value;        // associated data
        private Node left, right;   // left and right subtrees

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    // return value associated with the given key
    // if no such value, return null
    public Value get(Key key) {
        root = splay(root, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) return root.value;
        else return null;
    }

    /***************************************************************************
     *  Splay tree insertion.
     ***************************************************************************/
    public void put(Key key, Value value) {
        // splay key to root
        if (root == null) {
            root = new Node(key, value);
            return;
        }

        root = splay(root, key);

        int cmp = key.compareTo(root.key);

        // Insert new node at root
        if (cmp < 0) {
            Node n = new Node(key, value);
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        }

        // Insert new node at root
        else if (cmp > 0) {
            Node n = new Node(key, value);
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }

        // It was a duplicate key. Simply replace the value
        else {
            root.value = value;
        }

    }

    /***************************************************************************
     *  Splay tree deletion.
     ***************************************************************************/
    /* This splays the key, then does a slightly modified Hibbard deletion on
     * the root (if it is the node to be deleted; if it is not, the key was
     * not in the tree). The modification is that rather than swapping the
     * root (call it node A) with its successor, it's successor (call it Node B)
     * is moved to the root position by splaying for the deletion key in A's
     * right subtree. Finally, A's right child is made the new root's right
     * child.
     */
    public void remove(Key key) {
        if (root == null) return; // empty tree

        root = splay(root, key);

        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            if (root.left == null) {
                root = root.right;
            } else {
                Node x = root.right;
                root = root.left;
                splay(root, key);
                root.right = x;
            }
        }

        // else: it wasn't in the tree to remove
    }


    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.
    private Node splay(Node h, Key key) {
        if (h == null) return null;

        int cmp1 = key.compareTo(h.key);

        if (cmp1 < 0) {
            // key not in tree, so we're done
            if (h.left == null) {
                return h;
            }
            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                    h.left = rotateLeft(h.left);
            }

            if (h.left == null) return h;
            else return rotateRight(h);
        } else if (cmp1 > 0) {
            // key not in tree, so we're done
            if (h.right == null) {
                return h;
            }

            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null)
                    h.right = rotateRight(h.right);
            } else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }

            if (h.right == null) return h;
            else return rotateLeft(h);
        } else return h;
    }


    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // height of tree (1-node tree has height 0)
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }


    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return 1 + size(x.left) + size(x.right);
    }

    // right rotate
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    // test client
    public static void main(String[] args) {
		try {
			doOperationsOnDataSet("set1", "data_1");
			doOperationsOnDataSet("set1", "data_2");
			doOperationsOnDataSet("set1", "data_3");
			doOperationsOnDataSet("set2", "data_1");
			doOperationsOnDataSet("set2", "data_2");
			doOperationsOnDataSet("set2", "data_3");
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 


    }
    
    private static void doOperationsOnDataSet(String dataSetName, String fileName) throws IOException, InterruptedException {
    	SplayBST<Long, Integer> st = new SplayBST<>();
    	
    	List<Long> insertData = TreeDataReader.readFromFile("insert", dataSetName, fileName);
    	List<Long> searchData = TreeDataReader.readFromFile("search", dataSetName, fileName);
    	List<Long> deleteData = TreeDataReader.readFromFile("delete", dataSetName, fileName);
    	
    	doSplayInsert(st, insertData, dataSetName, fileName);
    	doSplaySearch(st, searchData, dataSetName, fileName);
    	doSplayDelete(st, deleteData, dataSetName, fileName);
    }
    
    private static void doSplayInsert(SplayBST<Long, Integer> st, List<Long> dataset, String dataSetName, String fileName) {
    	long startTime = System.nanoTime();
    	Integer constantValue = 0;
    	
		System.out.println("SplayBST | Insert | " + dataSetName + " | " + fileName + " | Started ");
    	for(Long dataItem:dataset) {
    		st.put(dataItem, constantValue);
    	}
    	System.out.println("SplayBST | Insert | " + dataSetName + " | " + fileName + " | Ended | Duration : " + (System.nanoTime()-startTime)/1000);
    
    }
    
    private static void doSplaySearch(SplayBST<Long, Integer> st, List<Long> dataset, String dataSetName, String fileName) {
    	long startTime = System.nanoTime();
    	System.out.println("SplayBST | Search | " + dataSetName + " | " + fileName + " | Started");
    	for(Long dataItem:dataset) {
    		st.get(dataItem);
    	}
    	System.out.println("SplayBST | Search | " + dataSetName + " | " + fileName + " | Ended | Duration : " + (System.nanoTime()-startTime)/1000);
    
    }
    
    private static void doSplayDelete(SplayBST<Long, Integer> st, List<Long> dataset, String dataSetName, String fileName) {
    	long startTime = System.nanoTime();
    	System.out.println("SplayBST | Delete | " + dataSetName + " | " + fileName + " | Started");
    	for(Long dataItem:dataset) {
    		st.remove(dataItem);
    	}
    	System.out.println("SplayBST | Delete | " + dataSetName + " | " + fileName + " | Ended | Duration : " + (System.nanoTime()-startTime)/1000);
    
    }

}