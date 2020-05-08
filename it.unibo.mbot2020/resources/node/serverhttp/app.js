/*
---------------------------------------------------------------
npm install express			//--save implicit
npm install serialport 
---------------------------------------------------------------
*/
const express  = require('express');
const path     = require("path");
const app      = express();
var SerialPort = require("serialport");
var bodyParser = require('body-parser');

var port              = 3000;
var arduinoPort       = "/dev/ttyUSB0";	//COM6
var arduinoSerialPort = new SerialPort("/dev/ttyUSB0", { baudRate: 115200  });
var commands          = "a,w,s,d,z,x,r,l,p,t,h";
var publicPath        = path.resolve(__dirname,"public")

arduinoSerialPort.on('open',function() {
  console.log('Serial Port ' + arduinoPort + ' is opened.');
});

app.use( express.static('public'));

app.use(bodyParser.urlencoded({
   extended: false
}));
app.use(bodyParser.json());


app.get('/', function (req, res) {
    res.send('Working');
})


app.get('/:action', function (req, res) {
   //var action = req.params.action || req.param('action');   
   var move = req.params.action;
   var pos  = commands.indexOf(move)
   if( pos >= 0 ){
   		arduinoSerialPort.write( move );
   		res.send("Move done:" + move)
   } else res.send("Sorry, I don't know the move: " + move);
 });

app.post('/:action', function (req, res) {
   var move = req.params.action;
   var pos  = commands.indexOf(move)
   if( pos >= 0 ){
   		arduinoSerialPort.write( move );
   		res.sendFile(__dirname + '/public/index.html');
   } else res.send("Sorry, I don't know the move: " + move);
 });

app.listen(port, function () {
  console.log('Example app listening on port http://192.168.1.8:' + port  );
});
