package org.silverpeas.sandbox.jee7test.scheduling;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 * @author mmoquillon
 */
@Singleton
@Startup
public class MessagePrinterScheduler {

  @Resource
  private TimerService timerService;

  @PostConstruct
  public void init() {
    System.out.println("INITIALIZE THE " + getClass().getSimpleName());
    TimerConfig config = new TimerConfig(getClass().getSimpleName(), false);
    ScheduleExpression schedule = new ScheduleExpression()
        .hour("*")
        .minute("*")
        .second("*/15");
    timerService.createCalendarTimer(schedule, config);
  }

  @Timeout
  public void schedule(Timer timer) {
    System.out.println("Hello boy, I'm " + timer.getInfo().toString() + "!");
  }

}
