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
@BusInterface(name = "org.alljoyn.bus.samples.simple.SimpleInterface")
public interface CFTabletopServiceInterface {

    @BusMethod
    public boolean attach(String name) throws BusException;
    
    @BusMethod
    public boolean receivePieceOfFile(String name, byte[] buf, boolean lastPiece) throws BusException;
    
    @BusMethod
    public boolean detach(String name) throws BusException;
    
    @BusMethod
    public String ping(String inStr) throws BusException;
    
    @BusMethod
    public int getStatus(String name) throws BusException;
    
    @BusMethod
    public boolean setIdle(String name) throws BusException;
}
