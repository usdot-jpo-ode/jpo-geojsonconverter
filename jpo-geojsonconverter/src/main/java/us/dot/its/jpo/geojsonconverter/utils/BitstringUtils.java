package us.dot.its.jpo.geojsonconverter.utils;

import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class BitstringUtils {

    /**
     * Convert an {@link Asn1Bitstring} type to a {@link ProcessedBitstring}
     * @param processedBitstring A type inheriting from processed bitstring to be populated
     * @param asnBitstring An Asn1Bitstring type
     */
    public static void processBitstring(ProcessedBitstring processedBitstring, Asn1Bitstring asnBitstring) {
        if (asnBitstring == null) return;
        for (int index = 0; index < processedBitstring.size(); index++) {
            processedBitstring.set(index, asnBitstring.get(index));
        }
    }
}
