package ModularStrategyBot.Strategies;

import ModularStrategyBot.Orders.rOrders;
import ModularStrategyBot.Path.Path;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.Team;

public abstract class Strategy implements I_RobotStrategy {
	
	static RobotController rc;
	
	
	// This is my fixed direction.  If i cannot go straight, i will go in this direction instead until i can go straight.
	// 0 == Left
	// 1 == Right
	int direc = 0;
	// This variable holds the location i was prior to my last step.  I will not go back to that spot in order to avoid bouning.
	MapLocation lastSpace;
	
	public Strategy(RobotController in) {
		rc = in;
		lastSpace = rc.senseHQLocation();
	}

	public abstract void run();

	
	public void takeStepTowards(MapLocation in) throws GameActionException {
		// Check Straight
		rc.setIndicatorString(2, "Direc: "+direc);
		MapLocation myLoc = rc.getLocation();
		System.out.println("Taking Step towards:  ("+in.x+","+in.y+")  from  ("+myLoc.x+","+myLoc.y+")");
		
		Direction dirS = myLoc.directionTo(in);
		Direction dirL = dirS.rotateLeft();
		Direction dirLL = dirL.rotateLeft();
		Direction dirLLL = dirLL.rotateLeft();
		Direction dirR = dirS.rotateRight();
		Direction dirRR = dirR.rotateRight();
		Direction dirRRR = dirRR.rotateRight();
		
		// See if i can move straight
		if ( rc.canMove(dirS) && rc.senseMine(myLoc.add(dirS)) == null ) {
			if ( !myLoc.add(dirS).equals(lastSpace) ) {
				rc.move(dirS);
				lastSpace = myLoc;
				return;				
			}
		}
		
		// Either there is a robot in the way, or there is a mine.
		// See if i can move 45 degrees left of straight
		if ( rc.canMove(dirL) && rc.senseMine(myLoc.add(dirL)) == null ) {
			if ( !myLoc.add(dirL).equals(lastSpace) ) {
				rc.move(dirL);
				lastSpace = myLoc;
				return;				
			}
		}
		
		// Either there is a robot in the way, or there is a mine.
		// See if i can move 45 degrees right of straight
		if ( rc.canMove(dirR) && rc.senseMine(myLoc.add(dirR)) == null ) {
			if ( !myLoc.add(dirR).equals(lastSpace) ) {
				rc.move(dirR);
				lastSpace = myLoc;
				return;				
			}
		}
		
		// If there is a mine straight ahead, defuse it.
		if ( rc.senseMine(myLoc.add(dirS)) != null ) {
			rc.defuseMine(myLoc.add(dirS));
			return;
		}

		// If there is a mine 45 degrees left, defuse it.
		if ( rc.senseMine(myLoc.add(dirL)) != null ) {
			rc.defuseMine(myLoc.add(dirL));
			return;
		}

		// If there is a mine 45 degrees right, defuse it.
		if ( rc.senseMine(myLoc.add(dirR)) != null ) {
			rc.defuseMine(myLoc.add(dirR));
			return;
		}
		
		// Since i cant move in any of the three forward directions, i will try to move at a steeper angle.
		// I use a fixed direction for this in order to avoid bouncing behavior.  That is, if i am moving 90/135 degrees left of straight, 
		//   i will continue to move in that direction until that direction becomes inviable.
		
		// Check 90 degrees in my fixed direction
		if ( direc == 0 ) dirS = dirLL;
		else  dirS = dirRR;
		if ( rc.canMove(dirS) ) {
			if ( rc.senseMine(myLoc.add(dirS)) == null ) {
				if ( !myLoc.add(dirS).equals(lastSpace) ) {
					rc.move(dirS);
					lastSpace = myLoc;
					return;				
				}
			}
			else {
				rc.defuseMine(myLoc.add(dirS));
				return;
			}
		}

		// Check 135 degrees in my fixed direction
		if ( direc == 0 ) dirS = dirLLL;
		else  dirS = dirRRR;
		if ( rc.canMove(dirS) ) {
			if ( rc.senseMine(myLoc.add(dirS)) == null ) {
				if ( !myLoc.add(dirS).equals(lastSpace) ) {
					rc.move(dirS);
					lastSpace = myLoc;
					return;				
				}
			}
			else {
				rc.defuseMine(myLoc.add(dirS));
				return;
			}
		}

		// If that direction did not work, swap my direction and try that way.
		direc = 1-direc; // Because direc is initialized at 0, this will always bounce it between 1 and 0
		
		
		// Check 90 degrees in my new fixed direction
		if ( direc == 0 ) dirS = dirLL;
		else  dirS = dirRR;
		if ( rc.canMove(dirS) ) {
			if ( rc.senseMine(myLoc.add(dirS)) == null ) {
				if ( !myLoc.add(dirS).equals(lastSpace) ) {
					rc.move(dirS);
					lastSpace = myLoc;
					return;				
				}
			}
			else {
				rc.defuseMine(myLoc.add(dirS));
				return;
			}
		}

		// Check 135 degrees in my fixed direction
		if ( direc == 0 ) dirS = dirLLL;
		else  dirS = dirRRR;
		if ( rc.canMove(dirS) ) {
			if ( rc.senseMine(myLoc.add(dirS)) == null ) {
				if ( !myLoc.add(dirS).equals(lastSpace) ) {
					rc.move(dirS);
					lastSpace = myLoc;
					return;				
				}
			}
			else {
				rc.defuseMine(myLoc.add(dirS));
				return;
			}
		}
	}
	
	
	public void goTo(MapLocation in) throws GameActionException { goTo(in,null); }
	public void goTo(MapLocation in,rOrders orders) throws GameActionException {
		while ( !rc.getLocation().equals(in) ) {
			if ( orders != null ) orders.executeOrders(rc);
			if ( rc.isActive() ) takeStepTowards(in);
			rc.yield();
		}
	}

	
	public void followPath(Path in) throws GameActionException {
		int path = 0;
		if ( rc.getLocation().equals(in.getLink(0)) ) path = 1;
		
		MapLocation nextLink;
		Direction dir;
		while ( true ) {
			if ( rc.isActive() ) {
				nextLink = in.getLink(path++);
				if ( nextLink == null ) return;

				dir = rc.getLocation().directionTo(nextLink);

				
				if ( rc.senseMine(nextLink) != rc.getTeam() && rc.senseMine(nextLink) != null ) {
					rc.defuseMine(nextLink);
					continue;
				}
				while ( !rc.canMove(dir) ) rc.yield();
				rc.move(dir);
			}
		}

		
	}
	
	
	public static boolean locIsOnMap(MapLocation in) {
		if ( in.x < 0 ) return false;
		if ( in.y < 0 ) return false;
		if ( in.x >= rc.getMapWidth() ) return false;
		if ( in.y >= rc.getMapHeight() ) return false;
		return true;
	}
	
}
