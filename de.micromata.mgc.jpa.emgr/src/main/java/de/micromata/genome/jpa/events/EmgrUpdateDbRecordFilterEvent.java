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

package de.micromata.genome.jpa.events;

import de.micromata.genome.jpa.DbRecord;
import de.micromata.genome.jpa.IEmgr;

/**
 * Filters de.micromata.genome.jpa.Emgr.update(DbRecord).
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EmgrUpdateDbRecordFilterEvent extends EmgrFilterEvent<Void>
{

  /**
   * The entity.
   */
  private DbRecord entity;

  public DbRecord getEntity()
  {
    return entity;
  }

  /**
   * Instantiates a new emgr update db record filter event.
   *
   * @param emgr the emgr
   * @param entity the entity
   */
  public EmgrUpdateDbRecordFilterEvent(IEmgr<?> emgr, DbRecord entity)
  {
    super(emgr);
    this.entity = entity;
  }

}
