%====================================================================================
% demo_req_a description   
%====================================================================================
context(ctxcallera, "localhost",  "TCP", "8072").
context(ctxcalleda, "127.0.0.1",  "TCP", "8074").
 qactor( callera, ctxcallera, "it.unibo.callera.Callera").
  qactor( calleda, ctxcalleda, "it.unibo.calleda.Calleda").
