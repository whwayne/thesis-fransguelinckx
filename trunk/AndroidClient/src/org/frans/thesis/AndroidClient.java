package org.frans.thesis;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.SessionListener;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.Status;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidClient extends Activity {
	
	/* Load the native alljoyn_java library. */
	static {
		System.loadLibrary("alljoyn_java");
	}
	
    private static final String TAG = "AndroidClient";
    private static final int MESSAGE_POST_TOAST = 1;
    private static final int MESSAGE_START_PROGRESS_DIALOG = 2;
    private static final int MESSAGE_STOP_PROGRESS_DIALOG = 3;
    
    private TextView textView;
    private Button pingButton;
    
    private ProgressDialog mDialog;
    private BusHandler mBusHandler;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_POST_TOAST:
                Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                break;
            case MESSAGE_START_PROGRESS_DIALOG:
                mDialog = ProgressDialog.show(AndroidClient.this, 
                                              "", 
                                              "Finding Tabletop Service.\nPlease wait...", 
                                              true,
                                              true);
                break;
            case MESSAGE_STOP_PROGRESS_DIALOG:
                mDialog.dismiss();
                break;                
            default:
                break;
            }
        }
    };
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView) findViewById(R.id.textView);
        pingButton = (Button) findViewById(R.id.pingButton);
        
       setPingButtonClickListener();
        
        HandlerThread busThread = new HandlerThread("BusHandler");
        busThread.start();
        mBusHandler = new BusHandler(busThread.getLooper(), textView);
        mBusHandler.sendEmptyMessage(BusHandler.CONNECT);
        mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);
    }
    
    private void setPingButtonClickListener() {
		this.pingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mBusHandler.sendEmptyMessage(BusHandler.PING);
			}
		});
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        mBusHandler.sendEmptyMessage(BusHandler.DISCONNECT);
    }
    
    /*
     * See the SimpleClient sample for a more complete description of the code used 
     * to connect this code to the Bus
     */
    class BusHandler extends Handler {
        private static final String SERVICE_NAME = "org.frans.thesis.tabletop";
        private static final short CONTACT_PORT = 42;
        
        public static final int CONNECT = 1;
        public static final int DISCONNECT = 2;
        public static final int PING = 3;
        public static final int JOIN_SESSION = 4;
        
        private BusAttachment mBus;
        private ProxyBusObject mProxyObj;
        private TabletopInterface mTabletopInterface;
        
        private int     mSessionId;
        private boolean mIsConnected;
        private boolean mIsStoppingDiscovery;
        
        private TextView textView;
        
        public BusHandler(Looper looper, TextView textView) {
            super(looper);
            mIsConnected = false;
            mIsStoppingDiscovery = false;
            this.textView = textView;
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
            case CONNECT: {
            	this.textView.setText("Connecting");
            	
            	mBus = new BusAttachment(getPackageName(), BusAttachment.RemoteMessage.Receive);
                
                mBus.registerBusListener(new BusListener() {
                    @Override
                    public void foundAdvertisedName(String name, short transport, String namePrefix) {
                    	logInfo(String.format("MyBusListener.foundAdvertisedName(%s, 0x%04x, %s)", name, transport, namePrefix));
                    	/*
                         * This client will only join the first service that it sees advertising
                         * the indicated well-known name.  If the program is already a member of 
                         * a session (i.e. connected to a service) we will not attempt to join 
                         * another session.
                         * It is possible to join multiple session however joining multiple 
                         * sessions is not shown in this sample. 
                         */
                    	if (! mIsConnected){
                    	    Message msg = obtainMessage(JOIN_SESSION, name);
                    	    sendMessage(msg);
                    	}
                    }
                });

                Status status = mBus.connect();
                logStatus("BusAttachment.connect()", status);
                
                status = mBus.findAdvertisedName(SERVICE_NAME);
                logStatus(String.format("BusAttachement.findAdvertisedName(%s)", SERVICE_NAME), status);
                break;
            }
            case JOIN_SESSION: {
                if (mIsStoppingDiscovery) {
                    break;
                }

            	this.textView.setText("Joining session");
            	
                short contactPort = CONTACT_PORT;
                SessionOpts sessionOpts = new SessionOpts();
                Mutable.IntegerValue sessionId = new Mutable.IntegerValue();

                Status status = mBus.joinSession((String) msg.obj, contactPort, sessionId, sessionOpts, new SessionListener(){
                    @Override
                    public void sessionLost(int sessionId) {
                        mIsConnected = false;
                        logInfo(String.format("MyBusListener.sessionLost(%d)", sessionId));
                        mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);
                    }
                });
                logStatus("BusAttachment.joinSession()", status);

                if (status == Status.OK) {
                	mProxyObj = mBus.getProxyBusObject(SERVICE_NAME, "/tabletopservive", sessionId.value,
                				                       new Class[] { TabletopInterface.class });
                   	mTabletopInterface = mProxyObj.getInterface(TabletopInterface.class);
                	mSessionId = sessionId.value;
                	mIsConnected = true;
                	mHandler.sendEmptyMessage(MESSAGE_STOP_PROGRESS_DIALOG);
                }
                break;
            }
            case DISCONNECT: {
            	this.textView.setText("Disconnecting");
            	
                mIsStoppingDiscovery = true;
                if (mIsConnected) {
                    Status status = mBus.leaveSession(mSessionId);
                    logStatus("BusAttachment.leaveSession()", status);
                }
                mBus.disconnect();
                getLooper().quit();
                break;
            }
            case PING:{
            	this.textView.setText("Ping");
            	
            	if (mTabletopInterface == null) {
                	break;
                }
            	try {
                    String message = (String)msg.obj;
                    String reply = mTabletopInterface.ping(message);
                    Message replyMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, reply);
                    mHandler.sendMessage(replyMsg);
                } catch (BusException ex) {
                    logException("TabletopInterface.getContact()", ex);
                }
                break;
            }
            }
        }
        
        private void logStatus(String msg, Status status) {
            String log = String.format("%s: %s", msg, status);
            if (status == Status.OK) {
                Log.i(TAG, log);
            } else {
                Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
                mHandler.sendMessage(toastMsg);
                Log.e(TAG, log);
            }
        }

        private void logException(String msg, BusException ex) {
            String log = String.format("%s: %s", msg, ex);
            Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
            mHandler.sendMessage(toastMsg);
            Log.e(TAG, log, ex);
        }
        
        private void logInfo(String msg) {
            Log.i(TAG, msg);
        }
    }
}