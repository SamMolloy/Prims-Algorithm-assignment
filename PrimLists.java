//Algorithm Design and Data Structures Assignment
//Implementation of Prims Algorithm using Adjacency lists and heap

import java.io.*;
import java.util.*;


class Heap
{
    private int[] h;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        h = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = h[k];

        // code yourself
        // must use hPos[] and dist[] arrays

        h[0] = 0;
        dist[0] = Integer.MIN_VALUE;

        while(dist[v] < dist[   h[k / 2]    ])
        {
            h[k] = h[k / 2];
            hPos[   h[k]    ] = k;
            k = k / 2;
        }

        h[k] = v;
        hPos[v] = k;

    }

    public void siftDown( int k) 
    {
        int v, j;
       
        v = h[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays


        j = 2 * k;
        while (j <= N)
        {
            //Checks to see if something is in the heap then locates the larger branch
            if (j + 1 <= N && dist[h[j]] > dist[h[j + 1]])
            {
                j++;
            }

            //compares the position with that of the node being moved
            if (dist[h[j]] >= dist[v])
            {
                break;
            }

            h[k] = h[j];
            k = j;
            j = k * 2;
        }

        h[k] = v;
        hPos[v] = k;

    }


    public void insert( int x) 
    {
        h[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = h[1];
        hPos[v] = 0; // v is no longer in heap
        h[N+1] = 0;  // put null node into empty spot
        
        h[1] = h[N--];
        siftDown(1);
        
        return v;
    }

}

class Graph 
{
    class Node 
    {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        
        String[] parts = line.split(splits);

        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z         
        adj = new Node[V+1];      
        for(v = 1; v <= V; ++v)
            adj[v] = z;               
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));    
            
            // write code to put edge into adjacency matrix    

            t = new Node();
            t.wgt = wgt;
            t.vert = u;
            t.next = adj[v];

            adj[v] = t;

            t = new Node();
            t.wgt = wgt;
            t.vert = v;
            t.next = adj[u];

            adj[u] = t;
            
        }	       
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v)
        {
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }


    
	public void MST_Prim(int s)
	{
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //code here

        //Distance from node to node
        dist = new int[V + 1];

        //parent node
        parent = new int[V + 1];

        //current heap position
        hPos = new int[V + 1];


        // initialising parent and position to zero, and dist to the max value
        for (v = 1; v <= V; v++)
        {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        
        dist[s] = 0;
        
        Heap pq =  new Heap(V, dist, hPos);
        pq.insert(s);

        while ( !pq.isEmpty()   )
        {
            // most of alg here

            v = pq.remove();//!!


            System.out.print("\n\nAdding edge " 
                + toChar(parent[v])  
                + "--("
                + dist[v]
                + ")--"
                + toChar(v)
                + "\n");

            //gets the sum of the weights
            wgt_sum += dist[v];

            //makes sure there are no duplicates
            dist[v] = -dist[v];

            for(t = adj[v]; t != z; t = t.next)
            {
                if(t.wgt < dist[t.vert])
                {
                    dist[t.vert] = t.wgt;
                    parent[t.vert] = v;

                    //checks if the vertex is empty and inserts a new one if it is
                    if(hPos[t.vert] == 0)
                    {
                        pq.insert(t.vert);
                    }
                    //if the vertex was not empty then it calls sift up
                    else
                    {
                        pq.siftUp(hPos[t.vert]);
                    }
                }
            }
            
        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
        
        mst = parent;                      		
	}
    
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

}

public class PrimLists 
{
    public static void main(String[] args) throws IOException
    {
        int s = 2;

        System.out.print("\n\nEnter Graph File Name:");

        Scanner input = new Scanner(System.in);

        String fname = input.nextLine();               

        Graph g = new Graph(fname);
       
        g.display();

        g.MST_Prim(s);

        g.showMST();
    }
    
    
}
