%====================================================================================
% nanodemo description   
%====================================================================================
context(ctxnanodemo, "localhost",  "TCP", "8012").
 qactor( datacleaner, ctxnanodemo, "rx.dataCleaner").
  qactor( distancefilter, ctxnanodemo, "rx.distanceFilter").
  qactor( nanorobot, ctxnanodemo, "it.unibo.nanorobot.Nanorobot").
