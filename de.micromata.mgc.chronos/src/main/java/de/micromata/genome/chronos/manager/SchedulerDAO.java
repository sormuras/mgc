//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.chronos.manager;

import java.util.List;
import java.util.Map;

import de.micromata.genome.chronos.JobDefinition;
import de.micromata.genome.chronos.JobStore;
import de.micromata.genome.chronos.Scheduler;
import de.micromata.genome.chronos.SchedulerConfigurationException;
import de.micromata.genome.chronos.State;
import de.micromata.genome.chronos.Trigger;
import de.micromata.genome.chronos.spi.Dispatcher;
import de.micromata.genome.chronos.spi.JobRunner;
import de.micromata.genome.chronos.spi.jdbc.JobResultDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDO;
import de.micromata.genome.chronos.spi.jdbc.SchedulerDisplayDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDO;
import de.micromata.genome.chronos.spi.jdbc.TriggerJobDisplayDO;

/**
 * Interface for the Chronos Scheduler.
 *
 * @author roger@micromata.de
 */
public interface SchedulerDAO
{

  /**
   * Get the global Scheduler manager.
   *
   * @return the scheduler manager
   */
  SchedulerManager getSchedulerManager();

  /**
   * Create a Dispatcher instance.
   * 
   * EXPERIMENTAL Feature.
   *
   * @param virtualHostName the virtual host name
   * @return the dispatcher
   * @throws Exception if any error occurs
   */
  public Dispatcher createDispatcher(String virtualHostName) throws Exception;

  /**
   * Apply Filter chain to Job run.
   *
   * @param jobRunner the job runner
   * @return the object
   * @throws Exception the exception
   */
  public Object filterJobRun(JobRunner jobRunner) throws Exception;

  /**
   * Invoke Job dispatched from Dispatcher.
   *
   * @param jobRunner the job runner
   * @return the object
   * @throws Exception the exception
   */
  public Object invokeJob(JobRunner jobRunner) throws Exception;

  /**
   * Gets the admin jobs.
   *
   * @param hostName the host name
   * @param jobName the job name
   * @param state the state
   * @param schedulerName the scheduler name
   * @param resultCount the result count
   * @return the admin jobs
   */
  public List<TriggerJobDisplayDO> getAdminJobs(final String hostName, final String jobName, final String state,
      final String schedulerName,
      final int resultCount);

  /**
   * Gets the next jobs.
   *
   * @param minNodeBindTimeout the min node bind timeout
   * @return the next jobs
   */
  public List<TriggerJobDO> getNextJobs(long minNodeBindTimeout);

  /**
   * Creates the or get scheduler.
   *
   * @param schedulerName the scheduler name
   * @return the scheduler do
   */
  public SchedulerDO createOrGetPersistScheduler(String schedulerName);

  /**
   * Persist.
   *
   * @param scheduler the scheduler
   */
  public void persist(SchedulerDO scheduler);

  /**
   * Es werden keinen neuen Jobs mehr gestartet. ThreadCount will be set to 0.
   *
   * @param schedulerName the scheduler name
   */
  public void denyNewJobs(final String schedulerName);

  /**
   * Setzt die Anzahl der vom Scheduler zu verarbeitenden Jobs.
   * <p>
   * Dies ist die Anzahl der Thread des zugehörigen Thread-Pools.
   * </p>
   * 
   * @param size the size of jopbs
   * @param schedulerName the name of the scheduler
   */
  public void setJobCount(final int size, final String schedulerName);

  /**
   * Build from the parameters a new TriggerJobDO, but does not safe the the job.
   * 
   * The PK of the job will already be set, although the job itself is not inserted into db
   *
   * @param scheduler the scheduler
   * @param jobName the job name
   * @param jobDefinition the job definition
   * @param info the info
   * @param trigger the trigger
   * @param hostName the host name
   * @param state the state
   * @return the trigger job do
   */
  public TriggerJobDO buildTriggerJob(final Scheduler scheduler, String jobName, final JobDefinition jobDefinition,
      final Object info,
      final Trigger trigger, final String hostName, State state);

  /**
   * Submits a new Job.
   * 
   * If the dispatcher is not started, the job will just inserted into the JobStore.
   *
   * @param schedulerName must not be null
   * @param jobName the job name
   * @param jobDefinition must not be null
   * @param arg may be null
   * @param trigger must not be null
   * @return Job reference (pk)
   * @throws SchedulerConfigurationException wenn ein nicht registrierter Scheduler angesprochen wird
   */
  public long submit(final String schedulerName, String jobName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger);

  /**
   * Submit.
   *
   * @param schedulerName the scheduler name
   * @param jobDefinition the job definition
   * @param arg the arg
   * @param trigger the trigger
   * @param hostName the host name
   * @return the Job reference (pk)
   * @throws SchedulerConfigurationException wenn ein nicht registrierter Scheduler angesprochen wird
   */
  public long submit(final String schedulerName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger,
      String hostName);

