package exercise;

/**
 * @Author ZhangGJ
 * @Date 2019/04/04
 */
public class E19_VarargStringArray {
    static void test(String... args) {
        for (String s : args) {
            System.out.print(" " + s);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        test("d", "dfdf");
        test(new String[] {"d", "dfdf"});
    }
}
