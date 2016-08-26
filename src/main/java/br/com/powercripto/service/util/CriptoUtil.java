package br.com.powercripto.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

/**
 * Diego 03/08/16.
 */
public class CriptoUtil {

    public static boolean verificaQuantidadeBitsZeros(byte[] b, Long quantidadeBitsZeros) {
        Long quantidadeIteracoes = 0L;
        Iterator<Boolean> iterator = new ByteArrayBitIterable(b).iterator();
        while (iterator.hasNext()) {
            Boolean bit1 = iterator.next();
            if (bit1) {
                return true;
            } else {
                quantidadeIteracoes++;
                if (quantidadeIteracoes >= quantidadeBitsZeros) {
                    return false;
                }
            }
        }

        return true;
    }

    public static byte[] gerarNovoHash() throws NoSuchAlgorithmException {
        byte[] b = new byte[300];
        new Random().nextBytes(b);

        return MessageDigest.getInstance("SHA-256").digest(b);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
