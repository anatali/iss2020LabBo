%====================================================================================
% gettaskdeploy description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883").
context(ctxgettaskdeploy, "127.0.0.1",  "TCP", "8025").
 qactor( taskdeploy, ctxgettaskdeploy, "it.unibo.taskdeploy.Taskdeploy").
