package de.micromata.genome.util.validation;

import java.util.List;

/**
 * Delegates methods to a parent context.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class ValContextDelegate extends ValContext
{

  /**
   * The parent.
   */
  protected ValContext parent;

  @Override
  public ValContext createSubContext(Object reference, String property)
  {
    return parent.createSubContext(reference, property);
  }

  /**
   * Instantiates a new val context delegate.
   *
   * @param parent the parent
   */
  public ValContextDelegate(ValContext parent)
  {
    this.parent = parent;
  }

  @Override
  public void error(String i18nkey)
  {
    parent.error(i18nkey);
  }

  @Override
  public void error(String property, String i18nkey)
  {
    parent.error(property, i18nkey);
  }

  @Override
  public void add(ValState valState, String property, String i18nkey)
  {
    parent.add(valState, property, i18nkey);
  }

  @Override
  public void addMessage(ValMessage vm)
  {
    parent.addMessage(vm);
  }

  @Override
  public List<ValMessage> getMessages()
  {
    return parent.getMessages();
  }

  @Override
  public boolean hasMessages()
  {
    return parent.hasMessages();
  }

  @Override
  public int hashCode()
  {
    return parent.hashCode();
  }

  @Override
  public boolean messageExitsForElement(ValMessage message)
  {
    return parent.messageExitsForElement(message);
  }

  @Override
  public String toString()
  {
    return parent.toString();
  }

  @Override
  public boolean equals(Object obj)
  {
    return parent.equals(obj);
  }

  @Override
  public void error(String property, String i18nkey, Exception ex)
  {
    parent.error(property, i18nkey, ex);
  }

  @Override
  public void warn(String i18nkey)
  {
    parent.warn(i18nkey);
  }

  @Override
  public void warn(String property, String i18nkey)
  {
    parent.warn(property, i18nkey);
  }

  @Override
  public void info(String i18nkey)
  {
    parent.info(i18nkey);
  }

  @Override
  public void info(String property, String i18nkey)
  {
    parent.info(property, i18nkey);
  }

  @Override
  public void add(ValState valState, String property, String i18nkey, Exception ex)
  {
    parent.add(valState, property, i18nkey, ex);
  }

  @Override
  public boolean hasErrors()
  {
    return parent.hasErrors();
  }

  @Override
  public boolean hasLocalError()
  {
    return parent.hasLocalError();
  }

}
