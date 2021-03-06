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

package de.micromata.genome.chronos;

/**
 * The scheduler is not correctly configured.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SchedulerConfigurationException extends SchedulerException
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 9029534181438687101L;

  /**
   * Instantiates a new scheduler configuration exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public SchedulerConfigurationException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Instantiates a new scheduler configuration exception.
   *
   * @param message the message
   */
  public SchedulerConfigurationException(final String message)
  {
    super(message);
  }

  /**
   * Instantiates a new scheduler configuration exception.
   *
   * @param cause the cause
   */
  public SchedulerConfigurationException(final Throwable cause)
  {
    super(cause);
  }

}
