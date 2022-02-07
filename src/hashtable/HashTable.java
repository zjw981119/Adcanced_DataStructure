package hashtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * HashTable class.
 */
public class HashTable {

  private Node[] headArray;
  //the total number of keys
  private int keyAmount;
  private final double peakLoad;
  private int arraySize;

  /**
   * Node class that represents key, data next and prev.
   */
  public class Node {
    // key: word
    String key;
    // data: count of words
    int data;
    Node next;
    Node prev;

    /**
     * Node class constructor.
     *
     * @param key  key
     * @param data data
     */
    public Node(String key, int data) {
      this.key = key;
      this.data = data;
      this.next = null;
      this.prev = null;
    }
  }

  /**
   * Create a hash table object, with initial keyAmount 0 and arraySize 10007
   */
  public HashTable() {
    peakLoad = 0.7;
    keyAmount = 0;
    //initial array size
    arraySize = 10007;
    headArray = new Node[arraySize];
  }

  /**
   * Insert the element into hashtable.
   *
   * @param key  key
   * @param data data
   */
  public void insert(String key, int data) {
    if (key == null) {
      throw new IllegalArgumentException();
    }

    //resize the hash table
    if ((keyAmount * 1.0 / headArray.length) > peakLoad) {
      resize();
    }

    //hash function
    int index = hash(key,arraySize);
    //create a node object
    Node node = new Node(key, data);
    //insert the node
    input(node, headArray, index);
  }

  /**
   * Extend current hash table when keyAmount / arraySize  > peakLoad
   */
  public void resize() {
    //extend the length of the array to twice the original length
    Node[] newHeadArray = new Node[headArray.length * 2];
    arraySize = arraySize * 2;
    keyAmount = 0;
    System.out.println("resize the hash table, new arraySize = " + arraySize);
    //iterate original hash table, insert every key-value pair into new hash table
    for (int i = 0; i < headArray.length; i++) {
      if (headArray[i] != null) {
        //insert head node into new hash table
        int headIndex = hash(headArray[i].key, newHeadArray.length);
        Node rehashHeadNode = new Node(headArray[i].key, headArray[i].data);
        input(rehashHeadNode, newHeadArray, headIndex);
        //insert remaining nodes
        Node tmp = headArray[i];
        while (tmp.next != null) {
          tmp = tmp.next;
          Node rehashNextNode = new Node(tmp.key, tmp.data);
          int nextIndex = hash(tmp.key, newHeadArray.length);
          input(rehashNextNode, newHeadArray, nextIndex);
        }
      }
    }
    //update reference to headArray
    headArray = newHeadArray;

  }

  /**
   * Delete node with input key.
   *
   * @param key key
   */
  public boolean delete(String key) {
    Node node = this.find(key);
    //no key
    if (node == null) {
      return false;
      //find the node
    } else {
      //node is the head of list
      if (node.prev == null) {
        int index = hash(key,arraySize);
        headArray[index] = node.next;
        //change next node's pointer
        if (node.next != null) {
          node.next.prev = null;
        }
        //node is not the head
      } else {
        //change previous node's pointer
        node.prev.next = node.next;
        //change next node's pointer
        if (node.next != null) {
          node.next.prev = node.prev;
        }
      }
      keyAmount--;
      return true;
    }
  }

  /**
   * Increase the data of the node with input key.
   * If the key already exists, increase the value by 1,
   * otherwise insert the key with value 1 into the hashtable.
   *
   * @param key key
   */
  public void increase(String key) {
    Node node = this.find(key);
    if (node != null) {
      node.data++;
    } else {
      insert(key, 1);
    }
  }

  /**
   * Find the element with input key.
   *
   * @param key key
   * @return node
   */
  public Node find(String key) {
    //get the index
    int index = hash(key,arraySize);
    Node tmp = headArray[index];
    //list is empty
    if (tmp == null) {
      return null;
    } else {
      //check the head
      if (tmp.key.equals(key)) {
        return tmp;
      }
      //traverse the list
      while (tmp.next != null) {
        tmp = tmp.next;
        if (tmp.key.equals(key)) {
          return tmp;
        }
      }
    }
    //find no key
    return null;
  }

  /**
   * List all elements in the hashtable.
   */
  public List<String> listAllKeys() {
    List<String> nodeList = new ArrayList<>();
    for (int i = 0; i < arraySize; i++) {
      if (headArray[i] != null) {
        Node tmp = headArray[i];
        nodeList.add("Key = " + tmp.key + ", Data = " + tmp.data);
        //traverse the list
        while (tmp.next != null) {
          tmp = tmp.next;
          nodeList.add("Key = " + tmp.key + ", Data = " + tmp.data);
        }
      }
    }
    return nodeList;
  }

  private void input(Node node, Node[] array, int index) {
    //if list is empty
    if (array[index] == null) {
      array[index] = node;
      keyAmount++;
      //list isn't empty
    } else {
      Node tmp = array[index];
      //find key at head
      if (tmp.key.equals(node.key)) {
        tmp.data = tmp.data + node.data;
      } else {
        //traverse the linked list and find key
        while (tmp.next != null) {
          tmp = tmp.next;
          if (tmp.key.equals(node.key)) {
            tmp.data = tmp.data + node.data;
            return;
          }
        }
        //find no key, insert the node
        tmp.next = node;
        node.prev = tmp;
        keyAmount++;
      }
    }
  }

  /**
   * Hash function.
   *
   * @param key key
   * @return index
   */
  private int hash(String key, int length) {
    if (key == null) {
      throw new IllegalArgumentException();
    }
    // transform String to number
    int number = 0;
    for (int i = 0; i < key.length(); i++) {
      // initial: number += (int) key.charAt(i) ,finally multiply by 128
      number = number * 31 + (int) key.charAt(i);
    }
    //division method
    return Math.abs(number) % length;
  }

  public static void main(String[] args) {
    //String str = "bbbbb bbbbb aabcc aabcc abbbc";
    String str = "bbb abc";
    String[] strList = str.split("\\s+");
    HashTable hashTable = new HashTable();
    //create the hashtable
    for (String s : strList) {
      hashTable.insert(s, 1);
    }

    Scanner sc = new Scanner(System.in);
    String input = "";
    String key = "";
    int data = 0;
    while (true) {
      System.out.println("command: ");
      input = sc.next();
      switch (input) {
        case "insert":
          key = sc.next();
          data = Integer.parseInt(sc.next());
          hashTable.insert(key, data);
          break;
        case "delete":
          key = sc.next();
          boolean result = hashTable.delete(key);
          if (result) {
            System.out.println("Delete operation succeeded");
          } else {
            System.out.println("Key doesn't exist.");
          }
          break;
        case "increase":
          key = sc.next();
          hashTable.increase(key);
          break;
        case "search":
          key = sc.next();
          Node node = hashTable.find(key);
          if (node == null) {
            System.out.println("Key doesn't exist.");
          } else {
            System.out.println("Key = " + node.key + ", data = " + node.data);
          }
          break;
        case "listAll":
          List<String> allNodes = hashTable.listAllKeys();
          for (String s : allNodes) {
            System.out.println(s);
          }
          break;
        case "q":
          return;
        default:
          break;
      }
    }
  }
}
