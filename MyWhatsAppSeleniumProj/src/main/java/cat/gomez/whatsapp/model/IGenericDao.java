package cat.gomez.whatsapp.model;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao<T extends Serializable> {
    
    void setClazz(final Class<T> clazzToSet);
    
    T findOne(final Long id);
  
    List<T> findAll();
  
    void create(final T entity);
  
    T update(final T entity);
  
    void delete(final T entity);
  
 }
