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

import de.micromata.genome.chronos.spi.DispatcherImpl;
import de.micromata.genome.chronos.spi.DispatcherImpl2;
import de.micromata.genome.chronos.spi.ram.RamJobStore;

/**
 * Implements a Scheduler Service with a RAM Jobstore.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class RAMSchedulerDAOImpl extends SchedulerBaseDAO
{

  @Override
  public DispatcherImpl createDispatcher(String virtualHostName) throws Exception
  {
    RamJobStore store = new RamJobStore();

    DispatcherImpl dispatcher = new DispatcherImpl2(virtualHostName, store);
    return dispatcher;
  }

}