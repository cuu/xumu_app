// Start of cut and paste area (to put back in Git repo version of this file)
module.exports = {
	
	// Order of parameters on all calls:
	// - success callback function
	// - error callback function
	// - native class name
	// - native method name
	// - arguments for method
	
quick: function(host, port,message, bcast) {
	cordova.exec(
				 // To access the success and error callbacks for initialization, these two functions should be in your project:
				 // UDPTransmitterInitializationSuccess(success)
				 // UDPTransmitterInitializationError(error)
				 function(success){UDPTransmitterInitializationSuccess(success);},
				 function(error){UDPTransmitterInitializationError(error);},
				 "UDPTransmit",
				 "quick",
				 [host, port,message,bcast]);
	return true;
},

on_pause: function()
{
	cordova.exec(
		function(success){},
		function(error){},
		"UDPTransmit",
		"on_pause",
		[]);
		return true;
},
		
};
// End of cut and paste area
