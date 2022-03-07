/*
 * ChainedHashTable.java
 *
 * Computer Science 112, Boston University
 * 
 * Modifications and additions by:
 *     name: Yutong Ai
 *     email: ellena0@bu.edu
 * 04/25/2021
 */

import java.util.*;     // to allow for the use of Arrays.toString() in testing

/*
 * A class that implements a hash table using separate chaining.
 */
public class ChainedHashTable implements HashTable {
    /* 
     * Private inner class for a node in a linked list
     * for a given position of the hash table
     */
    private class Node {
        private Object key;
        private LLQueue<Object> values;
        private Node next;
        
        private Node(Object key, Object value) {
            this.key = key;
            values = new LLQueue<Object>();
            values.insert(value);
            next = null;
        }
    }
    
    private Node[] table;      // the hash table itself
    private int numKeys;       // the total number of keys in the table
        
    /* hash function */
    public int h1(Object key) {
        int h1 = key.hashCode() % table.length;
        if (h1 < 0) {
            h1 += table.length;
        }
        return h1;
    }
    
    /*** Add your constructor here ***/
    public ChainedHashTable(int size){
        if(size<=0){
            throw new IllegalArgumentException("invalid size");
        }else{
            table = new Node[size];
            numKeys = 0;
        }
    }
    
