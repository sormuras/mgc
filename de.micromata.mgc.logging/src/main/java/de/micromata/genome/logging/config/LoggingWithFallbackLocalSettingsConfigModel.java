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

package de.micromata.genome.logging.config;

import org.apache.commons.lang3.StringUtils;

import de.micromata.genome.logging.Logging;
import de.micromata.genome.logging.LoggingWithFallback;
import de.micromata.genome.logging.spi.BaseLoggingLocalSettingsConfigModel;
import de.micromata.genome.logging.spi.log4j.Log4JLogging;
import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;

/**
 * configure FallbackLogging
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public abstract class LoggingWithFallbackLocalSettingsConfigModel extends BaseLoggingLocalSettingsConfigModel
{

  private String fallbackTypeId;

  private LsLoggingLocalSettingsConfigModel fallbackConfig;

  protected abstract LoggingWithFallback createFallbackLogging();

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    super.fromLocalSettings(localSettings);
    String fallbackId = localSettings.get(buildKey("fallback.typeId"));
    if (StringUtils.isNotBlank(fallbackId) == true) {
      fallbackConfig = new LsLoggingLocalSettingsConfigModel(getKeyPrefix() + "fallback.");
      fallbackConfig.fromLocalSettings(localSettings);
    }
  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    LocalSettingsWriter ret = super.toProperties(writer);
    if (fallbackConfig != null) {
      fallbackConfig.toProperties(ret);
    }
    return ret;
  }

  @Override
  public Logging createLogging()
  {
    LoggingWithFallback fallbackLogging = createFallbackLogging();
    if (fallbackConfig != null) {
      Logging fallback = fallbackConfig.createLogging();
      if (fallback instanceof LsLoggingImpl) {
        fallback = ((LsLoggingImpl) fallback).getTarget();
      }
      if (fallback instanceof Log4JLogging) {
        ((Log4JLogging) fallback).setLog4jCategoryPrefix(Log4JLogging.LOG4J_FALLBACK_PREFIX);
      }
      fallbackLogging.setSecondary(fallback);
    }
    return fallbackLogging;
  }

  public String getFallbackTypeId()
  {
    return fallbackTypeId;
  }

  public void setFallbackTypeId(String fallbackTypeId)
  {
    this.fallbackTypeId = fallbackTypeId;
  }

  public LsLoggingLocalSettingsConfigModel getFallbackConfig()
  {
    return fallbackConfig;
  }

  public void setFallbackConfig(LsLoggingLocalSettingsConfigModel fallbackConfig)
  {
    this.fallbackConfig = fallbackConfig;
  }

}
