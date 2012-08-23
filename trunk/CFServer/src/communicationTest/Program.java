package communicationTest;

import org.frans.thesis.service.CFFile;
import org.frans.thesis.service.CFTabletopClient;
import org.frans.thesis.service.CFTabletopClientManager;
import org.frans.thesis.service.CFTabletopService;
import org.frans.thesis.service.CFTabletopServiceListener;

public class Program implements CFTabletopServiceListener{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CFTabletopService service = new CFTabletopService(false);
		service.start();
	}

	@Override
	public void mobileDeviceConnected(String clientName,
			CFTabletopClientManager tabletopClientManager) {
		tabletopClientManager.setStatus(clientName, CFTabletopClient.REQUESTING_MUSIC);
	}

	@Override
	public void fileTransferred(CFFile file, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobileDeviceDisconnected(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientIsIdle(String name) {
		// TODO Auto-generated method stub
		
	}

}
