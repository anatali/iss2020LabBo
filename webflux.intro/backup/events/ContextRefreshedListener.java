package events;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/*
 Spring itself publishes a variety of events out of the box. 
 For example, the ApplicationContext will fire various framework events. 
 E.g. ContextRefreshedEvent, ContextStartedEvent, RequestHandledEvent etc.

These events provide application developers an option to hook into the lifecycle 
of the application and the context and add in their own custom logic where needed.
 */
public class ContextRefreshedListener 
implements ApplicationListener<ContextRefreshedEvent> {
  @Override
  public void onApplicationEvent(ContextRefreshedEvent cse) {
      System.out.println("Handling context re-freshed event. ");
  }
}