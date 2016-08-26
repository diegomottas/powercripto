package br.com.powercripto.service;

import br.com.powercripto.domain.Cripto;
import br.com.powercripto.repository.CriptoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Cripto.
 */
@Service
@Transactional
public class CriptoService {

    private final Logger log = LoggerFactory.getLogger(CriptoService.class);
    
    @Inject
    private CriptoRepository criptoRepository;
    
    /**
     * Save a cripto.
     * 
     * @param cripto the entity to save
     * @return the persisted entity
     */
    public Cripto save(Cripto cripto) {
        log.debug("Request to save Cripto : {}", cripto);
        Cripto result = criptoRepository.save(cripto);
        return result;
    }

    /**
     *  Get all the criptos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Cripto> findAll(Pageable pageable) {
        log.debug("Request to get all Criptos");
        Page<Cripto> result = criptoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one cripto by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Cripto findOne(Long id) {
        log.debug("Request to get Cripto : {}", id);
        Cripto cripto = criptoRepository.findOne(id);
        return cripto;
    }

    /**
     *  Delete the  cripto by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cripto : {}", id);
        criptoRepository.delete(id);
    }
}
