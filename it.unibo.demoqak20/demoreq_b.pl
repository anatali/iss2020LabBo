%====================================================================================
% demoreq_b description   
%====================================================================================
context(ctxdemo1, "localhost",  "TCP", "8010").
 qactor( qak1, ctxdemo1, "it.unibo.qak1.Qak1").
  qactor( qak2, ctxdemo1, "it.unibo.qak2.Qak2").
