%====================================================================================
% mbotdemo description   
%====================================================================================
context(ctxmbotdemo, "localhost",  "TCP", "8012").
 qactor( datacleaner, ctxmbotdemo, "rx.dataCleaner").
  qactor( distancefilter, ctxmbotdemo, "rx.distanceFilter").
  qactor( mbotrobot, ctxmbotdemo, "it.unibo.mbotrobot.Mbotrobot").
