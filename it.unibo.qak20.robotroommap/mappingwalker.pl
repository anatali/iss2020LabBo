%====================================================================================
% mappingwalker description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxmappingwalker, "localhost",  "TCP", "8030").
context(ctxbasicrobot, "dontcare",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mappingwalker, ctxmappingwalker, "it.unibo.mappingwalker.Mappingwalker").
