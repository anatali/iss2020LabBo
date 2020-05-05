%====================================================================================
% sonar description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxsonar, "localhost",  "TCP", "8068").
 qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
msglogging.
