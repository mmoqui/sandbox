package org.silverpeas.sandbox.jee7test.scheduling;

import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.service.MessageBucket;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

/**
 * @author mmoquillon
 */
@Singleton
@Startup
public class UserCountingScheduler {

  @Resource
  private TimerService timerService;

  @Inject
  private MessageBucket messageBucket;

  @PostConstruct
  public void init() {
    System.out.println("INITIALIZE THE " + getClass().getSimpleName());
    TimerConfig config = new TimerConfig(getClass().getSimpleName(), false);
    ScheduleExpression schedule = new ScheduleExpression()
        .hour("*")
        .minute("*")
        .second("*/10");
    timerService.createCalendarTimer(schedule, config);
  }

  @Timeout
  public void schedule(Timer timer) {
    int count = User.getAll().size();
    messageBucket.pour("[" + timer.getInfo().toString() + "] THERE IS " + count + " USERS YET");
  }

}
