%====================================================================================
% sentinel description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxsentinel, "localhost",  "TCP", "8055").
 qactor( sentinel, ctxsentinel, "it.unibo.sentinel.Sentinel").
