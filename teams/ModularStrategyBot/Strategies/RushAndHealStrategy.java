// COMPLETE & OPTIMIZED(ish)

package ModularStrategyBot.Strategies;

import java.util.PriorityQueue;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class RushAndHealStrategy extends Strategy {

	final int BC_CONST_ORDERS_ID = 7861;

	final int BC_CONST_DISTRESS_X = 2597;
	final int BC_CONST_DISTRESS_Y = 25970;
	
	public RushAndHealStrategy(RobotController in) { super(in); }

	public void run() {
		Direction dir;
		MapLocation myLoc;
		try {
			switch (rc.getType()) {
			case HQ:
				
			
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
				l1 = rc.senseHQLocation();
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
				//for ( int i = 0; i < 5; i++) {
				//	System.out.println("Spawning Guards...");
				//	while ( !rc.isActive() || !rc.canMove(dir)) rc.yield();
				//	rc.spawn(dir);
				//}
				
				

				rc.broadcast(BC_CONST_ORDERS_ID, 2);				
				while ( true ) { 
				
					// Engage attack mode
					if ( rc.isActive() ) {
						// Set spawn type
						// 2 == Defender

							
						
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
					rc.yield();
				}
			
			case SOLDIER:
				// Get my orders
				MapLocation myTarget;
				int myOrders = rc.readBroadcast(BC_CONST_ORDERS_ID);
				System.out.println("My Orders: "+myOrders);
				
				switch (myOrders) {
					case 2: // Defender
						rc.setIndicatorString(0, "Defender");
						boolean healing = false;
						myTarget = new MapLocation(rc.readBroadcast(BC_CONST_DISTRESS_X),rc.readBroadcast(BC_CONST_DISTRESS_Y));
						while ( true ) {
							if ( rc.isActive() ) {
								myLoc = rc.getLocation();
								
								if ( rc.getEnergon() <= 20.0 ) {
									healing = true;
									takeStepTowards(myTarget);
								}
								else {
									if ( healing ) {
										if ( rc.getEnergon() > 36.0 ) healing = false;
										else takeStepTowards(myTarget);
									}
									else {
										takeStepTowards(rc.senseEnemyHQLocation());
									}
								}
								
							}
							rc.yield();
						}
						
						
					case 3: // Shield Bay
						myTarget = new MapLocation(rc.readBroadcast(BC_CONST_DISTRESS_X),rc.readBroadcast(BC_CONST_DISTRESS_Y));
						rc.setIndicatorString(0, "Shield -> "+myTarget.x+","+myTarget.y);
						while ( true ) {
							if ( rc.isActive() ) {
								myLoc = rc.getLocation(); 
								
								
								
								// Check to see if i'm at my target
								if ( myLoc.equals(myTarget) ) {
									// If so, build
									rc.captureEncampment(RobotType.MEDBAY);
								}
								else {
									// If not, Go to my target
									takeStepTowards(myTarget);
								}
							}

							// Check for distress call
							
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
		else if ( rc.senseHQLocation().distanceSquaredTo(closest_1) <= rc.senseHQLocation().distanceSquaredTo(closest_2) ) return closest_1;
		else return closest_2;
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
