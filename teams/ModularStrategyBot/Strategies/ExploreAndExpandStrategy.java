package ModularStrategyBot.Strategies;

import java.util.PriorityQueue;

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
import battlecode.common.Upgrade;

public class ExploreAndExpandStrategy extends Strategy {

	final int BC_CONST_ORDERS_ID = 7861;
	final int BC_CONST_ORDERS_X = 4590;
	final int BC_CONST_ORDERS_Y = 45900;

	final int BC_CONST_DISTRESS_X = 2597;
	final int BC_CONST_DISTRESS_Y = 25970;
	final int BC_CONST_DISTRESS_RND = 25971;
	final int C_DIST_LEN = 20;
	
	final int BC_CONST_COUNT_DEF = 865;
	final int BC_CONST_COUNT_GEN = 8650;
	static TailPath tp;
	
	public ExploreAndExpandStrategy(RobotController in) { super(in); }

	public void run() {
		double myHealth = rc.getEnergon();
		Direction dir;
		MapLocation myLoc;
		tp = new TailPath(rc.senseEnemyHQLocation());
		try {
			switch (rc.getType()) {
			case HQ:
				
				PriorityQueue<MapLocationNode> listOfEncampments = null;
				
				// Find Spawn direction
				// Check Straight
				myLoc = rc.getLocation(); 
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
				
				listOfEncampments = getListOfEncampments();
				MapLocation nextEncampment;
				
				// Research fusion
				if ( listOfEncampments.size() > 20 ) {
					while ( rc.checkResearchProgress(Upgrade.FUSION) < 25 ) {
						if ( rc.isActive() ) rc.researchUpgrade(Upgrade.FUSION);
						rc.yield();
					}
				}
				
				
				// Find the first point, to spawn a med bay the bots can circle around
				int w1 = rc.getMapWidth()/2;
				int w2 = (rc.getMapWidth()-1)/2;
				int h1 = rc.getMapHeight()/2;
				int h2 = (rc.getMapHeight()-1)/2;
				MapLocation l1 = new MapLocation(w1,h1); 
				MapLocation l2 = new MapLocation(w1,h2); 
				MapLocation l3 = new MapLocation(w2,h1); 
				MapLocation l4 = new MapLocation(w2,h2);
				
				
				MapLocation nullDist = l1;
				l1 = rc.senseEnemyHQLocation();
				if ( l2.distanceSquaredTo(l1) < nullDist.distanceSquaredTo(l1) ) nullDist = l2;
				if ( l3.distanceSquaredTo(l1) < nullDist.distanceSquaredTo(l1) ) nullDist = l3;
				if ( l4.distanceSquaredTo(l1) < nullDist.distanceSquaredTo(l1) ) nullDist = l4;
				
				nullDist = findEncampmentClosestTo(nullDist);
				
				// Null the distress broadcasts
				rc.broadcast(BC_CONST_DISTRESS_X, nullDist.x);
				rc.broadcast(BC_CONST_DISTRESS_Y, nullDist.y);							

				// Spawn the first bot to create a shield
				rc.broadcast(BC_CONST_ORDERS_ID, 3);
				if ( !rc.isActive() ) rc.yield();
				if ( rc.canMove(dir) ) rc.spawn(dir);
				
				rc.yield();
				
				// Spawn 3 guards to protect the shield
				//rc.broadcast(BC_CONST_ORDERS_ID, 2);
				for ( int i = 0; i < 5; i++) {
					System.out.println("Spawning Guards...");
					while ( !rc.isActive() || !rc.canMove(dir)) rc.yield();
					rc.spawn(dir);
				}
				
				

				
				while ( true ) { 
				
					// Engage attack mode
					if ( ( listOfEncampments.size() == 0 || Clock.getRoundNum() > 2000 ) && rc.readBroadcast(BC_CONST_COUNT_DEF) > rc.readBroadcast(BC_CONST_COUNT_GEN)*1.0) {
						System.out.println("ENTER ATTACK MODE");
						rc.broadcast(BC_CONST_DISTRESS_RND, 9999);
						rc.broadcast(BC_CONST_DISTRESS_X, rc.senseEnemyHQLocation().x);
						rc.broadcast(BC_CONST_DISTRESS_Y, rc.senseEnemyHQLocation().y);
					}

					if ( rc.isActive() ) {
						// Set spawn type
						// 1 == Supplier/Generator
						// 2 == Defender

						System.out.println(rc.readBroadcast(BC_CONST_COUNT_GEN)+" > "+rc.readBroadcast(BC_CONST_COUNT_DEF));
						
						
						if ( rc.readBroadcast(BC_CONST_COUNT_GEN) > rc.readBroadcast(BC_CONST_COUNT_DEF) || listOfEncampments.size() == 0 ) {
							//System.out.println("IF");
							rc.broadcast(BC_CONST_ORDERS_ID, 2);
						}
						else {
							//System.out.println("ELSE");
							rc.broadcast(BC_CONST_ORDERS_ID, 1);
							
							// Set a target encampment
							if ( listOfEncampments.size() > 0 ) {
								nextEncampment = listOfEncampments.remove().data;
								rc.broadcast(BC_CONST_ORDERS_X, nextEncampment.x);
								rc.broadcast(BC_CONST_ORDERS_Y, nextEncampment.y);
								System.out.println("Sending next bot to "+nextEncampment.x+","+nextEncampment.y);
							}
							
						}
						//System.out.println("Reset??");
						
						// Spawn a soldier						
						if ( rc.canMove(dir) ) rc.spawn(dir);
						else { 
							Direction tempDir = dir;
							do { 
								tempDir = tempDir.rotateLeft();
							} while ( ( !rc.canMove(tempDir) || rc.senseMine(myLoc.add(tempDir)) != null ) && !tempDir.equals(dir) );
							if ( rc.canMove(tempDir) ) rc.spawn(tempDir);
						}
					}
					rc.broadcast(BC_CONST_COUNT_GEN,0);
					rc.broadcast(BC_CONST_COUNT_DEF,0);
					
					checkDistressCall(myHealth);
					myHealth = rc.getEnergon();
					
					if ( rc.readBroadcast(BC_CONST_DISTRESS_RND)+C_DIST_LEN < Clock.getRoundNum() ) {
						rc.broadcast(BC_CONST_DISTRESS_X, nullDist.x);
						rc.broadcast(BC_CONST_DISTRESS_Y, nullDist.y);	
					}

					rc.yield();
				}
			
			case SOLDIER:
				// Get my orders
				MapLocation myTarget;
				int myOrders = rc.readBroadcast(BC_CONST_ORDERS_ID);
				System.out.println("My Orders: "+myOrders);
				
				switch (myOrders) {
				case 1: // Supplier & Generator
					myTarget = new MapLocation(rc.readBroadcast(BC_CONST_ORDERS_X),rc.readBroadcast(BC_CONST_ORDERS_Y));
					rc.setIndicatorString(0, "Supplier -> "+myTarget.x+","+myTarget.y);
					while ( true ) {
						rc.broadcast(BC_CONST_COUNT_GEN, rc.readBroadcast(BC_CONST_COUNT_GEN)+1);
						if ( rc.isActive() ) {
							myLoc = rc.getLocation(); 
							
							
							
							// Check to see if i'm at my target
							if ( myLoc.equals(myTarget) ) {
								// If so, build
								int resSave = (rc.readBroadcast(BC_CONST_COUNT_DEF)+rc.readBroadcast(BC_CONST_COUNT_GEN))*3;
								if ( (rc.getTeamPower()-(Clock.getRoundNum()*7)) > (rc.senseCaptureCost()+resSave)*1.5 ) {
									while ( (rc.senseCaptureCost()+resSave) > rc.getTeamPower() ) rc.yield();
									rc.captureEncampment(RobotType.SUPPLIER);
								}
								else {
									while ( (rc.senseCaptureCost()+resSave) > rc.getTeamPower() ) rc.yield();
									rc.captureEncampment(RobotType.GENERATOR);
								}
							}
							else {
								// If not, Go to my target
								takeStepTowards(myTarget);
							}
							
			
							
							// Check for distress call
						}

						checkDistressCall(myHealth);
						myHealth = rc.getEnergon();
						
						rc.yield();
					}
						
					case 2: // Defender
						rc.setIndicatorString(0, "Defender");
						while ( true ) {
							rc.broadcast(BC_CONST_COUNT_DEF, rc.readBroadcast(BC_CONST_COUNT_DEF)+2);
							if ( rc.isActive() ) {
								myLoc = rc.getLocation();

								// Go to the latest distress location
								myTarget = new MapLocation(rc.readBroadcast(BC_CONST_DISTRESS_X),rc.readBroadcast(BC_CONST_DISTRESS_Y));
								//System.out.println("Targ: "+myTarget.x+", "+myTarget.y);
								if ( myLoc.equals(myTarget) ) { 
									// Do nothing
									
									takeStepTowards(rc.senseHQLocation());
								} 
								else { takeStepTowards(myTarget); }
							}
							rc.yield();
						}
						
						
					case 3: // Shield Bay
						myTarget = new MapLocation(rc.readBroadcast(BC_CONST_DISTRESS_X),rc.readBroadcast(BC_CONST_DISTRESS_Y));
						rc.setIndicatorString(0, "Shield -> "+myTarget.x+","+myTarget.y);
						while ( true ) {
							rc.broadcast(BC_CONST_COUNT_GEN, rc.readBroadcast(BC_CONST_COUNT_GEN)+1);
							if ( rc.isActive() ) {
								myLoc = rc.getLocation(); 
								
								
								
								// Check to see if i'm at my target
								if ( myLoc.equals(myTarget) ) {
									// If so, build
									int resSave = (rc.readBroadcast(BC_CONST_COUNT_DEF)+rc.readBroadcast(BC_CONST_COUNT_GEN))*3;
									while ( (rc.senseCaptureCost()+resSave) > rc.getTeamPower() ) {
										System.out.println("Saving Up...");
										rc.yield();
									}
									rc.captureEncampment(RobotType.MEDBAY);
								}
								else {
									// If not, Go to my target
									takeStepTowards(myTarget);
								}
							}

							// Check for distress call
							checkDistressCall(myHealth);
							myHealth = rc.getEnergon();
							
							rc.yield();
						}
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
						//System.out.println("Count: "+rc.readBroadcast(BC_CONST_COUNT_GEN));
						rc.broadcast(BC_CONST_COUNT_GEN, rc.readBroadcast(BC_CONST_COUNT_GEN)+2);
						
					}
					checkDistressCall(myHealth);
					myHealth = rc.getEnergon();
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
					checkDistressCall(myHealth);
					myHealth = rc.getEnergon();
					rc.yield();
				}

			case SUPPLIER:
				while ( true ) {
					if ( rc.isActive() ) {
						//System.out.println("Count: "+rc.readBroadcast(BC_CONST_COUNT_GEN));
						rc.broadcast(BC_CONST_COUNT_GEN, rc.readBroadcast(BC_CONST_COUNT_GEN)+2);
						
					}
					checkDistressCall(myHealth);
					myHealth = rc.getEnergon();
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
		Direction dirS = myLoc.directionTo(in);
		Direction dirL = dirS.rotateLeft();
		Direction dirLL = dirL.rotateLeft();
		Direction dirR = dirS.rotateRight();
		Direction dirRR = dirR.rotateRight();
		
		if ( rc.canMove(dirS) && rc.senseMine(myLoc.add(dirS)) == null ) {
			rc.move(dirS);
			return;
		}
		// Either there is a robot in the way, or there is a mine.

		if ( rc.canMove(dirL) && rc.senseMine(myLoc.add(dirL)) == null ) {
			rc.move(dirL);
			return;
		}
		
		if ( rc.canMove(dirR) && rc.senseMine(myLoc.add(dirR)) == null ) {
			rc.move(dirR);
			return;
		}
		
		if ( rc.senseMine(myLoc.add(dirS)) != null ) {
			rc.defuseMine(myLoc.add(dirS));
			return;
		}
		if ( rc.senseMine(myLoc.add(dirL)) != null ) {
			rc.defuseMine(myLoc.add(dirL));
			return;
		}
		if ( rc.senseMine(myLoc.add(dirR)) != null ) {
			rc.defuseMine(myLoc.add(dirR));
			return;
		}
		
		
		if ( rc.canMove(dirLL) ) {
			if ( rc.senseMine(myLoc.add(dirLL)) == null ) {
				rc.move(dirLL);
				return;				
			}
			else {
				rc.defuseMine(myLoc.add(dirLL));
				return;
			}
		}
		if ( rc.canMove(dirRR) ) {
			if ( rc.senseMine(myLoc.add(dirRR)) == null ) {
				rc.move(dirRR);
				return;				
			}
			else {
				rc.defuseMine(myLoc.add(dirRR));
				return;
			}
		}

	}
	
	public PriorityQueue<MapLocationNode> getListOfEncampments() throws GameActionException {
		
		PriorityQueue<MapLocationNode> list = new PriorityQueue<MapLocationNode>();
		
		//MapLocation[] array = rc.senseEncampmentSquares(new MapLocation(0,0), 99999, Team.NEUTRAL);
		MapLocation[] array = rc.senseAllEncampmentSquares();

		MapLocation myBase = rc.senseHQLocation();
		MapLocation enemyBase = rc.senseEnemyHQLocation();
		
		int distToMyBase,distToEnemyBase;
		for ( MapLocation n : array ) {
			//if ( rc.isActive() ) rc.researchUpgrade(Upgrade.FUSION);
			distToMyBase = myBase.distanceSquaredTo(n);
			distToEnemyBase = enemyBase.distanceSquaredTo(n);
			
			//if ( distToMyBase > distToEnemyBase ) { continue; }
			if ( distToMyBase > distToEnemyBase ) { list.add(new MapLocationNode(n,distToMyBase+10000)); }
			else { list.add(new MapLocationNode(n,distToMyBase)); }
			
			
		}
		
		return list;
	}
	
	public MapLocation findEncampmentClosestTo(MapLocation in) throws GameActionException {
		int size = 10;
		MapLocation[] array;
		do {
			 array = rc.senseEncampmentSquares(in, size, Team.NEUTRAL);
			 size += 5;	
		} while ( array.length == 0 );

		
		MapLocation closest_1 = null,closest_2 = null;
		int distance_1 = 9999,distance_2 = 9999;
		for ( MapLocation n : array ) {
			if ( in.distanceSquaredTo(n) <= distance_1 ) {
				distance_2 = distance_1;
				closest_2 = closest_1;

				distance_1 = in.distanceSquaredTo(n); 
				closest_1 = n;
			}
		}
		
		if ( closest_2 == null ) { return closest_1; }
		else if ( distance_1 < distance_2 ) return closest_1;
		else if ( rc.senseEnemyHQLocation().distanceSquaredTo(closest_1) <= rc.senseHQLocation().distanceSquaredTo(closest_2) ) return closest_1;
		else return closest_2;
	}
	
	public void checkDistressCall(double Old) throws GameActionException {
		double New = rc.getEnergon();
		if ( New < Old && (Old-New) > 5.0  ) {
			if ( Clock.getRoundNum() >= (rc.readBroadcast(BC_CONST_DISTRESS_RND)+C_DIST_LEN) || rc.getType() == RobotType.HQ || rc.getType() == RobotType.SHIELDS ) {
				MapLocation myLoc = rc.getLocation();			
				rc.broadcast(BC_CONST_DISTRESS_RND, Clock.getRoundNum());
				rc.broadcast(BC_CONST_DISTRESS_X, myLoc.x);
				rc.broadcast(BC_CONST_DISTRESS_Y, myLoc.y);				
			}
		}		
	}
	
	
	
	
	public class MapLocationNode implements Comparable<MapLocationNode> {
		
		MapLocation data;
		int distance;
		
		public MapLocationNode(MapLocation in, int dist) {
			distance = dist;
			data = in;
		}

		public int compareTo(MapLocationNode o) {
			if ( distance < o.distance ) { return -1; }
			if ( distance > o.distance ) { return 1; }
			return 0;
		}
	}	
}
