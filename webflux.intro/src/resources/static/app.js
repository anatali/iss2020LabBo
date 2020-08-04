var stompClient = null;
var hostAddr = "http://localhost:8080/move";

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

$.ajax(settings).done(function (response) {
  //console.log(response);  //The web page
  console.log("done move:" + themove );
});

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

function myIpAndConnect() {
      var ip = location.host;
      //alert(ip);
      document.getElementById("myIp").innerHTML = ip;
      connect()
}

function connect() {
    var socket  = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/display', function (msg) {
             showMsg(JSON.parse(msg.body).content);
        });
    });
}

function showMsg(message) {
	console.log(message );
    $("#applmsgs").html( "<pre><ks>"+message.replace(/\n/g,"<br/>")+"</ks></pre>" );
    //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

/*
function sendMove() {
    stompClient.send("/app/move", {}, JSON.stringify({'name': $("#name").val()}));
}
*/

function sendTheMove(move){
	console.log("sendTheMove " + move);
    stompClient.send("/app/move", {}, JSON.stringify({'name': move }));
}

function sendShowResourceRequest(){
	console.log(" sendShowResourceRequest " + stompClient );
    stompClient.send("/app/showresource", {}, JSON.stringify( {'name': 'getresource' }));
}

function sendStartResourceUpdating(){
	console.log(" sendStartResourceUpdating "  );
    stompClient.send("/app/startresourceupdating", {}, JSON.stringify( {'name': 'startupdateresource' })); 
}


$(function () {
    $("form").on('submit', function (e) { e.preventDefault();  });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
	$( "#showresource" ).click(function() { sendShowResourceRequest(  ) });
	$( "#resourceupdating" ).click(function() { sendStartResourceUpdating(  ) });
	$( "#resourceflux" ).click(function() { 
		stompClient.send("/app/resourceflux", {}, JSON.stringify( {'name': 'startresourceflux' }))
	});
	
});



