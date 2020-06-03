%====================================================================================
% tearoomsubsys description   
%====================================================================================
context(ctxtearoom_dummy, "localhost",  "TCP", "8050").
context(ctxbasicrobot, "192.168.1.68",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waiterwalker, ctxtearoom_dummy, "it.unibo.waiterwalker.Waiterwalker").
