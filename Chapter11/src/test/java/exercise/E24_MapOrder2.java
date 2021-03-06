package exercise;

import net.mindview.util.Countries;

import java.util.*;

/**
 * @Author ZhangGJ
 * @Date 2019/05/28
 */
public class E24_MapOrder2 {
    public static void main(String[] args) {
        Map<String, String> m1 = new LinkedHashMap<>(Countries.capitals(25));
        System.out.println(m1);
        String[] keys = m1.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        Map<String, String> m2 = new LinkedHashMap<>();
        for (String key : keys) {
            m2.put(key, m1.get(key));
        }
        System.out.println(m2);
    }
}
