/*
WARNING: You can convert the ES6 code to original JavaScript code with 
Babel(https://babeljs.io/en/repl)
*/

const SerialPort = require('serialport');
const Readline = require('@serialport/parser-readline')
 
var commands = "h,a,w,s,d,z,x,r,l,p,t,q";
var port  = new SerialPort("/dev/ttyUSB0", { baudRate: 115200  });  //, { autoOpen: false }
//var port  = new SerialPort("COM6", { baudRate: 115200  });  //, { autoOpen: false }

const parser = new Readline()
port.pipe(parser)

//parser.on('data', line => console.log(`> ${line}`))

 
//Node since version 7 provides the readline module
const readline = require('readline').createInterface({
  input: process.stdin,
  output: process.stdout,
  terminal: false
})

readline.on('line', function (line) {
  //console.log( line );
  if( line == "q" ){console.log("BYE"); readline.close(); process.exit(); }
  else if( line == "p" ){ parser.on('data', line => console.log(`> ${line}`)) }
  else if( line == "t" ){ parser.removeAllListeners(['data']);
  						  console.log( "You can give commands: " + commands) }
  else port.write( line  );
});
 
port.on("open", function () {
    console.log("open. You can give commands" + commands);
	//give Arduino time to reboot
 	//setTimeout( function(){ console.log('writing ...'); port.write( "a") },2000);
	//setTimeout( function(){ console.log('writing ...'); port.write( "h") },3000);
});  
 
