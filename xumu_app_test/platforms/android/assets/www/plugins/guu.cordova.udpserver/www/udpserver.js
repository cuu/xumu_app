cordova.define("guu.cordova.udpserver.udpserver", function(require, exports, module) { // Start of cut and paste area (to put back in Git repo version of this file)
module.exports = {
	
	// Order of parameters on all calls:
	// - success callback function
	// - error callback function
	// - native class name
	// - native method name
	// - arguments for method
	
init: function() {
	cordova.exec(
				 // To access the success and error callbacks for initialization, these two functions should be in your project:
				 // UDPServerterInitializationSuccess(success)
				 // UDPServerterInitializationError(error)
				 function(success){UDPServerInitiSuccess(success);},
				 function(error){UDPServerInitError(error);},
				 "UDPServer",
				 "init",
				 []);
	return true;
},
getMessage: function() {
cordova.exec(
			// To access the success and error callbacks for packet transmission, these two functions should be in your project:
			// UDPTransmissionSuccess(success)
			// UDPTransmissionError(error)
			function(success){UDPGetSuccess(success);},
			function(error){UDPGetError(error);},
			"UDPServer",
			"getMessage",
			[]);
return true;
},

on_pause: function() {
cordova.exec(
			function(success){},
			function(error){},
			"UDPServer",
			"on_pause",
			[]);
			return true;
}
};
// End of cut and paste area

});
