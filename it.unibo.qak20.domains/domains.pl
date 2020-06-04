%====================================================================================
% domains description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxdomains, "localhost",  "TCP", "8060").
 qactor( waiterwalker, ctxdomains, "it.unibo.waiterwalker.Waiterwalker").
  qactor( waiter, ctxdomains, "it.unibo.waiter.Waiter").
  qactor( clientsimulator, ctxdomains, "it.unibo.clientsimulator.Clientsimulator").
