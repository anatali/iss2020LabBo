%====================================================================================
% radarusage description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxradarursage, "localhost",  "TCP", "8044").
context(ctxradargui, "127.0.0.1",  "TCP", "8038").
 qactor( radargui, ctxradargui, "external").
  qactor( radarusage, ctxradarursage, "it.unibo.radarusage.Radarusage").
tracing.
