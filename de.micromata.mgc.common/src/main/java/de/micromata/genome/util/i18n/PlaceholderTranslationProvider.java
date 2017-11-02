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

package de.micromata.genome.util.i18n;

import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

import de.micromata.genome.util.text.PlaceHolderReplacer;
import de.micromata.genome.util.text.StringResolver;
import org.apache.commons.lang3.StringUtils;

/**
 * Evaluate I{} sub expression and translates them.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class PlaceholderTranslationProvider extends TranslationProviderWrapper
{
  public PlaceholderTranslationProvider(I18NTranslationProvider nested)
  {
    super(nested);

  }

  @Override
  public Object getTranslationForKey(String key)
  {
    Object value = super.getTranslationForKey(key);
    if ((value instanceof String) == false) {
      return value;
    }
    String svalue = (String) value;
    String resolved = PlaceHolderReplacer.resolveReplace(svalue, "I{", "}", new StringResolver()
    {

      @Override
      public String resolve(String placeholder)
      {
        return Objects.toString(getTranslationForKey(placeholder), StringUtils.EMPTY);
      }
    });
    return resolved;
  }
}
