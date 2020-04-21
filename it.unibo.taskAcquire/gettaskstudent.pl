%====================================================================================
% gettaskstudent description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxgettaskstudent, "localhost",  "TCP", "8028").
context(ctxgettaskdeploy, "127.0.0.1",  "TCP", "8025").
 qactor( taskdeploy, ctxgettaskdeploy, "external").
  qactor( student, ctxgettaskstudent, "it.unibo.student.Student").
tracing.
