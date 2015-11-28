cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-whitelist/whitelist.js",
        "id": "cordova-plugin-whitelist.whitelist",
        "runs": true
    },
    {
        "file": "plugins/edu.uic.travelmidwest.cordova.udptransmit/www/udptransmit.js",
        "id": "edu.uic.travelmidwest.cordova.udptransmit.udptransmit",
        "merges": [
            "udptransmit"
        ]
    },
    {
        "file": "plugins/com.example.hello/www/hello.js",
        "id": "com.example.hello.hello",
        "clobbers": [
            "hello"
        ]
    },
    {
        "file": "plugins/guu.cordova.udpserver/www/udpserver.js",
        "id": "guu.cordova.udpserver.udpserver",
        "merges": [
            "udpserver"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.0.0",
    "edu.uic.travelmidwest.cordova.udptransmit": "1.0.8",
    "com.example.hello": "0.7.0",
    "guu.cordova.udpserver": "0.0.1"
}
// BOTTOM OF METADATA
});