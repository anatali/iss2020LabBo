%====================================================================================
% demo0 description   
%====================================================================================
context(ctxdemo0, "localhost",  "TCP", "8055").
 qactor( demo, ctxdemo0, "it.unibo.demo.Demo").
  qactor( sender, ctxdemo0, "it.unibo.sender.Sender").
