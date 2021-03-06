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

package de.micromata.genome.db.jpa.xmldump.impl;

import de.micromata.genome.db.jpa.xmldump.api.XmlDumpRestoreContext;
import de.micromata.genome.jpa.metainf.EntityMetadata;

/**
 * The Interface XmlJpaPersistService.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public interface XmlJpaPersistService
{

  /**
   * Persist.
   *
   * @param ctx the ctx
   * @param data the data
   * @return the stored attached object
   */
  Object persist(XmlDumpRestoreContext ctx, Object data);

  /**
   * Calls listener and then store.
   *
   * @param ctx the ctx
   * @param entityMetadata the entity metadata
   * @param entity the entity
   * @return the stored attached object
   */
  Object persist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity);

  /**
   * Store entity.
   *
   * @param ctx the ctx
   * @param entityMetadata the entity metadata
   * @param entity the entity
   * @return the stored attached object
   */
  Object store(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object entity);

  /**
   * Flush the underlying entitymanager.
   * 
   * @param ctx the context
   */
  void flush(XmlDumpRestoreContext ctx);

  /**
   * Prepare persist.
   *
   * @param ctx the ctx
   * @param entityMetadata the entity metadata
   * @param data the data
   * @return != null if the entity is already persistet.
   */
  Object preparePersist(XmlDumpRestoreContext ctx, EntityMetadata entityMetadata, Object data);
}
