package section_11;

import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;

/**
 * @Author ZhangGJ
 * @Date 2019/09/04
 */
class FArray {
    public static <T> T[] fill(T[] a, Generator<T> gen) {
        for (int i = 0; i < a.length; i++)
            a[i] = gen.next();
        return a;
    }
}


public class PrimitiveGenericTest {
    public static void main(String[] args) {
        String[] strings = FArray.fill(new String[7], new RandomGenerator.String(10));
        for (String s : strings)
            System.out.println(s);
        Integer[] integers = FArray.fill(new Integer[7], new RandomGenerator.Integer());
        for (int i : integers)
            System.out.println(i);
        // Autoboxing won’t save you here. This won’t compile:
        // int[] b =
        //   FArray.fill(new int[7], new RandIntGenerator());
    }
}
