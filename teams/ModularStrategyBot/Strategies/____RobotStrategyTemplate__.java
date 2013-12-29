package ModularStrategyBot.Strategies;

import ModularStrategyBot.Path.*;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class ____RobotStrategyTemplate__ extends Strategy {

	public ____RobotStrategyTemplate__(RobotController in) { super(in); }

	public void run() {
		Direction dir;
		try {
			switch (rc.getType()) {
			case HQ:
				// Find Spawn direction
				// Check Straight
				MapLocation myLoc = rc.getLocation(); 
				dir = myLoc.directionTo(rc.senseEnemyHQLocation());
				if ( rc.senseMine(myLoc.add(dir)) == null ) { }
				else {
					// Check Left
					dir = dir.rotateLeft();
					if ( rc.senseMine(myLoc.add(dir)) == null ) { }	
					else {
						// Check Right
						dir = dir.rotateRight().rotateRight();
					}
				}
				while ( true ) { 
					if ( rc.isActive() ) {
						// Spawn a soldier
						if ( rc.canMove(dir) ) rc.spawn(dir);
					} 
					rc.yield();
				}
			
			case SOLDIER:
				while ( true ) {
					if ( rc.isActive() ) {
					}
					rc.yield();
				}
			case ARTILLERY:
				while ( true ) {
					if ( rc.isActive() ) {
					}
					rc.yield();
				}
				
			case GENERATOR:
				while ( true ) {
					if ( rc.isActive() ) {
					}
					rc.yield();
				}

			case MEDBAY:
				while ( true ) {
					if ( rc.isActive() ) {
					}
					rc.yield();
				}

			case SHIELDS:
				while ( true ) {
					if ( rc.isActive() ) {
					}
					rc.yield();
				}

			case SUPPLIER:
				while ( true ) {
					if ( rc.isActive() ) {
					}
					rc.yield();
				}

			default:
				break;
			
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
