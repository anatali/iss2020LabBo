%====================================================================================
% maitreddr description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxmaitreddr, "localhost",  "TCP", "8049").
 qactor( trustingwalker, ctxmaitreddr, "it.unibo.trustingwalker.Trustingwalker").
  qactor( maitreddr, ctxmaitreddr, "it.unibo.maitreddr.Maitreddr").
tracing.
