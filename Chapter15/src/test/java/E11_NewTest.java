import section_04.New;

import java.util.*;

/**
 * @Author ZhangGJ
 * @Date 2019/07/24
 */
public class E11_NewTest {
    public static void main(String[] args) {
        List<SixTuple<Byte, Short, String, Float, Double, Integer>> list = New.list();
        list.add(new SixTuple<Byte, Short, String, Float, Double, Integer>((byte) 1, (short) 1, "A",
            1.0F, 1.0, 1));
        System.out.println(list);
        Set<Sequence<String>> set = New.set();
        set.add(new Sequence<String>(5));
        System.out.println(set);
    }
}
