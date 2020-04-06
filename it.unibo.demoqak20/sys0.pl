%====================================================================================
% sys0 description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctx1, "localhost",  "TCP", "8010").
 qactor( qak1, ctx1, "it.unibo.qak1.Qak1").
  qactor( qak2, ctx1, "it.unibo.qak2.Qak2").
tracing.
