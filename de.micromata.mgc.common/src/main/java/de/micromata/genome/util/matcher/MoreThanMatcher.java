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

package de.micromata.genome.util.matcher;

/**
 * matcher.match(object) returns true if matcher.object &gt; arg
 *
 * @author roger@micromata.de
 * @param <T> the generic type
 */
public class MoreThanMatcher<T extends Comparable<T>>extends ComparatorMatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 4511221781327480260L;

  /**
   * Instantiates a new more than matcher.
   */
  public MoreThanMatcher()
  {
  }

  /**
   * Instantiates a new more than matcher.
   *
   * @param other the other
   */
  public MoreThanMatcher(T other)
  {
    super(other);
  }

  @Override
  public boolean match(T object)
  {
    if (other == null || object == null) {
      return false;
    }
    return other.compareTo(object) < 0;
  }

  @Override
  public String toString()
  {
    return "(" + other.toString() + " > EXPR)";
  }

}
