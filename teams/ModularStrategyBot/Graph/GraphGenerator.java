package ModularStrategyBot.Graph;

import ModularStrategyBot.Path.Path;
import battlecode.common.MapLocation;

public class GraphGenerator {

	int sizeX;
	int sizeY;
	Graph gr;

	public void genGraph(int sX, int sY, MapLocation[] mines) {
		gr = new Graph(sX*sY);
		
		sizeX = sX;
		sizeY = sY;
		boolean l,r,t,b;
		int index,x,y;
		for ( x = 0; x < sizeX; x++ ) {
			for ( y = 0; y < sizeY; y++ ) {
				l = !((x-1) == -1);
				r = !((x) == sizeX-1);
				t = !((y-1) == -1);
				b = !((y) == sizeY-1);
				index = convertXYtoIndex(x,y);
				
				// Upper-Left 
				if ( l && t ) 	gr.addEdge(index, convertXYtoIndex(x-1,y-1));
				// Upper 
				if ( t ) 		gr.addEdge(index, convertXYtoIndex(x,y-1));
				// Upper-Right 
				if ( r && t ) 	gr.addEdge(index, convertXYtoIndex(x+1,y-1));
				// Right 
				if ( r ) 		gr.addEdge(index, convertXYtoIndex(x+1,y));
				// Lower-Right 
				if ( r && b ) 	gr.addEdge(index, convertXYtoIndex(x+1,y+1));
				// Lower
				if ( b ) 		gr.addEdge(index, convertXYtoIndex(x,y+1));
				// Lower-Left
				if ( l && b ) 	gr.addEdge(index, convertXYtoIndex(x-1,y+1));
				// Left
				if ( l ) 		gr.addEdge(index, convertXYtoIndex(x-1,y));
				System.out.println("Gen: "+x+"\t"+y);
			}
		}
		
		for ( MapLocation ml : mines) {
			x = ml.x;
			y = ml.y;
			System.out.println("Mine: "+x+"\t"+y);
			
			l = !((x-1) == -1);
			r = !((x) == sizeX-1);
			t = !((y-1) == -1);
			b = !((y) == sizeY-1);
			index = convertXYtoIndex(x,y);
			
			// Upper-Left 
			if ( l && t ) 	gr.setWeight(index, convertXYtoIndex(x-1,y-1), gr.getWeight(index, convertXYtoIndex(x-1,y-1))+6);
			// Upper 
			if ( t ) 		gr.setWeight(index, convertXYtoIndex(x,y-1), gr.getWeight(index, convertXYtoIndex(x,y-1))+6);
			// Upper-Right 
			if ( r && t ) 	gr.setWeight(index, convertXYtoIndex(x+1,y-1), gr.getWeight(index, convertXYtoIndex(x+1,y-1))+6);
			// Right 
			if ( r ) 		gr.setWeight(index, convertXYtoIndex(x+1,y), gr.getWeight(index, convertXYtoIndex(x+1,y))+6);
			// Lower-Right 
			if ( r && b ) 	gr.setWeight(index, convertXYtoIndex(x+1,y+1), gr.getWeight(index, convertXYtoIndex(x+1,y+1))+6);
			// Lower
			if ( b ) 		gr.setWeight(index, convertXYtoIndex(x,y+1), gr.getWeight(index, convertXYtoIndex(x,y+1))+6);
			// Lower-Left
			if ( l && b ) 	gr.setWeight(index, convertXYtoIndex(x-1,y+1), gr.getWeight(index, convertXYtoIndex(x-1,y+1))+6);
			// Left
			if ( l ) 		gr.setWeight(index, convertXYtoIndex(x-1,y), gr.getWeight(index, convertXYtoIndex(x-1,y))+6);
			
		}
	}
	
	public int convertXYtoIndex(int x, int y) {
		int index = 0;
		index += (x)*sizeY;
		index += (y);
		
		return index;
	}
	public int convertIndextoX(int ind) {
		ind = ind/sizeY;
		return ind;
	}
	public int convertIndextoY(int ind) {
		ind = ind - (convertIndextoX(ind)*sizeY);
		return ind;
	}
	

	
	public Path getShortestPath(int fromX, int fromY, int toX, int toY ) {
		int from = convertXYtoIndex(fromX,fromY);
		int to = convertXYtoIndex(toX,toY);
		Path p = new Path();
		
		gr.Dijkstra(from);
		
		int next = gr.transitivePath[from][to];
		while ( next != from ) {
			MapLocation ml = new MapLocation(convertIndextoX(next),convertIndextoY(next));
			p.addLink(ml);			
			next = gr.transitivePath[from][next];
		}
		
		return p;
	}
	
}
