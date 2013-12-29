package ModularStrategyBot.Graph;

/* 2013-12-14
 * Version 1.2
 */

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import ModularStrategyBot.Path.Path;

public class Graph {
	
	public Node[] nodes;
	public int[][] weights;
	public int[][] transitiveWeights;
	public int[][] transitivePath;
	public int size;
	public int edgeCount = 0;
	
	public Graph(int sz) {
		size = sz;
		nodes = new Node[size];
		for ( int i = 0; i < size; i++ )
			nodes[i] = new Node(i);
		
		weights = new int[sz][sz];
		transitiveWeights = new int[sz][sz];
		transitivePath = new int[sz][sz];
	}
	
	public Graph clone() {
		// TODO --> Update the clone() method for the rest of the class variables.  Maybe??
		Graph g = new Graph(size);
		for ( Node in : nodes) {
			for ( Node in2 : in.edges ) {
				g.addEdge(in.id, in2.id);
			}
		}
		return g;
	}
	
	public boolean addEdge(int e1, int e2) {
		if ( nodes[e1].addEdge(nodes[e2]) ) {
			edgeCount += 2;
			setWeight(e1,e2,1);
		}
		return nodes[e2].addEdge(nodes[e1]);
	}
	
	public void setWeight(int e1, int e2, int w) {
		if ( e1 < e2 ) weights[e1][e2] = w;
		else weights[e2][e1] = w;
	}
	public int getWeight(int e1, int e2) {
		if ( e1 < e2 ) return weights[e1][e2];
		else return weights[e2][e1];
	}
	
	public void removeVertex(int in) {
		Node temp = nodes[in];
		nodes[in] = new Node(in);

		//for ( Node n : temp.edges ) {
		Node n;
		while( temp.edges.size() > 0 ) {
			//System.out.print("Loop:\t"+temp.edges.isEmpty()+"\t"+temp.edges.size());
			n = temp.edges.remove();
			//System.out.println("\t----:\t"+temp.edges.isEmpty()+"\t"+temp.edges.size());
			if (n == null) {
				System.out.println(">>> N IS NULL!!!");
				if (temp == null) { System.out.println(">>> nodes[in] IS NULL!!!"); }
				continue;
			}
			if (n.id == in) {
				System.out.println(">>> EQUALS!!!");
				continue;
			}
			if (nodes[n.id] == null) {
				System.out.println(">>> nodes[n] IS NULL!!!");
				continue;
			}
			if (temp == null) {
				System.out.println(">>> nodes[in] IS NULL!!!");
				continue;
			}
			if (nodes[n.id].edges == null) {
				System.out.println(">>> n.EDGES IS NULL!!!");
				continue;
			}
			nodes[n.id].edges.remove(temp);
			if ( nodes[n.id].degree > 0 ) nodes[n.id].degree = nodes[n.id].degree - 1;
		}
	}

	public void printGraph() {
		for ( int i = 0; i < size; i++) {
			LinkedList<Node> temp = nodes[i].edges;
			System.out.println("Node  "+i+"  Links to:");
			for ( Node n : temp ) {
				System.out.println("   Node "+n.id+"\t"+getWeight(n.id,i));
			}
		}
	}
	
	public void computeShortestPaths() {
		int k,i,j;
		for ( i = 0; i < weights.length; i++ ) {
			for ( j = 0; j < weights.length; j++ ) {
				if ( i == j ) transitiveWeights[i][j] = 0;
				else if ( ( weights[i][j] == 0 ) && ( transitiveWeights[i][j] == 0 ) ) transitiveWeights[i][j] = 9999;
				else {
					transitiveWeights[i][j] = weights[i][j];
					transitiveWeights[j][i] = weights[i][j];
				}
			}
		}

		for ( k = 0; k < weights.length; k++ ) {
			for ( i = 0; i < weights.length; i++ ) {
				for ( j = 0; j < weights.length; j++ ) {
					if ( transitiveWeights[i][j] > transitiveWeights[i][k] + transitiveWeights[k][j] ) transitiveWeights[i][j] = transitiveWeights[i][k] + transitiveWeights[k][j]; 
				}
			}
		}
	}
	
	public void copyMatrix(int[][] m1, int[][] m2) {
		int x,y;
		for ( x = 0; x < m1.length; x++ ) {
			for ( y = 0; y < m1[0].length; y++ ) {
				m2[x][y] = m1[x][y];
			}
		}
	}
	
	public void printTransitiveMatrix() {
		printMatrix(transitiveWeights);
	}
	public void printWeightMatrix() {
		printMatrix(weights);
	}
	public void printMatrix(int[][] m1) {
		System.out.print("\t");
		for ( int i = 0; i < size; i++ ) {
			System.out.print(i+"\t");
		}
		System.out.println();
		System.out.print("--------");
		for ( int i = 0; i < size; i++ ) {
			System.out.print("--------");
		}
		System.out.println();
		for ( int i = 0; i < size; i++ ) {
			System.out.print(i+"\t");
			for ( int j = 0; j < size; j++ ) {
				System.out.print(m1[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	public void calcDistanceMatrix() {
		transitiveWeights = deepCopyIntMatrix(weights);
		
		for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) if ( transitiveWeights[i][j] > 0 ) transitiveWeights[j][i] = transitiveWeights[i][j];
		
		// Loop through the matrix once and initialize any disconnected edges to infinity (or 99 in this case)
		for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) if ( transitiveWeights[i][j] == 0 ) transitiveWeights[i][j] = 9999;
		
		// Loop through the matrix n times, using a transitive-closure property to build the result matrix.
		for (int k = 0; k < size; k++ ) {
			for (int i = 0; i < size; i++ ) {
				for (int j = 0; j < size; j++ ) {
					if ( transitiveWeights[i][k] + transitiveWeights[k][j] < transitiveWeights[i][j] ) { transitiveWeights[i][j] = transitiveWeights[i][k] + transitiveWeights[k][j]; }
				}
			}
		}
	}
	
	private static int[][] deepCopyIntMatrix(int[][] input) {
	    if (input == null) return null;
	    int[][] result = new int[input.length][];
	    for (int r = 0; r < input.length; r++) {
	        result[r] = input[r].clone();
	    }
	    return result;
	}
	
	public void Dijkstra(int source) {
		int[] dist = new int[size];
		boolean[] visited = new boolean[size];
		
	    for ( Node v : nodes ) {
	        dist[v.id] = 9999;
	        visited[v.id] = false;
	        transitivePath[source][v.id] = -1;
	    }
	    
	    dist[source] = 0;
	    
		int u,uDist,alt;
	    while (true) {
		    uDist = 9999;
		    u = 0;
			for ( int i = 0; i < size; i++ ) {
				if ( ( dist[i] < uDist ) && !( visited[i] ) ) {
					uDist = dist[i];
					u = i;
				}
			}
			if ( uDist == 9999 ) break;
			
	        visited[u] = true;
	        
			//System.out.println("Vis: "+u+"\t");
			for ( Node v : nodes[u].edges ) {
				//System.out.println("   "+v.id+"\t"+dist[u]+"\t"+getWeight(u,v.id)+"\t"+dist[v.id]);
	            alt = dist[u] + getWeight(u,v.id);
	            if ( alt < dist[v.id] ) {
	                dist[v.id]  = alt;
	                transitivePath[source][v.id] = u;
	            }
	        }
			//System.out.println();
	    }
	}

}
