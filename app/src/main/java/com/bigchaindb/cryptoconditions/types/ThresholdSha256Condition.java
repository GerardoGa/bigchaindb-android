//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bigchaindb.cryptoconditions.types;

import com.bigchaindb.cryptoconditions.CompoundCondition;
import com.bigchaindb.cryptoconditions.CompoundSha256Condition;
import com.bigchaindb.cryptoconditions.Condition;
import com.bigchaindb.cryptoconditions.ConditionType;
import com.bigchaindb.cryptoconditions.der.DEROutputStream;
import com.bigchaindb.cryptoconditions.der.DERTags;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.EnumSet;

public class ThresholdSha256Condition extends CompoundSha256Condition implements CompoundCondition {
    private int threshold;
    private Condition[] subconditions;

    public ThresholdSha256Condition(int threshold, Condition[] subconditions) {
        super(calculateCost(threshold, subconditions), calculateSubtypes(subconditions));
        this.threshold = threshold;
        this.subconditions = (Condition[])Arrays.copyOf(subconditions, subconditions.length);
    }

    public ThresholdSha256Condition(byte[] fingerprint, long cost, EnumSet<ConditionType> subtypes) {
        super(fingerprint, cost, subtypes);
    }

    public ConditionType getType() {
        return ConditionType.THRESHOLD_SHA256;
    }

    protected byte[] getFingerprintContents() {
        try {
            sortConditions(this.subconditions);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DEROutputStream out = new DEROutputStream(baos);

            for(int i = 0; i < this.subconditions.length; ++i) {
                out.write(this.subconditions[i].getEncoded());
            }

            out.close();
            byte[] buffer = baos.toByteArray();
            baos = new ByteArrayOutputStream();
            out = new DEROutputStream(baos);
            out.writeTaggedObject(0, BigInteger.valueOf((long)this.threshold).toByteArray());
            out.writeTaggedConstructedObject(1, buffer);
            out.close();
            buffer = baos.toByteArray();
            baos = new ByteArrayOutputStream();
            out = new DEROutputStream(baos);
            out.writeEncoded(DERTags.CONSTRUCTED.getTag() + DERTags.SEQUENCE.getTag(), buffer);
            out.close();
            return baos.toByteArray();
        } catch (IOException var4) {
            throw new UncheckedIOException("DER Encoding Error", var4);
        }
    }

    private static void sortConditions(Condition[] conditions) {
        Arrays.sort(conditions, (c1, c2) -> {
            byte[] c1encoded = c1.getEncoded();
            byte[] c2encoded = c2.getEncoded();
            int minLength = Math.min(c1encoded.length, c2encoded.length);

            for(int i = 0; i < minLength; ++i) {
                int result = Integer.compareUnsigned(c1encoded[i], c2encoded[i]);
                if (result != 0) {
                    return result;
                }
            }

            return c1encoded.length - c2encoded.length;
        });
    }

    private static long calculateCost(int threshold, Condition[] subconditions) {
        Condition[] sortedConditions = (Condition[])Arrays.copyOf(subconditions, subconditions.length);
        Arrays.sort(sortedConditions, (c1, c2) -> {
            return (int)(c2.getCost() - c1.getCost());
        });
        long largestCosts = 0L;

        for(int i = 0; i < threshold; ++i) {
            largestCosts += sortedConditions[i].getCost();
        }

        return largestCosts + (long)(subconditions.length * 1024);
    }

    private static EnumSet<ConditionType> calculateSubtypes(Condition[] subconditions) {
        EnumSet<ConditionType> subtypes = EnumSet.noneOf(ConditionType.class);

        for(int i = 0; i < subconditions.length; ++i) {
            subtypes.add(subconditions[i].getType());
            if (subconditions[i] instanceof CompoundCondition) {
                subtypes.addAll(((CompoundCondition)subconditions[i]).getSubtypes());
            }
        }

        if (subtypes.contains(ConditionType.THRESHOLD_SHA256)) {
            subtypes.remove(ConditionType.THRESHOLD_SHA256);
        }

        return subtypes;
    }
}
