package br.com.powercripto.service.impl;

import br.com.powercripto.domain.Cripto;
import br.com.powercripto.repository.CriptoRepository;
import br.com.powercripto.service.CriptoService;
import br.com.powercripto.service.util.ThreadHashCalculation;
import br.com.powercripto.service.util.Timer;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Cripto.
 */
@Service
@Transactional
public class CriptoServiceImpl implements CriptoService {

    private final Logger log = LoggerFactory.getLogger(CriptoServiceImpl.class);

    private int numberOfThreads;
    private List<ThreadHashCalculation> lstThreadHashCalculations;

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

        numberOfThreads = 4;
        if (cripto.getQuantidadeRegistros() == null || cripto.getQuantidadeRegistros() < 1) {
            cripto.setQuantidadeRegistros(1L);
        }

        Cripto ultimoResultado = null;

        for (int i = 0; i < cripto.getQuantidadeRegistros(); i++) {
            Cripto clone = new Cripto(cripto.getQuantidadeBitZero(), cripto.getQuantidadeRegistros());
            ultimoResultado = gerarCripto(clone);
            log.debug("Hash " + (i+1) + "/" + cripto.getQuantidadeRegistros() + " gerado.");
        }

        return ultimoResultado;
    }

    private Cripto gerarCripto(Cripto cripto) {
        instantiateThreads(cripto);

        Timer timer = new Timer();
        timer.start();

        startThreads();
        runThreads();

        while (areAllThreadsRunning()) {
            //wait while threads are running, when the first ends, the other will stop, by following command from the volatile boolean
        }

        Long quantidadeHashesCalculado = stopThreads();

        timer.end();
        cripto.setTempo(timer.getTotalTime());
        cripto.setQuantidadeHashes(quantidadeHashesCalculado);
        cripto.setHash(getResultHash());

        log.debug("calculo de hashes concluído em: " + timer.getTotalTime().toString());

        return criptoRepository.save(cripto);
    }

    private void instantiateThreads(Cripto cripto) {
        lstThreadHashCalculations = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            lstThreadHashCalculations.add(new ThreadHashCalculation(cripto.getQuantidadeBitZero()));
        }
    }

    private void startThreads() {
        for (ThreadHashCalculation thread : lstThreadHashCalculations) {
            thread.start();
        }
    }

    private Long stopThreads() {
        Long quantidadeHashesCalculado = 0L;
        for (ThreadHashCalculation thread : lstThreadHashCalculations) {
            quantidadeHashesCalculado += thread.stopThread();
        }
        return quantidadeHashesCalculado;
    }

    private void runThreads() {
        for (ThreadHashCalculation thread : lstThreadHashCalculations) {
            thread.run();
        }
    }

    private boolean areAllThreadsRunning() {
        for (ThreadHashCalculation thread : lstThreadHashCalculations) {
            if (!thread.isThreadRunning()) {
                return false;
            }
        }
        return true;
    }

    private String getResultHash() {
        for (ThreadHashCalculation thread : lstThreadHashCalculations) {
            if (thread.getHash() != null && !thread.getHash().isEmpty()) {
                return thread.getHash();
            }
        }
        return null;
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
