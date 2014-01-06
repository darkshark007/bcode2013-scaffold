package ModularStrategyBot.Strategies;

import ModularStrategyBot.Broadcast.Broadcast;
import ModularStrategyBot.Broadcast.I_Broadcast;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class CornerToCornerStrategy extends Strategy {

	I_Broadcast bc;
	
	final int BC_ART_1_X 	= 0;
	final int BC_ART_1_Y 	= 1;
	final int BC_ART_1_ID 	= 2;
	final int BC_ART_2_X 	= 3;
	final int BC_ART_2_Y 	= 4;
	final int BC_ART_2_ID 	= 5;
	final int BC_ART_3_X 	= 6;
	final int BC_ART_3_Y 	= 7;
	final int BC_ART_3_ID 	= 8;
	final int BC_MED_X 		= 9;
	final int BC_MED_Y 		= 10;
	final int BC_JOB_ID 	= 11;
	
	
	public CornerToCornerStrategy(RobotController in) {
		super(in);
		bc = new Broadcast(rc);
	}

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
				int myOrders = bc.readBroadcast(BC_JOB_ID);
				switch (myOrders) {
					case 1: // Corner Scout
						while ( true ) {
							if ( rc.isActive() ) {
							}
							rc.yield();
						}

					case 2: // Artillery Bot
						while ( true ) {
							if ( rc.isActive() ) {
							}
							rc.yield();
						}
						
					case 3: // Defender
						while ( true ) {
							if ( rc.isActive() ) {
							}
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
			rc.breakpoint();
			e.printStackTrace();
		}
	}

}
