%====================================================================================
% plannerwithmap description   
%====================================================================================
context(ctxplannerwithmap, "localhost",  "TCP", "8038").
 qactor( plannerwithmap, ctxplannerwithmap, "it.unibo.plannerwithmap.Plannerwithmap").
  qactor( walker, ctxplannerwithmap, "it.unibo.walker.Walker").
