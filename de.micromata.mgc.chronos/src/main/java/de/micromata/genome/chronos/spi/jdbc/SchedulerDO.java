/////////////////////////////////////////////////////////////////////////////
//
// $RCSfile: SchedulerDO.java,v $
//
// genome

// Author    Wolfgang Jung (w.jung@micromata.de)
// Created   09.01.2007
// Copyright Micromata 09.01.2007
//
// $Id: SchedulerDO.java,v 1.8 2007-12-03 19:57:37 roger Exp $
// $Revision: 1.8 $
// $Date: 2007-12-03 19:57:37 $
//
/////////////////////////////////////////////////////////////////////////////
package de.micromata.genome.chronos.spi.jdbc;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.State;

/**
 * Repräsentiert die Datenbank-Sicht auf einen Scheduler als Bean.
 * 
 * @see Scheduler
 */
public class SchedulerDO extends StdRecordDO
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 5051649161102115041L;

  /**
   * The Constant UNSAVED_SCHEDULER_ID.
   */
  public static final long UNSAVED_SCHEDULER_ID = -1;

  /**
   * The name.
   */
  private String name;

  /**
   * The host name.
   */
  private String hostName;

  /**
   * The thread pool size.
   */
  private int threadPoolSize = 1;

  /**
   * The service retry time.
   */
  private int serviceRetryTime = 60000;

  /**
   * The job retry time.
   */
  private int jobRetryTime = 30000;

  /**
   * The job max retry count.
   */
  private int jobMaxRetryCount = 2;

  /**
   * The node binding timeout.
   */
  private int nodeBindingTimeout = 0;

  /**
   * The state.
   */
  private State state;

  /**
   * Instantiates a new scheduler do.
   */
  public SchedulerDO()
  {
    pk = UNSAVED_SCHEDULER_ID;
  }

  /**
   * Instantiates a new scheduler do.
   *
   * @param o the o
   */
  public SchedulerDO(SchedulerDO o)
  {
    super(o);
    this.pk = o.pk;
    this.name = o.name;
    this.hostName = o.hostName;
    this.threadPoolSize = o.threadPoolSize;
    this.serviceRetryTime = o.serviceRetryTime;
    this.jobRetryTime = o.jobRetryTime;
    this.jobMaxRetryCount = o.jobMaxRetryCount;
    this.nodeBindingTimeout = o.nodeBindingTimeout;
    this.state = o.state;
  }

  // private
  @Override
  public boolean equals(final Object obj)
  {
    if (obj instanceof SchedulerDO) {
      final SchedulerDO other = (SchedulerDO) obj;
      return other.getPk() == getPk();
    }
    return false;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public State getState()
  {
    return state;
  }

  @Override
  public int hashCode()
  {
    return getPk().intValue();
  }

  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(final String hostName)
  {
    this.hostName = hostName;
  }

  public int getThreadPoolSize()
  {
    return threadPoolSize;
  }

  public void setThreadPoolSize(final int threadPoolSize)
  {
    this.threadPoolSize = threadPoolSize;
    // if (threadPoolSize == 0) {
    // int i = 0;
    // }
  }

  public int getServiceRetryTime()
  {
    return this.serviceRetryTime;
  }

  public void setServiceRetryTime(final int serviceRetryTime)
  {
    this.serviceRetryTime = serviceRetryTime;
  }

  public int getJobRetryTime()
  {
    return this.jobRetryTime;
  }

  public void setJobRetryTime(final int jobRetryTime)
  {
    this.jobRetryTime = jobRetryTime;
  }

  public void setState(State state)
  {
    this.state = state;
  }

  public int getJobMaxRetryCount()
  {
    return jobMaxRetryCount;
  }

  public void setJobMaxRetryCount(int jobMaxRetryCount)
  {
    this.jobMaxRetryCount = jobMaxRetryCount;
  }

  @Override
  public String toString()
  {
    return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE, false, SchedulerDO.class);
  }

  public int getNodeBindingTimeout()
  {
    return nodeBindingTimeout;
  }

  public void setNodeBindingTimeout(int nodeBindingTimeout)
  {
    this.nodeBindingTimeout = nodeBindingTimeout;
  }

}
