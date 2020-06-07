%====================================================================================
% receiver description   
%====================================================================================
mqttBroker("localhost", "1883", "").
context(ctxreceiver, "localhost",  "MQTT", "8038").
 qactor( receiver, ctxreceiver, "it.unibo.receiver.Receiver").
