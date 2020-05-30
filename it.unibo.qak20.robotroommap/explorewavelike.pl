%====================================================================================
% explorewavelike description   
%====================================================================================
context(ctxexplorewawelike, "localhost",  "TCP", "8038").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( explorewavelike, ctxexplorewawelike, "it.unibo.explorewavelike.Explorewavelike").
  qactor( optimisticwalker, ctxexplorewawelike, "it.unibo.optimisticwalker.Optimisticwalker").
