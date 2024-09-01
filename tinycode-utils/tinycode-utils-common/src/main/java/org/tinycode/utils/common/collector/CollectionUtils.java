package org.tinycode.utils.common.collector;

import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    public static <T> T first(Collection<T> values) {
        if (isEmpty(values)) {
            return null;
        } else if (values instanceof List) {
            List<T> list = (List)values;
            return list.get(0);
        } else {
            return values.iterator().next();
        }
    }
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
