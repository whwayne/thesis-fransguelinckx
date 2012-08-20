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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

	/* This class will handle all AllJoyn calls. See onCreate(). */
	class CFBusHandler extends Handler implements RequestListener {
		static {
			System.loadLibrary("alljoyn_java");
		}
		
		/* These are the messages sent to the BusHandler from the UI. */
		public static final int CONNECT = 1;
		private static final short CONTACT_PORT = 42;
		public static final int DISCONNECT = 3;
		public static final int JOIN_SESSION = 2;
		public static final int PING = 4;
		public static final int PUBLISH_ON_FACEBOOK = 7;
		public static final int RECEIVE_MUSIC = 9;
		public static final int SEND_MUSIC = 8;
		public static final int SEND_PHOTOS = 6;

		/*
		 * Name used as the well-known name and the advertised name of the
		 * service this client is interested in. This name must be a unique name
		 * both to the bus and to the network as a whole.
		 * 
		 * The name uses reverse URL style of naming, and matches the name used
		 * by the service.
		 */
		private static final String SERVICE_NAME = "org.alljoyn.bus.samples.simple";
		public static final int START_POLLING_SERVER = 5;
		private BusAttachment mBus;
		private boolean mIsConnected;
//		private boolean mIsInASession;
		private boolean mIsStoppingDiscovery;
		private ProxyBusObject mProxyObj;
		private int mSessionId;
		private CFTabletopServiceInterface mSimpleInterface;
		private final String deviceName = android.os.Build.DEVICE;
		private Facebook facebook;
		private final String musicPath = "/sdcard/Music";
		private final String picturesPath = "/sdcard/pictures";
		private boolean secure;
		
		public CFBusHandler(Looper looper, Facebook facebook) {
			this(looper);
			this.facebook = facebook;
		}
		public CFBusHandler(Looper looper) {
			super(looper);
//			mIsInASession = false;
			mIsConnected = false;
			mIsStoppingDiscovery = false;
			this.sendEmptyMessage(CFBusHandler.CONNECT);
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
				mBus = new BusAttachment("CFBusHandler",
						BusAttachment.RemoteMessage.Receive);

				/*
				 * Create a bus listener class
				 */
				mBus.registerBusListener(new BusListener() {
					@Override
					public void foundAdvertisedName(String name,
							short transport, String namePrefix) {
						/*
						 * This client will only join the first service that it
						 * sees advertising the indicated well-known name. If
						 * the program is already a member of a session (i.e.
						 * connected to a service) we will not attempt to join
						 * another session. It is possible to join multiple
						 * session however joining multiple sessions is not
						 * shown in this sample.
						 */
						logInfo("found service");
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
				if (Status.OK != status) {
					return;
				}
				logInfo("connected");

				/*
				 * Now find an instance of the AllJoyn object we want to call.
				 * We start by looking for a name, then connecting to the device
				 * that is advertising that name.
				 * 
				 * In this case, we are looking for the well-known SERVICE_NAME.
				 */
				status = mBus.findAdvertisedName(SERVICE_NAME);
				if (Status.OK != status) {
					return;
				}
				logInfo("looking for service");

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
								mIsStoppingDiscovery = true;
								if (mIsConnected) {
									mBus
											.leaveSession(mSessionId);
								}
								mBus.disconnect();
								getLooper().quit();
								Message msg = obtainMessage(START_POLLING_SERVER);
								sendMessage(msg);

							}
						});

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
					logInfo("connected to service");
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
					mBus.leaveSession(mSessionId);
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
					} else if (status == 4) {
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
					List<String> files = readSDCardPictures();
					sendFiles(files);
					Message message = obtainMessage(START_POLLING_SERVER);
					sendMessage(message);

				}
				break;
			}
			case SEND_MUSIC: {
				if (mSimpleInterface != null) {
					List<String> files = readSDCardMusic();
					sendFiles(files);
					Message message = obtainMessage(START_POLLING_SERVER);
					sendMessage(message);

				}
				break;
			}

			case RECEIVE_MUSIC: {
				if (mSimpleInterface != null) {
					String path = "/sdcard/music2/CFFile"
							+ Calendar.getInstance().getTimeInMillis() + ".mp3";
					receiveFile(path);
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
				AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

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

		private List<String> readSDCardMusic() {
			List<String> tFileList = new ArrayList<String>();

			// It have to be matched with the directory in SDCard
			File f = new File(musicPath);

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

		private List<String> readSDCardPictures() {
			List<String> tFileList = new ArrayList<String>();

			// It have to be matched with the directory in SDCard
			File f = new File(picturesPath);

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
		public void onComplete(String response, Object state) {
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
		}

		@Override
		public void onIOException(IOException e, Object state) {
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
		}

		private void receiveFile(String path) {
			try {
				FileOutputStream os;
				os = new FileOutputStream(new File(path));
				byte[] buf = mSimpleInterface.getMusicFile(deviceName);
				while (buf.length != 1) {
					os.write(buf);
					buf = mSimpleInterface.getMusicFile(deviceName);
				}
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendFiles(List<String> files) {
			try {
				FileInputStream in;
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
		}
		
		/*
		 * print the status or result to the Android log. If the result is the
		 * expected result only print it to the log. Otherwise print it to the error
		 * log and Sent a Toast to the users screen.
		 */
		private void logInfo(String msg) {
			Log.i("Bushandler", msg);
		}
	}