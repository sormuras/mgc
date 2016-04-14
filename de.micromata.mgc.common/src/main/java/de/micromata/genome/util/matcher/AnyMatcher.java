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
 * matches if at least one given matcher matches.
 *
 * @param <T> the generic type
 */
public class AnyMatcher<T>extends MatcherBase<T>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 4055207023527099498L;

  /**
   * The matchers.
   */
  private Iterable<Matcher<? super T>> matchers;

  /**
   * Instantiates a new any matcher.
   *
   * @param matchers the matchers
   */
  public AnyMatcher(Iterable<Matcher<? super T>> matchers)
  {
    this.matchers = matchers;
  }

  @Override
  public boolean match(T object)
  {
    for (Matcher<? super T> m : matchers) {
      if (m.match(object) == true) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("anyof(");
    boolean isFirst = true;
    for (Matcher<? super T> m : matchers) {
      if (isFirst == false) {
        sb.append(", ");
      }
      isFirst = false;
      sb.append(m);
    }
    sb.append(")");
    return sb.toString();
  }

}
