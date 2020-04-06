%====================================================================================
% robot description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxrobot, "it.unibo.basicrobot.Basicrobot").
