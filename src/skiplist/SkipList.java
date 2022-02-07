package skiplist;

import java.util.Random;
import java.util.Scanner;


/**
 * SkipList class.
 */
public class SkipList {

  private Node head;
  private Node tail;

  //total level of skip-list
  private int listLevel;

  //possibility of going up
  private static final double PROBABILITY = 0.5;

  // negative infinity
  private static final int HEAD_KEY = Integer.MIN_VALUE;
  //positive infinity
  private static final int TAIL_KEY = Integer.MAX_VALUE;

  /**
   * Node class that represents key, up, down, left and right.
   */
  public class Node {
    private int key;
    private Node up;
    private Node down;
    private Node left;
    private Node right;

    /**
     * Node constructor.
     *
     * @param key key
     */
    public Node(int key) {
      this.key = key;
      this.up = null;
      this.down = null;
      this.left = null;
      this.right = null;
    }
  }

  /**
   * SkipList constructor.
   */
  public SkipList() {
    head = new Node(HEAD_KEY);
    tail = new Node(TAIL_KEY);
    linkHorizontal(head, tail);
    //initial level
    listLevel = 0;
  }

  /**
   * Search the node with input key at bottom
   *
   * @param key key
   * @return node at bottom
   */
  public Node search(int key) {
    Node tmp = findNode(key);
    if (tmp.key == key) {
      return tmp;
    } else {
      return null;
    }
  }

  /**
   * Insert new node.
   *
   * @param key key
   * @return false if key already exists
   * true if insertion succeeded
   */
  public boolean insert(int key) {
    Node current = findNode(key);
    //key already exists
    if (current.key == key) {
      return false;
    }

    //current is the previous node
    Node insertNode = new Node(key);
    //insert new node at bottom
    insertLink(current, insertNode);
    int currentLevel = 0;     //bottom level is 0

    //flip the coin
    Random random = new Random();
    while (random.nextDouble() < PROBABILITY) {
      //add a new level
      if (currentLevel >= listLevel) {
        Node newHead = new Node(HEAD_KEY);
        Node newTail = new Node(TAIL_KEY);
        //link horizontal nodes
        linkHorizontal(newHead, newTail);
        //link vertical nodes
        linkVertical(newHead, head);
        linkVertical(newTail, tail);
        head = newHead;
        tail = newTail;
        //already add a new level, level++
        listLevel++;
      }

      //current node find a way to go up
      while (current.up == null) {
        current = current.left;
      }
      current = current.up;

      Node extraInsertNode = new Node(key);
      //insert extra new node behind current node
      insertLink(current, extraInsertNode);
      //add vertical link
      linkVertical(extraInsertNode, insertNode);
      //change reference for next insert
      insertNode = extraInsertNode;

      //done, current level++
      currentLevel++;
    }

    return true;
  }


  /**
   * Delete the node with input key.
   *
   * @param key key
   * @return false if key doesn't exist
   * true if deletion succeeded
   */
  public boolean delete(int key) {
    Node current = findNode(key);
    //key doesn't exist
    if (current.key != key) {
      return false;
    }

    //delete node from bottom
    while (current != null) {
      linkHorizontal(current.left, current.right);
      current = current.up;
    }

    //update listLevel
    while (head.right == tail && listLevel != 0) {
      head = head.down;
      tail = tail.down;
      listLevel--;
    }

    return true;
  }

  /**
   * Print out the level of skiplist and all nodes.
   */
  public void listAllNodes() {
    System.out.println("The level of skiplist is " + listLevel);
    Node current = head;
    //reach the bottom
    while (current.down != null) {
      current = current.down;
    }
    current = current.right;
    while (current.key < TAIL_KEY) {
      System.out.print(current.key + " ");
      current = current.right;
    }
    System.out.println();
  }

  //find the node with input key at bottom(if exists)
  //otherwise return the previous node at bottom
  private Node findNode(int key) {
    Node current = head;
    while (true) {
      while (current.right.key != TAIL_KEY && current.right.key <= key) {
        //go right
        current = current.right;
      }
      if (current.down != null) {
        //go down
        current = current.down;
      } else {
        //reach the bottom list
        break;
      }
    }
    return current;
  }

  //link two horizontal nodes
  private void linkHorizontal(Node leftNode, Node rightNode) {
    leftNode.right = rightNode;
    rightNode.left = leftNode;
  }

  //link two vertical nodes
  private void linkVertical(Node upNode, Node downNode) {
    upNode.down = downNode;
    downNode.up = upNode;
  }

  //insert a new node after prevNode, need to change four pointers
  private void insertLink(Node prevNode, Node insertNode) {
    insertNode.left = prevNode;
    insertNode.right = prevNode.right;
    prevNode.right.left = insertNode;
    prevNode.right = insertNode;
  }

  /**
   * Main method.
   *
   * @param args no meaning
   */
  public static void main(String[] args) {
    SkipList skipList = new SkipList();
    Scanner sc = new Scanner(System.in);
    String input = "";
    String key = "";
    boolean result = false;
    while (true) {
      System.out.println("command: ");
      input = sc.next();
      switch (input) {
        case "insert":
          key = sc.next();
          result = skipList.insert(Integer.parseInt(key));
          if (result) {
            System.out.println("Insert operation succeeded");
          } else {
            System.out.println("Key already exists.");
          }
          skipList.listAllNodes();
          break;
        case "delete":
          key = sc.next();
          result = skipList.delete(Integer.parseInt(key));
          if (result) {
            System.out.println("Delete operation succeeded");
          } else {
            System.out.println("Key doesn't exist.");
          }
          skipList.listAllNodes();
          break;
        case "search":
          key = sc.next();
          Node node = skipList.search(Integer.parseInt(key));
          if (node == null) {
            System.out.println("Key doesn't exist.");
          } else {
            System.out.println("Key = " + node.key);
          }
          break;
        case "listAll":
          skipList.listAllNodes();
          break;
        case "q":
          return;
        default:
          break;
      }
    }
  }
}
