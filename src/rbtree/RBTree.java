package rbtree;

import java.util.Scanner;

//Tree class
public class RBTree<T extends Comparable<T>> {

  private RBTNode<T> root;
  private static final boolean RED = false;
  private static final boolean BLACK = true;

  /**
   * Node class that represents color, key, left, right and parent.
   */
  public static class RBTNode<T extends Comparable<T>> {
    boolean color;
    T key;
    RBTNode<T> left;
    RBTNode<T> right;
    RBTNode<T> parent;

    //constructor
    public RBTNode(T key, boolean color, RBTNode<T> left, RBTNode<T> right,
                   RBTNode<T> parent) {
      this.key = key;
      this.color = color;
      this.parent = parent;
      this.left = left;
      this.right = right;
    }
  }

  /**
   * RBTree constructor.
   */
  public RBTree() {
    this.root = null;
  }

  public RBTNode<T> getRoot() {
    return this.root;
  }

  /**
   * Get the parent of node.
   *
   * @param node node
   * @return parent node
   */
  public RBTNode<T> parentOf(RBTNode<T> node) {
    if (node != null) {
      return node.parent;
    }
    return null;
  }

  /**
   * Get the color of node.
   *
   * @param node node
   * @return color
   */
  public Boolean colorOf(RBTNode<T> node) {
    if (node != null) {
      return node.color;
    }
    //NIL is black
    return BLACK;
  }

  public void setParent(RBTNode<T> node, RBTNode<T> parent) {
    if (node != null) {
      node.parent = parent;
    }
  }

  public void setColor(RBTNode<T> node, Boolean color) {
    if (node != null) {
      node.color = color;
    }
  }

  public Boolean isRed(RBTNode<T> node) {
    if (node != null && node.color == RED) {
      return true;
    } else {
      return false;
    }
  }

  public Boolean isBlack(RBTNode<T> node) {
    return !isRed(node);
  }

  public void setRed(RBTNode<T> node) {
    if (node != null) {
      node.color = RED;
    }
  }

  public void setBlack(RBTNode<T> node) {
    if (node != null) {
      node.color = BLACK;
    }
  }

  /**
   * Print out the key by ascending order.
   *
   * @param node node object.
   */
  public void inorderTreeWalk(RBTNode<T> node) {
    if (node != null) {
      inorderTreeWalk(node.left);
      System.out.print(node.key + " ");
      inorderTreeWalk(node.right);
    }
  }

  /**
   * Get the height of rb-tree.
   *
   * @param node node object
   * @return height of current node.
   */
  public int getHeight(RBTNode<T> node) {
    if (node == null) {
      return 0;
    }
    int lHeight = getHeight(node.left);
    int rHeight = getHeight(node.right);
    if (lHeight >= rHeight) {
      return lHeight + 1;
    } else {
      return rHeight + 1;
    }
  }


  /**
   * Search the node with input key.
   *
   * @param x   node object
   * @param key key
   * @return node
   */
  public RBTNode<T> search(RBTNode<T> x, T key) {
    if (x == null || key.compareTo(x.key) == 0) {
      return x;
    }
    // key < x.key
    if (key.compareTo(x.key) < 0) {
      return search(x.left, key);
    } else {
      return search(x.right, key);
    }
  }

  /**
   * Get the minimum node(minimum key) in the tree.
   *
   * @param x node object
   * @return minimum node
   */
  public RBTNode<T> minimum(RBTNode<T> x) {
    while (x.left != null) {
      x = x.left;
    }
    return x;
  }

  /**
   * Get the maximum node(maximum key) in the tree.
   *
   * @param x node object
   * @return maximum node
   */
  public RBTNode<T> maximum(RBTNode<T> x) {
    while (x.right != null) {
      x = x.right;
    }
    return x;
  }

  /**
   * Get the successor of node with key.
   *
   * @param key node key
   * @return successor
   */
  public RBTNode<T> successor(T key) {
    RBTNode<T> x = search(root, key);
    if (x == null) {
      return null;
    }
    if (x.right != null) {
      return minimum(x.right);
    }
    RBTNode<T> y = x.parent;
    while (y != null && x == y.right) {
      x = y;
      y = y.parent;
    }
    return y;
  }

  /**
   * Get the predecessor of node with key.
   *
   * @param key node key
   * @return predecessor
   */
  public RBTNode<T> predecessor(T key) {
    RBTNode<T> x = search(root, key);
    if (x == null) {
      return null;
    }
    if (x.left != null) {
      return maximum(x.left);
    }
    RBTNode<T> y = x.parent;
    while (y != null && x == y.left) {
      x = y;
      y = y.parent;
    }
    return y;
  }

