package ModularStrategyBot.Graph;

import ModularStrategyBot.Path.Path;
import ModularStrategyBot.Path.TailPath;
import battlecode.common.MapLocation;

public class PathGenerator implements Runnable {

	static int height;
	static int width;
	static MapLocation[] mines;
	static MapLocation start;
	static MapLocation finish;
	
	public static void main(String[] args) {
		width = 55;
		height = 25;
		start = new MapLocation(54,24);
		finish = new MapLocation(0,0);
		MapLocation[] mines2 = {
		new MapLocation(53,18),
		new MapLocation(49,20),
		new MapLocation(52,18),
		new MapLocation(45,22),
		new MapLocation(47,22),
		new MapLocation(54,18),
		new MapLocation(50,20),
		new MapLocation(46,22),
		new MapLocation(48,22),
		new MapLocation(0,2),
		new MapLocation(51,20),
		new MapLocation(53,20),
		new MapLocation(49,22),
		new MapLocation(1,2),
		new MapLocation(52,20),
		new MapLocation(44,20),
		new MapLocation(48,18),
		new MapLocation(41,22),
		new MapLocation(54,14),
		new MapLocation(47,18),
		new MapLocation(40,22),
		new MapLocation(2,2),
		new MapLocation(51,16),
		new MapLocation(3,2),
		new MapLocation(49,18),
		new MapLocation(42,22),
		new MapLocation(45,20),
		new MapLocation(52,16),
		new MapLocation(50,18),
		new MapLocation(43,22),
		new MapLocation(4,2),
		new MapLocation(47,20),
		new MapLocation(46,20),
		new MapLocation(5,2),
		new MapLocation(53,16),
		new MapLocation(1,4),
		new MapLocation(2,4),
		new MapLocation(48,20),
		new MapLocation(6,2),
		new MapLocation(44,22),
		new MapLocation(51,18),
		new MapLocation(7,2),
		new MapLocation(0,6),
		new MapLocation(3,4),
		new MapLocation(5,4),
		new MapLocation(4,4),
		new MapLocation(8,2),
		new MapLocation(1,6),
		new MapLocation(6,4),
		new MapLocation(9,2),
		new MapLocation(2,6),
		new MapLocation(4,6),
		new MapLocation(11,2),
		new MapLocation(10,2),
		new MapLocation(3,6),
		new MapLocation(7,4),
		new MapLocation(50,22),
		new MapLocation(5,6),
		new MapLocation(12,2),
		new MapLocation(1,8),
		new MapLocation(8,4),
		new MapLocation(51,22),
		new MapLocation(10,4),
		new MapLocation(3,8),
		new MapLocation(6,6),
		new MapLocation(13,2),
		new MapLocation(9,4),
		new MapLocation(52,22),
		new MapLocation(2,8),
		new MapLocation(4,8),
		new MapLocation(11,4),
		new MapLocation(53,22),
		new MapLocation(0,10),
		new MapLocation(7,6),
		new MapLocation(14,2),
		new MapLocation(16,2),
		new MapLocation(9,6),
		new MapLocation(12,4),
		new MapLocation(5,8),
		new MapLocation(54,22),
		new MapLocation(8,6),
		new MapLocation(1,10),
		new MapLocation(15,2),
		new MapLocation(7,8),
		new MapLocation(14,4),
		new MapLocation(0,12),
		new MapLocation(4,10),
		new MapLocation(11,6),
		new MapLocation(18,2),
		new MapLocation(15,4),
		new MapLocation(2,10),
		new MapLocation(6,8),
		new MapLocation(13,4),
		new MapLocation(17,2),
		new MapLocation(10,6),
		new MapLocation(3,10),
		new MapLocation(13,6),
		new MapLocation(20,2),
		new MapLocation(6,10),
		new MapLocation(17,4),
		new MapLocation(3,12),
		new MapLocation(10,8),
		new MapLocation(21,2),
		new MapLocation(8,8),
		new MapLocation(1,12),
		new MapLocation(19,2),
		new MapLocation(12,6),
		new MapLocation(5,10),
		new MapLocation(16,4),
		new MapLocation(9,8),
		new MapLocation(2,12),
		new MapLocation(5,12),
		new MapLocation(12,8),
		new MapLocation(19,4),
		new MapLocation(2,14),
		new MapLocation(9,10),
		new MapLocation(16,6),
		new MapLocation(23,2),
		new MapLocation(18,4),
		new MapLocation(11,8),
		new MapLocation(14,6),
		new MapLocation(7,10),
		new MapLocation(8,10),
		new MapLocation(1,14),
		new MapLocation(22,2),
		new MapLocation(15,6),
		new MapLocation(4,12),
		new MapLocation(11,10),
		new MapLocation(18,6),
		new MapLocation(4,14),
		new MapLocation(0,16),
		new MapLocation(25,2),
		new MapLocation(1,16),
		new MapLocation(8,12),
		new MapLocation(15,8),
		new MapLocation(22,4),
		new MapLocation(24,2),
		new MapLocation(17,6),
		new MapLocation(20,4),
		new MapLocation(13,8),
		new MapLocation(6,12),
		new MapLocation(21,4),
		new MapLocation(14,8),
		new MapLocation(7,12),
		new MapLocation(10,10),
		new MapLocation(3,14),
		new MapLocation(28,2),
		new MapLocation(21,6),
		new MapLocation(14,10),
		new MapLocation(7,14),
		new MapLocation(6,14),
		new MapLocation(10,12),
		new MapLocation(3,16),
		new MapLocation(24,4),
		new MapLocation(17,8),
		new MapLocation(2,16),
		new MapLocation(9,12),
		new MapLocation(16,8),
		new MapLocation(13,10),
		new MapLocation(20,6),
		new MapLocation(27,2),
		new MapLocation(5,14),
		new MapLocation(12,10),
		new MapLocation(19,6),
		new MapLocation(26,2),
		new MapLocation(23,4),
		new MapLocation(2,18),
		new MapLocation(20,8),
		new MapLocation(13,12),
		new MapLocation(6,16),
		new MapLocation(12,12),
		new MapLocation(5,16),
		new MapLocation(30,2),
		new MapLocation(23,6),
		new MapLocation(16,10),
		new MapLocation(9,14),
		new MapLocation(15,10),
		new MapLocation(22,6),
		new MapLocation(1,18),
		new MapLocation(8,14),
		new MapLocation(19,8),
		new MapLocation(26,4),
		new MapLocation(25,4),
		new MapLocation(11,12),
		new MapLocation(18,8),
		new MapLocation(4,16),
		new MapLocation(29,2),
		new MapLocation(12,14),
		new MapLocation(5,18),
		new MapLocation(26,6),
		new MapLocation(19,10),
		new MapLocation(33,2),
		new MapLocation(8,16),
		new MapLocation(1,20),
		new MapLocation(22,8),
		new MapLocation(15,12),
		new MapLocation(29,4),
		new MapLocation(4,18),
		new MapLocation(18,10),
		new MapLocation(11,14),
		new MapLocation(25,6),
		new MapLocation(32,2),
		new MapLocation(0,20),
		new MapLocation(7,16),
		new MapLocation(14,12),
		new MapLocation(21,8),
		new MapLocation(28,4),
		new MapLocation(3,18),
		new MapLocation(10,14),
		new MapLocation(17,10),
		new MapLocation(24,6),
		new MapLocation(31,2),
		new MapLocation(32,4),
		new MapLocation(25,8),
		new MapLocation(18,12),
		new MapLocation(11,16),
		new MapLocation(14,14),
		new MapLocation(7,18),
		new MapLocation(35,2),
		new MapLocation(28,6),
		new MapLocation(21,10),
		new MapLocation(24,8),
		new MapLocation(17,12),
		new MapLocation(10,16),
		new MapLocation(3,20),
		new MapLocation(6,18),
		new MapLocation(31,4),
		new MapLocation(27,6),
		new MapLocation(34,2),
		new MapLocation(13,14),
		new MapLocation(20,10),
		new MapLocation(9,16),
		new MapLocation(16,12),
		new MapLocation(2,20),
		new MapLocation(23,8),
		new MapLocation(30,4),
		new MapLocation(14,16),
		new MapLocation(7,20),
		new MapLocation(28,8),
		new MapLocation(21,12),
		new MapLocation(35,4),
		new MapLocation(10,18),
		new MapLocation(3,22),
		new MapLocation(4,22),
		new MapLocation(18,14),
		new MapLocation(11,18),
		new MapLocation(32,6),
		new MapLocation(25,10),
		new MapLocation(39,2),
		new MapLocation(33,6),
		new MapLocation(40,2),
		new MapLocation(8,20),
		new MapLocation(15,16),
		new MapLocation(22,12),
		new MapLocation(29,8),
		new MapLocation(36,4),
		new MapLocation(23,12),
		new MapLocation(30,8),
		new MapLocation(37,4),
		new MapLocation(5,22),
		new MapLocation(12,18),
		new MapLocation(19,14),
		new MapLocation(26,10),
		new MapLocation(22,10),
		new MapLocation(15,14),
		new MapLocation(8,18),
		new MapLocation(1,22),
		new MapLocation(4,20),
		new MapLocation(36,2),
		new MapLocation(29,6),
		new MapLocation(12,16),
		new MapLocation(5,20),
		new MapLocation(37,2),
		new MapLocation(33,4),
		new MapLocation(26,8),
		new MapLocation(19,12),
		new MapLocation(2,22),
		new MapLocation(34,4),
		new MapLocation(23,10),
		new MapLocation(30,6),
		new MapLocation(9,18),
		new MapLocation(16,14),
		new MapLocation(31,6),
		new MapLocation(38,2),
		new MapLocation(17,14),
		new MapLocation(24,10),
		new MapLocation(13,16),
		new MapLocation(20,12),
		new MapLocation(6,20),
		new MapLocation(8,22),
		new MapLocation(22,14),
		new MapLocation(15,18),
		new MapLocation(26,12),
		new MapLocation(19,16),
		new MapLocation(40,4),
		new MapLocation(33,8),
		new MapLocation(44,2),
		new MapLocation(37,6),
		new MapLocation(12,20),
		new MapLocation(16,18),
		new MapLocation(9,22),
		new MapLocation(30,10),
		new MapLocation(23,14),
		new MapLocation(34,8),
		new MapLocation(41,4),
		new MapLocation(45,2),
		new MapLocation(13,20),
		new MapLocation(20,16),
		new MapLocation(17,18),
		new MapLocation(24,14),
		new MapLocation(31,10),
		new MapLocation(38,6),
		new MapLocation(35,8),
		new MapLocation(42,4),
		new MapLocation(10,22),
		new MapLocation(16,16),
		new MapLocation(9,20),
		new MapLocation(41,2),
		new MapLocation(34,6),
		new MapLocation(27,10),
		new MapLocation(20,14),
		new MapLocation(13,18),
		new MapLocation(6,22),
		new MapLocation(38,4),
		new MapLocation(31,8),
		new MapLocation(24,12),
		new MapLocation(17,16),
		new MapLocation(10,20),
		new MapLocation(35,6),
		new MapLocation(42,2),
		new MapLocation(21,14),
		new MapLocation(28,10),
		new MapLocation(7,22),
		new MapLocation(14,18),
		new MapLocation(39,4),
		new MapLocation(25,12),
		new MapLocation(32,8),
		new MapLocation(11,20),
		new MapLocation(18,16),
		new MapLocation(43,2),
		new MapLocation(29,10),
		new MapLocation(36,6),
		new MapLocation(21,18),
		new MapLocation(28,14),
		new MapLocation(35,10),
		new MapLocation(42,6),
		new MapLocation(49,2),
		new MapLocation(17,20),
		new MapLocation(24,16),
		new MapLocation(31,12),
		new MapLocation(38,8),
		new MapLocation(45,4),
		new MapLocation(13,22),
		new MapLocation(20,18),
		new MapLocation(27,14),
		new MapLocation(34,10),
		new MapLocation(47,4),
		new MapLocation(22,18),
		new MapLocation(15,22),
		new MapLocation(36,10),
		new MapLocation(29,14),
		new MapLocation(50,2),
		new MapLocation(43,6),
		new MapLocation(18,20),
		new MapLocation(32,12),
		new MapLocation(25,16),
		new MapLocation(46,4),
		new MapLocation(39,8),
		new MapLocation(14,22),
		new MapLocation(29,12),
		new MapLocation(36,8),
		new MapLocation(15,20),
		new MapLocation(22,16),
		new MapLocation(11,22),
		new MapLocation(18,18),
		new MapLocation(43,4),
		new MapLocation(39,6),
		new MapLocation(46,2),
		new MapLocation(25,14),
		new MapLocation(32,10),
		new MapLocation(21,16),
		new MapLocation(28,12),
		new MapLocation(14,20),
		new MapLocation(16,20),
		new MapLocation(48,2),
		new MapLocation(41,6),
		new MapLocation(44,4),
		new MapLocation(37,8),
		new MapLocation(30,12),
		new MapLocation(23,16),
		new MapLocation(26,14),
		new MapLocation(19,18),
		new MapLocation(12,22),
		new MapLocation(47,2),
		new MapLocation(40,6),
		new MapLocation(33,10),
		new MapLocation(22,20),
		new MapLocation(29,16),
		new MapLocation(36,12),
		new MapLocation(33,14),
		new MapLocation(40,10),
		new MapLocation(47,6),
		new MapLocation(25,18),
		new MapLocation(32,14),
		new MapLocation(39,10),
		new MapLocation(46,6),
		new MapLocation(43,8),
		new MapLocation(50,4),
		new MapLocation(18,22),
		new MapLocation(48,6),
		new MapLocation(41,10),
		new MapLocation(20,22),
		new MapLocation(34,14),
		new MapLocation(27,18),
		new MapLocation(51,4),
		new MapLocation(26,18),
		new MapLocation(19,22),
		new MapLocation(30,16),
		new MapLocation(23,20),
		new MapLocation(44,8),
		new MapLocation(37,12),
		new MapLocation(23,18),
		new MapLocation(30,14),
		new MapLocation(16,22),
		new MapLocation(41,8),
		new MapLocation(48,4),
		new MapLocation(34,12),
		new MapLocation(33,12),
		new MapLocation(40,8),
		new MapLocation(19,20),
		new MapLocation(26,16),
		new MapLocation(51,2),
		new MapLocation(37,10),
		new MapLocation(44,6),
		new MapLocation(49,4),
		new MapLocation(42,8),
		new MapLocation(35,12),
		new MapLocation(28,16),
		new MapLocation(21,20),
		new MapLocation(53,2),
		new MapLocation(20,20),
		new MapLocation(52,2),
		new MapLocation(45,6),
		new MapLocation(38,10),
		new MapLocation(31,14),
		new MapLocation(24,18),
		new MapLocation(17,22),
		new MapLocation(49,8),
		new MapLocation(35,16),
		new MapLocation(42,12),
		new MapLocation(31,18),
		new MapLocation(38,14),
		new MapLocation(24,22),
		new MapLocation(39,14),
		new MapLocation(46,10),
		new MapLocation(25,22),
		new MapLocation(32,18),
		new MapLocation(28,20),
		new MapLocation(53,6),
		new MapLocation(30,18),
		new MapLocation(23,22),
		new MapLocation(51,6),
		new MapLocation(44,10),
		new MapLocation(37,14),
		new MapLocation(52,6),
		new MapLocation(45,10),
		new MapLocation(48,8),
		new MapLocation(41,12),
		new MapLocation(34,16),
		new MapLocation(29,18),
		new MapLocation(36,14),
		new MapLocation(43,10),
		new MapLocation(50,6),
		new MapLocation(25,20),
		new MapLocation(32,16),
		new MapLocation(26,20),
		new MapLocation(33,16),
		new MapLocation(40,12),
		new MapLocation(47,8),
		new MapLocation(54,4),
		new MapLocation(22,22),
		new MapLocation(49,6),
		new MapLocation(24,20),
		new MapLocation(38,12),
		new MapLocation(31,16),
		new MapLocation(52,4),
		new MapLocation(45,8),
		new MapLocation(46,8),
		new MapLocation(39,12),
		new MapLocation(53,4),
		new MapLocation(28,18),
		new MapLocation(21,22),
		new MapLocation(42,10),
		new MapLocation(35,14),
		new MapLocation(43,14),
		new MapLocation(50,10),
		new MapLocation(29,22),
		new MapLocation(36,18),
		new MapLocation(47,12),
		new MapLocation(54,8),
		new MapLocation(33,20),
		new MapLocation(40,16),
		new MapLocation(51,10),
		new MapLocation(37,18),
		new MapLocation(44,14),
		new MapLocation(49,10),
		new MapLocation(42,14),
		new MapLocation(35,18),
		new MapLocation(28,22),
		new MapLocation(53,8),
		new MapLocation(46,12),
		new MapLocation(39,16),
		new MapLocation(32,20),
		new MapLocation(30,20),
		new MapLocation(37,16),
		new MapLocation(44,12),
		new MapLocation(41,14),
		new MapLocation(48,10),
		new MapLocation(27,22),
		new MapLocation(34,18),
		new MapLocation(31,20),
		new MapLocation(38,16),
		new MapLocation(45,12),
		new MapLocation(52,8),
		new MapLocation(50,8),
		new MapLocation(43,12),
		new MapLocation(36,16),
		new MapLocation(29,20),
		new MapLocation(40,14),
		new MapLocation(33,18),
		new MapLocation(47,10),
		new MapLocation(51,8),
		new MapLocation(26,22),
		new MapLocation(49,14),
		new MapLocation(52,12),
		new MapLocation(45,16),
		new MapLocation(38,20),
		new MapLocation(34,22),
		new MapLocation(48,14),
		new MapLocation(41,18),
		new MapLocation(37,20),
		new MapLocation(44,16),
		new MapLocation(51,12),
		new MapLocation(47,14),
		new MapLocation(33,22),
		new MapLocation(40,18),
		new MapLocation(36,20),
		new MapLocation(50,12),
		new MapLocation(43,16),
		new MapLocation(32,22),
		new MapLocation(46,14),
		new MapLocation(39,18),
		new MapLocation(53,10),
		new MapLocation(42,16),
		new MapLocation(35,20),
		new MapLocation(49,12),
		new MapLocation(31,22),
		new MapLocation(38,18),
		new MapLocation(45,14),
		new MapLocation(52,10),
		new MapLocation(34,20),
		new MapLocation(41,16),
		new MapLocation(48,12),
		new MapLocation(30,22),
		new MapLocation(50,16),
		new MapLocation(43,20),
		new MapLocation(53,14),
		new MapLocation(46,18),
		new MapLocation(39,22),
		new MapLocation(38,22),
		new MapLocation(49,16),
		new MapLocation(42,20),
		new MapLocation(41,20),
		new MapLocation(48,16),
		new MapLocation(45,18),
		new MapLocation(52,14),
		new MapLocation(44,18),
		new MapLocation(37,22),
		new MapLocation(51,14),
		new MapLocation(54,12),
		new MapLocation(47,16),
		new MapLocation(40,20),
		new MapLocation(36,22),
		new MapLocation(43,18),
		new MapLocation(50,14),
		new MapLocation(35,22),
		new MapLocation(42,18),
		new MapLocation(39,20),
		new MapLocation(46,16),
		new MapLocation(53,12),
	};




























		
		
		
		
		
		mines = mines2;
		(new Thread(new PathGenerator(height,width,mines,start,finish))).start();

	}
	
	
	public PathGenerator(int mapHeight, int mapWidth, MapLocation[] mns, MapLocation startA, MapLocation startB) {
		height = mapHeight;
		width = mapWidth;
		mines = mns;
		start = startA;
		finish = startB;
	}
	
