%====================================================================================
% sys0 description   
%====================================================================================
context(ctx0, "localhost",  "TCP", "8075").
 qactor( producer, ctx0, "it.unibo.producer.Producer").
  qactor( consumer, ctx0, "it.unibo.consumer.Consumer").
  qactor( sensorobserver, ctx0, "it.unibo.sensorobserver.Sensorobserver").
