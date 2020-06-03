%====================================================================================
% waiter description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxtearoom, "localhost",  "TCP", "8060").
 qactor( waiterwalker, ctxtearoom, "it.unibo.waiterwalker.Waiterwalker").
  qactor( waiter, ctxtearoom, "it.unibo.waiter.Waiter").
  qactor( clientsimulator, ctxtearoom, "it.unibo.clientsimulator.Clientsimulator").
