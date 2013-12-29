package ModularStrategyBot.Strategies;

import ModularStrategyBot.Path.*;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class ___ExploreAndExpandStrategy__OLD extends Strategy {

	final int BC_CONST_ORDERS_ID = 459;
	final int BC_CONST_ORDERS_X = 4590;
	final int BC_CONST_ORDERS_Y = 45900;
	final int BC_CONST_DISTRESS_X = 2597;
	final int BC_CONST_DISTRESS_Y = 25970;
	final int BC_CONST_DISTRESS_ID = 25971;
	static TailPath tp;
	
	public ___ExploreAndExpandStrategy__OLD(RobotController in) { super(in); }

	public void run() {
		Direction dir;
		tp = new TailPath(rc.senseEnemyHQLocation());
		try {
			switch (rc.getType()) {
			case HQ:
				// Null the distress broadcasts
				rc.broadcast(BC_CONST_DISTRESS_X, 999);
				rc.broadcast(BC_CONST_DISTRESS_Y, 999);							
				
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
					// Set encampment type
					double r = (Math.random()*50.0);
					// 0 == Supplier
					// 1 == Generator
					if ( r < 20.0 ) rc.broadcast(BC_CONST_ORDERS_ID,0);
					else rc.broadcast(BC_CONST_ORDERS_ID,1);

					
					if ( rc.isActive() ) {
						// Determine a random location for the robot
						if ( Clock.getRoundNum() >= 1500 || Clock.getRoundNum() == 680 ) {
							rc.broadcast(BC_CONST_ORDERS_ID, 9999);
							rc.broadcast(BC_CONST_ORDERS_X, 0);
							rc.broadcast(BC_CONST_ORDERS_Y, 59);							
						}
						else {
							rc.broadcast(BC_CONST_ORDERS_X, (int)(Math.random()*rc.getMapWidth()));
							rc.broadcast(BC_CONST_ORDERS_Y, (int)(Math.random()*rc.getMapHeight()));
						}
						
						// Spawn a soldier						
						if ( rc.canMove(dir) ) rc.spawn(dir);
					} 
					rc.yield();
				}
			
			case SOLDIER:
				// Get my orders
				MapLocation myLocation,myMainTarget,myPriorityTarget = null,myLastDistress; 
				myMainTarget = new MapLocation(rc.readBroadcast(BC_CONST_ORDERS_X),rc.readBroadcast(BC_CONST_ORDERS_Y));
				double myHealth = rc.getEnergon();
				int distressResponse = 0;
				GameObject distressObject = null;
				
				
				while ( true ) {
					if ( rc.isActive() ) {
						myLocation = rc.getLocation();
						
						// Clear my old distress call.
						if ( rc.readBroadcast(BC_CONST_DISTRESS_ID) == rc.getRobot().getID() ) {
							//System.out.println("CLEAR D_ID> "+rc.readBroadcast(BC_CONST_DISTRESS_ID));
							rc.broadcast(BC_CONST_DISTRESS_X, 999);
							rc.broadcast(BC_CONST_DISTRESS_Y, 999);							
							rc.broadcast(BC_CONST_DISTRESS_ID, 0);							
						}

						
						
						// Check for distress calls and/or order changes.  If so, override current orders
						if ( rc.readBroadcast(BC_CONST_ORDERS_ID) == 9999 ) {
							// "9999" is a flag indicating that all active bots should override their current orders temporarily and go to the new Target
							myPriorityTarget = new MapLocation(rc.readBroadcast(BC_CONST_ORDERS_X),rc.readBroadcast(BC_CONST_ORDERS_Y));
						}
						if ( distressResponse == 0 ) {
							myLastDistress = new MapLocation(rc.readBroadcast(BC_CONST_DISTRESS_X),rc.readBroadcast(BC_CONST_DISTRESS_Y));
							if ( rc.readBroadcast(BC_CONST_DISTRESS_ID) != rc.getRobot().getID() && myLocation.distanceSquaredTo(myLastDistress) <= 14 ) {
								System.out.println("Responding to Distress Call!!  From : "+rc.readBroadcast(BC_CONST_DISTRESS_ID)+"\t at\t"+myLastDistress.x+", "+myLastDistress.y);
								//distressObject = rc.senseObjectAtLocation(myLastDistress);
								//distressObject = rc.senseNearbyGameObjects(Robot.class, myLastDistress, 1, null)[0];
								GameObject[] tempO = rc.senseNearbyGameObjects(Robot.class, myLastDistress, 14, null);
								for ( GameObject n : tempO ) if ( n.getID() == rc.readBroadcast(BC_CONST_DISTRESS_ID) ) distressObject = n; 
								
								// Confirm the distress call
								if ( distressObject == null ) {
									System.out.println("False Alarm.  Clearing it");
									rc.broadcast(BC_CONST_DISTRESS_X, 999);
									rc.broadcast(BC_CONST_DISTRESS_Y, 999);							
									rc.broadcast(BC_CONST_DISTRESS_ID, 0);
								}
								else { 
									System.out.println("Distress call confirmed, locked onto target!");
									distressResponse = 20;
								}
							}
						}
							
						
						
						// Check to see if i am on a bonus tile.  If so, upgrade myself.
						if ( rc.senseEncampmentSquare(myLocation) ) {
							/*
							double r = (Math.random()*50.0);
							System.out.println("Random: "+r);
							if ( r < 40.0 ) rc.captureEncampment(RobotType.SUPPLIER);
							else rc.captureEncampment(RobotType.GENERATOR);
							continue;
							*/
							int r = rc.readBroadcast(BC_CONST_ORDERS_ID);
							//System.out.println("Random: "+r);
							if ( r == 0 ) rc.captureEncampment(RobotType.SUPPLIER);
							else if ( r == 1 ) rc.captureEncampment(RobotType.GENERATOR);
							continue;

						}
						
						// If nothing else, progress one move towards my target
						if ( myPriorityTarget != null ) {
							System.out.println("Engaging Priority Target: "+myPriorityTarget.x+", "+myPriorityTarget.y);
							takeStepTowards(myPriorityTarget);
							if ( rc.getLocation().equals(myPriorityTarget) ) myPriorityTarget = null;
						}
						else if ( distressResponse > 0 ) {
							if ( distressObject == null ) System.out.println("Null!> "+rc.readBroadcast(BC_CONST_DISTRESS_X)+","+rc.readBroadcast(BC_CONST_DISTRESS_Y)+","+rc.readBroadcast(BC_CONST_DISTRESS_ID));
							if ( rc.canSenseObject(distressObject) ) {
								MapLocation distressLocation = rc.senseLocationOf(distressObject);
								if ( myLocation.equals(distressLocation) ) {
									distressResponse--;
									rc.yield();
									continue; 
								}
								takeStepTowards(distressLocation);
							}
							else {
								distressResponse = 0;
							}	
						}
						else {
							if ( myLocation.equals(myMainTarget) ) {
								myMainTarget = new MapLocation((int)(Math.random()*rc.getMapWidth()),(int)(Math.random()*rc.getMapHeight()));
							}
							takeStepTowards(myMainTarget);
						}
						if (distressResponse > 0) distressResponse--;

						
						
						// Check my health and determine whether i should send out a distress call
						if ( rc.getEnergon() < myHealth ) {
							myLocation = rc.getLocation();
							System.out.println("Sending Distress Call!!\t\t\t\t"+myLocation.x+", "+myLocation.y);
							// My health has decreased since last check, so i am probably under attack.  Send out a distress signal by broadcasting my coordinates.
							rc.broadcast(BC_CONST_DISTRESS_X, myLocation.x);
							rc.broadcast(BC_CONST_DISTRESS_Y, myLocation.y);
							rc.broadcast(BC_CONST_DISTRESS_ID, rc.getRobot().getID());
							myHealth = rc.getEnergon();
						}

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
			//rc.breakpoint();
			e.printStackTrace();
		}
	}
	
	public static void takeStepTowards(MapLocation in) throws GameActionException {
		// Check Straight
		MapLocation myLoc = rc.getLocation();
		Direction dir = myLoc.directionTo(in);
		MapLocation m = myLoc.add(dir);
		Team mineTeam = rc.senseMine(m);
		boolean formTail = false;
		while (true) {
			if ( m.equals(in) && mineTeam != null && mineTeam != rc.getTeam() ) {
				rc.defuseMine(m);
				for ( int i = 0; i < 12; i++ ) rc.yield();
				if ( tp.checkPath(rc.getLocation().add(dir)) ) {
					formTail = true;
					break;
				}
				while ( !rc.canMove(dir) ) rc.yield();
				rc.move(dir);
				return;
			}
			
			if ( mineTeam == null
				//|| mineTeam == myTeam // Comment out this line if mines are not laid by this strategy
					&& rc.canMove(dir)
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
						&& rc.canMove(dir)
						) {
					if ( tp.checkPath(rc.getLocation().add(dir)) ) {
						formTail = true;
						break;
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
							&& rc.canMove(dir)
							) {
						if ( tp.checkPath(rc.getLocation().add(dir)) ) {
							formTail = true;
							break;
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
							break;
						}
						while ( !rc.canMove(dir) ) rc.yield();
						rc.move(dir);
					}
				}
			}
			break;
		}
		if ( formTail ) { }
	}
	
}
