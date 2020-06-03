%====================================================================================
% tearoom description   
%====================================================================================
mqttBroker("mqtt.eclipse.org", "1883", "unibo/polar").
context(ctxtearoom, "localhost",  "TCP", "8050").
 qactor( tearoom, ctxtearoom, "it.unibo.tearoom.Tearoom").
  qactor( teatables, ctxtearoom, "it.unibo.teatables.Teatables").
  qactor( waiterwalker, ctxtearoom, "it.unibo.waiterwalker.Waiterwalker").
  qactor( waitermind, ctxtearoom, "it.unibo.waitermind.Waitermind").
