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

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;

/*
 * The BusInterface annotation is used to tell the code that this interface is an AllJoyn interface.
 *
 * The 'name' value is used to specify by which name this interface will be known.  If the name is
 * not given the fully qualified name of the Java interface is be used.  In most instances its best
 * to assign an interface name since it helps promote code reuse.
 */
/**
 * The interface that forms the communication gateway between the tabeltop and
 * mobile devices. It contains method that connected mobile devices can call.
 */
@BusInterface(name = "org.alljoyn.bus.samples.simple.SimpleInterface")
public interface CFTabletopServiceInterface {

	/**
	 * A method that is called when a new mobile device has connected.
	 * 
	 * @param name
	 *            The name of the new device. This name must be unique, so there
	 *            can be no other devices that has already been connected with
	 *            the same name.
	 * @return True if the name of the new device was accepted. False if the
	 *         name wasn't accepted.
	 * @throws BusException
	 */
	@BusMethod
	public boolean attach(String name) throws BusException;

	/**
	 * A method to indicate a mobile device is closing its connection to the
	 * tabletop.
	 * 
	 * @param name
	 *            The name of the client that is going to close the connection.
	 * @return True if the transaction succeeded.
	 * @throws BusException
	 */
	@BusMethod
	public boolean detach(String name) throws BusException;

	/**
	 * Returns the path of an image that has to be published on facebook.
	 * 
	 * @param clientName
	 *            The name of the client that has to publish.
	 * @return True if the transaction succeeded and vice versa.
	 */
	@BusMethod
	public String getFileToPublish(String clientName);

	/**
	 * Returns the next piece of a file that is being sent to a client with a
	 * given name.
	 * 
	 * @param clientName
	 *            The name of the client that is requesting the next piece of
	 *            the file.
	 * @return The next piece of the file as an array of bytes. Returns an array
	 *         with length 1 if all data has been sent.
	 */
	@BusMethod
	public byte[] getMusicFile(String clientName);

	/**
	 * Returns the status of a client with a given name.
	 * 
	 * @param name
	 *            The name of the client of which the status is requested.
	 * @return
	 * 0 = idle (nothing has to happen)
	 * 1 = 
	 * 2 = 
	 * 3 = 
	 * 4 =
	 * @throws BusException
	 */
	@BusMethod
	public int getStatus(String name) throws BusException;

	/**
	 * A method to send a piece of a file from a mobile device to the tabletop.
	 * On the tabletop the piece is joined with the rest of the file.
	 * 
	 * @param path
	 *            The orinal path of the file in the filesystem of the mobile
	 *            device.
	 * @param name
	 *            The name of the client that sends the file.
	 * @param buf
	 *            The array of bytes that is being sent.
	 * @param lastPiece
	 *            A boolean that indicates whether its the last piece. True
	 *            indicates it is and vice versa.
	 * @return True if the transaction succeeded.
	 * @throws BusException
	 * @pre Pieces of files have to be sent in the correct order, as they are
	 *      reassembled consecutively.
	 */
	@BusMethod
	public boolean receivePieceOfFile(String path, String name, byte[] buf,
			boolean lastPiece) throws BusException;

	/**
	 * Sets the status of the mobile device with a given name to IDLE.
	 * 
	 * @param name
	 *            The name of the device of which the status has to be set to
	 *            IDLE.
	 * @return True if the transaction succeeded and vice versa.
	 * @throws BusException
	 */
	@BusMethod
	public boolean setIdle(String name) throws BusException;
}
