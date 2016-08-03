package br.com.powercripto.service.util;

import java.util.Iterator;

/**
 * Diego 03/08/16.
 * Originally taken from StackOverflow website
 */
public class ByteArrayBitIterable {
    private final byte[] array;

    public ByteArrayBitIterable(byte[] array) {
        this.array = array;
    }

    public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            private int bitIndex = 0;
            private int arrayIndex = 0;

            @Override
            public boolean hasNext() {
                return (arrayIndex < array.length) && (bitIndex < 8);
            }

            @Override
            public Boolean next() {
                Boolean val = (array[arrayIndex] >> (7 - bitIndex) & 1) == 1;
                bitIndex++;
                if (bitIndex == 8) {
                    bitIndex = 0;
                    arrayIndex++;
                }
                return val;
            }

        };
    }

}
