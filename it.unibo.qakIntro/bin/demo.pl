%====================================================================================
% demo description   
%====================================================================================
context(ctxdemo, "localhost",  "TCP", "8055").
 qactor( demo, ctxdemo, "it.unibo.demo.Demo").
  qactor( sender, ctxdemo, "it.unibo.sender.Sender").
