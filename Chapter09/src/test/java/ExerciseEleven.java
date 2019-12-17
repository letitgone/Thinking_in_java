import section_03.Apply;
import section_03.Processor;

/**
 * @Author ZhangGJ
 * @Date 2019/04/17
 */
class CharacterPairSwapper {
    static String swap(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length() - 1; i += 2) {
            char c1 = sb.charAt(i);
            char c2 = sb.charAt(i + 1);
            sb.setCharAt(i, c2);
            sb.setCharAt(i + 1, c1);
        }
        return sb.toString();
    }
}


class SwapperAdapter implements Processor {
    public String name() {
        return CharacterPairSwapper.class.getSimpleName();
    }

    public String process(Object input) {
        return CharacterPairSwapper.swap((String) input);
    }
}


public class ExerciseEleven {
    public static void main(String[] args) {
        Apply.process(new SwapperAdapter(), "1234");
        Apply.process(new SwapperAdapter(), "abcde");
    }
}
