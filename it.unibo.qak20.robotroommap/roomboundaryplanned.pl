%====================================================================================
% roomboundaryplanned description   
%====================================================================================
context(ctxboundaryplanned, "localhost",  "TCP", "8068").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( roomboudaryexplorer, ctxboundaryplanned, "it.unibo.roomboudaryexplorer.Roomboudaryexplorer").
