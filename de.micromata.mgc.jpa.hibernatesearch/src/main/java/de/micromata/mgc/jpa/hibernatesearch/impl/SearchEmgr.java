package de.micromata.mgc.jpa.hibernatesearch.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import de.micromata.genome.jpa.Emgr;
import de.micromata.genome.jpa.events.impl.EmgrEventQuery;
import de.micromata.genome.jpa.metainf.ColumnMetadata;
import de.micromata.mgc.jpa.hibernatesearch.api.ISearchEmgr;
import de.micromata.mgc.jpa.hibernatesearch.api.SearchEmgrFactory;
import de.micromata.mgc.jpa.hibernatesearch.events.SearchEmgrReindexEventFilterEvent;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 * @param <EMGR>
 */
public class SearchEmgr<EMGR extends SearchEmgr<?>>extends Emgr<EMGR> implements ISearchEmgr<EMGR>
{

  public SearchEmgr(EntityManager entityManager, SearchEmgrFactory<EMGR> emgrFactory)
  {
    super(entityManager, emgrFactory);

  }

  @Override
  public QueryBuilder getFullTexteSearchQueryBuilder()
  {
    // TODO Auto-generated method stub
    return null;
  }

  private String[] getDefaultSearchFields(Class<?> type)
  {
    Map<String, ColumnMetadata> fm = ((SearchEmgrFactory<?>) getEmgrFactory()).getSearchableFieldsForEntity(type);
    String[] sfa = new String[fm.size()];
    int idx = 0;
    for (String fn : fm.keySet()) {
      sfa[idx] = fn;
      ++idx;
    }
    return sfa;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> searchAttached(String expression, Class<T> type, String... fields)
  {
    if (fields.length == 0) {
      fields = getDefaultSearchFields(type);
    }
    FullTextEntityManager ftem = getFullTextEntityManager();
    org.hibernate.search.query.dsl.QueryBuilder qb = ftem.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(type)
        .get();
    org.apache.lucene.search.Query luceneQuery = qb
        .keyword()
        .onFields(fields)
        .matching(expression)
        .createQuery();

    return searchAttached(luceneQuery, type);
  }

  @Override
  public <T> List<T> searchWildcardAttached(String expression, Class<T> type, String... fields)
  {
    if (fields.length == 0) {
      fields = getDefaultSearchFields(type);
    }
    FullTextEntityManager ftem = getFullTextEntityManager();
    org.hibernate.search.query.dsl.QueryBuilder qb = ftem.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(type)
        .get();
    org.apache.lucene.search.Query luceneQuery = qb
        .keyword()
        .wildcard()
        .onFields(fields)
        .matching(expression)
        .createQuery();

    return searchAttached(luceneQuery, type);
  }

  @Override
  public <T> List<T> searchWildcardDetached(String expression, Class<T> type, String... fields)
  {
    List<T> ret = searchWildcardAttached(expression, type, fields);
    detach(ret);
    return ret;
  }

  @Override
  public <T> List<T> searchAttached(org.apache.lucene.search.Query luceneQuery, Class<T> type)
  {
    FullTextEntityManager ftem = getFullTextEntityManager();
    javax.persistence.Query jpaQuery = new EmgrEventQuery(this,
        ftem.createFullTextQuery(luceneQuery, type));
    List<T> lret = jpaQuery.getResultList();
    return lret;
  }

  @Override
  public <T> List<T> searchDetached(String expression, Class<T> type, String... fields)
  {
    List<T> ret = searchAttached(expression, type, fields);
    detach(ret);
    return ret;
  }

  @Override
  public void reindex(Object entity)
  {
    filterEvent(new SearchEmgrReindexEventFilterEvent(this, entity), (event) -> {
      event.getSearchEmgr().getFullTextEntityManager().index(event.getEntity());
    });
  }

  @Override
  public FullTextEntityManager getFullTextEntityManager()
  {
    FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
        .getFullTextEntityManager(getEntityManager());
    return fullTextEntityManager;
  }

}