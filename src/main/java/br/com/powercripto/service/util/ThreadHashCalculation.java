package br.com.powercripto.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

/**
 * Diego 03/08/16.
 */
public class ThreadHashCalculation extends Thread {
    private final Logger log = LoggerFactory.getLogger(CriptoUtil.class);

    private String hash;
    private Long quantidadeBitZero;
    private Long quantidadeHashesCalculado = 0L;

    private volatile boolean threadRunning = true;

    public ThreadHashCalculation(Long quantidadeBitZero) {
        this.quantidadeBitZero = quantidadeBitZero;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    @Override
    public void run() {
        try {
            byte[] digest;
            do {
                digest = CriptoUtil.gerarNovoHash();
                quantidadeHashesCalculado++;
            } while (CriptoUtil.verificaQuantidadeBitsZeros(digest, quantidadeBitZero) && threadRunning);


            if (!CriptoUtil.verificaQuantidadeBitsZeros(digest, quantidadeBitZero)) {
                hash = CriptoUtil.bytesToHex(digest);
            }

            threadRunning = false;
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Long stopThread() {
        threadRunning = false;
        return quantidadeHashesCalculado;
    }
}
