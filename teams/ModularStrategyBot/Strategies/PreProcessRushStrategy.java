package ModularStrategyBot.Strategies;

import ModularStrategyBot.Graph.GraphGenerator;
import ModularStrategyBot.Graph.PathGenerator;
import ModularStrategyBot.Orders.*;
import ModularStrategyBot.Path.Path;
import ModularStrategyBot.Path.TailPath;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class PreProcessRushStrategy extends Strategy {

	public PreProcessRushStrategy(RobotController in) { super(in); }

	static Path rushPath;
	
	public void run() {
		Direction dir;
		try {
			switch (rc.getType()) {
			case HQ:
				System.out.println("I EXIST!!"+rc.getRobot().getID()+" --\t"+this.toString());
				// Lookup the Map's shortest Path
				if ( rushPath == null ) rushPath = lookupPath(rc.getMapHeight(),rc.getMapWidth(),rc.senseMineLocations(rc.getLocation(), 99999, null).length,rc.senseHQLocation(),rc.senseEnemyHQLocation());
				// If the shortest path doesnt exist, calculate it manually using the PathGenerator class.
				if ( rushPath == null ) {
					
					/* Override 
					I_RobotStrategy strat = new RushStrategy(rc);
					strat.run();
					/* Dead Code */
					
					MapLocation[] ml = rc.senseMineLocations(rc.getLocation(), 99999, null);
					System.out.println("\twidth = "+rc.getMapWidth()+";");
					System.out.println("\theight = "+rc.getMapHeight()+";");
					System.out.println("\tstart = new MapLocation("+rc.senseHQLocation().x+","+rc.senseHQLocation().y+");");
					System.out.println("\tfinish = new MapLocation("+rc.senseEnemyHQLocation().x+","+rc.senseEnemyHQLocation().y+");");
					System.out.println("\tMapLocation[] mines2 = {");
					for ( MapLocation n : ml ) {
						System.out.println("\tnew MapLocation("+n.x+","+n.y+"),");
					}
					System.out.println("\t};");
					while (true) rc.yield();
				}

				
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
				int waitingSince = Clock.getRoundNum();
				int waitConst = 0;				
				int waitMoves = 0;
				//rc.wearHat();
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
				if ( Clock.getRoundNum() % (waitConst*10) <= 9 ) isFirst = true; 	
				/* */
				//System.out.println(waitConst+"\t"+isFirst+"\t"+((Clock.getRoundNum()-40) % (waitConst*10)));
				
				//System.out.println("I EXIST!!"+rc.getRobot().getID()+" --\t"+isFirst+"\t"+this.toString());
				if ( rushPath == null ) rushPath = lookupPath(rc.getMapHeight(),rc.getMapWidth(),rc.senseMineLocations(rc.getLocation(), 99999, null).length,rc.senseHQLocation(),rc.senseEnemyHQLocation());
				if ( rushPath == null ) {
					/* Override */
					I_RobotStrategy strat = new RushStrategy(rc);
					strat.run();
					/* Dead Code */
				}
				int path = 1;
				MapLocation nextLink,myLoc,m;
				boolean formTail = false;
				TailPath tp = new TailPath(rc.senseEnemyHQLocation());
				while ( true ) {
					if ( rc.isActive() ) {
						myLoc = rc.getLocation();
						nextLink = rushPath.getLink(path++);
						if ( nextLink == null ) formTail = true;
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
						dir = rc.getLocation().directionTo(nextLink);
						m = rc.getLocation().add(dir);
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
							//for ( int i = (waitConst-2)*10; i > 0; i-- ) rc.yield();
							for ( int i = ((waitConst)*10)-(Clock.getRoundNum()-waitingSince); i > 0; i-- ) rc.yield();
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
	
	public static Path lookupPath(int mapHeight, int mapWidth, int numMines, MapLocation startA, MapLocation startB) {
		Path path = null;
		
		// "maze1.xml"
		if ( ( mapHeight == 60 ) && ( mapWidth == 60 ) && ( startA.equals(new MapLocation(59,0)) ) && ( startB.equals(new MapLocation(0,59)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(58,1));
			path.addLinkE(new MapLocation(58,2));
			path.addLinkE(new MapLocation(57,3));
			path.addLinkE(new MapLocation(56,3));
			path.addLinkE(new MapLocation(55,3));
			path.addLinkE(new MapLocation(54,2));
			path.addLinkE(new MapLocation(53,2));
			path.addLinkE(new MapLocation(52,2));
			path.addLinkE(new MapLocation(51,3));
			path.addLinkE(new MapLocation(51,4));
			path.addLinkE(new MapLocation(52,5));
			path.addLinkE(new MapLocation(52,6));
			path.addLinkE(new MapLocation(51,7));
			path.addLinkE(new MapLocation(50,8));
			path.addLinkE(new MapLocation(49,8));
			path.addLinkE(new MapLocation(48,8));
			path.addLinkE(new MapLocation(47,8));
			path.addLinkE(new MapLocation(46,8));
			path.addLinkE(new MapLocation(45,7));
			path.addLinkE(new MapLocation(45,6));
			path.addLinkE(new MapLocation(45,5));
			path.addLinkE(new MapLocation(45,4));
			path.addLinkE(new MapLocation(45,3));
			path.addLinkE(new MapLocation(45,2));
			path.addLinkE(new MapLocation(45,1));
			path.addLinkE(new MapLocation(44,0));
			path.addLinkE(new MapLocation(43,0));
			path.addLinkE(new MapLocation(42,0));
			path.addLinkE(new MapLocation(41,0));
			path.addLinkE(new MapLocation(40,0));
			path.addLinkE(new MapLocation(39,0));
			path.addLinkE(new MapLocation(38,1));
			path.addLinkE(new MapLocation(37,1));
			path.addLinkE(new MapLocation(36,2));
			path.addLinkE(new MapLocation(35,3));
			path.addLinkE(new MapLocation(35,4));
			path.addLinkE(new MapLocation(34,5));
			path.addLinkE(new MapLocation(35,6));
			path.addLinkE(new MapLocation(34,7));
			path.addLinkE(new MapLocation(35,8));
			path.addLinkE(new MapLocation(34,9));
			path.addLinkE(new MapLocation(35,10));
			path.addLinkE(new MapLocation(35,11));
			path.addLinkE(new MapLocation(34,12));
			path.addLinkE(new MapLocation(33,11));
			path.addLinkE(new MapLocation(32,11));
			path.addLinkE(new MapLocation(31,11));
			path.addLinkE(new MapLocation(30,11));
			path.addLinkE(new MapLocation(29,12));
			path.addLinkE(new MapLocation(28,12));
			path.addLinkE(new MapLocation(27,12));
			path.addLinkE(new MapLocation(26,12));
			path.addLinkE(new MapLocation(25,12));
			path.addLinkE(new MapLocation(24,12));
			path.addLinkE(new MapLocation(23,13));
			path.addLinkE(new MapLocation(22,14));
			path.addLinkE(new MapLocation(22,15));
			path.addLinkE(new MapLocation(22,16));
			path.addLinkE(new MapLocation(22,17));
			path.addLinkE(new MapLocation(22,18));
			path.addLinkE(new MapLocation(22,19));
			path.addLinkE(new MapLocation(22,20));
			path.addLinkE(new MapLocation(22,21));
			path.addLinkE(new MapLocation(22,22));
			path.addLinkE(new MapLocation(21,23));
			path.addLinkE(new MapLocation(20,24));
			path.addLinkE(new MapLocation(19,25));
			path.addLinkE(new MapLocation(18,26));
			path.addLinkE(new MapLocation(18,27));
			path.addLinkE(new MapLocation(18,28));
			path.addLinkE(new MapLocation(17,29));
			path.addLinkE(new MapLocation(17,30));
			path.addLinkE(new MapLocation(16,31));
			path.addLinkE(new MapLocation(15,32));
			path.addLinkE(new MapLocation(14,33));
			path.addLinkE(new MapLocation(13,33));
			path.addLinkE(new MapLocation(12,33));
			path.addLinkE(new MapLocation(11,33));
			path.addLinkE(new MapLocation(10,33));
			path.addLinkE(new MapLocation(9,33));
			path.addLinkE(new MapLocation(8,33));
			path.addLinkE(new MapLocation(7,33));
			path.addLinkE(new MapLocation(6,33));
			path.addLinkE(new MapLocation(5,34));
			path.addLinkE(new MapLocation(5,35));
			path.addLinkE(new MapLocation(5,36));
			path.addLinkE(new MapLocation(5,37));
			path.addLinkE(new MapLocation(5,38));
			path.addLinkE(new MapLocation(5,39));
			path.addLinkE(new MapLocation(5,40));
			path.addLinkE(new MapLocation(5,41));
			path.addLinkE(new MapLocation(5,42));
			path.addLinkE(new MapLocation(4,43));
			path.addLinkE(new MapLocation(5,44));
			path.addLinkE(new MapLocation(6,45));
			path.addLinkE(new MapLocation(6,46));
			path.addLinkE(new MapLocation(6,47));
			path.addLinkE(new MapLocation(6,48));
			path.addLinkE(new MapLocation(6,49));
			path.addLinkE(new MapLocation(5,50));
			path.addLinkE(new MapLocation(5,51));
			path.addLinkE(new MapLocation(5,52));
			path.addLinkE(new MapLocation(5,53));
			path.addLinkE(new MapLocation(6,54));
			path.addLinkE(new MapLocation(7,55));
			path.addLinkE(new MapLocation(8,56));
			path.addLinkE(new MapLocation(7,57));
			path.addLinkE(new MapLocation(6,57));
			path.addLinkE(new MapLocation(5,56));
			path.addLinkE(new MapLocation(4,56));
			path.addLinkE(new MapLocation(3,56));
			path.addLinkE(new MapLocation(2,56));
			path.addLinkE(new MapLocation(1,57));
		}
		
		// "uncorrelated"
		if ( ( mapHeight == 50 ) && ( mapWidth == 50 ) && ( startA.equals(new MapLocation(8,20)) ) && ( startB.equals(new MapLocation(41,29)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(9,20));
			path.addLinkE(new MapLocation(10,20));
			path.addLinkE(new MapLocation(11,20));
			path.addLinkE(new MapLocation(12,20));
			path.addLinkE(new MapLocation(13,19));
			path.addLinkE(new MapLocation(14,18));
			path.addLinkE(new MapLocation(15,18));
			path.addLinkE(new MapLocation(16,18));
			path.addLinkE(new MapLocation(17,18));
			path.addLinkE(new MapLocation(18,19));
			path.addLinkE(new MapLocation(19,18));
			path.addLinkE(new MapLocation(20,18));
			path.addLinkE(new MapLocation(21,19));
			path.addLinkE(new MapLocation(21,20));
			path.addLinkE(new MapLocation(21,21));
			path.addLinkE(new MapLocation(21,22));
			path.addLinkE(new MapLocation(21,23));
			path.addLinkE(new MapLocation(22,24));
			path.addLinkE(new MapLocation(23,25));
			path.addLinkE(new MapLocation(24,25));
			path.addLinkE(new MapLocation(25,24));
			path.addLinkE(new MapLocation(26,24));
			path.addLinkE(new MapLocation(27,25));
			path.addLinkE(new MapLocation(28,26));
			path.addLinkE(new MapLocation(27,27));
			path.addLinkE(new MapLocation(26,28));
			path.addLinkE(new MapLocation(27,29));
			path.addLinkE(new MapLocation(28,30));
			path.addLinkE(new MapLocation(29,31));
			path.addLinkE(new MapLocation(30,31));
			path.addLinkE(new MapLocation(31,30));
			path.addLinkE(new MapLocation(32,29));
			path.addLinkE(new MapLocation(33,28));
			path.addLinkE(new MapLocation(34,29));
			path.addLinkE(new MapLocation(35,29));
			path.addLinkE(new MapLocation(36,28));
			path.addLinkE(new MapLocation(37,29));
			path.addLinkE(new MapLocation(38,29));
			path.addLinkE(new MapLocation(39,29));
		}
		
		// "zigzag"
		if ( ( mapHeight == 25 ) && ( mapWidth == 55 ) && ( startA.equals(new MapLocation(54,24)) ) && ( startB.equals(new MapLocation(0,0)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(53,23));
			path.addLinkE(new MapLocation(52,23));
			path.addLinkE(new MapLocation(51,23));
			path.addLinkE(new MapLocation(50,23));
			path.addLinkE(new MapLocation(49,23));
			path.addLinkE(new MapLocation(48,23));
			path.addLinkE(new MapLocation(47,23));
			path.addLinkE(new MapLocation(46,23));
			path.addLinkE(new MapLocation(45,23));
			path.addLinkE(new MapLocation(44,23));
			path.addLinkE(new MapLocation(43,23));
			path.addLinkE(new MapLocation(42,23));
			path.addLinkE(new MapLocation(41,23));
			path.addLinkE(new MapLocation(40,23));
			path.addLinkE(new MapLocation(39,23));
			path.addLinkE(new MapLocation(38,23));
			path.addLinkE(new MapLocation(37,23));
			path.addLinkE(new MapLocation(36,23));
			path.addLinkE(new MapLocation(35,23));
			path.addLinkE(new MapLocation(34,23));
			path.addLinkE(new MapLocation(33,23));
			path.addLinkE(new MapLocation(32,23));
			path.addLinkE(new MapLocation(31,23));
			path.addLinkE(new MapLocation(30,23));
			path.addLinkE(new MapLocation(29,22));
			path.addLinkE(new MapLocation(28,21));
			path.addLinkE(new MapLocation(27,20));
			path.addLinkE(new MapLocation(26,19));
			path.addLinkE(new MapLocation(25,18));
			path.addLinkE(new MapLocation(26,17));
			path.addLinkE(new MapLocation(27,16));
			path.addLinkE(new MapLocation(26,15));
			path.addLinkE(new MapLocation(25,14));
			path.addLinkE(new MapLocation(26,13));
			path.addLinkE(new MapLocation(27,12));
			path.addLinkE(new MapLocation(26,11));
			path.addLinkE(new MapLocation(25,11));
			path.addLinkE(new MapLocation(24,11));
			path.addLinkE(new MapLocation(23,11));
			path.addLinkE(new MapLocation(22,11));
			path.addLinkE(new MapLocation(21,11));
			path.addLinkE(new MapLocation(20,11));
			path.addLinkE(new MapLocation(19,11));
			path.addLinkE(new MapLocation(18,11));
			path.addLinkE(new MapLocation(17,11));
			path.addLinkE(new MapLocation(16,11));
			path.addLinkE(new MapLocation(15,11));
			path.addLinkE(new MapLocation(14,11));
			path.addLinkE(new MapLocation(13,11));
			path.addLinkE(new MapLocation(12,11));
			path.addLinkE(new MapLocation(11,11));
			path.addLinkE(new MapLocation(10,11));
			path.addLinkE(new MapLocation(9,11));
			path.addLinkE(new MapLocation(8,11));
			path.addLinkE(new MapLocation(7,11));
			path.addLinkE(new MapLocation(6,11));
			path.addLinkE(new MapLocation(5,11));
			path.addLinkE(new MapLocation(4,11));
			path.addLinkE(new MapLocation(3,11));
			path.addLinkE(new MapLocation(2,10));
			path.addLinkE(new MapLocation(1,9));
			path.addLinkE(new MapLocation(0,8));
			path.addLinkE(new MapLocation(0,7));
			path.addLinkE(new MapLocation(0,6));
			path.addLinkE(new MapLocation(0,5));
			path.addLinkE(new MapLocation(0,4));
			path.addLinkE(new MapLocation(0,3));
			path.addLinkE(new MapLocation(0,2));
		}

		
		
		
		
		
		return path;
	}
}
