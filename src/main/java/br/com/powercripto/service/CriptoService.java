package br.com.powercripto.service;

import br.com.powercripto.domain.Cripto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Cripto.
 */
public interface CriptoService {

    /**
     * Save a cripto.
     * 
     * @param cripto the entity to save
     * @return the persisted entity
     */
    Cripto save(Cripto cripto);

    /**
     *  Get all the criptos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Cripto> findAll(Pageable pageable);

    /**
     *  Get the "id" cripto.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Cripto findOne(Long id);

    /**
     *  Delete the "id" cripto.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
