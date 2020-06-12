%====================================================================================
% dcsender description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxdcsender, "localhost",  "TCP", "8039").
context(ctxdcreceiver, "127.0.0.1",  "TCP", "8037").
 qactor( dcreceiver, ctxdcreceiver, "external").
  qactor( dcsender, ctxdcsender, "it.unibo.dcsender.Dcsender").
tracing.
msglogging.
