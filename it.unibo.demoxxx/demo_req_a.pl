%====================================================================================
% demo_req_a description   
%====================================================================================
context(ctxdemoreqa, "localhost",  "TCP", "8010").
 qactor( callera, ctxdemoreqa, "it.unibo.callera.Callera").
  qactor( calleda, ctxdemoreqa, "it.unibo.calleda.Calleda").
