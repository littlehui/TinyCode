package org.tinycode.utils.common.number;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName RandomUtil
 * @author littlehui
 * @date 2021/7/18 10:42
 * @version 1.0
 **/
public class RandomUtil {

    /**
     * 从给定的List数组中随机返回特定的数量
     * @param list 被随机的列表
     * @param selected 随机条数
     * @author littlehui
     * @date 2021/11/15 16:09
     * @return java.util.List<T>
     */
    public static <T> List<T> getRandomNum(List<T> list, int selected) {
        List<T> reList = new ArrayList<T>();
        Random random = new Random();
        // 先抽取，备选数量的个数
        if (list.size() >= selected) {
            for (int i = 0; i < selected; i++) {
                // 随机数的范围为0-list.size()-1;
                int target = random.nextInt(list.size());
                reList.add(list.get(target));
                list.remove(target);
            }
        } else {
            selected = list.size();
            for (int i = 0; i < selected; i++) {
                // 随机数的范围为0-list.size()-1;
                int target = random.nextInt(list.size());
                reList.add(list.get(target));
                list.remove(target);
            }
        }
        return reList;
    }
}
