Implement several advanced data structures in Java: **hashtable, skiplist, red-black tree and binomial heap**. Implemented interactive code in the main method, user is able to use command line to check each function of the advanced data structure.

#### 1 Hashtable

- Implement a hashtable for text, given a string as input, construct a hash with words as keys, and word counts as values. The hashtable has the ability to resize the storage size when data number reach the threshold.
- Each word(key) can only appear once in the data structure.
- Using doubly linked list to store data and manage collision.
- Operations: insert(key, value), delete(key), increase(key), find(key), list-all-keys

#### 2 Skiplist

- Skiplists are used for sorting values, but in a datastructure more efficient than lists or arrays, and more guaranteed than binary search trees(more balanced).
- Operations: insert(key), delete(key), search(key), list-all-keys

#### 3 Red-black tree

- Red-black tree is a balanced binary search tree data structure, with complex implementation.
- Operations: 
  - Binary-search-tree operations: sort, search(key), min, max, successor(key), predecessor(key) 
  - Specific red-black tree precedures: rotation(), insert(key), delete(key)

Note: Didn't implement delete function, even more complicated than insert function.

#### 4 Binomialheap

- Binomialheap is an advanced data structure which is used to implement priority queue.
- Operations: make-heap, insert(key), minimum, extractMin, union, decreaseKey, delete.

