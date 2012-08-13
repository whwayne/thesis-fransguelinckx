/*
 * Copyright 2010-2011, Qualcomm Innovation Center, Inc.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.alljoyn.bus.samples.simpleclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Client extends Activity {

	private Facebook facebook = new Facebook("292760657458958");
	String FILENAME = "CFClient_data";

	/* Load the native alljoyn_java library. */
	static {
		System.loadLibrary("alljoyn_java");
	}

	private static final int MESSAGE_POST_TOAST = 3;
	private static final int MESSAGE_START_PROGRESS_DIALOG = 4;
	private static final int MESSAGE_STOP_PROGRESS_DIALOG = 5;

	private static final String TAG = "SimpleClient";

	private EditText mEditText;
	private ArrayAdapter<String> mListViewArrayAdapter;
	private ListView mListView;
	private Menu menu;

	private final String deviceName = android.os.Build.DEVICE;

	/* Handler used to make calls to AllJoyn methods. See onCreate(). */
	private BusHandler mBusHandler;

	private ProgressDialog mDialog;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_POST_TOAST:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case MESSAGE_START_PROGRESS_DIALOG:
				mDialog = ProgressDialog.show(Client.this, "",
						"Finding Simple Service.\nPlease wait...", true, true);
				break;
			case MESSAGE_STOP_PROGRESS_DIALOG:
				mDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String[] permissions = { "offline_access", "publish_stream",
				"user_photos", "publish_checkins", "photo_upload" };

		facebook.authorize(this, permissions, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				logInfo("Facebook login complete.");
			}

			@Override
			public void onFacebookError(FacebookError error) {
				logInfo(error.getMessage());
			}

			@Override
			public void onError(DialogError e) {
				logInfo(e.getMessage());
			}

			@Override
			public void onCancel() {
				logInfo("Cancelled");
			}
		});

		mListViewArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		mListView = (ListView) findViewById(R.id.ListView);
		mListView.setAdapter(mListViewArrayAdapter);

		mEditText = (EditText) findViewById(R.id.EditText);
		mEditText
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView view, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_NULL
								&& event.getAction() == KeyEvent.ACTION_UP) {
							/* Call the remote object's Ping method. */
							Message msg = mBusHandler.obtainMessage(
									BusHandler.PING, view.getText().toString());
							mBusHandler.sendMessage(msg);
						}
						return true;
					}
				});

		/*
		 * Make all AllJoyn calls through a separate handler thread to prevent
		 * blocking the UI.
		 */
		HandlerThread busThread = new HandlerThread("BusHandler");
		busThread.start();
		mBusHandler = new BusHandler(busThread.getLooper());

		/* Connect to an AllJoyn object. */
		mBusHandler.sendEmptyMessage(BusHandler.CONNECT);
		mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.quit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		/* Disconnect to prevent resource leaks. */
		mBusHandler.sendEmptyMessage(BusHandler.DISCONNECT);
	}

	/* This class will handle all AllJoyn calls. See onCreate(). */
	class BusHandler extends Handler implements RequestListener {
		/*
		 * Name used as the well-known name and the advertised name of the
		 * service this client is interested in. This name must be a unique name
		 * both to the bus and to the network as a whole.
		 * 
		 * The name uses reverse URL style of naming, and matches the name used
		 * by the service.
		 */
		private static final String SERVICE_NAME = "org.alljoyn.bus.samples.simple";
		private static final short CONTACT_PORT = 42;

		private BusAttachment mBus;
		private ProxyBusObject mProxyObj;
		private CFTabletopServiceInterface mSimpleInterface;

		private int mSessionId;
		private boolean mIsInASession;
		private boolean mIsConnected;
		private boolean mIsStoppingDiscovery;

		/* These are the messages sent to the BusHandler from the UI. */
		public static final int CONNECT = 1;
		public static final int JOIN_SESSION = 2;
		public static final int DISCONNECT = 3;
		public static final int PING = 4;
		public static final int START_POLLING_SERVER = 5;
		public static final int SEND_PHOTOS = 6;
		public static final int PUBLISH_ON_FACEBOOK = 7;
		public static final int SEND_MUSIC = 8;
		public static final int RECEIVE_MUSIC = 9;

		public BusHandler(Looper looper) {
			super(looper);

			mIsInASession = false;
			mIsConnected = false;
			mIsStoppingDiscovery = false;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * Connect to a remote instance of an object implementing the
			 * SimpleInterface.
			 */
			case CONNECT: {
				/*
				 * All communication through AllJoyn begins with a
				 * BusAttachment.
				 * 
				 * A BusAttachment needs a name. The actual name is unimportant
				 * except for internal security. As a default we use the class
				 * name as the name.
				 * 
				 * By default AllJoyn does not allow communication between
				 * devices (i.e. bus to bus communication). The second argument
				 * must be set to Receive to allow communication between
				 * devices.
				 */
				mBus = new BusAttachment(getPackageName(),
						BusAttachment.RemoteMessage.Receive);

				/*
				 * Create a bus listener class
				 */
				mBus.registerBusListener(new BusListener() {
					@Override
					public void foundAdvertisedName(String name,
							short transport, String namePrefix) {
						logInfo(String
								.format("MyBusListener.foundAdvertisedName(%s, 0x%04x, %s)",
										name, transport, namePrefix));
						/*
						 * This client will only join the first service that it
						 * sees advertising the indicated well-known name. If
						 * the program is already a member of a session (i.e.
						 * connected to a service) we will not attempt to join
						 * another session. It is possible to join multiple
						 * session however joining multiple sessions is not
						 * shown in this sample.
						 */
						if (!mIsConnected) {
							Message msg = obtainMessage(JOIN_SESSION, name);
							sendMessage(msg);
						}
					}
				});

				/*
				 * To communicate with AllJoyn objects, we must connect the
				 * BusAttachment to the bus.
				 */
				Status status = mBus.connect();
				logStatus("BusAttachment.connect()", status);
				if (Status.OK != status) {
					finish();
					return;
				}

				/*
				 * Now find an instance of the AllJoyn object we want to call.
				 * We start by looking for a name, then connecting to the device
				 * that is advertising that name.
				 * 
				 * In this case, we are looking for the well-known SERVICE_NAME.
				 */
				status = mBus.findAdvertisedName(SERVICE_NAME);
				logStatus(String.format(
						"BusAttachement.findAdvertisedName(%s)", SERVICE_NAME),
						status);
				if (Status.OK != status) {
					finish();
					return;
				}

				break;
			}
			case (JOIN_SESSION): {
				/*
				 * If discovery is currently being stopped don't join to any
				 * other sessions.
				 */
				if (mIsStoppingDiscovery) {
					break;
				}

				/*
				 * In order to join the session, we need to provide the
				 * well-known contact port. This is pre-arranged between both
				 * sides as part of the definition of the chat service. As a
				 * result of joining the session, we get a session identifier
				 * which we must use to identify the created session
				 * communication channel whenever we talk to the remote side.
				 */
				short contactPort = CONTACT_PORT;
				SessionOpts sessionOpts = new SessionOpts();
				Mutable.IntegerValue sessionId = new Mutable.IntegerValue();

				Status status = mBus.joinSession((String) msg.obj, contactPort,
						sessionId, sessionOpts, new SessionListener() {
							@Override
							public void sessionLost(int sessionId) {
								mIsConnected = false;
								logInfo(String.format(
										"MyBusListener.sessionLost(%d)",
										sessionId));
								mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);
								mIsStoppingDiscovery = true;
								if (mIsConnected) {
									Status status = mBus.leaveSession(mSessionId);
									logStatus("BusAttachment.leaveSession()", status);
								}
								mBus.disconnect();
								getLooper().quit();
								Message msg = obtainMessage(START_POLLING_SERVER);
								sendMessage(msg);

							}
						});
				logStatus("BusAttachment.joinSession() - sessionId: "
						+ sessionId.value, status);

				if (status == Status.OK) {
					/*
					 * To communicate with an AllJoyn object, we create a
					 * ProxyBusObject. A ProxyBusObject is composed of a name,
					 * path, sessionID and interfaces.
					 * 
					 * This ProxyBusObject is located at the well-known
					 * SERVICE_NAME, under path "/SimpleService", uses sessionID
					 * of CONTACT_PORT, and implements the SimpleInterface.
					 */
					mProxyObj = mBus
							.getProxyBusObject(
									SERVICE_NAME,
									"/SimpleService",
									sessionId.value,
									new Class<?>[] { CFTabletopServiceInterface.class });

					/*
					 * We make calls to the methods of the AllJoyn object
					 * through one of its interfaces.
					 */
					mSimpleInterface = mProxyObj
							.getInterface(CFTabletopServiceInterface.class);

					mSessionId = sessionId.value;
					mIsConnected = true;
					mHandler.sendEmptyMessage(MESSAGE_STOP_PROGRESS_DIALOG);

					try {
						mSimpleInterface.attach(android.os.Build.DEVICE);
						msg = obtainMessage(START_POLLING_SERVER);
						sendMessage(msg);
					} catch (BusException e) {
					}
				}
				break;
			}

			/* Release all resources acquired in the connect. */
			case DISCONNECT: {
				mIsStoppingDiscovery = true;
				if (mIsConnected) {
					Status status = mBus.leaveSession(mSessionId);
					logStatus("BusAttachment.leaveSession()", status);
				}
				mBus.disconnect();
				getLooper().quit();
				msg = obtainMessage(CONNECT);
				sendMessage(msg);
				break;
			}

			case START_POLLING_SERVER: {
				boolean idle = true;
				while (idle) {
					int status = 0;
					try {
						status = mSimpleInterface.getStatus(deviceName);
					} catch (BusException e) {
						e.printStackTrace();
					}
					if (status == 1) {
						idle = false;
						Message message = obtainMessage(SEND_PHOTOS);
						sendMessage(message);
					} else if (status == 2) {
						idle = false;
						Message message = obtainMessage(PUBLISH_ON_FACEBOOK);
						sendMessage(message);
					} else if (status == 3) {
						idle = false;
						Message message = obtainMessage(SEND_MUSIC);
						sendMessage(message);
					}  else if (status == 4) {
						idle = false;
						Message message = obtainMessage(RECEIVE_MUSIC);
						sendMessage(message);
					} else {
						// logInfo("Client idle");
					}
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				break;
			}

			case SEND_PHOTOS: {
				if (mSimpleInterface != null) {
					// sendUiMessage(MESSAGE_PING, msg.obj);
					// sendUiMessage(MESSAGE_PING_REPLY, "reply");
					List<String> files = ReadSDCard();
					FileInputStream in;
					try {
						for (String file : files) {
							in = new FileInputStream(file);
							byte[] buf = new byte[100000];
							int len;
							while ((len = in.read(buf)) > 0) {
								if (len != 100000) {
									mSimpleInterface.receivePieceOfFile(file,
											deviceName, buf, true);
								} else {
									mSimpleInterface.receivePieceOfFile(file,
											deviceName, buf, false);
								}
								buf = new byte[100000];
							}
							in.close();
						}
						mSimpleInterface.setIdle(deviceName);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (BusException e) {
						e.printStackTrace();
					}
					Message message = obtainMessage(START_POLLING_SERVER);
					sendMessage(message);

				}
				break;
			}
			case SEND_MUSIC: {
				if (mSimpleInterface != null) {
					// sendUiMessage(MESSAGE_PING, msg.obj);
					// sendUiMessage(MESSAGE_PING_REPLY, "reply");
					List<String> files = ReadSDCardMusic();
					FileInputStream in;
					try {
						for (String file : files) {
							in = new FileInputStream(file);
							byte[] buf = new byte[100000];
							int len;
							while ((len = in.read(buf)) > 0) {
								if (len != 100000) {
									mSimpleInterface.receivePieceOfFile(file,
											deviceName, buf, true);
								} else {
									mSimpleInterface.receivePieceOfFile(file,
											deviceName, buf, false);
								}
								buf = new byte[100000];
							}
							in.close();
						}
						mSimpleInterface.setIdle(deviceName);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (BusException e) {
						e.printStackTrace();
					}
					Message message = obtainMessage(START_POLLING_SERVER);
					sendMessage(message);

				}
				break;
			}

			case RECEIVE_MUSIC: {
				if (mSimpleInterface != null) {
					FileOutputStream os;
					try {
						os = new FileOutputStream(new File(
								"/sdcard/music2/CFFile"
										+ Calendar.getInstance()
												.getTimeInMillis() + ".mp3"));
						byte[] buf = mSimpleInterface.getMusicFile(deviceName);
						while (buf.length != 1) {
							os.write(buf);
							buf = mSimpleInterface.getMusicFile(deviceName);
						}
						os.flush();
						os.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message message = obtainMessage(START_POLLING_SERVER);
					sendMessage(message);

				}
				break;
			}
			case PUBLISH_ON_FACEBOOK: {
				String file = mSimpleInterface.getFileToPublish(deviceName);
				byte[] data;
				Bitmap bi;
				ByteArrayOutputStream baos;
				Bundle params;
				AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(
						facebook);

				data = null;
				bi = BitmapFactory.decodeFile(file);
				baos = new ByteArrayOutputStream();
				bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				data = baos.toByteArray();

				params = new Bundle();
				params.putString(Facebook.TOKEN, facebook.getAccessToken());
				params.putString("method", "photos.upload");
				params.putByteArray("picture", data);

				mAsyncRunner.request(null, params, "POST", this, null);

				Message message = obtainMessage(START_POLLING_SERVER);
				sendMessage(message);

				break;
			}

			default:
				break;
			}
		}

		/* Helper function to send a message to the UI thread. */
		private void sendUiMessage(int what, Object obj) {
			mHandler.sendMessage(mHandler.obtainMessage(what, obj));
		}

		@Override
		public void onComplete(String response, Object state) {
		}

		@Override
		public void onIOException(IOException e, Object state) {
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
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

	@SuppressWarnings("unused")
	private void logException(String msg, BusException ex) {
		String log = String.format("%s: %s", msg, ex);
		Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
		mHandler.sendMessage(toastMsg);
		Log.e(TAG, log, ex);
	}

	/*
	 * print the status or result to the Android log. If the result is the
	 * expected result only print it to the log. Otherwise print it to the error
	 * log and Sent a Toast to the users screen.
	 */
	private void logInfo(String msg) {
		Log.i(TAG, msg);
	}

	private List<String> ReadSDCardMusic() {
		List<String> tFileList = new ArrayList<String>();

		// It have to be matched with the directory in SDCard
		File f = new File("/sdcard/Music/");

		File[] files = f.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			// add the selected file type only
			String curFile = file.getPath();
			String ext = curFile.substring(curFile.lastIndexOf(".") + 1,
					curFile.length()).toLowerCase();
			if (ext.equals("mp3"))
				tFileList.add(file.getPath());
		}

		return tFileList;
	}

	private List<String> ReadSDCard() {
		List<String> tFileList = new ArrayList<String>();

		// It have to be matched with the directory in SDCard
		File f = new File("/sdcard/pictures/");

		File[] files = f.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			// add the selected file type only
			String curFile = file.getPath();
			String ext = curFile.substring(curFile.lastIndexOf(".") + 1,
					curFile.length()).toLowerCase();
			if (ext.equals("jpg"))
				tFileList.add(file.getPath());
		}

		return tFileList;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}
