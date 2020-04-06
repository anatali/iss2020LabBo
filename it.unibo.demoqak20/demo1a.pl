%====================================================================================
% demo1a description   
%====================================================================================
context(ctxdemo1a, "localhost",  "TCP", "8010").
 qactor( caller1a, ctxdemo1a, "it.unibo.caller1a.Caller1a").
  qactor( called1a, ctxdemo1a, "it.unibo.called1a.Called1a").