	@Override
	public void run() {
		System.out.println("Generating Path:\n"+height+"\t"+width+"\t"+mines.length+"\t"+start+"\t"+finish);
		GraphGenerator graph = new GraphGenerator();
		graph.genGraph(width,height,mines);
		Path rushPath = graph.getShortestPath(start.x, start.y, finish.x, finish.y);

		/* Add the Tail
		// Add the tail
		MapLocation last = rushPath.removeLastLink();
		MapLocation next = rushPath.removeLastLink();
		tailPath tp = new tailPath(finish.x,finish.y);
		if ( tp.checkPath(next) ) {
			Path tail = tp.getPathStartingAt(next);
			rushPath.addPathE(tail);
		}
		else {
			rushPath.addLinkE(next);
			Path tail = tp.getPathStartingAt(last);
			rushPath.addPathE(tail);
		}
		/* */
		
		/* Instead of Adding a tail, remove all tail elements and allow the bot to process it's tail generically */
		TailPath tp = new TailPath(finish.x,finish.y);
		MapLocation next;
		do { 
			next = rushPath.removeLastLink();
		} while ( tp.checkPath(next) );
		rushPath.addLinkE(next);
		
		System.out.println("Path Generated:");
		System.out.println("if ( ( mapHeight == "+height+" ) && ( mapWidth == "+width+" ) && ( startA.equals(new MapLocation("+start.x+","+start.y+")) ) && ( startB.equals(new MapLocation("+finish.x+","+finish.y+")) ) ) {");
		System.out.println("path = new Path();");
		for ( MapLocation p = rushPath.nextLink(); p != null; p = rushPath.nextLink() ) {
			//System.out.println(p.x+"\t"+p.y);
			System.out.println("path.addLinkE(new MapLocation("+p.x+","+p.y+"));");
		}		
		System.out.println("}");
	}
	
	
	public class tailPath {
		
