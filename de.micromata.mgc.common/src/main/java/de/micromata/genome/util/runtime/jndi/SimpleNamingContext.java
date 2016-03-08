package de.micromata.genome.util.runtime.jndi;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple implementation for the naming context.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SimpleNamingContext implements Context
{

  private final Log logger = LogFactory.getLog(getClass());

  private final String root;

  private final Hashtable<String, Object> boundObjects;

  private final Hashtable<String, Object> environment = new Hashtable<String, Object>();

  /**
   * Create a new naming context.
   */
  public SimpleNamingContext()
  {
    this("");
  }

  /**
   * Create a new naming context with the given naming root.
   */
  public SimpleNamingContext(String root)
  {
    this.root = root;
    this.boundObjects = new Hashtable<String, Object>();
  }

  /**
   * Create a new naming context with the given naming root, the given name/object map, and the JNDI environment
   * entries.
   */
  public SimpleNamingContext(String root, Hashtable<String, Object> boundObjects, Hashtable<String, Object> env)
  {
    this.root = root;
    this.boundObjects = boundObjects;
    if (env != null) {
      this.environment.putAll(env);
    }
  }

  // Actual implementations of Context methods follow

  @Override
  public NamingEnumeration<NameClassPair> list(String root) throws NamingException
  {
    if (logger.isDebugEnabled()) {
      logger.debug("Listing name/class pairs under [" + root + "]");
    }
    return new NameClassPairEnumeration(this, root);
  }

  @Override
  public NamingEnumeration<Binding> listBindings(String root) throws NamingException
  {
    if (logger.isDebugEnabled()) {
      logger.debug("Listing bindings under [" + root + "]");
    }
    return new BindingEnumeration(this, root);
  }

  /**
   * Look up the object with the given name.
   * <p>
   * Note: Not intended for direct use by applications. Will be used by any standard InitialContext JNDI lookups.
   * 
   * @throws javax.naming.NameNotFoundException if the object could not be found
   */
  @Override
  public Object lookup(String lookupName) throws NameNotFoundException
  {
    String name = this.root + lookupName;
    if (logger.isDebugEnabled()) {
      logger.debug("Static JNDI lookup: [" + name + "]");
    }
    if ("".equals(name)) {
      return new SimpleNamingContext(this.root, this.boundObjects, this.environment);
    }
    Object found = this.boundObjects.get(name);
    if (found == null) {
      if (!name.endsWith("/")) {
        name = name + "/";
      }
      for (String boundName : this.boundObjects.keySet()) {
        if (boundName.startsWith(name)) {
          return new SimpleNamingContext(name, this.boundObjects, this.environment);
        }
      }
      throw new NameNotFoundException(
          "Name [" + this.root + lookupName + "] not bound; " + this.boundObjects.size() + " bindings: [" +
              collectionToDelimitedString(this.boundObjects.keySet(), ",") + "]");
    }
    return found;
  }

  /**
   * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
   * <p>
   * Useful for {@code toString()} implementations.
   * 
   * @param coll the {@code Collection} to convert
   * @param delim the delimiter to use (typically a ",")
   * @return the delimited {@code String}
   */
  public static String collectionToDelimitedString(Collection<?> coll, String delim)
  {
    return collectionToDelimitedString(coll, delim, "", "");
  }

  /**
   * Convert a {@link Collection} to a delimited {@code String} (e.g. CSV).
   * <p>
   * Useful for {@code toString()} implementations.
   * 
   * @param coll the {@code Collection} to convert
   * @param delim the delimiter to use (typically a ",")
   * @param prefix the {@code String} to start each element with
   * @param suffix the {@code String} to end each element with
   * @return the delimited {@code String}
   */
  public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix)
  {
    if (CollectionUtils.isEmpty(coll)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    Iterator<?> it = coll.iterator();
    while (it.hasNext()) {
      sb.append(prefix).append(it.next()).append(suffix);
      if (it.hasNext()) {
        sb.append(delim);
      }
    }
    return sb.toString();
  }

  @Override
  public Object lookupLink(String name) throws NameNotFoundException
  {
    return lookup(name);
  }

  /**
   * Bind the given object to the given name. Note: Not intended for direct use by applications if setting up a
   * JVM-level JNDI environment. Use SimpleNamingContextBuilder to set up JNDI bindings then.
   * 
   * @see de.micromata.genome.util.runtime.jndi.springframework.mock.jndi.SimpleNamingContextBuilder#bind
   */
  @Override
  public void bind(String name, Object obj)
  {
    if (logger.isInfoEnabled()) {
      logger.info("Static JNDI binding: [" + this.root + name + "] = ["
          + SimpleNamingContextBuilder.jndiObjectToString(obj) + "]");
    }
    if (obj == null) {
      throw new IllegalArgumentException("Cannot bind null object to JNDI: " + this.root + name);
    }
    this.boundObjects.put(this.root + name, obj);
  }

  @Override
  public void unbind(String name)
  {
    if (logger.isInfoEnabled()) {
      logger.info("Static JNDI remove: [" + this.root + name + "]");
    }
    this.boundObjects.remove(this.root + name);
  }

  @Override
  public void rebind(String name, Object obj)
  {
    bind(name, obj);
  }

  @Override
  public void rename(String oldName, String newName) throws NameNotFoundException
  {
    Object obj = lookup(oldName);
    unbind(oldName);
    bind(newName, obj);
  }

  @Override
  public Context createSubcontext(String name)
  {
    String subcontextName = this.root + name;
    if (!subcontextName.endsWith("/")) {
      subcontextName += "/";
    }
    Context subcontext = new SimpleNamingContext(subcontextName, this.boundObjects, this.environment);
    bind(name, subcontext);
    return subcontext;
  }

  @Override
  public void destroySubcontext(String name)
  {
    unbind(name);
  }

  @Override
  public String composeName(String name, String prefix)
  {
    return prefix + name;
  }

  @Override
  public Hashtable<String, Object> getEnvironment()
  {
    return this.environment;
  }

  @Override
  public Object addToEnvironment(String propName, Object propVal)
  {
    return this.environment.put(propName, propVal);
  }

  @Override
  public Object removeFromEnvironment(String propName)
  {
    return this.environment.remove(propName);
  }

  @Override
  public void close()
  {
  }

  public String nameToString(Name name)
  {
    StringBuilder sb = new StringBuilder();
    Enumeration<String> en = name.getAll();
    while (en.hasMoreElements()) {
      String part = en.nextElement();
      if (sb.length() > 0) {
        sb.append('/');
      }
      sb.append(part);
    }
    return sb.toString();
  }
  // Unsupported methods follow: no support for javax.naming.Name

  @Override
  public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
  {
    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
  {
    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public Object lookup(Name name) throws NamingException
  {
    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public Object lookupLink(Name name) throws NamingException
  {
    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public void bind(Name name, Object obj) throws NamingException
  {
    bind(nameToString(name), obj);
  }

  @Override
  public void unbind(Name name) throws NamingException
  {
    unbind(nameToString(name));
  }

  @Override
  public void rebind(Name name, Object obj) throws NamingException
  {
    rebind(nameToString(name), obj);
  }

  @Override
  public void rename(Name oldName, Name newName) throws NamingException
  {
    rename(nameToString(oldName), nameToString(newName));
  }

  @Override
  public Context createSubcontext(Name name) throws NamingException
  {
    return createSubcontext(nameToString(name));
  }

  @Override
  public void destroySubcontext(Name name) throws NamingException
  {
    destroySubcontext(nameToString(name));
  }

  @Override
  public String getNameInNamespace() throws NamingException
  {

    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public NameParser getNameParser(Name name) throws NamingException
  {

    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public NameParser getNameParser(String name) throws NamingException
  {
    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  @Override
  public Name composeName(Name name, Name prefix) throws NamingException
  {
    throw new OperationNotSupportedException("SimpleNamingContext does not support [javax.naming.Name]");
  }

  private static abstract class AbstractNamingEnumeration<T> implements NamingEnumeration<T>
  {

    private Iterator<T> iterator;

    private AbstractNamingEnumeration(SimpleNamingContext context, String proot) throws NamingException
    {
      if (!"".equals(proot) && !proot.endsWith("/")) {
        proot = proot + "/";
      }
      String root = context.root + proot;
      Map<String, T> contents = new HashMap<String, T>();
      for (String boundName : context.boundObjects.keySet()) {
        if (boundName.startsWith(root)) {
          int startIndex = root.length();
          int endIndex = boundName.indexOf('/', startIndex);
          String strippedName = (endIndex != -1 ? boundName.substring(startIndex, endIndex)
              : boundName.substring(startIndex));
          if (!contents.containsKey(strippedName)) {
            try {
              contents.put(strippedName, createObject(strippedName, context.lookup(proot + strippedName)));
            } catch (NameNotFoundException ex) {
              // cannot happen
            }
          }
        }
      }
      if (contents.size() == 0) {
        throw new NamingException("Invalid root: [" + context.root + proot + "]");
      }
      this.iterator = contents.values().iterator();
    }

    protected abstract T createObject(String strippedName, Object obj);

    @Override
    public boolean hasMore()
    {
      return this.iterator.hasNext();
    }

    @Override
    public T next()
    {
      return this.iterator.next();
    }

    @Override
    public boolean hasMoreElements()
    {
      return this.iterator.hasNext();
    }

    @Override
    public T nextElement()
    {
      return this.iterator.next();
    }

    @Override
    public void close()
    {
    }
  }

  private static class NameClassPairEnumeration extends AbstractNamingEnumeration<NameClassPair>
  {

    private NameClassPairEnumeration(SimpleNamingContext context, String root) throws NamingException
    {
      super(context, root);
    }

    @Override
    protected NameClassPair createObject(String strippedName, Object obj)
    {
      return new NameClassPair(strippedName, obj.getClass().getName());
    }
  }

  private static class BindingEnumeration extends AbstractNamingEnumeration<Binding>
  {

    private BindingEnumeration(SimpleNamingContext context, String root) throws NamingException
    {
      super(context, root);
    }

    @Override
    protected Binding createObject(String strippedName, Object obj)
    {
      return new Binding(strippedName, obj);
    }
  }

}
