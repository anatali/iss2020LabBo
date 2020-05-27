%====================================================================================
% explorewawelike description   
%====================================================================================
context(ctxexplorewawelike, "localhost",  "TCP", "8038").
context(ctxbasicrobot, "192.168.1.22",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( explorewawelike, ctxexplorewawelike, "it.unibo.explorewawelike.Explorewawelike").
