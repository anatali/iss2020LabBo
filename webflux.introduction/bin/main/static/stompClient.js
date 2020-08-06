//https://github.com/sockjs/sockjs-client
var Stomp       = require('stompjs');
const readline = require('readline');

//var stompClient = Stomp.overTCP('localhost', 8082);
//var stompClient = Stomp.overWS('ws://localhost:8082/stomp'); 


//SIMULA UNA FORM che invia comandi POST
function sendRequestData( params, method) {
    method = method || "post"; // il metodo POST è usato di default
    //console.log(" sendRequestData  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", hostAddr);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" sendRequestData " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}


function postJQuery(themove){
var form = new FormData();
form.append("name",  "move");
form.append("value", "r");

let myForm = document.getElementById('myForm');
let formData = new FormData(myForm);


var settings = {
  "url": "http://localhost:8080/move",
  "method": "POST",
  "timeout": 0,
  "headers": {
       "Content-Type": "text/plain"
   },
  "processData": false,
  "mimeType": "multipart/form-data",
  "contentType": false,
  "data": form
};

 
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function sendUpdateRequest(){
	console.log(" sendUpdateRequest "  );
    //stompClient.send("/app/update", {}, JSON.stringify({'name': 'update' }));
}

function connect() {
    console.log("CONNECT INIT");
    //var stompClient = Stomp.overWS('ws://localhost:8082/it-unibo-iss'); 
    var stompClient = Stomp.overTCP('localhost', 8082); 
    
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log("SUBSCRIBING");
        stompClient.subscribe('/topic/display', function (msg) {
             showMsg(JSON.parse(msg.body).content);	//See ResourceRep
        });
    });
	console.log("CONNECTED");
    sendUpdateRequest();
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

 

function sendTheMove(move){
	console.log("sendTheMove " + move);
    stompClient.send("/app/move", {}, JSON.stringify({'name': move }));
}



function showMsg(message) {
console.log(message );
    $("#applmsgs").html( "<pre>"+message.replace(/\n/g,"<br/>")+"</pre>" );
    //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

console.log(" START "  ); 
connect();
var rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});
rl.question("What do you think of node.js? ", function(answer) {
  // TODO: Log the answer in a database
  console.log("Thank you for your valuable feedback:", answer);

  rl.close();
});
 
