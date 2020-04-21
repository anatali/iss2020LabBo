%====================================================================================
% gettaskstudent1 description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxgettaskstudent1, "somehost1",  "TCP", "8028").
context(ctxgettaskdeploy, "somehost2",  "TCP", "8025").
 qactor( taskdeploy, ctxgettaskdeploy, "external").
  qactor( student1, ctxgettaskstudent1, "it.unibo.student1.Student1").
