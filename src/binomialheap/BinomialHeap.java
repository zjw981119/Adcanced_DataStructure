package binomialheap;

import java.util.Scanner;

/**
 * Binomial heap class.
 */
public class BinomialHeap {

  private BinomialNode head;

  /**
   * BinomialNode class that represents the node in binomial heap.
   */
  private static class BinomialNode {
    int key;
    int degree;
    BinomialNode child;
    BinomialNode parent;
    BinomialNode next;
    /**
     * Node constructor.
     *
     * @param key key
     */
    public BinomialNode(int key) {
      this.key = key;
      degree = 0;
      child = null;
      parent = null;
      next = null;
    }

    @Override
    public String toString() {
      return "key = " + key;
    }
  }

  /**
   * BinomialHeap constructor.
   */
  public BinomialHeap() {
    head = null;
  }

  /**
   * Merge root1-list and root2-list by ascending order(root degree) into one root list.
   *
   * @param root1 root for list1
   * @param root2 root for list2
   * @return root of the new list
   */
  private BinomialNode merge(BinomialNode root1, BinomialNode root2) {

    //one root list is empty, return other root
    if (root1 == null) {
      return root2;
    }
    if (root2 == null) {
      return root1;
    }

    BinomialNode newRoot = null;
    //use for changing pointer
    BinomialNode current = null;
    BinomialNode prev = null;

    while (root1 != null && root2 != null) {

      //check degree, prepare for changing next pointer
      if (root1.degree <= root2.degree) {
        current = root1;
        root1 = root1.next;
      } else {
        current = root2;
        root2 = root2.next;
      }

      //set root
      if (prev == null) {
        prev = current;
        newRoot = current;
      } else {
        //change prev's next pointer
        prev.next = current;
        prev = current;
      }

      //change current's next pointer
      if (root1 != null) {
        current.next = root1;
      } else {
        current.next = root2;
      }
    }
    //System.out.println("newRoot.key="+newRoot.key);
//    System.out.println("Merge:");
//    printHeap(newRoot);
    return newRoot;

  }

  /**
   * Add child(one binomial tree) to root(another binomial tree).
   *
   * @param child root of one binomial tree
   * @param root  root of the other binomial tree.
   */
  private void linkChild(BinomialNode child, BinomialNode root) {
    child.parent = root;
    //left-most child
    child.next = root.child;
    root.child = child;
    root.degree++;
  }

  /**
   * Combine two binomial heaps(ordered).
   *
   * @param heapRoot1 root of heap1
   * @param heapRoot2 root of heap2
   * @return the root of new heap.
   */
  public BinomialNode union(BinomialNode heapRoot1, BinomialNode heapRoot2) {
    //merge two root lists by ascending order(root degree)
    BinomialNode root = merge(heapRoot1, heapRoot2);
    if (root == null) {
      return null;
    }

    BinomialNode prev = null;
    BinomialNode current = root;
    BinomialNode next = current.next;
    //combine binomial trees with same degree
    while (next != null) {
      if ((current.degree != next.degree)
              || (next.next != null && next.degree == next.next.degree)) {
        //Case1: current root's degree != next root's degree
        //Case2: current root's degree = next root's degree = next.next's degree
        //traverse forward
        prev = current;
        current = next;
      } else if (current.key <= next.key) {
        //Case3: current root's degree = next root's degree != next.next's degree
        //       current root's key <= next root's key

        //change current root's next pointer, delete next node
        current.next = next.next;
        //next root become current root's child
        linkChild(next, current);
      } else {
        //Case4: current root's degree = next root's degree != next.next's degree
        //       current root's key > next root's key

        //the root of binomial heap may change, update root
        if (prev == null) {
          root = next;
        } else{
          //change the previous node's next pointer
          prev.next = next;
        }
        //current root become next root's child
        linkChild(current, next);
        current = next;
      }
      next = current.next;
    }
    return root;
  }


