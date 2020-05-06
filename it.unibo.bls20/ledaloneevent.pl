%====================================================================================
% ledaloneevent description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ledaloneevent/events").
context(ctxledalone, "localhost",  "TCP", "8080").
 qactor( led, ctxledalone, "it.unibo.led.Led").
msglogging.
