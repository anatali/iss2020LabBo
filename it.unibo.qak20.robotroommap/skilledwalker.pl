%====================================================================================
% skilledwalker description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxskilledwalker, "localhost",  "TCP", "8049").
context(ctxfortrustingwalker, "dontcare",  "TCP", "8043").
 qactor( trustingwalker, ctxfortrustingwalker, "external").
  qactor( skilledwalker, ctxskilledwalker, "it.unibo.skilledwalker.Skilledwalker").
