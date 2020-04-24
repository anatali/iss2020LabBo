%====================================================================================
% demo_req_a description   
%====================================================================================
context(ctxdemoreqa, "localhost",  "TCP", "8010").
 qactor( callera1, ctxdemoreqa, "it.unibo.callera1.Callera1").
  qactor( callera2, ctxdemoreqa, "it.unibo.callera2.Callera2").
  qactor( calleda, ctxdemoreqa, "it.unibo.calleda.Calleda").
