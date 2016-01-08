package de.micromata.genome.jpa.metainf;

/**
 * The Class EmgrDbElementBean.
 * 
 * equals/hashCode is the underlying javaType.
 *
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 */
public class EmgrDbElementBean implements EmgrDbElement
{

  /**
   * The java type.
   */
  private Class<?> javaType;

  /**
   * The database name.
   */
  private String databaseName;

  @Override
  public int hashCode()
  {
    return javaType.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if ((obj instanceof EmgrDbElementBean) == false) {
      return false;
    }
    return javaType.equals(((EmgrDbElementBean) obj).javaType);
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String toString()
  {

    return javaType == null ? super.toString() : javaType.getName();
  }

  @Override
  public Class<?> getJavaType()
  {
    return javaType;
  }

  /**
   * Sets the java type.
   *
   * @param javaType the new java type
   */
  public void setJavaType(Class<?> javaType)
  {
    this.javaType = javaType;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public String getDatabaseName()
  {
    return databaseName;
  }

  /**
   * Sets the database name.
   *
   * @param databaseName the new database name
   */
  public void setDatabaseName(String databaseName)
  {
    this.databaseName = databaseName;
  }

}
