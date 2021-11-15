package org.tinycode.utils.common.string;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

/**
 * Json序列化工具 
 * @author littlehui
 * @date 2021/11/15 14:44
 * @version 1.0
 */
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转化为Json格式字符串
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将Json格式字符串转化为对象
     *
     * @param <T>
     * @param json
     * @param requiredType
     * @return
     */
    public static <T> T toObject(String json, Class<T> requiredType) {
        try {
            if(json==null|| json.length()==0) {
                return null;
            }
            return (T) OBJECT_MAPPER.readValue(json, requiredType);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage() + ",json:" + json);
        }
    }

    /**
     * 将Json格式字符串转化为对象
     *
     * @param <T>
     * @param json
     * @param type
     * @return
     */
    public static <T> T toObject(String json, TypeReference<T> type) {
        try {
            if(json==null|| json.length()==0) {
                return null;
            }
            return  OBJECT_MAPPER.readValue(json, type);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
