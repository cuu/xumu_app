/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

// This plugin sends UDP packets.

package edu.uic.udptransmit;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;

import udp_printf.*;

public class UDPTransmit extends CordovaPlugin {
	
	DatagramPacket datagramPacket;
	DatagramSocket datagramSocket;
	boolean successInitializingTransmitter = false;
	
	// Constructor
	public UDPTransmit() {
	}

private void sendMessage(String data, CallbackContext callbackContext) 
{
	boolean messageSent = false;
	String error="";
	if (successInitializingTransmitter) 
	{
 		byte[] bytes = data.getBytes();
 		datagramPacket.setData(bytes);
 		try {
 			datagramSocket.send(datagramPacket);
 			messageSent = true;
			datagramSocket.close();
			successInitializingTransmitter = false;
 		} catch (IOException e) {
 			e.printStackTrace();
 			error = e.toString();
		}
 	}else
 	{
 		error ="successInitializingTransmitter false";
 	}
 	
	if (messageSent)
		callbackContext.success("Success transmitting UDP packet: " + datagramPacket);
	else
		callbackContext.error("Error transmitting UDP packet: " + datagramPacket +" "+error);												
}
				
  private void initialize(String host, int port,boolean bcast, CallbackContext callbackContext) 
  {
   boolean successResolvingIPAddress = false;
    // create packet
    InetAddress address = null;
   try {
      // 'host' can be a ddd.ddd.ddd.ddd or named URL, so doesn't always resolve
    address = InetAddress.getByName(host);
     successResolvingIPAddress = true;
   } catch (UnknownHostException e) {
      	// TODO Auto-generated catch block
      e.printStackTrace();
   }            		
     if (successResolvingIPAddress) 
     {
       byte[] bytes= new byte[0];
       datagramPacket = new DatagramPacket(bytes, 0, address, port);
       try {
        datagramSocket = new DatagramSocket();
        datagramSocket.setBroadcast(bcast);
        successInitializingTransmitter = true;
    						
     		} catch (SocketException e) 
     		{
    			e.printStackTrace();
   			}      
   		}
			if (successInitializingTransmitter)
      	callbackContext.success("Success initializing UDP transmitter using datagram socket: " + datagramSocket);
      else
      	callbackContext.error("Error initializing UDP transmitter using datagram socket: " + datagramSocket);   			     	
  }
  
	// Handles and dispatches "exec" calls from the JS interface (udptransmit.js)
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if("quick".equals(action)) 
		{
			final String host = args.getString(0);
			final int port = args.getInt(1);
			final String msg = args.getString(2);
			final boolean bcast = args.getBoolean(3);
			
			// Run the UDP transmitter initialization on its own thread (just in case, see sendMessage comment)
			cordova.getThreadPool().execute(new Runnable() {
            	public void run() {
            		initialize(host, port,bcast, callbackContext);
            		sendMessage(msg,callbackContext);
            	}

            });
 			return true;
		}
		else if("on_pause".equals(action))
		{
			cordova.getThreadPool().execute(new Runnable() {
				public void run() {
					if(successInitializingTransmitter)
					{
						datagramSocket.close();
						successInitializingTransmitter = false;
					}
				}
			});
			return true;
		}
		return false;
	}
}
