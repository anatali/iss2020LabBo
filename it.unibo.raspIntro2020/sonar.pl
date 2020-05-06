%====================================================================================
% sonar description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/sonar/events").
context(ctxsonar, "localhost",  "TCP", "8068").
 qactor( sonarsimulator, ctxsonar, "rx.sonarSimulator").
  qactor( datalogger, ctxsonar, "rx.dataLogger").
  qactor( datacleaner, ctxsonar, "rx.dataCleaner").
  qactor( distancefilter, ctxsonar, "rx.distanceFilter").
  qactor( sonardatasource, ctxsonar, "sensors.sonarHCSR04SupportActor").
  qactor( sonar, ctxsonar, "it.unibo.sonar.Sonar").
