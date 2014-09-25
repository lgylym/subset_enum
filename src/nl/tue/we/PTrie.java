package nl.tue.we;

import java.util.BitSet;

/**
 * Patricia Trie, storing the long bits version, fix to 64 bits
 * For experiment comparison purpose only
 * Long is considered as a bit string from left to right
 * Created by yluo on 9/23/2014.
 */
public class PTrie {

    PTrieNode root;

    public static long[] lmasks = new long[Long.SIZE];
    public static long[] rmasks = new long[Long.SIZE];



    public class PTrieNode{
        PTrieNode left;
        PTrieNode right;
        int start;
        int end;
        long prefix;//prefix[start,...,end)

        public PTrieNode(long s) {
            prefix = s;
            left = null;
            right = null;
            start = 0;
            end = Long.SIZE;
        }


        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("start:" + start + ",end:" + end+" signature:" + Long.toBinaryString(prefix));
            return result.toString();
        }

    }

    public PTrie() {
        root = null;

        lmasks[0] = -1;//1111...1111
        rmasks[Long.SIZE-1] = -1;//1111...1111
        for(int i = 1; i < lmasks.length; i++) {
            lmasks[i] = lmasks[i-1]>>>1;
        }

        for(int i = rmasks.length-2; i >= 0; i--) {
            rmasks[i] = rmasks[i+1]<<1;
        }

        /*
        lmask
        1111
        0111
        0011
        0001

        rmask
        1000
        1100
        1110
        1111*/
    }


    public void print() {
        print(root);
    }

    private void print(PTrieNode node) {
        System.out.println(node);
        if(node.left != null) {
            print(node.left);
        }
        if(node.right != null) {
            print(node.right);
        }
    }


    public void put(long r) {
        if(root == null) {
            root = new PTrieNode(r);
        }else {
            root = insert(root,r);
        }
    }

    private PTrieNode insert(PTrieNode node, long r) {

        long n_prefix = getSubstring(node.prefix, node.start, node.end);
        long r_prefix = getSubstring(r, node.start, node.end);
        long result = n_prefix ^ r_prefix;
        if(result == 0) {//two prefixes are equal
            //node.end is the splitting point
            if(node.end == Long.SIZE) {
                //now we reach the end, do nothing
                return node;
            }
            boolean currentBit = get(r, node.end);
            //insert to the next level
            if(currentBit) {
                node.right = insert(node.right, r);
//                if(node.right == null) {
//                    node.right = new PTrieNode(r);
//                    node.right.start = node.end;
//                }else {
//                    node.right = insert(node.right, r);
//                }
            }else {
                node.left = insert(node.left, r);
//                if(node.left == null) {
//                    node.left = new PTrieNode(r);
//                    node.left.start = node.end;
//                }else {
//                    node.left = insert(node.left, r);
//                }
            }
            return node;

        }else {//two prefixes are not equal
            int splitting = Long.numberOfLeadingZeros(result);
            //splitting should be between [node.start, node.end)
            PTrieNode newParent = new PTrieNode(node.prefix);
            PTrieNode newChild = new PTrieNode(r);

            newParent.start = node.start;
            newParent.end = splitting;

            newChild.start = splitting;

            node.start = splitting;

            boolean currentBit = get(r,splitting);
            if(currentBit) {
                newParent.left = node;
                newParent.right = newChild;
            }else {
                newParent.left = newChild;
                newParent.right = node;
            }
            return newParent;
        }
    }


    public long getSubsets(long r) {
        return getSubset(root, r);
    }



    private long getSubset(PTrieNode node, long r) {
        if(node == null) {return 0;}
        long count = 0;
        long r_prefix = getSubstring(r, node.start, node.end);
        long n_prefix = getSubstring(node.prefix, node.start, node.end);
        //make sure r_prefix contains n_prefix
        long result = (~r_prefix)&n_prefix;
        if(result == 0) {//r_prefix contains n_prefix
            //System.out.println("hit:" + node);
            if(node.end == Long.SIZE) {
                count++;
            }else {
                boolean bit = get(r,node.end);
                if(bit) {
                    count += getSubset(node.right, r);
                    count += getSubset(node.left, r);
                }else {
                    count += getSubset(node.left, r);
                }
            }
        }
        return count;
    }




    /**
     *
     * @param r
     * @param from
     * @param to
     * @return r[from,to), from <= to
     */
    private static long getSubstring(long r, int from, int to) {
        assert from <= to;
        if(from == to) {
            //no common prefix to share
            return 0;
        }else {
            return lmasks[from]&r&rmasks[to-1];
        }
    }

    /**
     * get the idx position of r
     * @param r
     * @param idx
     * @return
     */
    private static boolean get(long r, int idx) {
        assert idx >= 0;
        assert idx < Long.SIZE;
        if(((Long.MIN_VALUE >>> idx)&r) == 0) {
            return false;
        }else {
            return true;
        }
    }
}
