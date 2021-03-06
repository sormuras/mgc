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
 * Matcher base with a left and right matcher.
 *
 * @author roger
 * @param <T> the generic type
 */
public abstract class LeftRightMatcherBase<T>extends MatcherBase<T>
{

  /**
   * The left matcher.
   */
  protected Matcher<T> leftMatcher;

  /**
   * The right matcher.
   */
  protected Matcher<T> rightMatcher;

  /**
   * Instantiates a new left right matcher base.
   */
  public LeftRightMatcherBase()
  {

  }

  /**
   * Instantiates a new left right matcher base.
   *
   * @param leftMatcher the left matcher
   * @param rightMatcher the right matcher
   */
  public LeftRightMatcherBase(Matcher<T> leftMatcher, Matcher<T> rightMatcher)
  {
    this.leftMatcher = leftMatcher;
    this.rightMatcher = rightMatcher;
  }

  public Matcher<T> getLeftMatcher()
  {
    return leftMatcher;
  }

  public void setLeftMatcher(Matcher<T> leftMatcher)
  {
    this.leftMatcher = leftMatcher;
  }

  public Matcher<T> getRightMatcher()
  {
    return rightMatcher;
  }

  public void setRightMatcher(Matcher<T> rightMatcher)
  {
    this.rightMatcher = rightMatcher;
  }

}
