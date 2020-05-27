%====================================================================================
% plannertest description   
%====================================================================================
context(ctxplannertest, "localhost",  "TCP", "8038").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( plantester, ctxplannertest, "it.unibo.plantester.Plantester").