  /**
   * Insert a new node using the given key.
   *
   * @param key key
   */
  public void insert(int key) {
    if (contains(key)) {
      System.out.println("Key already exists");
      return;
    }
    //BinomialNode insertNode = new BinomialNode(key);
    BinomialNode newHeap = makeHeap(key);
    //use union to insert new node.
    this.head = union(this.head, newHeap);
  }

  /**
   * Make a new binomial heap, which has only one node.
   * @param key key of new node.
   * @return head of binomial heap
   */
  public BinomialNode makeHeap(int key){
    BinomialHeap newHeap = new BinomialHeap();

    BinomialNode insertNode = new BinomialNode(key);
    newHeap.head = insertNode;
    return newHeap.head;
  }

  /**
   * Find the minimum node in binomial heap.
   *
   * @return the minimum node.
   */
  public BinomialNode minimum() {
    BinomialNode minNode = head;
    if (head == null) {
      return null;
    }
    int min = head.key;
    BinomialNode tmp = head;
    //find the minimum node
    while (tmp != null) {
      if (tmp.key < min) {
        min = tmp.key;
        minNode = tmp;
      }
      tmp = tmp.next;
    }
    return minNode;
  }

  /**
   * Reverse the child nodes(sort by ascending order in degree) of deleted nodes.
   *
   * @param root the left-most child
   * @return head of the new heap
   */
  private BinomialNode reverse(BinomialNode root) {
    if (root == null) {
      return null;
    }

    BinomialNode next;
    BinomialNode prev = null;
    //set the first child
    root.parent = null;
    while (root.next != null) {
      next = root.next;
      //reverse nodes by changing next pointer to previous node
      root.next = prev;
      prev = root;
      root = next;
      //child become root, update parent
      root.parent = null;
    }
    //set the last child's next pointer to first child
    root.next = prev;

//    System.out.println("Reverse:");
//    printHeap(root);
    return root;
  }

  /**
   * Find the minimum node and delete it from the binomial heap.
   *
   * @return return the minimum node
   */
  public BinomialNode extractMin() {
    BinomialNode minNode = minimum();
    if (minNode == null) {
      return null;
    }
    //head is the minimum node, change head
    if (head == minNode) {
      head = head.next;
    } else {
      BinomialNode prev = head;
      while (prev.next != minNode) {
        prev = prev.next;
      }
      //prev.next == minNode
      //change the next pointer of previous node of minNode
      prev.next = minNode.next;
    }

    //update the binomial heap
    head = union(head, reverse(minNode.child));
    //return minimum node
    return minNode;
  }

  /**
   * Delete the node with given key.
   *
   * @param key key
   */
  public void delete(int key){
    //decrease key to negative infinity
    decreaseKey(key,Integer.MIN_VALUE);
    //extract min
    extractMin();
  }

//  public void delete(int key) {
//    BinomialNode deleteNode = search(head, key);
//    //key doesn't exist
//    if (deleteNode == null) {
//      System.out.println("Key doesn't exist.");
//      return;
//    }
//    BinomialNode parent = deleteNode.parent;
//    //change keys and move the deleted node up to root
//    while (parent != null) {
//      int tmp = deleteNode.key;
//      deleteNode.key = parent.key;
//      parent.key = tmp;
//      //move upward
//      deleteNode = parent;
//      parent = deleteNode.parent;
//    }
//
//    //find previous node of deleted node
//    //if deleted node is head, change head
//    if (head.key == key) {
//      head = deleteNode.next;
//    } else {
//      BinomialNode prev = head;
//      while (prev.next.key != key) {
//        prev = prev.next;
//      }
//      //prev.next.key == key
//      //change the next pointer of previous node of deleted node
//      prev.next = deleteNode.next;
//    }
//
//    //update the binomial heap
//    head = union(head, reverse(deleteNode.child));
//  }

