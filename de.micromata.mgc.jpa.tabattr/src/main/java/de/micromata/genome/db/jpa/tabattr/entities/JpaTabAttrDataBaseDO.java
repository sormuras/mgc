/////////////////////////////////////////////////////////////////////////////
//
// Project   Micromata Genome 
//
// Author    r.kommer.extern@micromata.de
// Created   18.02.2013
// Copyright Micromata 2013
//
/////////////////////////////////////////////////////////////////////////////

package de.micromata.genome.db.jpa.tabattr.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import de.micromata.genome.jpa.StdRecordDO;

/**
 * Abstract JPA Data table.
 *
 * @author roger
 * @param <A> the generic type
 */
@MappedSuperclass
public abstract class JpaTabAttrDataBaseDO<A extends JpaTabAttrBaseDO<?, ?>, PK extends Serializable>
    extends StdRecordDO<PK>
{

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -1040673599827915302L;

  /**
   * Maximum string length fitting into the datarow columns.
   * 
   * NOTE: If you chance this, you have also change the jpa annotation.
   * 
   */
  public static final int DATA_MAXLENGTH = 2990;

  /**
   * Link to parent.
   * 
   * Annotate the getter in the derived class with proper ManyToOne annotation
   */
  A parent;

  /**
   * Row number of splittet datas.
   */
  private int datarow;

  /**
   * The string encoded data.
   */
  private String data;

  /**
   * Instantiates a new jpa tab attr data base do.
   */
  public JpaTabAttrDataBaseDO()
  {

  }

  /**
   * Instantiates a new jpa tab attr data base do.
   *
   * @param parent the parent
   */
  public JpaTabAttrDataBaseDO(A parent)
  {
    this.parent = parent;
  }

  /**
   * Instantiates a new jpa tab attr data base do.
   *
   * @param parent the parent
   * @param value the value
   */
  public JpaTabAttrDataBaseDO(A parent, String value)
  {
    this.parent = parent;
    this.data = value;
  }

  /**
   * Gets the datarow.
   *
   * @return the datarow
   */
  @Column(name = "DATAROW", nullable = false)
  public int getDatarow()
  {
    return datarow;
  }

  /**
   * Gets the data.
   *
   * @return the data
   */
  @Column(name = "DATACOL", length = DATA_MAXLENGTH)
  public String getData()
  {
    return data;
  }

  /**
   * Sets the datarow.
   *
   * @param datarow the new datarow
   */
  public void setDatarow(int datarow)
  {
    this.datarow = datarow;
  }

  /**
   * Sets the data.
   *
   * @param data the new data
   */
  public void setData(String data)
  {
    this.data = data;
  }

  /**
   * Gets the parent.
   *
   * @return the parent
   */
  @Transient
  public A getParent()
  {
    return parent;
  }

  /**
   * Sets the parent.
   *
   * @param parent the new parent
   */
  public void setParent(A parent)
  {
    this.parent = parent;
  }

}
