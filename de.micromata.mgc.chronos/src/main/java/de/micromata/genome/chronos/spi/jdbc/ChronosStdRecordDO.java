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

package de.micromata.genome.chronos.spi.jdbc;

import java.io.Serializable;
import java.util.Date;

import de.micromata.genome.util.types.Converter;

/**
 * The Class StdRecordDO.
 *
 * @author roger@micromata.de
 */
public class ChronosStdRecordDO implements /** BaseStdRecord, */
    Serializable
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 3891095023083295900L;

  /**
   * The pk.
   */
  protected Long pk;

  /**
   * The created by.
   */
  private String createdBy;

  /**
   * The modified by.
   */
  private String modifiedBy;

  /**
   * The modified at.
   */
  private Date modifiedAt;

  /**
   * The created at.
   */
  private Date createdAt;

  /**
   * The update counter.
   */
  private Integer updateCounter;

  /**
   * Instantiates a new std record do.
   */
  public ChronosStdRecordDO()
  {

  }

  /**
   * Instantiates a new std record do.
   *
   * @param other the other
   */
  public ChronosStdRecordDO(ChronosStdRecordDO other)
  {
    this.pk = other.pk;
    this.createdAt = other.createdAt;
    this.createdBy = other.createdBy;
    this.modifiedAt = other.modifiedAt;
    this.modifiedBy = other.modifiedBy;
    this.updateCounter = other.updateCounter;
  }

  public String getModifiedBy()
  {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }

  public Date getModifiedAt()
  {
    return modifiedAt;
  }

  public String getModifiedAtString()
  {
    if (modifiedAt == null) {
      return "";
    }
    return Converter.formatByIsoDateFormat(modifiedAt);
  }

  public void setModifiedAt(Date modifiedAt)
  {
    this.modifiedAt = modifiedAt;
  }

  public void setModifiedAtString(String str)
  {
    this.modifiedAt = Converter.parseIsoDateToDate(str);
  }

  public Date getCreatedAt()
  {
    return createdAt;
  }

  public String getCreatedAtString()
  {
    if (createdAt == null) {
      return "";
    }
    return Converter.formatByIsoDateFormat(createdAt);
  }

  public void setCreatedAtString(String str)
  {
    createdAt = Converter.parseIsoDateToDate(str);
  }

  public void setCreatedAt(Date createdAt)
  {
    this.createdAt = createdAt;
  }

  // Für optimistic locking

  public Integer getUpdateCounter()
  {
    return updateCounter;
  }

  public void setUpdateCounter(Integer updateCounter)
  {
    this.updateCounter = updateCounter;
  }

  public Long getPk()
  {
    return pk;
  }

  public void setPk(Long pk)
  {
    this.pk = pk;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }

  // compat < R3

  public long getId()
  {
    return getPk();
  }
}
