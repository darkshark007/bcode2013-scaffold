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
import battlecode.common.Team;

public class RushStrategy extends Strategy {

	public RushStrategy(RobotController in) { super(in); }

	static Path rushPath;
		
	public void run() {
		Direction dir;
		try {
			switch (rc.getType()) {
			case HQ:
				// Lookup the Map's shortest Path
				
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
				MapLocation m,enemyLoc = rc.senseEnemyHQLocation();
				boolean isFirst = false;
				int waitingSince = Clock.getRoundNum();
				int waitConst = 0;				
				int waitMoves = 0;
				
				//Make the constant relative to 1/3 of the map
				int lX = Math.abs(rc.senseHQLocation().x - enemyLoc.x)/3;
				int lY = Math.abs(rc.senseHQLocation().y - enemyLoc.y)/3;
				if ( lY > lX ) waitConst = lY;
				else waitConst = lX;
				if ( waitConst > 10 ) waitConst = 10;
			
				
				if ( (Clock.getRoundNum()+(waitConst*10)-10) % (waitConst*10) <= 9 ) isFirst = true; 	
				/* */
				
				//Team myTeam = rc.getTeam();
				Team mineTeam;
				TailPath tp = new TailPath(enemyLoc);
				boolean formTail = false;
				while ( true ) {
					if ( rc.isActive() ) {
						myLoc = rc.getLocation();
						if ( tp.checkPath(myLoc) ) formTail = true;
						if ( formTail ) {
							dir = rc.getLocation().directionTo(rc.senseHQLocation()).opposite().rotateRight().rotateRight();
							m = myLoc.add(dir);
							// Check Right, Right
							if ( locIsOnMap(m) && tp.checkPath(m) && rc.canMove(dir) && rc.senseMine(m) == null ) {
								while ( !rc.canMove(dir) ) rc.yield();
								rc.move(dir);					
							}
							else {
								// Check Right
								dir = dir.rotateLeft();
								m = myLoc.add(dir);
								if ( locIsOnMap(m) && tp.checkPath(m) && rc.canMove(dir) && rc.senseMine(m) == null ) {
									while ( !rc.canMove(dir) ) rc.yield();
									rc.move(dir);					
								}
								else {
									// Check Straight
									dir = dir.rotateLeft();
									m = myLoc.add(dir);
									if ( locIsOnMap(m) && tp.checkPath(m) && rc.canMove(dir) && rc.senseMine(m) == null ) {
										while ( !rc.canMove(dir) ) rc.yield();
										rc.move(dir);					
									}
									else {
										// Check Left
										dir = dir.rotateLeft();
										m = myLoc.add(dir);
										// Check Right, Right
										if ( locIsOnMap(m) && tp.checkPath(m) && rc.canMove(dir) && rc.senseMine(m) == null ) {
											while ( !rc.canMove(dir) ) rc.yield();
											rc.move(dir);					
										}
										else {
											// Check Left, Left
											dir = dir.rotateLeft();
											m = myLoc.add(dir);
											if ( locIsOnMap(m) && tp.checkPath(m) && rc.canMove(dir) && rc.senseMine(m) == null ) {
												while ( !rc.canMove(dir) ) rc.yield();
												rc.move(dir);					
											}
										}
									}
								}
							}
							rc.yield();
							continue;
						}
						
						// Not
						// Check Straight
						dir = myLoc.directionTo(enemyLoc);
						m = myLoc.add(dir);
						mineTeam = rc.senseMine(m);
						if ( mineTeam == null
							//|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
							) {
							if ( tp.checkPath(rc.getLocation().add(dir)) ) {
								formTail = true;
								continue;
							}
							while ( !rc.canMove(dir) ) rc.yield();
							rc.move(dir);					
						}
						else {
							// Check Left 45 degrees
							dir = dir.rotateLeft();
							m = myLoc.add(dir);
							mineTeam = rc.senseMine(m);
							if ( locIsOnMap(m) && mineTeam == null
									//|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
									) {
								if ( tp.checkPath(rc.getLocation().add(dir)) ) {
									formTail = true;
									continue;
								}
								while ( !rc.canMove(dir) ) rc.yield();
								rc.move(dir);					
							}
							else {
								// Check Right 45 degrees
								dir = dir.rotateRight().rotateRight();
								m = myLoc.add(dir);
								mineTeam = rc.senseMine(m);
								if ( locIsOnMap(m) && mineTeam == null
										//|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
										) {
									if ( tp.checkPath(rc.getLocation().add(dir)) ) {
										formTail = true;
										continue;
									}
									while ( !rc.canMove(dir) ) rc.yield();
									rc.move(dir);					
								}
								else {
									if ( !locIsOnMap(myLoc.add(dir)) ) dir = dir.rotateLeft();
									
									rc.defuseMine(myLoc.add(dir));
									for ( int i = 0; i < 12; i++ ) rc.yield();
									if ( tp.checkPath(rc.getLocation().add(dir)) ) {
										formTail = true;
										continue;
									}
									while ( !rc.canMove(dir) ) rc.yield();
									rc.move(dir);
								}
							}
						}
					}
					waitMoves++;
					if ( waitMoves == waitConst && isFirst ) {
						//System.out.println("I SHOULD YIELD NOW!");
						//for ( int i = (waitConst-2)*10; i > 0; i-- ) rc.yield();
						for ( int i = ((waitConst)*10)-(Clock.getRoundNum()-waitingSince); i > 0; i-- ) rc.yield();
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
