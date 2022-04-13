package org.tinycode.utils.common.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/4/13 15:57
 */
public class ObjectUtil {


    public static <M,T> List<T> convertList(List<M> objList , Converter<M,T> converter) {
        List<T> list = new ArrayList<T>();
        for (M m : objList) {
            list.add(converter.convert(m));
        }
        return list;
    }

    public static interface  Converter<H,Q>{
        public Q convert(H h);
    }
}
