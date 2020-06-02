%====================================================================================
% trustingwalker description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxtrustingwalker, "localhost",  "TCP", "8043").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( trustingwalker, ctxtrustingwalker, "it.unibo.trustingwalker.Trustingwalker").
