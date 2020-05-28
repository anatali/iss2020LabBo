%====================================================================================
% roomboundaryplanned description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/roomboundaryplanned").
context(ctxboundaryplanned, "localhost",  "TCP", "8068").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( roomboundaryexplorer, ctxboundaryplanned, "it.unibo.roomboundaryexplorer.Roomboundaryexplorer").
