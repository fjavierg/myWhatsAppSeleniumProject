package cat.gomez.whatsapp.model;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractJpaDao< T extends Serializable > {
    
    private Class< T > clazz;
  
    @PersistenceContext
    EntityManager entityManager;

    
    public void setClazz( Class< T > clazzToSet ) {
       this.clazz = clazzToSet;
    }
  
    public T findOne( Long id ){
       return entityManager.find( clazz, id );
    }
    public List< T > findAll(){
       return entityManager.createQuery( "from " + clazz.getName() )
        .getResultList();
    }
  
    public void create( T entity ){
       entityManager.persist( entity );
    }
  
    public T update( T entity ){
       return entityManager.merge( entity );
    }
  
    public void delete( T entity ){
       entityManager.remove( entity );
    }
 }
