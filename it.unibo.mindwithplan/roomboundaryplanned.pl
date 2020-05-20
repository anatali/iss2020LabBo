%====================================================================================
% roomboundaryplanned description   
%====================================================================================
mqttBroker("localhost", "1883", "").
context(ctxboundaryplanned, "localhost",  "TCP", "8068").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( roomboudaryexplorer, ctxboundaryplanned, "it.unibo.roomboudaryexplorer.Roomboudaryexplorer").
