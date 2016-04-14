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

package de.micromata.mgc.application.webserver.config;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import de.micromata.genome.util.runtime.LocalSettings;
import de.micromata.genome.util.runtime.config.ALocalSettingsPath;
import de.micromata.genome.util.runtime.config.AbstractCompositLocalSettingsConfigModel;
import de.micromata.genome.util.runtime.config.LocalSettingsWriter;
import de.micromata.genome.util.validation.ValContext;

/**
 * Represents a jetty configuration.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class JettyConfigModel extends AbstractCompositLocalSettingsConfigModel
{
  /**
   * prefix for the localsetings
   */
  private String prefix;

  @ALocalSettingsPath(defaultValue = "8080", comment = "Port the server listened")
  private String port;
  @ALocalSettingsPath(defaultValue = "/genome",
      comment = "subpath the application will running (part of the url)")
  private String contextpath;

  @ALocalSettingsPath(defaultValue = "http://localhost:8080/genome",
      comment = "Url the application is reachable")
  private String publicUrl;
  @ALocalSettingsPath(defaultValue = "3600", comment = "Session timout in seconds")
  private String sessionTimeout;
  @ALocalSettingsPath(key = "jmx.enabled", defaultValue = "false",
      comment = "If enabled JMX will be enabled")
  private String serverEnableJmx;

  @ALocalSettingsPath(key = "requestlogging.enabled", defaultValue = "false",
      comment = "If enabled all request will be logged into local filesystem")
  private String serverRequestLoggingEnabled;

  @ALocalSettingsPath(defaultValue = "false", comment = "Use server with HTTPS")
  private String sslEnabled;

  @ALocalSettingsPath(comment = "Port number for HTTPS")
  private String sslPort;
  @ALocalSettingsPath(comment = "Use only HTTPS (no HTTP)")
  private String sslOnly;

  @ALocalSettingsPath(comment = "Location of your SSL Keystore")
  private String sslKeystorePath;
  @ALocalSettingsPath(comment = "Password for the SSL Keystore")
  private String sslKeystorePassword;
  @ALocalSettingsPath(comment = "Password for the SSL Keystore")
  private String sslKeyManagerPassword;
  @ALocalSettingsPath(comment = "Path to trust store")
  private String trustStorePath;
  @ALocalSettingsPath(comment = "Password trust store")
  private String trustStorePassword;

  @ALocalSettingsPath(comment = "Alias used from inside the key store")
  private String sslCertAlias;

  public JettyConfigModel()
  {
    this("genome.jetty");
  }

  public JettyConfigModel(String prefix)
  {
    this.prefix = prefix;
  }

  @Override
  public String getKeyPrefix()
  {
    return prefix;
  }

  @Override
  public void validate(ValContext valContext)
  {
    ValContext ctx = valContext.createSubContext(this, "");

    if (StringUtils.isBlank(port) == true) {
      ctx.directError("serverPort", "Please provide a server port");
    } else {
      if (NumberUtils.isDigits(port) == false) {
        ctx.directError("serverPort", "Please provide numeric port number");
      }
    }
    if (isInt(sessionTimeout) == false) {
      ctx.directError("serverPort", "Please provide a numeric value for the session timeout");
    }
    if (isSslEnabled() == false) {
      return;
    }
    if (StringUtils.isBlank(sslPort) == true) {
      ctx.directError("sslPort", "Please provide a server port");
    } else {
      if (NumberUtils.isDigits(port) == false) {
        ctx.directError("sslPort", "Please provid numeric port number");
      }
    }
    if (StringUtils.isBlank(sslKeystorePath) == true) {
      ctx.directError("sslKeystorePath", "Please provide a keystore file");
    } else {
      File keystorefile = new File(sslKeystorePath);
      if (keystorefile.exists() == false) {
        ctx.directError("sslKeystorePath", "Cannot find keystore: " + keystorefile.getAbsolutePath());
      }
    }
    if (StringUtils.isBlank(trustStorePath) == true) {
      ctx.directError("trustStorePath", "Please provide a trust store file");
    } else {
      File keystorefile = new File(trustStorePath);
      if (keystorefile.exists() == false) {
        ctx.directError("trustStorePath", "Cannot find trust store: " + keystorefile.getAbsolutePath());
      }
    }
  }

  @Override
  public void fromLocalSettings(LocalSettings localSettings)
  {
    String cfgpuburl = localSettings.get("cfg.public.url");
    if (StringUtils.isBlank(localSettings.get(buildKey("publicUrl"))) == true &&
        StringUtils.isBlank(cfgpuburl) == false) {
      localSettings.getMap().put(buildKey("publicUrl"), cfgpuburl);
    }
    super.fromLocalSettings(localSettings);

  }

  @Override
  public LocalSettingsWriter toProperties(LocalSettingsWriter writer)
  {
    LocalSettingsWriter ret = super.toProperties(writer);
    ret.put("cfg.public.url", publicUrl, "Alias to public url");
    return ret;
  }

  public String getPort()
  {
    return port;
  }

  public int getPortAsInt()
  {
    return Integer.parseInt(port);
  }

  public void setPort(String serverPort)
  {
    this.port = serverPort;
  }

  public String getContextPath()
  {
    return contextpath;
  }

  public void setContextPath(String serverContextPath)
  {
    this.contextpath = serverContextPath;
  }

  public String getPublicUrl()
  {
    return publicUrl;
  }

  public void setPublicUrl(String publicUrl)
  {
    this.publicUrl = publicUrl;
  }

  public int getSessionTimeoutAsInt()
  {
    return asInt(sessionTimeout);
  }

  public boolean isServerEnableJmx()
  {
    return "true".equals(getServerEnableJmx());
  }

  public String getServerEnableJmx()
  {
    return serverEnableJmx;
  }

  public void setServerEnableJmx(String serverEnableJmx)
  {
    this.serverEnableJmx = serverEnableJmx;
  }

  public void setServerEnableJmx(boolean serverEnableJmx)
  {
    this.serverEnableJmx = Boolean.toString(serverEnableJmx);
  }

  public String getServerRequestLoggingEnabled()
  {
    return serverRequestLoggingEnabled;
  }

  public void setServerRequestLoggingEnabled(String serverRequestLoggingEnabled)
  {
    this.serverRequestLoggingEnabled = serverRequestLoggingEnabled;
  }

  public void setServerRequestLoggingEnabled(boolean serverRequestLoggingEnabled)
  {
    this.serverRequestLoggingEnabled = Boolean.toString(serverRequestLoggingEnabled);
  }

  public boolean isServerRequestLoggingEnabled()
  {
    return "true".equals(serverRequestLoggingEnabled);
  }

  public String getSslEnabled()
  {
    return sslEnabled;
  }

  public boolean isSslEnabled()
  {
    return "true".equals(sslEnabled);
  }

  public void setSslEnabled(String sslEnabled)
  {
    this.sslEnabled = sslEnabled;
  }

  public void setSslEnabled(boolean sslEnabled)
  {
    this.sslEnabled = Boolean.toString(sslEnabled);
  }

  public String getSslKeystorePath()
  {
    return sslKeystorePath;
  }

  public void setSslKeystorePath(String sslKeystorePath)
  {
    this.sslKeystorePath = sslKeystorePath;
  }

  public String getSslKeystorePassword()
  {
    return sslKeystorePassword;
  }

  public void setSslKeystorePassword(String sslKeystorePassword)
  {
    this.sslKeystorePassword = sslKeystorePassword;
  }

  public String getSslKeyManagerPassword()
  {
    return sslKeyManagerPassword;
  }

  public void setSslKeyManagerPassword(String sslKeyManagerPassword)
  {
    this.sslKeyManagerPassword = sslKeyManagerPassword;
  }

  public String getSslPort()
  {
    return sslPort;
  }

  public void setSslPort(String sslPort)
  {
    this.sslPort = sslPort;
  }

  public int getSslPortAsInt()
  {
    return Integer.parseInt(sslPort);
  }

  public String getSslCertAlias()
  {
    return sslCertAlias;
  }

  public void setSslCertAlias(String sslCertAlias)
  {
    this.sslCertAlias = sslCertAlias;
  }

  public String getContextpath()
  {
    return contextpath;
  }

  public void setContextpath(String contextpath)
  {
    this.contextpath = contextpath;
  }

  public String getTrustStorePath()
  {
    return trustStorePath;
  }

  public void setTrustStorePath(String trustStore)
  {
    this.trustStorePath = trustStore;
  }

  public String getTrustStorePassword()
  {
    return trustStorePassword;
  }

  public void setTrustStorePassword(String trustStorePassword)
  {
    this.trustStorePassword = trustStorePassword;
  }

  public String getSslOnly()
  {
    return sslOnly;
  }

  public boolean isSslOnly()
  {
    return "true".equals(sslOnly);
  }

  public void setSslOnly(String sslOnly)
  {
    this.sslOnly = sslOnly;
  }

  public void setSslOnly(boolean sslOnly)
  {
    this.sslOnly = Boolean.toString(sslOnly);
  }

}
