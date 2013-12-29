package ModularStrategyBot.Strategies;

import ModularStrategyBot.Graph.GraphGenerator;
import ModularStrategyBot.Graph.PathGenerator;
import ModularStrategyBot.Orders.*;
import ModularStrategyBot.Path.Path;
import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class ___PreProcessRushStrategy___OLD extends Strategy {

	public ___PreProcessRushStrategy___OLD(RobotController in) { super(in); }

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
					
					/* Override */
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
		
		
		// "WorldSeed"
		if ( ( mapHeight == 40 ) && ( mapWidth == 40 ) && ( startA.equals(new MapLocation(3,21)) ) && ( startB.equals(new MapLocation(36,21)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(4,22));
			path.addLinkE(new MapLocation(5,23));
			path.addLinkE(new MapLocation(6,24));
			path.addLinkE(new MapLocation(7,23));
			path.addLinkE(new MapLocation(8,22));
			path.addLinkE(new MapLocation(9,21));
			path.addLinkE(new MapLocation(10,22));
			path.addLinkE(new MapLocation(11,23));
			path.addLinkE(new MapLocation(12,23));
			path.addLinkE(new MapLocation(13,22));
			path.addLinkE(new MapLocation(14,21));
			path.addLinkE(new MapLocation(15,22));
			path.addLinkE(new MapLocation(16,23));
			path.addLinkE(new MapLocation(17,24));
			path.addLinkE(new MapLocation(18,24));
			path.addLinkE(new MapLocation(19,24));
			path.addLinkE(new MapLocation(20,24));
			path.addLinkE(new MapLocation(21,24));
			path.addLinkE(new MapLocation(22,24));
			path.addLinkE(new MapLocation(23,23));
			path.addLinkE(new MapLocation(24,22));
			path.addLinkE(new MapLocation(25,21));
			path.addLinkE(new MapLocation(26,22));
			path.addLinkE(new MapLocation(27,23));
			path.addLinkE(new MapLocation(28,23));
			path.addLinkE(new MapLocation(29,22));
			path.addLinkE(new MapLocation(30,21));
			path.addLinkE(new MapLocation(31,22));
			path.addLinkE(new MapLocation(32,23));
			path.addLinkE(new MapLocation(33,24));
			path.addLinkE(new MapLocation(34,23));
			path.addLinkE(new MapLocation(35,22));
			path.addLinkE(new MapLocation(36,22));
			path.addLinkE(new MapLocation(36,23));
			path.addLinkE(new MapLocation(37,22));
			path.addLinkE(new MapLocation(37,21));
			path.addLinkE(new MapLocation(38,21));
			path.addLinkE(new MapLocation(37,20));
			path.addLinkE(new MapLocation(36,20));
			path.addLinkE(new MapLocation(36,19));
			path.addLinkE(new MapLocation(35,20));
			path.addLinkE(new MapLocation(35,21));
			path.addLinkE(new MapLocation(34,21));
		}
		
		// "Indianapolis"
		if ( ( mapHeight == 21 ) && ( mapWidth == 70 ) && ( startA.equals(new MapLocation(0,10)) ) && ( startB.equals(new MapLocation(69,10)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(1,9));
			path.addLinkE(new MapLocation(2,8));
			path.addLinkE(new MapLocation(3,7));
			path.addLinkE(new MapLocation(4,6));
			path.addLinkE(new MapLocation(5,5));
			path.addLinkE(new MapLocation(6,4));
			path.addLinkE(new MapLocation(7,4));
			path.addLinkE(new MapLocation(8,4));
			path.addLinkE(new MapLocation(9,4));
			path.addLinkE(new MapLocation(10,4));
			path.addLinkE(new MapLocation(11,4));
			path.addLinkE(new MapLocation(12,4));
			path.addLinkE(new MapLocation(13,4));
			path.addLinkE(new MapLocation(14,4));
			path.addLinkE(new MapLocation(15,4));
			path.addLinkE(new MapLocation(16,4));
			path.addLinkE(new MapLocation(17,4));
			path.addLinkE(new MapLocation(18,4));
			path.addLinkE(new MapLocation(19,4));
			path.addLinkE(new MapLocation(20,4));
			path.addLinkE(new MapLocation(21,4));
			path.addLinkE(new MapLocation(22,4));
			path.addLinkE(new MapLocation(23,4));
			path.addLinkE(new MapLocation(24,4));
			path.addLinkE(new MapLocation(25,4));
			path.addLinkE(new MapLocation(26,4));
			path.addLinkE(new MapLocation(27,4));
			path.addLinkE(new MapLocation(28,4));
			path.addLinkE(new MapLocation(29,4));
			path.addLinkE(new MapLocation(30,4));
			path.addLinkE(new MapLocation(31,4));
			path.addLinkE(new MapLocation(32,4));
			path.addLinkE(new MapLocation(33,4));
			path.addLinkE(new MapLocation(34,4));
			path.addLinkE(new MapLocation(35,4));
			path.addLinkE(new MapLocation(36,4));
			path.addLinkE(new MapLocation(37,4));
			path.addLinkE(new MapLocation(38,4));
			path.addLinkE(new MapLocation(39,4));
			path.addLinkE(new MapLocation(40,4));
			path.addLinkE(new MapLocation(41,4));
			path.addLinkE(new MapLocation(42,4));
			path.addLinkE(new MapLocation(43,4));
			path.addLinkE(new MapLocation(44,4));
			path.addLinkE(new MapLocation(45,4));
			path.addLinkE(new MapLocation(46,4));
			path.addLinkE(new MapLocation(47,4));
			path.addLinkE(new MapLocation(48,4));
			path.addLinkE(new MapLocation(49,4));
			path.addLinkE(new MapLocation(50,4));
			path.addLinkE(new MapLocation(51,4));
			path.addLinkE(new MapLocation(52,4));
			path.addLinkE(new MapLocation(53,4));
			path.addLinkE(new MapLocation(54,4));
			path.addLinkE(new MapLocation(55,4));
			path.addLinkE(new MapLocation(56,4));
			path.addLinkE(new MapLocation(57,4));
			path.addLinkE(new MapLocation(58,4));
			path.addLinkE(new MapLocation(59,4));
			path.addLinkE(new MapLocation(60,4));
			path.addLinkE(new MapLocation(61,4));
			path.addLinkE(new MapLocation(62,4));
			path.addLinkE(new MapLocation(63,4));
			path.addLinkE(new MapLocation(64,5));
			path.addLinkE(new MapLocation(65,6));
			path.addLinkE(new MapLocation(66,7));
			path.addLinkE(new MapLocation(67,8));
			path.addLinkE(new MapLocation(68,9));
			path.addLinkE(new MapLocation(68,10));
			path.addLinkE(new MapLocation(67,10));
			path.addLinkE(new MapLocation(68,11));
			path.addLinkE(new MapLocation(69,11));
			path.addLinkE(new MapLocation(69,12));
			path.addLinkE(new MapLocation(70,11));
			path.addLinkE(new MapLocation(70,10));
			path.addLinkE(new MapLocation(71,10));
			path.addLinkE(new MapLocation(70,9));
			path.addLinkE(new MapLocation(69,9));
			path.addLinkE(new MapLocation(69,8));
		}
		
		// "Ulaanbaatar"
		if ( ( mapHeight == 25 ) && ( mapWidth == 55 ) && ( startA.equals(new MapLocation(53,18)) ) && ( startB.equals(new MapLocation(1,6)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(52,18));
			path.addLinkE(new MapLocation(51,19));
			path.addLinkE(new MapLocation(50,20));
			path.addLinkE(new MapLocation(49,20));
			path.addLinkE(new MapLocation(48,21));
			path.addLinkE(new MapLocation(47,21));
			path.addLinkE(new MapLocation(46,21));
			path.addLinkE(new MapLocation(45,22));
			path.addLinkE(new MapLocation(44,22));
			path.addLinkE(new MapLocation(43,22));
			path.addLinkE(new MapLocation(42,22));
			path.addLinkE(new MapLocation(41,23));
			path.addLinkE(new MapLocation(40,23));
			path.addLinkE(new MapLocation(39,23));
			path.addLinkE(new MapLocation(38,22));
			path.addLinkE(new MapLocation(37,23));
			path.addLinkE(new MapLocation(36,22));
			path.addLinkE(new MapLocation(35,23));
			path.addLinkE(new MapLocation(34,23));
			path.addLinkE(new MapLocation(33,23));
			path.addLinkE(new MapLocation(32,23));
			path.addLinkE(new MapLocation(31,23));
			path.addLinkE(new MapLocation(30,22));
			path.addLinkE(new MapLocation(29,23));
			path.addLinkE(new MapLocation(28,23));
			path.addLinkE(new MapLocation(27,23));
			path.addLinkE(new MapLocation(26,23));
			path.addLinkE(new MapLocation(25,23));
			path.addLinkE(new MapLocation(24,22));
			path.addLinkE(new MapLocation(23,23));
			path.addLinkE(new MapLocation(22,22));
			path.addLinkE(new MapLocation(21,23));
			path.addLinkE(new MapLocation(20,23));
			path.addLinkE(new MapLocation(19,23));
			path.addLinkE(new MapLocation(18,23));
			path.addLinkE(new MapLocation(17,23));
			path.addLinkE(new MapLocation(16,23));
			path.addLinkE(new MapLocation(15,23));
			path.addLinkE(new MapLocation(14,22));
			path.addLinkE(new MapLocation(13,21));
			path.addLinkE(new MapLocation(12,21));
			path.addLinkE(new MapLocation(11,21));
			path.addLinkE(new MapLocation(10,20));
			path.addLinkE(new MapLocation(9,20));
			path.addLinkE(new MapLocation(8,19));
			path.addLinkE(new MapLocation(7,18));
			path.addLinkE(new MapLocation(6,18));
			path.addLinkE(new MapLocation(5,18));
			path.addLinkE(new MapLocation(4,18));
			path.addLinkE(new MapLocation(3,17));
			path.addLinkE(new MapLocation(2,16));
			path.addLinkE(new MapLocation(2,15));
			path.addLinkE(new MapLocation(1,14));
			path.addLinkE(new MapLocation(1,13));
			path.addLinkE(new MapLocation(0,12));
			path.addLinkE(new MapLocation(0,11));
			path.addLinkE(new MapLocation(0,10));
			path.addLinkE(new MapLocation(0,9));
			path.addLinkE(new MapLocation(0,8));
			path.addLinkE(new MapLocation(1,7));
			path.addLinkE(new MapLocation(1,8));
			path.addLinkE(new MapLocation(2,7));
			path.addLinkE(new MapLocation(2,6));
			path.addLinkE(new MapLocation(3,6));
			path.addLinkE(new MapLocation(2,5));
			path.addLinkE(new MapLocation(1,5));
			path.addLinkE(new MapLocation(1,4));
			path.addLinkE(new MapLocation(0,5));
			path.addLinkE(new MapLocation(0,6));
			path.addLinkE(new MapLocation(-1,6));
			path.addLinkE(new MapLocation(0,7));
		}
		
		// "Shard"
		if ( ( mapHeight == 40 ) && ( mapWidth == 40 ) && ( startA.equals(new MapLocation(11,21)) ) && ( startB.equals(new MapLocation(28,18)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(10,20));
			path.addLinkE(new MapLocation(9,19));
			path.addLinkE(new MapLocation(8,18));
			path.addLinkE(new MapLocation(7,17));
			path.addLinkE(new MapLocation(8,16));
			path.addLinkE(new MapLocation(9,15));
			path.addLinkE(new MapLocation(10,14));
			path.addLinkE(new MapLocation(11,13));
			path.addLinkE(new MapLocation(12,12));
			path.addLinkE(new MapLocation(13,11));
			path.addLinkE(new MapLocation(14,10));
			path.addLinkE(new MapLocation(15,11));
			path.addLinkE(new MapLocation(16,12));
			path.addLinkE(new MapLocation(17,11));
			path.addLinkE(new MapLocation(18,10));
			path.addLinkE(new MapLocation(19,10));
			path.addLinkE(new MapLocation(20,11));
			path.addLinkE(new MapLocation(21,12));
			path.addLinkE(new MapLocation(22,11));
			path.addLinkE(new MapLocation(23,10));
			path.addLinkE(new MapLocation(24,9));
			path.addLinkE(new MapLocation(25,10));
			path.addLinkE(new MapLocation(26,11));
			path.addLinkE(new MapLocation(26,12));
			path.addLinkE(new MapLocation(27,13));
			path.addLinkE(new MapLocation(27,14));
			path.addLinkE(new MapLocation(27,15));
			path.addLinkE(new MapLocation(27,16));
			path.addLinkE(new MapLocation(27,17));
			path.addLinkE(new MapLocation(27,18));
			path.addLinkE(new MapLocation(26,18));
			path.addLinkE(new MapLocation(27,19));
			path.addLinkE(new MapLocation(28,19));
			path.addLinkE(new MapLocation(28,20));
			path.addLinkE(new MapLocation(29,19));
			path.addLinkE(new MapLocation(29,18));
			path.addLinkE(new MapLocation(30,18));
			path.addLinkE(new MapLocation(29,17));
			path.addLinkE(new MapLocation(28,17));
			path.addLinkE(new MapLocation(28,16));
		}

		// "Shanghai"
		if ( ( mapHeight == 69 ) && ( mapWidth == 69 ) && ( startA.equals(new MapLocation(10,6)) ) && ( startB.equals(new MapLocation(58,62)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(9,7));
			path.addLinkE(new MapLocation(10,8));
			path.addLinkE(new MapLocation(11,9));
			path.addLinkE(new MapLocation(11,10));
			path.addLinkE(new MapLocation(12,11));
			path.addLinkE(new MapLocation(12,12));
			path.addLinkE(new MapLocation(13,13));
			path.addLinkE(new MapLocation(12,14));
			path.addLinkE(new MapLocation(13,15));
			path.addLinkE(new MapLocation(14,16));
			path.addLinkE(new MapLocation(15,17));
			path.addLinkE(new MapLocation(16,18));
			path.addLinkE(new MapLocation(17,19));
			path.addLinkE(new MapLocation(18,18));
			path.addLinkE(new MapLocation(19,18));
			path.addLinkE(new MapLocation(20,19));
			path.addLinkE(new MapLocation(20,20));
			path.addLinkE(new MapLocation(19,21));
			path.addLinkE(new MapLocation(20,22));
			path.addLinkE(new MapLocation(21,23));
			path.addLinkE(new MapLocation(22,24));
			path.addLinkE(new MapLocation(22,25));
			path.addLinkE(new MapLocation(23,26));
			path.addLinkE(new MapLocation(24,27));
			path.addLinkE(new MapLocation(24,28));
			path.addLinkE(new MapLocation(24,29));
			path.addLinkE(new MapLocation(25,30));
			path.addLinkE(new MapLocation(26,31));
			path.addLinkE(new MapLocation(25,32));
			path.addLinkE(new MapLocation(26,33));
			path.addLinkE(new MapLocation(25,34));
			path.addLinkE(new MapLocation(26,35));
			path.addLinkE(new MapLocation(26,36));
			path.addLinkE(new MapLocation(27,37));
			path.addLinkE(new MapLocation(28,38));
			path.addLinkE(new MapLocation(29,39));
			path.addLinkE(new MapLocation(28,40));
			path.addLinkE(new MapLocation(29,41));
			path.addLinkE(new MapLocation(29,42));
			path.addLinkE(new MapLocation(29,43));
			path.addLinkE(new MapLocation(30,44));
			path.addLinkE(new MapLocation(31,45));
			path.addLinkE(new MapLocation(32,46));
			path.addLinkE(new MapLocation(33,47));
			path.addLinkE(new MapLocation(34,48));
			path.addLinkE(new MapLocation(34,49));
			path.addLinkE(new MapLocation(34,50));
			path.addLinkE(new MapLocation(35,51));
			path.addLinkE(new MapLocation(34,52));
			path.addLinkE(new MapLocation(35,53));
			path.addLinkE(new MapLocation(36,54));
			path.addLinkE(new MapLocation(36,55));
			path.addLinkE(new MapLocation(36,56));
			path.addLinkE(new MapLocation(37,57));
			path.addLinkE(new MapLocation(37,58));
			path.addLinkE(new MapLocation(38,59));
			path.addLinkE(new MapLocation(38,60));
			path.addLinkE(new MapLocation(39,61));
			path.addLinkE(new MapLocation(39,62));
			path.addLinkE(new MapLocation(40,63));
			path.addLinkE(new MapLocation(41,64));
			path.addLinkE(new MapLocation(42,64));
			path.addLinkE(new MapLocation(43,64));
			path.addLinkE(new MapLocation(44,64));
			path.addLinkE(new MapLocation(45,64));
			path.addLinkE(new MapLocation(46,64));
			path.addLinkE(new MapLocation(47,64));
			path.addLinkE(new MapLocation(48,64));
			path.addLinkE(new MapLocation(49,64));
			path.addLinkE(new MapLocation(50,63));
			path.addLinkE(new MapLocation(51,64));
			path.addLinkE(new MapLocation(52,64));
			path.addLinkE(new MapLocation(53,63));
			path.addLinkE(new MapLocation(54,63));
			path.addLinkE(new MapLocation(55,63));
			path.addLinkE(new MapLocation(56,62));
			path.addLinkE(new MapLocation(57,63));
			path.addLinkE(new MapLocation(58,63));
			path.addLinkE(new MapLocation(58,64));
			path.addLinkE(new MapLocation(59,63));
			path.addLinkE(new MapLocation(59,62));
			path.addLinkE(new MapLocation(60,62));
			path.addLinkE(new MapLocation(59,61));
			path.addLinkE(new MapLocation(58,61));
			path.addLinkE(new MapLocation(58,60));
			path.addLinkE(new MapLocation(57,61));
			path.addLinkE(new MapLocation(57,62));
		}
		
		// "Northstar"
		if ( ( mapHeight == 40 ) && ( mapWidth == 40 ) && ( startA.equals(new MapLocation(10,27)) ) && ( startB.equals(new MapLocation(29,12)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(9,26));
			path.addLinkE(new MapLocation(8,25));
			path.addLinkE(new MapLocation(7,24));
			path.addLinkE(new MapLocation(6,23));
			path.addLinkE(new MapLocation(5,22));
			path.addLinkE(new MapLocation(4,21));
			path.addLinkE(new MapLocation(3,20));
			path.addLinkE(new MapLocation(2,19));
			path.addLinkE(new MapLocation(2,18));
			path.addLinkE(new MapLocation(3,17));
			path.addLinkE(new MapLocation(4,16));
			path.addLinkE(new MapLocation(5,15));
			path.addLinkE(new MapLocation(6,14));
			path.addLinkE(new MapLocation(7,13));
			path.addLinkE(new MapLocation(8,12));
			path.addLinkE(new MapLocation(9,11));
			path.addLinkE(new MapLocation(10,10));
			path.addLinkE(new MapLocation(11,9));
			path.addLinkE(new MapLocation(12,8));
			path.addLinkE(new MapLocation(13,7));
			path.addLinkE(new MapLocation(14,6));
			path.addLinkE(new MapLocation(15,5));
			path.addLinkE(new MapLocation(16,4));
			path.addLinkE(new MapLocation(17,3));
			path.addLinkE(new MapLocation(18,2));
			path.addLinkE(new MapLocation(19,1));
			path.addLinkE(new MapLocation(20,1));
			path.addLinkE(new MapLocation(21,2));
			path.addLinkE(new MapLocation(21,3));
			path.addLinkE(new MapLocation(21,4));
			path.addLinkE(new MapLocation(22,5));
			path.addLinkE(new MapLocation(23,6));
			path.addLinkE(new MapLocation(24,7));
			path.addLinkE(new MapLocation(25,8));
			path.addLinkE(new MapLocation(26,9));
			path.addLinkE(new MapLocation(27,10));
			path.addLinkE(new MapLocation(28,11));
			path.addLinkE(new MapLocation(28,12));
			path.addLinkE(new MapLocation(27,12));
			path.addLinkE(new MapLocation(28,13));
			path.addLinkE(new MapLocation(29,13));
			path.addLinkE(new MapLocation(29,14));
			path.addLinkE(new MapLocation(30,13));
			path.addLinkE(new MapLocation(30,12));
			path.addLinkE(new MapLocation(31,12));
			path.addLinkE(new MapLocation(30,11));
			path.addLinkE(new MapLocation(29,11));
			path.addLinkE(new MapLocation(29,10));
		}
		
		// "Highway"
		if ( ( mapHeight == 60 ) && ( mapWidth == 60 ) && ( startA.equals(new MapLocation(7,48)) ) && ( startB.equals(new MapLocation(52,48)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(8,47));
			path.addLinkE(new MapLocation(9,46));
			path.addLinkE(new MapLocation(10,45));
			path.addLinkE(new MapLocation(11,44));
			path.addLinkE(new MapLocation(12,43));
			path.addLinkE(new MapLocation(13,43));
			path.addLinkE(new MapLocation(14,44));
			path.addLinkE(new MapLocation(15,45));
			path.addLinkE(new MapLocation(16,46));
			path.addLinkE(new MapLocation(17,47));
			path.addLinkE(new MapLocation(18,46));
			path.addLinkE(new MapLocation(18,45));
			path.addLinkE(new MapLocation(18,44));
			path.addLinkE(new MapLocation(17,43));
			path.addLinkE(new MapLocation(17,42));
			path.addLinkE(new MapLocation(16,41));
			path.addLinkE(new MapLocation(15,40));
			path.addLinkE(new MapLocation(14,39));
			path.addLinkE(new MapLocation(13,38));
			path.addLinkE(new MapLocation(13,37));
			path.addLinkE(new MapLocation(14,36));
			path.addLinkE(new MapLocation(15,35));
			path.addLinkE(new MapLocation(16,34));
			path.addLinkE(new MapLocation(17,33));
			path.addLinkE(new MapLocation(18,32));
			path.addLinkE(new MapLocation(19,31));
			path.addLinkE(new MapLocation(20,30));
			path.addLinkE(new MapLocation(21,29));
			path.addLinkE(new MapLocation(22,28));
			path.addLinkE(new MapLocation(23,27));
			path.addLinkE(new MapLocation(24,27));
			path.addLinkE(new MapLocation(25,27));
			path.addLinkE(new MapLocation(26,27));
			path.addLinkE(new MapLocation(27,27));
			path.addLinkE(new MapLocation(28,26));
			path.addLinkE(new MapLocation(29,25));
			path.addLinkE(new MapLocation(30,25));
			path.addLinkE(new MapLocation(31,26));
			path.addLinkE(new MapLocation(32,27));
			path.addLinkE(new MapLocation(33,27));
			path.addLinkE(new MapLocation(34,27));
			path.addLinkE(new MapLocation(35,27));
			path.addLinkE(new MapLocation(36,27));
			path.addLinkE(new MapLocation(37,28));
			path.addLinkE(new MapLocation(38,29));
			path.addLinkE(new MapLocation(39,30));
			path.addLinkE(new MapLocation(40,31));
			path.addLinkE(new MapLocation(41,32));
			path.addLinkE(new MapLocation(42,33));
			path.addLinkE(new MapLocation(43,34));
			path.addLinkE(new MapLocation(44,35));
			path.addLinkE(new MapLocation(45,36));
			path.addLinkE(new MapLocation(46,37));
			path.addLinkE(new MapLocation(45,38));
			path.addLinkE(new MapLocation(44,39));
			path.addLinkE(new MapLocation(43,40));
			path.addLinkE(new MapLocation(42,41));
			path.addLinkE(new MapLocation(41,42));
			path.addLinkE(new MapLocation(40,43));
			path.addLinkE(new MapLocation(39,44));
			path.addLinkE(new MapLocation(40,45));
			path.addLinkE(new MapLocation(41,46));
			path.addLinkE(new MapLocation(42,47));
			path.addLinkE(new MapLocation(43,46));
			path.addLinkE(new MapLocation(44,45));
			path.addLinkE(new MapLocation(45,44));
			path.addLinkE(new MapLocation(46,43));
			path.addLinkE(new MapLocation(47,43));
			path.addLinkE(new MapLocation(48,44));
			path.addLinkE(new MapLocation(49,45));
			path.addLinkE(new MapLocation(50,46));
			path.addLinkE(new MapLocation(51,47));
			path.addLinkE(new MapLocation(51,48));
			path.addLinkE(new MapLocation(50,48));
			path.addLinkE(new MapLocation(51,49));
			path.addLinkE(new MapLocation(52,49));
			path.addLinkE(new MapLocation(52,50));
			path.addLinkE(new MapLocation(53,49));
			path.addLinkE(new MapLocation(53,48));
			path.addLinkE(new MapLocation(54,48));
			path.addLinkE(new MapLocation(53,47));
			path.addLinkE(new MapLocation(52,47));
			path.addLinkE(new MapLocation(52,46));
		}
		
		// "beachhead"
		if ( ( mapHeight == 60 ) && ( mapWidth == 60 ) && ( startA.equals(new MapLocation(27,2)) ) && ( startB.equals(new MapLocation(27,57)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(26,1));
			path.addLinkE(new MapLocation(25,0));
			path.addLinkE(new MapLocation(24,0));
			path.addLinkE(new MapLocation(23,0));
			path.addLinkE(new MapLocation(22,0));
			path.addLinkE(new MapLocation(21,0));
			path.addLinkE(new MapLocation(20,0));
			path.addLinkE(new MapLocation(19,1));
			path.addLinkE(new MapLocation(18,2));
			path.addLinkE(new MapLocation(17,3));
			path.addLinkE(new MapLocation(16,4));
			path.addLinkE(new MapLocation(15,5));
			path.addLinkE(new MapLocation(14,6));
			path.addLinkE(new MapLocation(13,7));
			path.addLinkE(new MapLocation(12,8));
			path.addLinkE(new MapLocation(11,7));
			path.addLinkE(new MapLocation(10,8));
			path.addLinkE(new MapLocation(9,9));
			path.addLinkE(new MapLocation(8,10));
			path.addLinkE(new MapLocation(7,11));
			path.addLinkE(new MapLocation(6,12));
			path.addLinkE(new MapLocation(5,13));
			path.addLinkE(new MapLocation(4,14));
			path.addLinkE(new MapLocation(3,15));
			path.addLinkE(new MapLocation(2,16));
			path.addLinkE(new MapLocation(1,17));
			path.addLinkE(new MapLocation(0,18));
			path.addLinkE(new MapLocation(0,19));
			path.addLinkE(new MapLocation(0,20));
			path.addLinkE(new MapLocation(0,21));
			path.addLinkE(new MapLocation(0,22));
			path.addLinkE(new MapLocation(0,23));
			path.addLinkE(new MapLocation(0,24));
			path.addLinkE(new MapLocation(0,25));
			path.addLinkE(new MapLocation(0,26));
			path.addLinkE(new MapLocation(0,27));
			path.addLinkE(new MapLocation(0,28));
			path.addLinkE(new MapLocation(0,29));
			path.addLinkE(new MapLocation(0,30));
			path.addLinkE(new MapLocation(0,31));
			path.addLinkE(new MapLocation(0,32));
			path.addLinkE(new MapLocation(0,33));
			path.addLinkE(new MapLocation(0,34));
			path.addLinkE(new MapLocation(0,35));
			path.addLinkE(new MapLocation(0,36));
			path.addLinkE(new MapLocation(0,37));
			path.addLinkE(new MapLocation(0,38));
			path.addLinkE(new MapLocation(0,39));
			path.addLinkE(new MapLocation(0,40));
			path.addLinkE(new MapLocation(0,41));
			path.addLinkE(new MapLocation(1,42));
			path.addLinkE(new MapLocation(2,43));
			path.addLinkE(new MapLocation(3,44));
			path.addLinkE(new MapLocation(4,45));
			path.addLinkE(new MapLocation(5,46));
			path.addLinkE(new MapLocation(6,47));
			path.addLinkE(new MapLocation(7,48));
			path.addLinkE(new MapLocation(8,49));
			path.addLinkE(new MapLocation(9,50));
			path.addLinkE(new MapLocation(10,51));
			path.addLinkE(new MapLocation(11,52));
			path.addLinkE(new MapLocation(12,51));
			path.addLinkE(new MapLocation(13,51));
			path.addLinkE(new MapLocation(14,51));
			path.addLinkE(new MapLocation(15,52));
			path.addLinkE(new MapLocation(16,51));
			path.addLinkE(new MapLocation(17,50));
			path.addLinkE(new MapLocation(18,49));
			path.addLinkE(new MapLocation(19,49));
			path.addLinkE(new MapLocation(20,50));
			path.addLinkE(new MapLocation(21,51));
			path.addLinkE(new MapLocation(22,52));
			path.addLinkE(new MapLocation(23,53));
			path.addLinkE(new MapLocation(24,54));
			path.addLinkE(new MapLocation(25,55));
			path.addLinkE(new MapLocation(26,56));
			path.addLinkE(new MapLocation(26,57));
			path.addLinkE(new MapLocation(25,57));
			path.addLinkE(new MapLocation(26,58));
			path.addLinkE(new MapLocation(27,58));
			path.addLinkE(new MapLocation(27,59));
			path.addLinkE(new MapLocation(28,58));
			path.addLinkE(new MapLocation(28,57));
			path.addLinkE(new MapLocation(29,57));
			path.addLinkE(new MapLocation(28,56));
			path.addLinkE(new MapLocation(27,56));
			path.addLinkE(new MapLocation(27,55));
		}
		
		// "basetrade"
		if ( ( mapHeight == 20 ) && ( mapWidth == 70 ) && ( startA.equals(new MapLocation(69,0)) ) && ( startB.equals(new MapLocation(0,19)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(68,0));
			path.addLinkE(new MapLocation(67,0));
			path.addLinkE(new MapLocation(66,0));
			path.addLinkE(new MapLocation(65,0));
			path.addLinkE(new MapLocation(64,0));
			path.addLinkE(new MapLocation(63,0));
			path.addLinkE(new MapLocation(62,0));
			path.addLinkE(new MapLocation(61,0));
			path.addLinkE(new MapLocation(60,0));
			path.addLinkE(new MapLocation(59,0));
			path.addLinkE(new MapLocation(58,0));
			path.addLinkE(new MapLocation(57,0));
			path.addLinkE(new MapLocation(56,0));
			path.addLinkE(new MapLocation(55,0));
			path.addLinkE(new MapLocation(54,0));
			path.addLinkE(new MapLocation(53,0));
			path.addLinkE(new MapLocation(52,0));
			path.addLinkE(new MapLocation(51,0));
			path.addLinkE(new MapLocation(50,0));
			path.addLinkE(new MapLocation(49,0));
			path.addLinkE(new MapLocation(48,0));
			path.addLinkE(new MapLocation(47,0));
			path.addLinkE(new MapLocation(46,0));
			path.addLinkE(new MapLocation(45,0));
			path.addLinkE(new MapLocation(44,0));
			path.addLinkE(new MapLocation(43,0));
			path.addLinkE(new MapLocation(42,0));
			path.addLinkE(new MapLocation(41,0));
			path.addLinkE(new MapLocation(40,0));
			path.addLinkE(new MapLocation(39,0));
			path.addLinkE(new MapLocation(38,0));
			path.addLinkE(new MapLocation(37,0));
			path.addLinkE(new MapLocation(36,0));
			path.addLinkE(new MapLocation(35,0));
			path.addLinkE(new MapLocation(34,0));
			path.addLinkE(new MapLocation(33,0));
			path.addLinkE(new MapLocation(32,0));
			path.addLinkE(new MapLocation(31,0));
			path.addLinkE(new MapLocation(30,0));
			path.addLinkE(new MapLocation(29,0));
			path.addLinkE(new MapLocation(28,0));
			path.addLinkE(new MapLocation(27,0));
			path.addLinkE(new MapLocation(26,0));
			path.addLinkE(new MapLocation(25,0));
			path.addLinkE(new MapLocation(24,0));
			path.addLinkE(new MapLocation(23,0));
			path.addLinkE(new MapLocation(22,1));
			path.addLinkE(new MapLocation(21,2));
			path.addLinkE(new MapLocation(20,3));
			path.addLinkE(new MapLocation(19,4));
			path.addLinkE(new MapLocation(18,5));
			path.addLinkE(new MapLocation(17,6));
			path.addLinkE(new MapLocation(16,7));
			path.addLinkE(new MapLocation(15,8));
			path.addLinkE(new MapLocation(14,9));
			path.addLinkE(new MapLocation(13,10));
			path.addLinkE(new MapLocation(12,11));
			path.addLinkE(new MapLocation(11,12));
			path.addLinkE(new MapLocation(10,13));
			path.addLinkE(new MapLocation(9,14));
			path.addLinkE(new MapLocation(8,15));
			path.addLinkE(new MapLocation(7,16));
			path.addLinkE(new MapLocation(6,17));
			path.addLinkE(new MapLocation(5,17));
			path.addLinkE(new MapLocation(4,17));
			path.addLinkE(new MapLocation(3,17));
			path.addLinkE(new MapLocation(2,17));
			path.addLinkE(new MapLocation(1,18));
			path.addLinkE(new MapLocation(0,18));
			path.addLinkE(new MapLocation(0,17));
			path.addLinkE(new MapLocation(-1,18));
			path.addLinkE(new MapLocation(-1,19));
			path.addLinkE(new MapLocation(-2,19));
			path.addLinkE(new MapLocation(-1,20));
			path.addLinkE(new MapLocation(0,20));
			path.addLinkE(new MapLocation(0,21));
			path.addLinkE(new MapLocation(1,20));
			path.addLinkE(new MapLocation(1,19));
			path.addLinkE(new MapLocation(2,19));
		}
		
		// "backdoor"
		if ( ( mapHeight == 41 ) && ( mapWidth == 41 ) && ( startA.equals(new MapLocation(20,34)) ) && ( startB.equals(new MapLocation(20,6)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(19,33));
			path.addLinkE(new MapLocation(18,32));
			path.addLinkE(new MapLocation(17,32));
			path.addLinkE(new MapLocation(16,32));
			path.addLinkE(new MapLocation(15,31));
			path.addLinkE(new MapLocation(14,30));
			path.addLinkE(new MapLocation(13,29));
			path.addLinkE(new MapLocation(12,28));
			path.addLinkE(new MapLocation(12,27));
			path.addLinkE(new MapLocation(11,26));
			path.addLinkE(new MapLocation(10,25));
			path.addLinkE(new MapLocation(9,24));
			path.addLinkE(new MapLocation(8,23));
			path.addLinkE(new MapLocation(7,22));
			path.addLinkE(new MapLocation(6,21));
			path.addLinkE(new MapLocation(5,20));
			path.addLinkE(new MapLocation(6,19));
			path.addLinkE(new MapLocation(7,18));
			path.addLinkE(new MapLocation(8,17));
			path.addLinkE(new MapLocation(9,16));
			path.addLinkE(new MapLocation(10,15));
			path.addLinkE(new MapLocation(11,14));
			path.addLinkE(new MapLocation(12,13));
			path.addLinkE(new MapLocation(12,12));
			path.addLinkE(new MapLocation(13,11));
			path.addLinkE(new MapLocation(14,10));
			path.addLinkE(new MapLocation(15,9));
			path.addLinkE(new MapLocation(16,8));
			path.addLinkE(new MapLocation(17,7));
			path.addLinkE(new MapLocation(18,6));
			path.addLinkE(new MapLocation(19,7));
			path.addLinkE(new MapLocation(20,7));
			path.addLinkE(new MapLocation(20,8));
			path.addLinkE(new MapLocation(21,7));
			path.addLinkE(new MapLocation(21,6));
			path.addLinkE(new MapLocation(22,6));
			path.addLinkE(new MapLocation(21,5));
			path.addLinkE(new MapLocation(20,5));
			path.addLinkE(new MapLocation(20,4));
			path.addLinkE(new MapLocation(19,5));
			path.addLinkE(new MapLocation(19,6));
		}
		
		// "arraydotutil"
		if ( ( mapHeight == 60 ) && ( mapWidth == 60 ) && ( startA.equals(new MapLocation(5,5)) ) && ( startB.equals(new MapLocation(54,54)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(6,6));
			path.addLinkE(new MapLocation(7,7));
			path.addLinkE(new MapLocation(8,8));
			path.addLinkE(new MapLocation(9,9));
			path.addLinkE(new MapLocation(10,10));
			path.addLinkE(new MapLocation(11,11));
			path.addLinkE(new MapLocation(12,12));
			path.addLinkE(new MapLocation(13,13));
			path.addLinkE(new MapLocation(14,14));
			path.addLinkE(new MapLocation(15,15));
			path.addLinkE(new MapLocation(16,16));
			path.addLinkE(new MapLocation(17,17));
			path.addLinkE(new MapLocation(18,18));
			path.addLinkE(new MapLocation(19,19));
			path.addLinkE(new MapLocation(20,20));
			path.addLinkE(new MapLocation(21,21));
			path.addLinkE(new MapLocation(22,22));
			path.addLinkE(new MapLocation(23,23));
			path.addLinkE(new MapLocation(24,24));
			path.addLinkE(new MapLocation(25,25));
			path.addLinkE(new MapLocation(26,26));
			path.addLinkE(new MapLocation(27,27));
			path.addLinkE(new MapLocation(28,28));
			path.addLinkE(new MapLocation(29,29));
			path.addLinkE(new MapLocation(30,30));
			path.addLinkE(new MapLocation(31,31));
			path.addLinkE(new MapLocation(32,32));
			path.addLinkE(new MapLocation(33,33));
			path.addLinkE(new MapLocation(34,34));
			path.addLinkE(new MapLocation(35,35));
			path.addLinkE(new MapLocation(36,36));
			path.addLinkE(new MapLocation(37,37));
			path.addLinkE(new MapLocation(38,38));
			path.addLinkE(new MapLocation(39,39));
			path.addLinkE(new MapLocation(40,40));
			path.addLinkE(new MapLocation(41,41));
			path.addLinkE(new MapLocation(42,42));
			path.addLinkE(new MapLocation(43,43));
			path.addLinkE(new MapLocation(44,44));
			path.addLinkE(new MapLocation(45,45));
			path.addLinkE(new MapLocation(46,46));
			path.addLinkE(new MapLocation(47,47));
			path.addLinkE(new MapLocation(48,48));
			path.addLinkE(new MapLocation(49,49));
			path.addLinkE(new MapLocation(50,50));
			path.addLinkE(new MapLocation(51,51));
			path.addLinkE(new MapLocation(52,52));
			path.addLinkE(new MapLocation(53,53));
			path.addLinkE(new MapLocation(53,54));
			path.addLinkE(new MapLocation(52,54));
			path.addLinkE(new MapLocation(53,55));
			path.addLinkE(new MapLocation(54,55));
			path.addLinkE(new MapLocation(54,56));
			path.addLinkE(new MapLocation(55,55));
			path.addLinkE(new MapLocation(55,54));
			path.addLinkE(new MapLocation(56,54));
			path.addLinkE(new MapLocation(55,53));
			path.addLinkE(new MapLocation(54,53));
			path.addLinkE(new MapLocation(54,52));
		}
		
		// "Akviligjuaq"
		if ( ( mapHeight == 25 ) && ( mapWidth == 25 ) && ( startA.equals(new MapLocation(22,21)) ) && ( startB.equals(new MapLocation(2,3)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(21,21));
			path.addLinkE(new MapLocation(20,20));
			path.addLinkE(new MapLocation(19,20));
			path.addLinkE(new MapLocation(18,19));
			path.addLinkE(new MapLocation(17,18));
			path.addLinkE(new MapLocation(16,17));
			path.addLinkE(new MapLocation(16,16));
			path.addLinkE(new MapLocation(15,15));
			path.addLinkE(new MapLocation(14,14));
			path.addLinkE(new MapLocation(13,13));
			path.addLinkE(new MapLocation(12,12));
			path.addLinkE(new MapLocation(11,11));
			path.addLinkE(new MapLocation(10,10));
			path.addLinkE(new MapLocation(9,9));
			path.addLinkE(new MapLocation(8,8));
			path.addLinkE(new MapLocation(7,7));
			path.addLinkE(new MapLocation(6,8));
			path.addLinkE(new MapLocation(5,7));
			path.addLinkE(new MapLocation(4,6));
			path.addLinkE(new MapLocation(3,5));
			path.addLinkE(new MapLocation(2,4));
			path.addLinkE(new MapLocation(2,5));
			path.addLinkE(new MapLocation(3,4));
			path.addLinkE(new MapLocation(3,3));
			path.addLinkE(new MapLocation(4,3));
			path.addLinkE(new MapLocation(3,2));
			path.addLinkE(new MapLocation(2,2));
			path.addLinkE(new MapLocation(2,1));
			path.addLinkE(new MapLocation(1,2));
			path.addLinkE(new MapLocation(1,3));
			path.addLinkE(new MapLocation(0,3));
			path.addLinkE(new MapLocation(1,4));
		}
		
		// "Cairo"
		if ( ( mapHeight == 30 ) && ( mapWidth == 60 ) && ( startA.equals(new MapLocation(20,11)) ) && ( startB.equals(new MapLocation(39,18)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(21,10));
			path.addLinkE(new MapLocation(22,10));
			path.addLinkE(new MapLocation(23,10));
			path.addLinkE(new MapLocation(24,9));
			path.addLinkE(new MapLocation(25,10));
			path.addLinkE(new MapLocation(26,11));
			path.addLinkE(new MapLocation(27,12));
			path.addLinkE(new MapLocation(28,13));
			path.addLinkE(new MapLocation(29,14));
			path.addLinkE(new MapLocation(30,15));
			path.addLinkE(new MapLocation(31,16));
			path.addLinkE(new MapLocation(32,17));
			path.addLinkE(new MapLocation(33,18));
			path.addLinkE(new MapLocation(34,19));
			path.addLinkE(new MapLocation(35,18));
			path.addLinkE(new MapLocation(36,17));
			path.addLinkE(new MapLocation(37,16));
			path.addLinkE(new MapLocation(38,17));
			path.addLinkE(new MapLocation(38,18));
			path.addLinkE(new MapLocation(37,18));
			path.addLinkE(new MapLocation(38,19));
			path.addLinkE(new MapLocation(39,19));
			path.addLinkE(new MapLocation(39,20));
			path.addLinkE(new MapLocation(40,19));
			path.addLinkE(new MapLocation(40,18));
			path.addLinkE(new MapLocation(41,18));
			path.addLinkE(new MapLocation(40,17));
			path.addLinkE(new MapLocation(39,17));
			path.addLinkE(new MapLocation(39,16));
		}
		
		// "britain"
		if ( ( mapHeight == 30 ) && ( mapWidth == 30 ) && ( startA.equals(new MapLocation(5,5)) ) && ( startB.equals(new MapLocation(24,24)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(5,6));
			path.addLinkE(new MapLocation(6,7));
			path.addLinkE(new MapLocation(7,8));
			path.addLinkE(new MapLocation(8,9));
			path.addLinkE(new MapLocation(9,10));
			path.addLinkE(new MapLocation(10,11));
			path.addLinkE(new MapLocation(11,12));
			path.addLinkE(new MapLocation(12,13));
			path.addLinkE(new MapLocation(13,14));
			path.addLinkE(new MapLocation(14,15));
			path.addLinkE(new MapLocation(15,16));
			path.addLinkE(new MapLocation(16,16));
			path.addLinkE(new MapLocation(17,17));
			path.addLinkE(new MapLocation(18,18));
			path.addLinkE(new MapLocation(19,19));
			path.addLinkE(new MapLocation(20,20));
			path.addLinkE(new MapLocation(21,21));
			path.addLinkE(new MapLocation(22,22));
			path.addLinkE(new MapLocation(23,23));
			path.addLinkE(new MapLocation(23,24));
			path.addLinkE(new MapLocation(22,24));
			path.addLinkE(new MapLocation(23,25));
			path.addLinkE(new MapLocation(24,25));
			path.addLinkE(new MapLocation(24,26));
			path.addLinkE(new MapLocation(25,25));
			path.addLinkE(new MapLocation(25,24));
			path.addLinkE(new MapLocation(26,24));
			path.addLinkE(new MapLocation(25,23));
			path.addLinkE(new MapLocation(24,23));
			path.addLinkE(new MapLocation(24,22));
		}
		
		// "boxes"
		if ( ( mapHeight == 40 ) && ( mapWidth == 40 ) && ( startA.equals(new MapLocation(36,3)) ) && ( startB.equals(new MapLocation(3,36)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(35,2));
			path.addLinkE(new MapLocation(34,1));
			path.addLinkE(new MapLocation(33,0));
			path.addLinkE(new MapLocation(32,0));
			path.addLinkE(new MapLocation(31,0));
			path.addLinkE(new MapLocation(30,1));
			path.addLinkE(new MapLocation(29,2));
			path.addLinkE(new MapLocation(28,3));
			path.addLinkE(new MapLocation(27,4));
			path.addLinkE(new MapLocation(26,5));
			path.addLinkE(new MapLocation(25,6));
			path.addLinkE(new MapLocation(24,7));
			path.addLinkE(new MapLocation(23,8));
			path.addLinkE(new MapLocation(22,9));
			path.addLinkE(new MapLocation(21,10));
			path.addLinkE(new MapLocation(20,11));
			path.addLinkE(new MapLocation(19,12));
			path.addLinkE(new MapLocation(18,13));
			path.addLinkE(new MapLocation(17,14));
			path.addLinkE(new MapLocation(16,15));
			path.addLinkE(new MapLocation(15,16));
			path.addLinkE(new MapLocation(14,17));
			path.addLinkE(new MapLocation(13,18));
			path.addLinkE(new MapLocation(12,19));
			path.addLinkE(new MapLocation(11,20));
			path.addLinkE(new MapLocation(10,21));
			path.addLinkE(new MapLocation(9,22));
			path.addLinkE(new MapLocation(8,23));
			path.addLinkE(new MapLocation(7,24));
			path.addLinkE(new MapLocation(6,25));
			path.addLinkE(new MapLocation(5,26));
			path.addLinkE(new MapLocation(4,27));
			path.addLinkE(new MapLocation(3,28));
			path.addLinkE(new MapLocation(2,29));
			path.addLinkE(new MapLocation(1,30));
			path.addLinkE(new MapLocation(0,31));
			path.addLinkE(new MapLocation(0,32));
			path.addLinkE(new MapLocation(0,33));
			path.addLinkE(new MapLocation(1,34));
			path.addLinkE(new MapLocation(2,35));
			path.addLinkE(new MapLocation(2,36));
			path.addLinkE(new MapLocation(1,36));
			path.addLinkE(new MapLocation(2,37));
			path.addLinkE(new MapLocation(3,37));
			path.addLinkE(new MapLocation(3,38));
			path.addLinkE(new MapLocation(4,37));
			path.addLinkE(new MapLocation(4,36));
			path.addLinkE(new MapLocation(5,36));
			path.addLinkE(new MapLocation(4,35));
			path.addLinkE(new MapLocation(3,35));
			path.addLinkE(new MapLocation(3,34));
		}
		
		// "bloodbath"
		if ( ( mapHeight == 64 ) && ( mapWidth == 64 ) && ( startA.equals(new MapLocation(7,57)) ) && ( startB.equals(new MapLocation(56,6)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(6,56));
			path.addLinkE(new MapLocation(5,55));
			path.addLinkE(new MapLocation(5,54));
			path.addLinkE(new MapLocation(6,53));
			path.addLinkE(new MapLocation(7,52));
			path.addLinkE(new MapLocation(8,51));
			path.addLinkE(new MapLocation(9,50));
			path.addLinkE(new MapLocation(10,49));
			path.addLinkE(new MapLocation(11,48));
			path.addLinkE(new MapLocation(12,47));
			path.addLinkE(new MapLocation(13,46));
			path.addLinkE(new MapLocation(14,45));
			path.addLinkE(new MapLocation(15,44));
			path.addLinkE(new MapLocation(16,43));
			path.addLinkE(new MapLocation(17,42));
			path.addLinkE(new MapLocation(18,41));
			path.addLinkE(new MapLocation(19,40));
			path.addLinkE(new MapLocation(20,39));
			path.addLinkE(new MapLocation(21,38));
			path.addLinkE(new MapLocation(22,37));
			path.addLinkE(new MapLocation(23,36));
			path.addLinkE(new MapLocation(24,35));
			path.addLinkE(new MapLocation(25,34));
			path.addLinkE(new MapLocation(26,33));
			path.addLinkE(new MapLocation(27,32));
			path.addLinkE(new MapLocation(28,31));
			path.addLinkE(new MapLocation(29,30));
			path.addLinkE(new MapLocation(30,29));
			path.addLinkE(new MapLocation(31,28));
			path.addLinkE(new MapLocation(32,27));
			path.addLinkE(new MapLocation(33,26));
			path.addLinkE(new MapLocation(34,25));
			path.addLinkE(new MapLocation(35,26));
			path.addLinkE(new MapLocation(36,26));
			path.addLinkE(new MapLocation(37,27));
			path.addLinkE(new MapLocation(38,27));
			path.addLinkE(new MapLocation(39,27));
			path.addLinkE(new MapLocation(40,26));
			path.addLinkE(new MapLocation(41,26));
			path.addLinkE(new MapLocation(42,25));
			path.addLinkE(new MapLocation(41,24));
			path.addLinkE(new MapLocation(41,23));
			path.addLinkE(new MapLocation(42,22));
			path.addLinkE(new MapLocation(43,21));
			path.addLinkE(new MapLocation(44,20));
			path.addLinkE(new MapLocation(45,19));
			path.addLinkE(new MapLocation(46,18));
			path.addLinkE(new MapLocation(45,17));
			path.addLinkE(new MapLocation(46,16));
			path.addLinkE(new MapLocation(47,15));
			path.addLinkE(new MapLocation(48,14));
			path.addLinkE(new MapLocation(49,13));
			path.addLinkE(new MapLocation(50,12));
			path.addLinkE(new MapLocation(51,11));
			path.addLinkE(new MapLocation(52,10));
			path.addLinkE(new MapLocation(53,9));
			path.addLinkE(new MapLocation(54,8));
			path.addLinkE(new MapLocation(55,7));
			path.addLinkE(new MapLocation(56,7));
			path.addLinkE(new MapLocation(56,8));
			path.addLinkE(new MapLocation(57,7));
			path.addLinkE(new MapLocation(57,6));
			path.addLinkE(new MapLocation(58,6));
			path.addLinkE(new MapLocation(57,5));
			path.addLinkE(new MapLocation(56,5));
			path.addLinkE(new MapLocation(56,4));
			path.addLinkE(new MapLocation(55,5));
			path.addLinkE(new MapLocation(55,6));
			path.addLinkE(new MapLocation(54,6));
		}
		
		// "BlastRadius"
		if ( ( mapHeight == 60 ) && ( mapWidth == 60 ) && ( startA.equals(new MapLocation(9,8)) ) && ( startB.equals(new MapLocation(50,51)) ) ) {
			path = new Path();
			path.addLinkE(new MapLocation(8,9));
			path.addLinkE(new MapLocation(9,10));
			path.addLinkE(new MapLocation(10,11));
			path.addLinkE(new MapLocation(11,12));
			path.addLinkE(new MapLocation(12,13));
			path.addLinkE(new MapLocation(13,14));
			path.addLinkE(new MapLocation(14,15));
			path.addLinkE(new MapLocation(15,16));
			path.addLinkE(new MapLocation(16,17));
			path.addLinkE(new MapLocation(17,18));
			path.addLinkE(new MapLocation(18,19));
			path.addLinkE(new MapLocation(19,20));
			path.addLinkE(new MapLocation(20,21));
			path.addLinkE(new MapLocation(21,22));
			path.addLinkE(new MapLocation(22,23));
			path.addLinkE(new MapLocation(23,24));
			path.addLinkE(new MapLocation(24,25));
			path.addLinkE(new MapLocation(25,26));
			path.addLinkE(new MapLocation(26,27));
			path.addLinkE(new MapLocation(27,28));
			path.addLinkE(new MapLocation(27,29));
			path.addLinkE(new MapLocation(27,30));
			path.addLinkE(new MapLocation(28,31));
			path.addLinkE(new MapLocation(29,32));
			path.addLinkE(new MapLocation(30,32));
			path.addLinkE(new MapLocation(31,33));
			path.addLinkE(new MapLocation(31,34));
			path.addLinkE(new MapLocation(32,35));
			path.addLinkE(new MapLocation(33,34));
			path.addLinkE(new MapLocation(34,35));
			path.addLinkE(new MapLocation(35,36));
			path.addLinkE(new MapLocation(36,37));
			path.addLinkE(new MapLocation(37,38));
			path.addLinkE(new MapLocation(38,39));
			path.addLinkE(new MapLocation(39,40));
			path.addLinkE(new MapLocation(40,41));
			path.addLinkE(new MapLocation(41,42));
			path.addLinkE(new MapLocation(42,43));
			path.addLinkE(new MapLocation(43,44));
			path.addLinkE(new MapLocation(44,45));
			path.addLinkE(new MapLocation(45,46));
			path.addLinkE(new MapLocation(46,47));
			path.addLinkE(new MapLocation(47,48));
			path.addLinkE(new MapLocation(48,49));
			path.addLinkE(new MapLocation(49,50));
			path.addLinkE(new MapLocation(49,51));
			path.addLinkE(new MapLocation(48,51));
			path.addLinkE(new MapLocation(49,52));
			path.addLinkE(new MapLocation(50,52));
			path.addLinkE(new MapLocation(50,53));
			path.addLinkE(new MapLocation(51,52));
			path.addLinkE(new MapLocation(51,51));
			path.addLinkE(new MapLocation(52,51));
			path.addLinkE(new MapLocation(51,50));
			path.addLinkE(new MapLocation(50,50));
			path.addLinkE(new MapLocation(50,49));
		}
		
		// "maze1"
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
			path.addLinkE(new MapLocation(0,58));
			path.addLinkE(new MapLocation(0,57));
			path.addLinkE(new MapLocation(-1,58));
			path.addLinkE(new MapLocation(-1,59));
			path.addLinkE(new MapLocation(-2,59));
			path.addLinkE(new MapLocation(-1,60));
			path.addLinkE(new MapLocation(0,60));
			path.addLinkE(new MapLocation(0,61));
			path.addLinkE(new MapLocation(1,60));
			path.addLinkE(new MapLocation(1,59));
			path.addLinkE(new MapLocation(2,59));
			path.addLinkE(new MapLocation(1,58));
		}
		
		
		
		
		
		return path;
	}
}
