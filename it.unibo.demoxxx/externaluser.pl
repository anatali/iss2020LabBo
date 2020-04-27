%====================================================================================
% externaluser description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxexternaluser, "localhost",  "TCP", "8015").
context(ctxexternalresource, "127.0.0.1",  "TCP", "8048").
 qactor( resource, ctxexternalresource, "external").
  qactor( user, ctxexternaluser, "it.unibo.user.User").