  /**
   * Transform the configuration of two nodes on the right into the configuration
   * on the left by changing a constant number of pointers.
   *
   * @param x current node
   */
  public void leftRotate(RBTNode<T> x) {
    // set y
    RBTNode<T> y = x.right;
    //turn y's left subtree into x's right subtree
    x.right = y.left;
    if (y.left != null) {
      y.left.parent = x;
    }
    //link x's parent to y
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } else if (x == x.parent.left) {
      x.parent.left = y;
    } else {
      x.parent.right = y;
    }
    //put x on y's left
    y.left = x;
    x.parent = y;
  }

  /**
   * Transform the configuration of two nodes on the left into the configuration
   * on the right by changing a constant number of pointers.
   *
   * @param x current node
   */
  public void rightRotate(RBTNode<T> x) {
    // set y
    RBTNode<T> y = x.left;
    //turn y's right subtree into x's left subtree
    x.left = y.right;
    if (y.right != null) {
      y.right.parent = x;
    }
    //link x's parent to y
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } else if (x == x.parent.left) {
      x.parent.left = y;
    } else {
      x.parent.right = y;
    }
    //put x on y's right
    y.right = x;
    x.parent = y;
  }

  /**
   * Insert a new node into the RBT.
   *
   * @param z new node
   */
  public void insert(RBTNode<T> z) {
    RBTNode<T> y = null;
    RBTNode<T> x = this.root;
    //insert z as in BST
    while (x != null) {
      y = x;
      //z.key < x.key
      if (z.key.compareTo(x.key) < 0) {
        x = x.left;
      } else {
        x = x.right;
      }
    }
    z.parent = y;
    if (y == null) {
      root = z;
    } else if (z.key.compareTo(y.key) < 0) {
      //z.key < y.key
      y.left = z;
    } else {
      y.right = z;
    }
    //set color
    setRed(z);
    //restore RBT properties
    insertFixUp(z);
  }

  /**
   * Restore RBT properties.
   *
   * @param z the inserted node
   */
  public void insertFixUp(RBTNode<T> z) {
    RBTNode<T> parent = parentOf(z);
    RBTNode<T> grandParent;
    // parent node exists and the color is red
    while (parent != null && isRed(parent)) {
      grandParent = parentOf(parent);
      //parent node is the left child of z's grandparent
      if (parent == grandParent.left) {
        //uncle node
        RBTNode<T> uncle = grandParent.right;
        //case 1: uncle node is red
        if (uncle != null && isRed(uncle)) {
          setBlack(parent);
          setBlack(uncle);
          setRed(grandParent);
          z = grandParent;
          parent = parentOf(z);
          continue;
        }

        if (z == parent.right) {
          //case 2: z is the right child and uncle is black
          RBTNode<T> tmp;
          leftRotate(parent);
          tmp = parent;
          parent = z;
          z = tmp;
        }
        //case 3: z is the left child and uncle is black
        setBlack(parent);
        setRed(grandParent);
        rightRotate(grandParent);
      } else {
        //parent node is the right child of z's grandparent
        //uncle node
        RBTNode<T> uncle = grandParent.left;
        //case 1: uncle node is red
        if (uncle != null && isRed(uncle)) {
          setBlack(parent);
          setBlack(uncle);
          setRed(grandParent);
          z = grandParent;
          parent = parentOf(z);
          continue;
        }

        if (z == parent.left) {
          //case 2: z is the left child and uncle is black
          RBTNode<T> tmp;
          rightRotate(parent);
          tmp = parent;
          parent = z;
          z = tmp;
        }
        //case 3: z is the left child and uncle is black
        setBlack(parent);
        setRed(grandParent);
        leftRotate(grandParent);
      }
    }
    setBlack(this.root);
  }

  /**
   * Delete a node from the RBT.
   *
   * @param z node
   */
  public void delete(RBTNode<T> z) {

  }

  public static void main(String[] args) {
    RBTree<Integer> rbTree = new RBTree<Integer>();
    // int[] iniTree = {275, 711, 260, 515, 442, 800, 900, 50, 270, 20, 30};
      int[] iniTree = {5,4,3,2,1};
    for (int key : iniTree) {
      rbTree.insert(new RBTNode<Integer>(key, RED, null, null, null));
    }
    Scanner sc = new Scanner(System.in);
    String input = "";
    String number = "";
    while (true) {
      System.out.println("command: ");
      input = sc.next();
      switch (input) {
        case "insert":
          number = sc.next();
          rbTree.insert(new RBTNode<Integer>
                  (Integer.parseInt(number), RED, null, null, null));
          System.out.println("The height of tree is: "
                  + rbTree.getHeight(rbTree.getRoot()));
          break;
        case "search":
          number = sc.next();
          RBTNode tmp = rbTree.search(rbTree.getRoot(), Integer.parseInt(number));
          if (tmp == null) {
            System.out.println("Key doesn't exist.");
          } else {
            System.out.println("Key = " + tmp.key);
          }

          break;
        case "minimum":
          System.out.println("The minimum node's key is: "
                  + rbTree.minimum(rbTree.getRoot()).key);
          break;
        case "maximum":
          System.out.println("The maximum node's key is: "
                  + rbTree.maximum(rbTree.getRoot()).key);
          break;
        case "successor":
          number = sc.next();
          RBTNode successor = rbTree.successor(Integer.parseInt(number));
          if (successor == null) {
            System.out.println("No successor");
          } else {
            System.out.println("The successor of " + number + " is "
                    + successor.key);
          }
          break;
        case "predecessor":
          number = sc.next();
          RBTNode predecessor = rbTree.predecessor(Integer.parseInt(number));
          if (predecessor == null) {
            System.out.println("No predecessor");
          } else {
            System.out.println("The predecessor of " + number + " is "
                    + predecessor.key);
          }
          break;
        case "sort":
          rbTree.inorderTreeWalk(rbTree.getRoot());
          System.out.println();
          break;
        case "q":
          return;
        default:
          break;
      }
    }
  }

}