  /**
   * Submit.
   *
   * @param schedulerName the scheduler name
   * @param jobDefinition the job definition
   * @param arg the arg
   * @param trigger the trigger
   * @return the long
   */
  public long submit(final String schedulerName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger);

  /**
   * Submits a job defined by standard Job ID.
   *
   * @param jobId the job id
   * @param args the args
   * @param trigger the trigger
   * @return the long
   */
  public long submitStdAdminJob(String jobId, final Map<String, String> args, Trigger trigger);

  /**
   * Submits a job defined by standard Job ID.
   *
   * @param jobId the job id
   * @param args the args
   * @return the long
   */
  public long submitStdAdminJob(String jobId, final Map<String, String> args);

  /**
   * Submits a job defined by standard Job ID from ContextChronos.
   * 
   * @param jobId the id of the job
   * @param arg the argument for the job
   * @return the database pk of the job
   */
  public long submitStdJob(String jobId, final Object arg);

  /**
   * Submit std job from ContextChronos.
   *
   * @param jobId A job id from ContextChronos
   * @param arg the arg
   * @param trigger the trigger
   * @return the long
   */
  public long submitStdJob(String jobId, final Object arg, Trigger trigger);

  /**
   * Submits a new Job.
   * 
   * If the dispatcher is not started, the job will just inserted into the JobStore.
   * 
   * @param schedulerName must not be null
   * @param jobDefinition must not be null
   * @param arg may be null
   * @param trigger must not be null
   * @param hostName if null, uses the current hostname
   * @param jobName the name of the job
   * @return Job reference (pk)
   * @throws SchedulerConfigurationException wenn ein nicht registrierter Scheduler angesprochen wird
   */
  public long submit(final String schedulerName, String jobName, final JobDefinition jobDefinition, final Object arg,
      final Trigger trigger, String hostName);

  /**
   * Gets the schedulers.
   *
   * @return the schedulers
   */
  public List<SchedulerDO> getSchedulers();

  /**
   * Gets the scheduler.
   * 
   * If not exits it will be created with standard settings.
   *
   * @param name the name
   * @return the scheduler
   */
  public Scheduler getScheduler(String name);

  /**
   * Get or create if not exists the scheduler.
   *
   * @param schedulerName the scheduler name
   * @param createByDefault the create by default
   * @return the creates the scheduler
   */
  Scheduler getCreateScheduler(String schedulerName, boolean createByDefault);

  /**
   * Gets the unique job names.
   *
   * @return the unique job names
   */
  List<String> getUniqueJobNames();

  /**
   * Gets the admin job by pk.
   *
   * @param pk the pk
   * @return the admin job by pk
   */
  TriggerJobDO getAdminJobByPk(final long pk);

  /**
   * Gets the job by pk.
   *
   * @param pk the pk
   * @return the job by pk
   */
  public TriggerJobDO getJobByPk(long pk);

  /**
   * Gets the admin schedulers.
   *
   * @return the admin schedulers
   */
  List<SchedulerDisplayDO> getAdminSchedulers();

  /**
   * Gets the scheduler by pk.
   *
   * @param pk the pk
   * @return the scheduler by pk
   */
  public Scheduler getSchedulerByPk(Long pk);

  /**
   * Gets the results for job.
   *
   * @param jobId the job id
   * @return the results for job
   */
  public List<JobResultDO> getResultsForJob(long jobId);

  /**
   * Update job.
   *
   * @param job the job
   */
  public void updateJob(TriggerJobDO job);

  /**
   * Adds the to reserved if nessary.
   *
   * @param job the job
   */
  public void addToReservedIfNessary(TriggerJobDO job);

  /**
   * Sets the job state.
   *
   * @param pk the pk
   * @param newState the new state
   * @param oldState the old state
   * @return the int
   */
  public int setJobState(long pk, String newState, String oldState);

  /**
   * Will be called on app startup.
   */
  public void initialize();

  /**
   * Internal callbackback to intialize with configuration
   * @param manager the manger for the scheduler
   */
  void init(SchedulerManager manager);

  /**
   * Will be called for shutdown scheduler.
   */
  public void shutdown();

  /**
   * After shutdown, restart dispatcher.
   */
  public void restart();

  /**
   * Used only for tests.
   * 
   * Used only internally.
   *
   * @return the job store
   */
  JobStore getJobStore();

  /**
   * Thread which dispatches the jobs into scheduler.
   * 
   * Used only internally.
   * 
   * @return the {@link Dispatcher} for the scheduler
   */
  Dispatcher getDispatcher();

  /**
   * defaults to MGC
   * 
   * @return the name of the application
   */
  String getShortApplicationName();
}
