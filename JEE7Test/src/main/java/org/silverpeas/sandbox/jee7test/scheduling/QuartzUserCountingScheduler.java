package org.silverpeas.sandbox.jee7test.scheduling;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.silverpeas.sandbox.jee7test.init.annotation.Initialization;
import org.silverpeas.sandbox.jee7test.init.annotation.Initializer;
import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.service.MessageBucket;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author mmoquillon
 */
@Initializer
public class QuartzUserCountingScheduler implements Job {

  @Initialization
  public void init() {
    try {
      SchedulerFactory schedulerFactory = new StdSchedulerFactory();
      Scheduler scheduler = schedulerFactory.getScheduler();
      scheduler.start();

      JobDetail job = newJob(getClass())
          .withIdentity(getClass().getSimpleName())
          .build();
      ScheduleBuilder schedule = simpleSchedule()
          .withIntervalInSeconds(10)
          .repeatForever();
      Trigger trigger = newTrigger()
          .withIdentity(getClass().getSimpleName())
          .startAt(Date.from(Instant.now().plusSeconds(5))) // the CDI container bootstrapping isn't yet completed
          .withSchedule(schedule)
          .build();

      scheduler.scheduleJob(job, trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute(final JobExecutionContext context) throws JobExecutionException {
    int count = User.getAll().size();
    getMessageBucket().pour("[" + context.getJobDetail().getKey().getName() + "] THERE IS " + count +
        " USERS YET");
  }

  private MessageBucket getMessageBucket() {
    return ServiceProvider.getService(MessageBucket.class);
  }
}
