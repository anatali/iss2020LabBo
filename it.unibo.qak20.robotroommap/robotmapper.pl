%====================================================================================
% robotmapper description   
%====================================================================================
context(ctxrobotmapper, "localhost",  "TCP", "8030").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( robotmapper, ctxrobotmapper, "it.unibo.robotmapper.Robotmapper").
