%====================================================================================
% optimisticwalker description   
%====================================================================================
context(ctxoptimisticwalker, "localhost",  "TCP", "8043").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( optimisticwalker, ctxoptimisticwalker, "it.unibo.optimisticwalker.Optimisticwalker").
