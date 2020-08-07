var ws   =  null;
var host = location.host;
var url  = "ws://"+host+"/data";
//alert("appl.js url=" + url)

function setConnected(connected) 
{
	document.getElementById('connect').disabled    = connected;
	document.getElementById('disconnect').disabled = !connected;
	document.getElementById('echo').disabled = !connected;
	document.getElementById('data').disabled = !connected;
}

function connect() 
{
	ws = new WebSocket(url);
	ws.onopen = function() {
		setConnected(true);
		log('Info: Connection Established.');
	};
	
	ws.onmessage = function(event) {
		log(event.data);
	};
	
	ws.onclose = function(event) {
		setConnected(false);
		log('Info: Closing Connection.');
	};
}

function connectForData() 
{
	ws = new WebSocket("ws://"+host+"/data");
	ws.onopen = function() {
		setConnected(true);
		log('Info: connectForData Established.');
	};
	
	ws.onmessage = function(event) {
		log(event.data);
	};
	
	ws.onclose = function(event) {
		setConnected(false);
		log('Info: Closing Connection.');
	};
}

function disconnect() 
{
	if (ws != null) {
		ws.close();
		ws = null;
	}
	setConnected(false);
}

function echo() {
 	if (ws != null) 
	{
		var message = document.getElementById('message').value;
		log('Sent to server :: ' + message);
		ws.send(message);
	} else {
		alert('connection not established, please connect.');
	}
}

function data(){
 //alert("data ws=" + ws);
	if (ws != null) 
	{
		var message = "get hot data";  
		log('Sent to server :: ' + message);
		ws.send(message);
	} else {
		alert('connection not established, now I connect for data');
		//connectForData();
	}
}

function log(message) 
{
	var console = document.getElementById('logging');
	var p       = document.createElement('p');
	p.appendChild(document.createTextNode(message));
	console.appendChild(p);
}