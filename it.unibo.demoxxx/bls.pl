%====================================================================================
% bls description   
%====================================================================================
context(ctxbutton, "localhost",  "TCP", "8074").
context(ctxbls, "127.0.0.1",  "TCP", "8095").
 qactor( led, ctxbls, "it.unibo.led.Led").
  qactor( button, ctxbutton, "it.unibo.button.Button").
  qactor( main, ctxbutton, "it.unibo.main.Main").
tracing.
