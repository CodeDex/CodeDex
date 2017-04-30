package com.github.codedex.sourceparser;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Patrick "IPat" Hein
 */

public final class Utils {
    private Utils() { /* Helper class with static-only methods */ }

    public static <T> Set<T> checkSet(Set<T> set) {
        if (set == null)
            return new LinkedHashSet<>();
        else
            return set;
    }

    public static <T> List<T> checkList(List<T> list) {
        if (list == null)
            return new LinkedList<>();
        else
            return list;
    }
}
