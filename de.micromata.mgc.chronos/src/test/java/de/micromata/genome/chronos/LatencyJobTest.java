package de.micromata.genome.chronos;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import de.micromata.genome.chronos.manager.SchedulerDAO;
import de.micromata.genome.chronos.manager.SchedulerManager;
import de.micromata.genome.chronos.spi.AbstractFutureJob;
import de.micromata.genome.chronos.util.ClassJobDefinition;
import de.micromata.genome.chronos.util.CronTrigger;
import de.micromata.genome.util.types.TimeInMillis;

/**
 * 
 * @author roger
 * 
 */
public class LatencyJobTest extends BaseSchedulerTestCase
{
  private static final Logger log = Logger.getLogger(LatencyJobTest.class);

  private static boolean stopTest = false;

  private static int jobCount = 0;

  public static class SimpleJob extends AbstractFutureJob
  {
    @Override
    public Object call(Object argument) throws Exception
    {
      log.warn("LatencyJobTest.SimpleJob.call");
      if (stopTest == true) {
        throw new JobAbortException("test only");
      }
      ++jobCount;
      return null;
    }
  }

  @Test
  public void testCalcNextChronTrigger()
  {
    CronTrigger ct = new CronTrigger("* * * * *");
    Date d = new Date(System.currentTimeMillis() - TimeInMillis.HOUR);
    ct.setNextFireTime(d);
    Date nd = ct.updateAfterRun(null, JobCompletion.JOB_COMPLETED);
    log.warn("CronTrigger oldTime: " + JobDebugUtils.dateToString(d) + " recalc date: " + nd);
  }

  @Test
  public void testRun()
  {
    stopTest = false;
    jobCount = 0;
    final String testname = "testLatencyJobs";
    final SchedulerManager manager = SchedulerManager.get();
    SchedulerDAO scheddao = ChronosServiceManager.get().getSchedulerDAO();

    scheddao.getDispatcher().setMinNodeBindTime(60000);
    scheddao.getScheduler(testname);

    scheddao.submit(testname, new ClassJobDefinition(SimpleJob.class), null, createTriggerDefinition("p1"));
    sleep(13000);

    stopTest = true;
    sleep(2000);
    Assert.assertTrue("Jobcount: " + jobCalled, jobCount >= 8);
  }

}
