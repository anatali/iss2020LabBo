%====================================================================================
% trustingwalker description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxtrustingwalker, "localhost",  "TCP", "8043").
context(ctxbasicrobot, "192.168.1.68",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( trustingwalker, ctxtrustingwalker, "it.unibo.trustingwalker.Trustingwalker").
  qactor( walkerconsole, ctxtrustingwalker, "it.unibo.walkerconsole.Walkerconsole").
