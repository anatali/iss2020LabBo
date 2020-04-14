%====================================================================================
% boundaryrobot description   
%====================================================================================
context(ctxrobotboundary, "localhost",  "TCP", "8018").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( robotboundary, ctxrobotboundary, "it.unibo.robotboundary.Robotboundary").
