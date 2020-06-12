%====================================================================================
% dcreceiver description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxdcreceiver, "localhost",  "TCP", "8037").
 qactor( dcreceiver, ctxdcreceiver, "it.unibo.dcreceiver.Dcreceiver").
