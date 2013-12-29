package ModularStrategyBot.Strategies;

import ModularStrategyBot.Path.Path;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.Team;

public abstract class Strategy implements I_RobotStrategy {
	
	static RobotController rc;
	
	public Strategy(RobotController in) {
		rc = in;
	}

	public abstract void run();

	/* */
	public void goTo(MapLocation in) throws GameActionException {
		Direction dir;
		MapLocation m,myLoc;
		Team myTeam = rc.getTeam();
		Team mineTeam;
		while ( true ) {
			myLoc = rc.getLocation();
			if ( myLoc.equals(in) ) return;
			if ( rc.isActive() ) {
				// Check Straight
				dir = myLoc.directionTo(in);
				m = myLoc.add(dir);
				mineTeam = rc.senseMine(m);
				if ( mineTeam == null
					|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
					) {
					while ( !rc.canMove(dir) ) rc.yield();
					rc.move(dir);					
				}
				else {
					// Check Left 45 degrees
					dir = dir.rotateLeft();
					m = myLoc.add(dir);
					mineTeam = rc.senseMine(m);
					if ( locIsOnMap(m) && mineTeam == null
							|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
							) {
						while ( !rc.canMove(dir) ) rc.yield();
						rc.move(dir);					
					}
					else {
						// Check Right 45 degrees
						dir = dir.rotateRight().rotateRight();
						m = myLoc.add(dir);
						mineTeam = rc.senseMine(m);
						if ( locIsOnMap(m) && mineTeam == null
								|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
								) {
							while ( !rc.canMove(dir) ) rc.yield();
							rc.move(dir);					
						}
						else {
							dir = dir.rotateLeft();
							rc.defuseMine(myLoc.add(dir));
							for ( int i = 0; i < 12; i++ ) rc.yield();
							while ( !rc.canMove(dir) ) rc.yield();
							rc.move(dir);
						}
					}
				}
				/* */
}
			rc.yield();
		}
	}
	/* */
	
	/*
	public void goTo(MapLocation in) throws GameActionException {
		Direction dir;
		MapLocation m;
		while ( true ) {
			if ( rc.getLocation().equals(in) ) return;
			if ( rc.isActive() ) {
				dir = rc.getLocation().directionTo(in);
				m = rc.getLocation().add(dir);
				if ( rc.senseMine(m) != rc.getTeam() && rc.senseMine(m) != null ) {
					rc.defuseMine(m);
				}
				while ( !rc.canMove(dir) || rc.roundsUntilActive() > 0 ) rc.yield();
				rc.move(dir);				
			}
		}
	}
	/* */
	
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
