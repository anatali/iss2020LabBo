%====================================================================================
% virtualdemo description   
%====================================================================================
context(ctxvirtualdemo, "localhost",  "TCP", "8014").
 qactor( datacleaner, ctxvirtualdemo, "rx.dataCleaner").
  qactor( distancefilter, ctxvirtualdemo, "rx.distanceFilter").
  qactor( virtualrobot, ctxvirtualdemo, "it.unibo.virtualrobot.Virtualrobot").
