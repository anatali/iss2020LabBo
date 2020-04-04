%====================================================================================
% sensor description   
%====================================================================================
context(ctxsensor, "localhost",  "TCP", "8075").
 qactor( sensorobserver, ctxsensor, "it.unibo.sensorobserver.Sensorobserver").
