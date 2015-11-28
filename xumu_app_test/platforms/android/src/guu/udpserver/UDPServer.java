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

package guu.udpserver;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.*;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.DhcpInfo;  

import udp_printf.*;
import com.snappydb.*;

public class UDPServer extends CordovaPlugin 
{
	
	DatagramPacket datagramPacket;
	DatagramSocket datagramSocket;
	boolean successInitializingServer = false;
	boolean constructed=false;
	boolean successResolvingBcastAddress = false;
	final int port = 9999;
		
	public JSONObject json_obj;
	public JSONArray json_array;
	public int json_array_count;
	
	public Map<String, Runnable> methodMap;
	private JSONArray _args;
	private CallbackContext _callbackContext;
	
	private final static int PACKETSIZE = 128;
	// Constructor wont be executed in cordova
	public UDPServer() 
	{
		constructor();
	}


	public static String getCurrentTimeStamp(){
    try {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

        return currentTimeStamp;
    } catch (Exception e) {
        e.printStackTrace();

        return null;
    }
	}

	public void constructor()
	{
		if(constructed==true) return;
		
		methodMap = new HashMap<String, Runnable>();
		
		methodMap.put("init", new Runnable() 
		{
			public void run() { 
				init_udp_server(_args,_callbackContext);
			};
    }); 
    
		methodMap.put("getMessage", new Runnable() 
		{
			public void run() { 
				getMessage(_args,_callbackContext);
			};
    });

		methodMap.put("on_pause", new Runnable() 
		{
			public void run() { 
				on_pause(_args,_callbackContext);
			};
    });
		
		try
		{
			json_obj = new JSONObject();
			json_array = new JSONArray();
			json_obj.put("devices",json_array);
		}catch (Exception e)
		{
			
		}
		
    constructed=true;
	}

	public void store_json_to_snappy()
	{
		// store json_obj to snappyDB
		//mac {time1:..... time2:.... time3:....}
		
	}
	
	private void on_pause(JSONArray args, CallbackContext callbackContext)
	{
		//在手机on pause 时停止监听
		//if(successInitializingServer)
		DatagramClient.printf("udpserver on_pause");	
		successInitializingServer = false;	
		datagramSocket.close();
		
		
	}
	
 	private void getMessage(JSONArray args, CallbackContext callbackContext) 
 	{
		// Only attempt to send a packet if the transmitter initialization was successful
		if (successInitializingServer) 
			callbackContext.success( json_obj.toString());
		else
			callbackContext.error("Server not Initialized ");
	}

	private InetAddress getBroadcastAddress() throws IOException 
	{
		Context context = cordova.getActivity().getApplicationContext();
    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    DhcpInfo dhcp = wifi.getDhcpInfo();
    // handle null somehow
    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
    byte[] quads = new byte[4];
    for (int k = 0; k < 4; k++)
      quads[k] = (byte) (broadcast >> (k * 8));
    return InetAddress.getByAddress(quads);
	}
	
  private void init_udp_server(JSONArray args,CallbackContext callbackContext)
  {
		
  	InetAddress address = null;
		boolean found_mac=false;
		boolean found_zigbee_device=false;
		try 
  	{
  	// 'host' can be a ddd.ddd.ddd.ddd or named URL, so doesn't always resolve
    	address = getBroadcastAddress();
    	successResolvingBcastAddress = true;
			DatagramClient.printf("phone bcast addr: "+ address.toString());
    } catch (Exception e) 
    {
    	e.printStackTrace();
    	DatagramClient.printf(e.toString());
    }
		if(successInitializingServer) return;
    if (successResolvingBcastAddress) 
    {
			try 
			{
							
				datagramSocket = new DatagramSocket( port ,address );
    		datagramSocket.setBroadcast(true);
    		successInitializingServer = true;
         			
				if (successInitializingServer)
					callbackContext.success("Success initializing UDP server using datagram socket: " + datagramSocket);
				else
					callbackContext.error("Error initializing UDP server using datagram socket: " + datagramSocket);

				while(successInitializingServer==true)
				{
					// Create a packet
					try{
						DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
						datagramSocket.receive( packet ) ;
						final String test = new String(packet.getData());
						final String ip_addr =new String( packet.getAddress().toString());
						DatagramClient.printf("phone receive: "+ ip_addr + " " + packet.getPort() + ": " + test ) ;

						// 它上传的只有这样的格式 {"Mac":"xxxxx"}
						// and {"zigbee_device":"xxxxxxx"}
						try{
							String time_str = getCurrentTimeStamp();
							
							JSONObject obj = new JSONObject(test);
							obj.put("ip",ip_addr);
							obj.put("time",time_str);
							for(int j=0;j< json_array.length();j++)
							{
								if(obj.has("Mac") && json_array.getJSONObject(j).has("Mac") )
								{
									if(json_array.getJSONObject(j).getString("Mac").equals(obj.getString("Mac")))
									{
										json_array.put(j, obj);//replace 
										found_mac = true;
									}
								}
								if(obj.has("zigbee_device") && json_array.getJSONObject(j).has("zigbee_device") )
								{
									if(json_array.getJSONObject(j).getString("zigbee_device").equals(obj.getString("zigbee_device")))
									{
										json_array.put(j, obj);//replace 
										found_zigbee_device = true;
									}
								}								
							}
							
							if(found_mac == false&& obj.has("Mac"))
							{
								json_array.put(obj);
								DatagramClient.printf("push a mac");
							}
							if(found_zigbee_device==false && obj.has("zigbee_device"))
							{
								json_array.put(obj);
								DatagramClient.printf("push zigbee device");
							}
							
							found_mac = false;
							found_zigbee_device=false;
						} catch (JSONException je)
						{
							DatagramClient.printf(je.toString());
						}
						
						cordova.getActivity().runOnUiThread(new Runnable() 
						{
							public void run() 
							{
						    	webView.loadUrl("javascript:udp_server_get_msg()");
						  }
						});						
						
						//datagramSocket.send( packet ) ;
					}catch (Exception e)
					{
						
					}
				}							
      } catch (SocketException e) 
      {
				e.printStackTrace();
				DatagramClient.printf(e.toString());
      }      
    }else
    {
			callbackContext.error("Error get bcast addr ");
    }
  }

	// Handles and dispatches "exec" calls from the JS interface (udpserver.js)
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException 
	{
	
		_args = args;
		_callbackContext = callbackContext;
		
		if(constructed==false)
		{
			constructor();
		}
		
		if(constructed)
		{
			cordova.getThreadPool().execute( methodMap.get(action) );
			return true;
		}
    return false;
	}
}
