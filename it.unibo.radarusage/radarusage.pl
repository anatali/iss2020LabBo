%====================================================================================
% radarusage description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/polar").
context(ctxradargui, "localhost",  "TCP", "8038").
context(ctxradarursage, "127.0.0.1",  "TCP", "8044").
 qactor( radargui, ctxradargui, "external").
  qactor( radarusage, ctxradarursage, "it.unibo.radarusage.Radarusage").
