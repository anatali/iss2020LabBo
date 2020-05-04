%====================================================================================
% blsledonly description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxblsledonly, "localhost",  "TCP", "8075").
 qactor( led, ctxblsledonly, "it.unibo.led.Led").
