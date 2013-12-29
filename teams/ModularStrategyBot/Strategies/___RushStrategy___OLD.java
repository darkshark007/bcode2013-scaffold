package ModularStrategyBot.Strategies;

import ModularStrategyBot.Graph.GraphGenerator;
import ModularStrategyBot.Graph.PathGenerator;
import ModularStrategyBot.Graph.PathGenerator.tailPath;
import ModularStrategyBot.Orders.*;
import ModularStrategyBot.Path.Path;
import ModularStrategyBot.Path.TailPath;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class ___RushStrategy___OLD extends Strategy {

	public ___RushStrategy___OLD(RobotController in) { super(in); }

	static Path rushPath;
		
	public void run() {
		Direction dir;
		try {
			switch (rc.getType()) {
			case HQ:
				System.out.println("I EXIST!!"+rc.getRobot().getID()+" --\t"+this.toString());
				// Lookup the Map's shortest Path
				if ( rushPath == null ) rushPath = lookupPath(rc,rc.senseHQLocation(),rc.senseEnemyHQLocation());
				
				// Spawn direction
				dir = rc.getLocation().directionTo(rushPath.getLink(0));
				//while (!rc.canMove(dir) || !(rc.senseMine(rc.getLocation().add(dir)) == null))
				//	dir = dir.rotateLeft();
				while ( true ) { 
					if ( rc.isActive() ) {
						
						// Spawn a soldier
						if ( rc.canMove(dir) ) rc.spawn(dir);
					} 
					rc.yield();
				}
			
			case SOLDIER:
				boolean isFirst = false;
				int waitConst = 0;				
				int waitMoves = 0;
				//int waitConst = 15;

				/* S2 -- 5 -> 10 -> 15
				if ( Clock.getRoundNum() >= 150 ) {
					waitConst = 15;
					if ( (Clock.getRoundNum()-150) % (waitConst*10) <= 9 ) isFirst = true; 
				}
				else if ( Clock.getRoundNum() >= 50 ) {
					waitConst = 10;
					if ( (Clock.getRoundNum()-50) % (waitConst*10) <= 9 ) isFirst = true; 
				}
				else {
					waitConst = 5;
					if ( Clock.getRoundNum() % (waitConst*10) <= 9 ) isFirst = true; 	
				}
				/* */
				
				/* S2 -- 5 -> 5 -> 5 -> 10
				if ( Clock.getRoundNum() >= 150 ) {
					waitConst = 10;
					if ( (Clock.getRoundNum()-150) % (waitConst*10) <= 9 ) isFirst = true; 
				}
				else {
					waitConst = 5;
					if ( Clock.getRoundNum() % (waitConst*10) <= 9 ) isFirst = true; 	
				}
				/* */
				
				/* S3 -- 15 -> 10 -> 5
				if ( Clock.getRoundNum() >= 250 ) {
					waitConst = 5;
					if ( (Clock.getRoundNum()-250) % (waitConst*10) <= 9 ) isFirst = true; 
				}
				else if ( Clock.getRoundNum() >= 150 ) {
					waitConst = 10;
					if ( (Clock.getRoundNum()-150) % (waitConst*10) <= 9 ) isFirst = true; 
				}
				else {
					waitConst = 15;
					if ( Clock.getRoundNum() % (waitConst*10) <= 9 ) isFirst = true; 	
				}
				/* */
				
				/* S4 -- 10 -> 5
				if ( Clock.getRoundNum() >= 100 ) {
					waitConst = 5;
					if ( (Clock.getRoundNum()-100) % (waitConst*10) <= 9 ) isFirst = true; 
				}
				else {
					waitConst = 10;
					if ( Clock.getRoundNum() % (waitConst*10) <= 9 ) isFirst = true; 	
				}
				/* */

				
				/* S1 -- Constant */
				waitConst = 10;
				if ( (Clock.getRoundNum()+(waitConst*10)-10) % (waitConst*10) <= 9 ) isFirst = true; 	
				/* */
				System.out.println(waitConst+"\t"+isFirst+"\t"+((Clock.getRoundNum()-12) % (waitConst*10)));
				
				//System.out.println("I EXIST!!"+rc.getRobot().getID()+" --\t"+isFirst+"\t"+this.toString());
				if ( rushPath == null ) rushPath = lookupPath(rc,rc.senseHQLocation(),rc.senseEnemyHQLocation());
				int path = 1;
				MapLocation nextLink;
				while ( true ) {
					if ( rc.isActive() ) {
						nextLink = rushPath.getLink(path++);
						while ( nextLink == null ) rc.yield();
						
						dir = rc.getLocation().directionTo(nextLink);
						MapLocation m = rc.getLocation().add(dir);
						if ( rc.senseMine(m) != rc.getTeam() && rc.senseMine(m) != null ) {
							rc.defuseMine(m);
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
							rc.yield();
						}
						while ( !rc.canMove(dir) ) rc.yield();
						rc.move(dir);
						waitMoves++;
						if ( waitMoves == waitConst && isFirst ) {
							System.out.println("I SHOULD YIELD NOW!");
							for ( int i = (waitConst-2)*10; i > 0; i-- ) rc.yield();
						}
					}
					rc.yield();
				}
			
			}
		} catch (Exception e) {
			rc.breakpoint();
			e.printStackTrace();
		}
	}
	
	public static Path lookupPath(RobotController rc, MapLocation startA, MapLocation startB) {
		Path path = new Path();
		
		TailPath p = new TailPath(startB.x,startB.y);
		
		MapLocation next = startA;
		do {
			next = next.add(next.directionTo(startB));
			path.addLinkE(next);
		} while (!p.checkPath(next));
		next = path.removeLastLink();
		Path tailPath = p.getPathStartingAt(next);
		
		path.addPathE(tailPath);
		
		
		return path;
	}
}
