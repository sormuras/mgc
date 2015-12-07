package de.micromata.mgc.db.jpa.multipc;

import javax.persistence.EntityManager;

import de.micromata.genome.jpa.DefaultEmgr;
import de.micromata.genome.jpa.EmgrFactory;

public class MultiPcSecondEmgrFactory extends EmgrFactory<DefaultEmgr>
{
  static MultiPcSecondEmgrFactory INSTANCE;

  public static synchronized MultiPcSecondEmgrFactory get()
  {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    INSTANCE = new MultiPcSecondEmgrFactory();
    return INSTANCE;
  }

  protected MultiPcSecondEmgrFactory()
  {
    //    super("de.micromata.genome.jpa.multipc.second");
    super("de.micromata.genome.jpa.multipc.auto");
  }

  @Override
  protected DefaultEmgr createEmgr(EntityManager entityManager)
  {
    return new DefaultEmgr(entityManager, this);
  }

}
