%====================================================================================
% gettask description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxgettask, "localhost",  "TCP", "8025").
 qactor( taskdeploy, ctxgettask, "external").
  qactor( student, ctxgettask, "it.unibo.student.Student").
tracing.
