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

package de.micromata.genome.db.jpa.tabattr.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Methods to manage/validate Timeable DOs.
 */
public interface TimeableService {

  /**
   * Returns the attrRow which is valid at the given Date or null.
   *
   * @param entity The entity with timeable attrRows.
   * @param groupName The group name.
   * @param date The date to find a row for.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * @return The attrRow or null.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowValidAtDate(final EntityWithTimeableAttr<PK, T> entity, final String groupName, final Date date);

  /**
   * Returns the attrRow which is valid at the given Date or null.
   *
   * @param attrRows The attrRows.
   * @param date The date to find a row for.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The attrRow or null.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowValidAtDate(final List<T> attrRows, final Date date);

  /**
   * Returns the attrRow which is valid at the given Date depending on the type of the group or null.
   *
   * @param attrRows The attrRows.
   * @param group The group.
   * @param date The date to find a row for.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   *
   * @return The attrRow or null.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowValidAtDate(final List<T> attrRows, final AttrGroup group, final Date date);

  /**
   * Returns a List of attrRows whose startTime is within the given range.
   *
   * @param entity The entity with timeable attrRows.
   * @param groupName The group name.
   * @param start The start date of the range.
   * @param end The end date of the range.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The attrRows.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> getAttrRowsWithinDateRange(final EntityWithTimeableAttr<PK, T> entity, final String groupName,
      final Date start, final Date end);

  /**
   * Returns a List of attrRows whose startTime is within the given range.
   *
   * @param attrRows The attrRows to filter.
   * @param start The start date of the range.
   * @param end The end date of the range.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The attrRows.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> getAttrRowsWithinDateRange(final List<T> attrRows, final Date start, final Date end);

  /**
   * Returns an attrRow which is in the same month as the given date or null. If there are multiple attrRows in the
   * list, it will return the first one.
   *
   * @param attrRows The attrRows to look for.
   * @param dateToSelectAttrRow The date to find a row for in the same month.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The attrRow or null.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForSameMonth(final List<T> attrRows, final Date dateToSelectAttrRow);

  /**
   * Returns an attrRow which is in the same month as the given date or null. If there are multiple attrRows in the
   * list, it will return the first one.
   *
   * @param entity The entity with timeable attrRows.
   * @param group The group.
   * @param dateToSelectAttrRow The date to find a row for in the same month.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The attrRow or null.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForSameMonth(final EntityWithTimeableAttr<PK, T> entity, final AttrGroup group,
      final Date dateToSelectAttrRow);

  /**
   * Returns an attrRow which is in the same month as the given date or null. If there are multiple attrRows in the
   * list, it will return the first one.
   *
   * @param entity The entity with timeable attrRows.
   * @param groupName The group name.
   * @param dateToSelectAttrRow The date to find a row for in the same month.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   *
   * @return The attrRow or null.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  T getAttrRowForSameMonth(final EntityWithTimeableAttr<PK, T> entity, final String groupName,
      final Date dateToSelectAttrRow);

  /**
   * Returns the attrRows belonging to the given group.
   *
   * @param entity The entity with timeable attrRows
   * @param group The group to find attrRows for.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   *
   * @return The attrRows belonging to the given group.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> getTimeableAttrRowsForGroup(final EntityWithTimeableAttr<PK, T> entity, final AttrGroup group);

  /**
   * Returns the attrRows belonging to the given group name.
   *
   * @param entity The entity with timeable attrRows
   * @param groupName The group name to find attrRows for.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The attrRows belonging to the given group.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> getTimeableAttrRowsForGroupName(final EntityWithTimeableAttr<PK, T> entity, final String groupName);

  /**
   * Sorts the attrRows by start time descending, if the start time of a row is null, this row will the first element of
   * the list.
   *
   * @param attrRows The TimeableAttrRows to sort.
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return The sorted TimeableAttrRow.
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<T> sortTimeableAttrRowsByDateDescending(List<T> attrRows);

  /**
   * Get a set of available years in timeable attr data.
   *
   * @param entityList The list of entities with timeable attrRows
   * @param <PK> the type of he pk
   * @param <T> the type of the entity
   * 
   * @return the list of available years
   */
  <PK extends Serializable, T extends TimeableAttrRow<PK>>
  List<Integer> getAvailableStartTimeYears(final List<? extends EntityWithTimeableAttr<PK, T>> entityList);
}
