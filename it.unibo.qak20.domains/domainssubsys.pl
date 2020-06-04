%====================================================================================
% domainssubsys description   
%====================================================================================
context(ctxdomains_dummy, "localhost",  "TCP", "8050").
context(ctxbasicrobot, "192.168.1.68",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( waiterwalker, ctxdomains_dummy, "it.unibo.waiterwalker.Waiterwalker").