    /*
     * insert - insert the specified (key, value) pair in the hash table.
     * Returns true if the pair can be added and false if there is overflow.
     */
    public boolean insert(Object key, Object value) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
            int index = h1(key);
            Node item = table[index];
            if(table[index] == null){
                table[index] = new Node(key, value);
                numKeys++;
            }else{
                while(item!=null){
                    if(key.equals(item.key)){
                        item.values.insert(value);
                        return true;
                    }
                    item = item.next;
                }
                Node newN = new Node(key, value); //here, item = null.
                newN.next = table[index];
                table[index] = newN;
                numKeys++;
            }
            return true;  
        }
    
    /*
     * search - search for the specified key and return the
     * associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> search(Object key) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
        int index = h1(key);
        Node trav = table[index];
        if(table[index]==null){ //bucket not found
            return null;
        }else{ //bucket exists
            while(trav!=null){
                if(key.equals(trav.key)){ //found in the bucket, return queue.
                    return trav.values;
                }
                trav = trav.next;
            }
            return null; //not found in the bucket, not exists.
        }   
        
    }
    
    /* 
     * remove - remove from the table the entry for the specified key
     * and return the associated collection of values, or null if the key 
     * is not in the table
     */
    public Queue<Object> remove(Object key) {
        /** Replace the following line with your implementation. **/
        if (key == null) {
            throw new IllegalArgumentException("key must be non-null");
        }
        int index = h1(key);
        if(table[index]==null){ //bucket not found
            return null;
        }else{
            Node trav = table[index];
            Node nextN = trav.next;
            Queue<Object> res = null;
            if(key.equals(trav.key)){
                res = trav.values;
                table[index] = nextN;
                numKeys--;
                return res;
            }
            while(nextN!=null){
                if(key.equals(nextN.key)){ //found in the bucket
                    res = nextN.values;
                    trav.next = nextN.next;
                    numKeys--;
                    return res;
                }
                trav = nextN;
                nextN = nextN.next;
            }
            return null; //not found in the bucket.
        }
    }
    
    
    /*** Add the other required methods here ***/
    /*
     * getNumKeys() - an accessor method for the number of keys.
     */
    public int getNumKeys(){
        return numKeys;
    }

    /*
     * load() - returns a value of type double that represents the load 
     * factor of the table: the number of keys in the table divided by 
     * the size of the table.
     */
    public double load(){
        return (double)numKeys/table.length;
    }

    /*
     * getAllKeys() - returns an array of type Object containing all of the 
     * keys in the hash table.
     */
    public Object[] getAllKeys(){
        Object[] res = new Object[numKeys];
        int count = 0;
        for(int i=0; i<table.length;i++){
            if(table[i]!=null){
                Node trav = table[i];
                while(trav!=null){
                    res[count] = trav.key;
                    count++;
                    trav = trav.next;
                }
            }   
        }
        return res;
    }

    /*
     * resize() - grows the table to have that new size. 
     */
    public void resize(int size){
        if(size<table.length){
            throw new IllegalArgumentException("new size too small");
        }
        if(size!=table.length){
            ChainedHashTable newT = new ChainedHashTable(size);
            for(int i=0; i<table.length;i++){
                if(table[i]!=null){
                    Node trav = table[i];
                    while(trav!=null){
                        newT.insert(trav.key,trav.values);
                        trav = trav.next;
                    }
                }
            }
            table = newT.table;
        }else{
            return;
        }
    }
    
    
    /*
     * toString - returns a string representation of this ChainedHashTable
     * object. *** You should NOT change this method. ***
     */
    public String toString() {
        String s = "[";
        
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                s += "null";
            } else {
                String keys = "{";
                Node trav = table[i];
                while (trav != null) {
                    keys += trav.key;
                    if (trav.next != null) {
                        keys += "; ";
                    }
                    trav = trav.next;
                }
                keys += "}";
                s += keys;
            }
        
            if (i < table.length - 1) {
                s += ", ";
            }
        }       
        
        s += "]";
        return s;
    }

    public static void main(String[] args) {
        /** Add your unit tests here **/
        /*ChainedHashTable table = new ChainedHashTable(5);
        table.insert("howdy", 15);
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        System.out.println(table.getNumKeys());
        table.insert("howdy", 25);     // insert a duplicate
        System.out.println(table.getNumKeys());
        System.out.println(table);
        System.out.println(table.search("howdy"));
        System.out.println(table.remove("apple"));
        System.out.println(table);
        
        ChainedHashTable table = new ChainedHashTable(5);
        table.insert("howdy", 15);
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        System.out.println(table.load());
        table.insert("pear", 6);
        System.out.println(table.load());
        

        ChainedHashTable table = new ChainedHashTable(5);
        table.insert("howdy", 15);
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        table.insert("howdy", 25);    // insert a duplicate
        Object[] keys = table.getAllKeys();
        System.out.println(Arrays.toString(keys));
        

        ChainedHashTable table = new ChainedHashTable(5);
        table.insert("howdy", 15);
        table.insert("goodbye", 10);
        table.insert("apple", 5);
        System.out.println(table);
        table.resize(7);
        System.out.println(table);
        */
        
        System.out.println("(1.1) Testing on insert");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            boolean results = table.insert("apple", 5);
            boolean expected = true;
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println(table); 
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.println("[{appl}, null, null, null, null]"); 
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == expected);
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(1.2) Testing on insert");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("apple", 5);
            boolean results = table.insert("howdy", 15);
            boolean expected = true;
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println(table); 
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.println("[{howdy; apple}, null, null, null, null]"); 
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == expected);
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 
        
        System.out.println("(2.1) Testing on search");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            Queue<Object> results = table.search("apple");
            Queue<Object> expected = new LLQueue<Object>();
            expected.insert(5);
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println(table); 
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.peek() == expected.peek());
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(2.2) Testing on search");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            table.insert("howdy", 25);  
            Queue<Object> results = table.search("howdy");
            Queue<Object> expected = new LLQueue<Object>();
            expected.insert(15);
            expected.insert(25);
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println(table); 
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.peek() == expected.peek());
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(3.1) Testing on remove");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            System.out.println(table); 
            Queue<Object> results = table.remove("apple");
            Queue<Object> expected = new LLQueue<Object>();
            expected.insert(5);
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println(table); 
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.peek() == expected.peek());
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(3.2) Testing on remove");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            table.insert("howdy", 25);  
            System.out.println(table); 
            Queue<Object> results = table.remove("howdy");
            Queue<Object> expected = new LLQueue<Object>();
            expected.insert(15);
            expected.insert(25);
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println(table); 
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.peek() == expected.peek());
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 
        
        System.out.println("(4.1) Testing on getNumKeys");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            table.insert("howdy", 25);
            table.remove("howdy");
            System.out.println(table); 
            int results = table.getNumKeys();
            int expected = 2;
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == expected);
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(4.2) Testing on getNumKeys");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("apple", 5);
            table.insert("howdy", 25);
            table.insert("apple", 30);
            System.out.println(table); 
            int results = table.getNumKeys();
            int expected = 2;
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == expected);
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 
       
        System.out.println("(5.1) Testing on load");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            double results = table.load();
            double expected = 0.6;
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == expected);
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(5.2) Testing on load");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            table.insert("pear", 6);
            double results = table.load();
            double expected = 0.8;
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results == expected);
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 
        
        System.out.println("(6.1) Testing on getAllKeys");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            table.insert("howdy", 25); 
            String results = Arrays.toString(table.getAllKeys());
            String expected = "[apple, howdy, goodbye]";
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.equals(expected));
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(6.2) Testing on getAllKeys");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5);
            table.insert("howdy", 25); 
            table.insert("howdy", 10); 
            table.insert("apple", 15);
            String results = Arrays.toString(table.getAllKeys());
            String expected = "[apple, howdy, goodbye]";
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.equals(expected));
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 
         
        System.out.println("(7.1) Testing on resize");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5); 
            table.resize(7);
            String results = table.toString();
            String expected = "[null, {apple}, null, null, null, {howdy}, {goodbye}]";
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.equals(expected));
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 

        System.out.println("(7.2) Testing on resize");
        try {
            ChainedHashTable table = new ChainedHashTable(5);
            table.insert("howdy", 15);
            table.insert("goodbye", 10);
            table.insert("apple", 5); 
            table.insert("apple", 10); 
            table.resize(7);
            String results = table.toString();
            String expected = "[null, {apple}, null, null, null, {howdy}, {goodbye}]";
            System.out.println("actual results:");
            System.out.println(results);
            System.out.println("expected results:");
            System.out.println(expected);
            System.out.print("MATCHES EXPECTED RESULTS?: ");
            System.out.println(results.equals(expected));
        } catch (Exception e) {
            System.out.println("INCORRECTLY THREW AN EXCEPTION: " + e);
        }
        System.out.println(); 
        

    }
}
