package dggs.organizing_work_boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyUtil {
    private static Environment env;

    @Autowired
    public PropertyUtil(Environment environment) {
        env = environment;
    }

    // EgovFramework getProperty() 느낌으로 바로 사용
    public static String getProperty(String key) {
        String value = env.getProperty(key);
        return toUtf8(value);
    }

    // 기본값 지정
    public static String getProperty(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }

    // 한글 깨짐 방지 변환
    private static String toUtf8(String value) {
        if (value == null) return null;
        try {
            // ISO-8859-1로 읽힌 것을 UTF-8로 변환
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return value; // 실패 시 원래값 반환
        }
    }
}
