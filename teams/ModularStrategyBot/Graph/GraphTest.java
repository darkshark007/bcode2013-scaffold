package ModularStrategyBot.Graph;

import ModularStrategyBot.Path.Path;
import battlecode.common.MapLocation;

public class GraphTest {

	public static void main(String[] args) {
		
		MapLocation[] ml = new MapLocation[10];
		
		ml[0] = new MapLocation(1,4);
		ml[1] = new MapLocation(2,4);
		ml[2] = new MapLocation(3,4);
		ml[3] = new MapLocation(3,5);
		ml[4] = new MapLocation(3,6);
		ml[5] = new MapLocation(4,1);
		ml[6] = new MapLocation(4,2);
		ml[7] = new MapLocation(4,3);
		ml[8] = new MapLocation(5,3);
		ml[9] = new MapLocation(6,3);
		
		GraphGenerator g = new GraphGenerator();
		g.genGraph(8,8,ml);
		
		Path p = g.getShortestPath(1,6,6,1);
	}
}
