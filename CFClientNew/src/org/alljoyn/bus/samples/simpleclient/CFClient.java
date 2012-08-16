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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;

//import com.facebook.android.Facebook;

public class CFClient extends Activity {
//	private Facebook facebook = new Facebook("292760657458958");
	private static final String TAG = "SimpleClient";
	/* Load the native alljoyn_java library. */
	static {
		System.loadLibrary("alljoyn_java");
	}

	/* Handler used to make calls to AllJoyn methods. See onCreate(). */
	private CFBusHandler mBusHandler;

	/*
	 * print the status or result to the Android log. If the result is the
	 * expected result only print it to the log. Otherwise print it to the error
	 * log and Sent a Toast to the users screen.
	 */
	private void logInfo(String msg) {
		Log.i(TAG, msg);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		String[] permissions = { "offline_access", "publish_stream",
//				"user_photos", "publish_checkins", "photo_upload" };
//
//		facebook.authorize(this, permissions, new DialogListener() {
//			@Override
//			public void onCancel() {
//				logInfo("Cancelled");
//			}
//
//			@Override
//			public void onComplete(Bundle values) {
//				logInfo("Facebook login complete.");
//			}
//
//			@Override
//			public void onError(DialogError e) {
//				logInfo(e.getMessage());
//			}
//
//			@Override
//			public void onFacebookError(FacebookError error) {
//				logInfo(error.getMessage());
//			}
//		});

		/*
		 * Make all AllJoyn calls through a separate handler thread to prevent
		 * blocking the UI.
		 */
		logInfo("starting busthread");
		HandlerThread busThread = new HandlerThread("BusHandler");
		busThread.start();
		logInfo("busthread started");
		mBusHandler = new CFBusHandler(busThread.getLooper());
		logInfo("new bus handler created");
		/* Connect to an AllJoyn object. */
		mBusHandler.sendEmptyMessage(CFBusHandler.CONNECT);
		logInfo("Connecting");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/* Disconnect to prevent resource leaks. */
		mBusHandler.sendEmptyMessage(CFBusHandler.DISCONNECT);
	}
}
