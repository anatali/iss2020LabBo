%====================================================================================
% gettaskstudent2 description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxgettaskstudent2, "dontcare1",  "TCP", "8030").
context(ctxgettaskdeploy, "dontcare2",  "TCP", "8025").
 qactor( taskdeploy, ctxgettaskdeploy, "external").
  qactor( student2, ctxgettaskstudent2, "it.unibo.student2.Student2").
