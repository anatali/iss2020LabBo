%====================================================================================
% buttonalone description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxbuttonalone, "localhost",  "TCP", "8065").
 qactor( button, ctxbuttonalone, "it.unibo.button.Button").
msglogging.
