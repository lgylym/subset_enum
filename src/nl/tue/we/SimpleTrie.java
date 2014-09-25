package nl.tue.we;

import java.util.*;

/**
 * Created by yluo on 2/19/14.
 */


public class SimpleTrie {

    protected SimpleTrieNode root;

    public class SimpleTrieNode {
        SimpleTrieNode left;//0
        SimpleTrieNode right;//1
        long data;

        public SimpleTrieNode(long data) {
            this.left = null;
            this.right = null;
            this.data = data;
        }

    }

    public SimpleTrie() {
        root = new SimpleTrieNode(0);
    }

    /**
     * put part of the signature into the trie
     * @param r part of the signature, start from the head
     */
    public void put(long r) {
        //SimpleTrieNode currentNode = root;

        insert(root, r, Long.SIZE-1);
//        //iterate over bits of sigPart
//        for(int i = Long.SIZE-1; i >= 0; i--) {
//            long currentBit = (r>>i)&1;
//
//            if(currentBit == 1) {
//                if(currentNode.right == null) {
//                    currentNode.right = new SimpleTrieNode();
//                }
//                currentNode = currentNode.right;
//            }else {//currentBit == 0
//                if(currentNode.left == null) {
//                    currentNode.left = new SimpleTrieNode();
//                }
//                currentNode = currentNode.left;
//            }
//        }
    }

    private void insert(SimpleTrieNode node, long r, int cursor) {
        if(cursor < 0) {
            return;
        }
        long currentBit = (r>>cursor)&1;
        //System.out.println(cursor);
        //System.out.println("***"+Long.toBinaryString(r));
        if(currentBit == 1) {
            if(node.right == null) {
                node.right = new SimpleTrieNode(1);
            }
            insert(node.right, r, cursor-1);
        }else {
            if(node.left == null) {
                node.left = new SimpleTrieNode(0);
            }
            insert(node.left, r, cursor-1);
        }
    }

    /**
     * print the trie in a bfs manner
     * basically for testing purpose
     */
    public void printBFS() {
        LinkedList<SimpleTrieNode> queue = new LinkedList<SimpleTrieNode>();
        queue.add(root);
        while(!queue.isEmpty()) {
            SimpleTrieNode curr =  queue.poll();
            if(curr.left != null) {
                System.out.print(0+"x");
                queue.add(curr.left);
            }

            if(curr.right != null) {
                System.out.print(1+"x");
                queue.add(curr.right);
            }
        }
    }

    public void printMe() {
        printTree(root,0);
    }


    public void printTree(SimpleTrieNode node, int depth) {
        if(node == null) {
            return;
        }

        System.out.print(node.data + "*");
        printTree(node.left, depth + 1);
        printTree(node.right, depth+1);
        //System.out.print(" depth"+depth+":");
        //System.out.print(" ");
//        if(node.left != null) {
//            System.out.print("0");
//            printTree(node.left, depth+1);
//        }
//        if(node.right != null) {
//            System.out.print("1");
//            printTree(node.right, depth+1);
//        }
        //System.out.print("\n");
    }


    /**
     * get subsets of a given sigPart, that exist in the trie
     * @param r
     * @return
     */
    public long getSubsets(long r) {
        long count = 0;
        LinkedList<SimpleTrieNode> next = new LinkedList<SimpleTrieNode>();
        LinkedList<SimpleTrieNode> curr = new LinkedList<SimpleTrieNode>();
        LinkedList<SimpleTrieNode> temp;

        next.add(root);

        for(int i = Long.SIZE-1; i >= 0; i--) {
            temp = curr;
            curr = next;
            next = temp;
            next.clear();
            long currentBit = (r>>i)&1;

            //System.out.println(currentBit);

            if(currentBit == 1) {
                for(SimpleTrieNode node:curr) {
                    if(node.left != null) next.add(node.left);
                    if(node.right != null) next.add(node.right);
                }
            }else {//currentBit == 0
                for(SimpleTrieNode node:curr) {
                    if(node.left != null) next.add(node.left);
                }
            }
        }

        count += next.size();
        return count;
    }

}
