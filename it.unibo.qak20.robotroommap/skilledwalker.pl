%====================================================================================
% skilledwalker description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxskilledwalker, "localhost",  "TCP", "8049").
 qactor( trustingwalker, ctxskilledwalker, "it.unibo.trustingwalker.Trustingwalker").
  qactor( skilledwalker, ctxskilledwalker, "it.unibo.skilledwalker.Skilledwalker").
tracing.