		tailPathNode root;
		
		public tailPath(int x, int y) {
			root = new tailPathNode(x-2,y);
			tailPathNode next;
			next = root.next = new tailPathNode(x-1,y+1);
			next = next.next = new tailPathNode(x,y+1);
			next = next.next = new tailPathNode(x,y+2);
			next = next.next = new tailPathNode(x+1,y+1);
			next = next.next = new tailPathNode(x+1,y);
			next = next.next = new tailPathNode(x+2,y);
			next = next.next = new tailPathNode(x+1,y-1);
			next = next.next = new tailPathNode(x,y-1);
			next = next.next = new tailPathNode(x,y-2);
			next = next.next = new tailPathNode(x-1,y-1);
			next = next.next = new tailPathNode(x-1,y);
			next.next = root;
		}
		
		public boolean checkPath(MapLocation test) {			
			tailPathNode start = root;
			do {
				if ( start.data.equals(test)) return true;
				start = start.next;	
			} while ( start != root );
			return false;
		}
		
		public Path getPathStartingAt(MapLocation in) {
			tailPathNode start = root;
			do {
				if ( start.data.equals(in)) break;
				start = start.next;
				if ( start == root ) return null;
			} while ( true );
			
			Path pth = new Path();
			
			tailPathNode current = start;
			do {
				pth.addLinkE(current.data);
				current = current.next;	
			} while ( current != start );
			
			return pth;
		}
	}
	
	public class tailPathNode {
		
		MapLocation data;
		tailPathNode next;
		
		public tailPathNode(int x, int y) {
			data = new MapLocation(x,y);
		}
	}

	

}
