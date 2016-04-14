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

package de.micromata.genome.util.matcher.string;

import org.apache.commons.lang.StringUtils;

/**
 * The Class ContainsIgnoreCaseMatcher.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 * @param <T> the generic type
 */
public class ContainsIgnoreCaseMatcher<T>extends StringPatternMatcherBase<T>
{

  /**
   * Instantiates a new contains ignore case matcher.
   */
  public ContainsIgnoreCaseMatcher()
  {

  }

  /**
   * Instantiates a new contains ignore case matcher.
   *
   * @param pattern the pattern
   */
  public ContainsIgnoreCaseMatcher(String pattern)
  {
    super(pattern);
  }

  @Override
  public boolean matchString(String token)
  {
    return StringUtils.containsIgnoreCase(token, pattern);
  }

  @Override
  public String toString()
  {
    return "<EXPR>.containsIgnoreCase(" + pattern + ")";
  }
}
