package de.micromata.genome.logging.loghtmlwindow;

import java.util.Date;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import de.micromata.genome.logging.LogAttribute;
import de.micromata.genome.logging.LogEntry;
import de.micromata.genome.logging.LogWriteEntry;
import de.micromata.genome.logging.Logging;
import de.micromata.genome.util.types.DateUtils;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LogJsonUtils
{
  public static JsonObject logEntryToJson(LogWriteEntry le)
  {
    JsonObject ret = new JsonObject();
    String date = DateUtils.getStandardDateTimeFormat().format(new Date(le.getTimestamp()));
    ret.set("logTime", date);
    ret.set("logTimestamp", le.getTimestamp());
    ret.set("logLevel", le.getLevel().name());
    ret.set("logCategory", le.getCategory());
    ret.set("logMessage", le.getMessage());
    ret.set("logAttributes", logEntryAttributes(le.getAttributes()));
    ret.set("allAttrs", true);
    return ret;
  }

  public static JsonObject logEntryToJson(Logging logging, LogEntry le, boolean allAttrs)
  {
    JsonObject ret = new JsonObject();
    String date = DateUtils.getStandardDateTimeFormat().format(new Date(le.getTimestamp()));
    ret.set("id", logging.formatLogId(le.getLogEntryIndex()));
    ret.set("logTime", date);
    ret.set("logTimestamp", le.getTimestamp());
    ret.set("logLevel", le.getLevelName());
    ret.set("logCategory", le.getCategory());
    ret.set("logMessage", le.getMessage());
    if (allAttrs == true) {
      ret.set("logAttributes", logEntryAttributes(le.getAttributes()));
    } else {
      ret.set("logAttributes", new JsonArray());
    }
    ret.set("allAttrs", allAttrs);

    return ret;
  }

  public static JsonArray logEntryAttributes(List<LogAttribute> les)
  {
    JsonArray ret = new JsonArray();
    for (LogAttribute le : les) {
      JsonObject jla = new JsonObject();
      jla.set("typeName", le.getTypeName());
      jla.set("value", le.getValue());
      ret.add(jla);
    }
    return ret;
  }
}