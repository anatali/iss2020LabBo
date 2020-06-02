%====================================================================================
% tearoom description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxtearoom, "localhost",  "TCP", "8050").
 qactor( teatables, ctxtearoom, "it.unibo.teatables.Teatables").
