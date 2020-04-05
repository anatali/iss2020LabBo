%====================================================================================
% demo description   
%====================================================================================
context(ctxdemo, "localhost",  "TCP", "8055").
 qactor( demo, ctxdemo, "it.unibo.demo.Demo").
  qactor( sentinel, ctxdemo, "it.unibo.sentinel.Sentinel").
  qactor( sender, ctxdemo, "it.unibo.sender.Sender").
