package br.com.powercripto.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

/**
 * Diego 03/08/16.
 */
public class CriptoUtil {

    public static String calcularHash(int quantidadeBitsZeros) throws NoSuchAlgorithmException {

        byte[] digest;
        do {
            digest = gerarNovoHash();
        } while (verificaQuantidadeBitsZeros(digest, quantidadeBitsZeros));

        return new String(digest);
    }

    private static boolean verificaQuantidadeBitsZeros(byte[] b, int quantidadeBitsZeros) {
        int quantidadeIteracoes = 0;
        Iterator<Boolean> iterator = new ByteArrayBitIterable(b).iterator();
        while (iterator.hasNext()) {
            Boolean bit1 = iterator.next();
            if(bit1) {
                return false;
            } else {
                quantidadeIteracoes++;
                if (quantidadeIteracoes >= quantidadeBitsZeros) {
                    return true;
                }
            }
        }

        return false;
    }

    private static byte[] gerarNovoHash() throws NoSuchAlgorithmException {
        byte[] b = new byte[300];
        new Random().nextBytes(b);

        return MessageDigest.getInstance("SHA-256").digest(b);
    }
}
