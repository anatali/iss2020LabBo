%====================================================================================
% ledalone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxledalone, "localhost",  "TCP", "8080").
 qactor( led, ctxledalone, "it.unibo.led.Led").
  qactor( anobserver, ctxledalone, "it.unibo.anobserver.Anobserver").
msglogging.