  /**
   * Decrease key in the binomial heap.
   *
   * @param originalKey original key
   * @param objectKey   object key
   */
  public void decreaseKey(int originalKey, int objectKey) {
    if (objectKey >= originalKey) {
      System.out.println("Updated key is no smaller than current key.");
      return;
    }

    //find the node.
    BinomialNode updatedNode = search(head, originalKey);
    //original key doesn't exist
    if (updatedNode == null) {
      System.out.println("Key doesn't exist.");
      return;
    }
    updatedNode.key = objectKey;
    BinomialNode child = updatedNode;
    BinomialNode parent = child.parent;
    //change key between child and parent
    while (parent != null && child.key < parent.key) {
      int tmp = parent.key;
      parent.key = child.key;
      child.key = tmp;
      //move upward
      child = parent;
      parent = child.parent;
    }
  }


  /**
   * Print binomial heap.
   */
  public void printHeap(BinomialNode node) {
    if (node == null) {
      return;
    }
    int num = 0;
    BinomialNode current = node;
    while (current != null) {
      System.out.println("BinomialHeap: B" + num);
      System.out.println(current.key + "(" + current.degree + ") is root");
      printChild(current.child, current, 1);
      num++;
      current = current.next;
    }
  }

  /**
   * Print information about child nodes
   *
   * @param node     current node
   * @param prev     previous node of current node(sibling or parent)
   * @param position 1: left child
   *                 2: sibling
   */
  private void printChild(BinomialNode node, BinomialNode prev, int position) {
    while (node != null) {
      //node is left child
      if (position == 1) {
        System.out.println(node.key + "(" + node.degree + ")" + " is " + prev.key + "'s child");
      } else {
        System.out.println(node.key + "(" + node.degree + ")" + " is " + prev.key + "'s sibling");
      }

      //DFS
      if (node.child != null) {
        printChild(node.child, node, 1);
      }

      prev = node;
      node = node.next;
      position = 2;
    }
  }


  /**
   * Search node with given key in binomial heap.
   *
   * @param node root of current binomial heap
   * @param key  key
   * @return node
   */
  private BinomialNode search(BinomialNode node, int key) {
    BinomialNode child;
    BinomialNode parent = node;
    while (parent != null) {
      if (parent.key == key) {
        return parent;
      } else {
        //recursion
        child = search(parent.child, key);
        if (child != null) {
          return child;
        }
        parent = parent.next;
      }
    }
    return null;
  }

  /**
   * Check if the binomial heap contains given key.
   *
   * @param key key
   * @return true if BH contains key.
   */
  private boolean contains(int key) {
    return search(head, key) != null;
  }


  public static void main(String[] args) {
    //int[] numberList = {12, 7, 25, 15, 28, 33, 41};
    int[] numberList = {1,2,3,4,5,6,7};
    BinomialHeap binomialHeap = new BinomialHeap();
    //create the binomial heap
    for (int s : numberList) {
      binomialHeap.insert(s);
    }

    Scanner sc = new Scanner(System.in);
    String input = "";
    int key = 0;
    BinomialNode tmp;
    while (true) {
      System.out.println("command: ");
      input = sc.next();
      switch (input) {
        case "insert":
          key = Integer.parseInt(sc.next());
          binomialHeap.insert(key);
          break;
        case "minimum":
          tmp = binomialHeap.minimum();
          System.out.println(tmp.toString());
          break;
        case "extractMin":
          tmp = binomialHeap.extractMin();
          System.out.println(tmp.toString());
          break;
        case "decreaseKey":
          key = Integer.parseInt(sc.next());
          int objectKey = Integer.parseInt(sc.next());
          binomialHeap.decreaseKey(key, objectKey);
          break;
        case "delete":
          key = Integer.parseInt(sc.next());
          binomialHeap.delete(key);
          break;
        case "print":
          binomialHeap.printHeap(binomialHeap.head);
          break;
        case "q":
          return;
        default:
          break;
      }
    }

  }
}
