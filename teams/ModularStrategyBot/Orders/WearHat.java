package ModularStrategyBot.Orders;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class WearHat implements I_Orders {
	
	boolean isWearingHat = false;

	@Override
	public void executeOrders(RobotController in) {
		// TODO Auto-generated method stub
		try {
			in.setIndicatorString(0,"R: "+Clock.getRoundNum());
			if ( !isWearingHat ) {	
				in.wearHat();
				isWearingHat = true;
			}
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
