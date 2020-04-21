%====================================================================================
% sentinel description   
%====================================================================================
context(ctxsentinel, "localhost",  "TCP", "8055").
 qactor( sentinel, ctxsentinel, "it.unibo.sentinel.Sentinel").
  qactor( watcher, ctxsentinel, "it.unibo.watcher.Watcher").
