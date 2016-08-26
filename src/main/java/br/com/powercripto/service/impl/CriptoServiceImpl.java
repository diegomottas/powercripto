package br.com.powercripto.service.impl;

import br.com.powercripto.domain.Cripto;
import br.com.powercripto.repository.CriptoRepository;
import br.com.powercripto.service.CriptoService;
import br.com.powercripto.service.util.ThreadHashCalculation;
import br.com.powercripto.service.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Cripto.
 */
@Service
@Transactional
public class CriptoServiceImpl implements CriptoService {

    private final Logger log = LoggerFactory.getLogger(CriptoServiceImpl.class);

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

        log.debug("Iniciando calculo de hashes...");

        Timer timer = new Timer();
        timer.start();

        ThreadHashCalculation t1 = new ThreadHashCalculation(cripto.getQuantidadeBitZero());
        ThreadHashCalculation t2 = new ThreadHashCalculation(cripto.getQuantidadeBitZero());
        ThreadHashCalculation t3 = new ThreadHashCalculation(cripto.getQuantidadeBitZero());
        ThreadHashCalculation t4 = new ThreadHashCalculation(cripto.getQuantidadeBitZero());

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.run();
        t2.run();
        t3.run();
        t4.run();

        while (t1.isThreadRunning() || t2.isThreadRunning() || t3.isThreadRunning() || t4.isThreadRunning()) {
            //wait while threads are running, when the first ends, the other will stop, by following command from the volatile boolean
        }

        Long quantidadeHashesCalculado = 0L;
        quantidadeHashesCalculado += t1.stopThread();
        quantidadeHashesCalculado += t2.stopThread();
        quantidadeHashesCalculado += t3.stopThread();
        quantidadeHashesCalculado += t4.stopThread();

        timer.end();
        cripto.setTempo(timer.getTotalTime());
        cripto.setQuantidadeHashes(quantidadeHashesCalculado);

        log.debug("calculo de hashes concluído em: " + timer.getTotalTime().toString());

        Cripto result = criptoRepository.save(cripto);
        return result;
    }

    /**
     * Get all the criptos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cripto> findAll(Pageable pageable) {
        log.debug("Request to get all Criptos");
        Page<Cripto> result = criptoRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one cripto by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Cripto findOne(Long id) {
        log.debug("Request to get Cripto : {}", id);
        Cripto cripto = criptoRepository.findOne(id);
        return cripto;
    }

    /**
     * Delete the  cripto by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cripto : {}", id);
        criptoRepository.delete(id);
    }
}
