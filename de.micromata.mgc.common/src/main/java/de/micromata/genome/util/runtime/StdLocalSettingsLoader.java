package de.micromata.genome.util.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.micromata.genome.util.collections.OrderedProperties;

/**
 * Standard implementation to load local-settings.properties.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class StdLocalSettingsLoader implements LocalSettingsLoader
{
  /**
   * Factory method to create and initialize local settings.
   */
  public static Function<LocalSettingsLoader, LocalSettings> defaultLocalSettingsFactory = (
      loader) -> new LocalSettings(loader);
  /**
   * The Constant log.
   */
  private static final Logger LOG = Logger.getLogger(LocalSettings.class);

  private List<String> warns = new ArrayList<>();
  private List<File> loadedFiles = new ArrayList<>();

  protected String localSettingsPrefixName = "local-settings";
  protected String localSettingsFileName;

  protected File workingDirectory = new File(".");

  Function<LocalSettingsLoader, LocalSettings> localSettingsFactory;

  public StdLocalSettingsLoader()
  {

  }

  public StdLocalSettingsLoader(String localSettingsFileName,
      Function<LocalSettingsLoader, LocalSettings> localSettingsFactory, String prefix)
  {
    this.localSettingsFileName = localSettingsFileName;
    this.localSettingsFactory = localSettingsFactory;
    if (prefix != null) {
      this.localSettingsPrefixName = prefix;
    }
  }

  @Override
  public String getLocalSettingsFileName()
  {
    if (StringUtils.isEmpty(localSettingsFileName) == false) {
      return localSettingsFileName;
    }
    localSettingsFileName = System.getProperty("localsettings");
    if (StringUtils.isEmpty(localSettingsFileName) == true) {
      localSettingsFileName = getLocalSettingsPrefixName() + ".properties";
    }
    return localSettingsFileName;
  }

  @Override
  public boolean localSettingsExists()
  {
    File lsFile = new File(getLocalSettingsFileName());
    return lsFile.exists();
  }

  @Override
  public LocalSettings loadSettings()
  {
    LocalSettings ls = newLocalSettings();
    getLocalSettingsFileName();
    loadSettingsImpl(ls);
    return ls;
  }

  protected LocalSettings newLocalSettings()
  {
    if (localSettingsFactory != null) {
      return localSettingsFactory.apply(this);
    }
    return defaultLocalSettingsFactory.apply(this);
  }

  public void loadSettingsImpl(LocalSettings ls)
  {
    //log.info("Loading localSettingsfile: " + new File(localSettingsFile).getAbsolutePath());
    loadSettings(ls, getLocalSettingsFile(), ls.getMap(), true, true);
    loadOptionalDev(ls);
    loadSystemEnv(ls);
    loadSystemProperties(ls);
  }

  protected void loadOptionalDev(LocalSettings ls)
  {
    loadSettings(ls, new File(getWorkingDirectory(), localSettingsPrefixName + "-dev.properties"), ls.getMap(), false,
        false);
  }

  protected void loadSystemEnv(LocalSettings ls)
  {
    Map<String, String> envmap = System.getenv();

    for (Map.Entry<String, String> me : envmap.entrySet()) {
      ls.getMap().put(me.getKey(), me.getValue());
    }
  }

  protected void loadSystemProperties(LocalSettings ls)
  {
    Properties props = System.getProperties();
    for (Object k : props.keySet()) {
      String key = (String) k;
      ls.getMap().put(key, props.getProperty(key));
    }
  }

  @Override
  public boolean loadSettings(LocalSettings ls, File localSettingsFile, Map<String, String> target,
      boolean originalLocalSettingsFile,
      boolean warn)
  {
    if (localSettingsFile.exists() == false) {
      if (warn == true) {
        warns.add("Cannot find localsettings file: " + localSettingsFile.getAbsolutePath());
      }
      return false;
    }
    if (LOG.isDebugEnabled() == true) {
      LOG.debug("Load localsettings: " + localSettingsFile.getAbsolutePath());
    }
    FileInputStream fin = null;
    try {
      OrderedProperties props = newProperties(originalLocalSettingsFile);
      fin = new FileInputStream(localSettingsFile);
      props.load(fin, new LocalSettingsIncludeReplacer(ls, localSettingsFile.getAbsoluteFile().getParentFile()));
      for (String k : props.keySet()) {
        target.put(k, props.get(k));
      }
    } catch (IOException ex) {
      throw new RuntimeIOException(ex);
    } finally {
      IOUtils.closeQuietly(fin);
    }
    loadedFiles.add(localSettingsFile);
    return true;
  }

  protected OrderedProperties newProperties(boolean originalLocalSettingsFile)
  {
    return new OrderedProperties();
  }

  @Override
  public File getWorkingDirectory()
  {
    return workingDirectory;
  }

  public void setWorkingDirectory(File woringDirectory)
  {
    this.workingDirectory = woringDirectory;
  }

  @Override
  public List<String> getWarns()
  {
    return warns;
  }

  @Override
  public List<File> getLoadedFiles()
  {
    return loadedFiles;
  }

  public String getLocalSettingsPrefixName()
  {
    return localSettingsPrefixName;
  }

  public void setLocalSettingsPrefixName(String localSettingsPrefixName)
  {
    this.localSettingsPrefixName = localSettingsPrefixName;
  }

  public void setLocalSettingsFileName(String localSettingsFileName)
  {
    this.localSettingsFileName = localSettingsFileName;
  }

}