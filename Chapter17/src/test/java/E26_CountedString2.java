import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.mindview.util.Print.print;

/**
 * @Author ZhangGJ
 * @Date 2019/10/02
 */
class CountedString2 {
    private static List<String> created = new ArrayList<String>();
    private String s;
    private char c;
    private int id;

    public CountedString2(String str, char ci) {
        s = str;
        c = ci;
        created.add(s);
        // id is the total number of instances
        // of this string in use by CountedString2:
        for (String s2 : created)
            if (s2.equals(s))
                id++;
    }

    public String toString() {
        return "String: " + s + " id: " + id + " char: " + c + " hashCode(): " + hashCode();
    }

    public int hashCode() {
        // The very simple approach:
        // return s.hashCode() * id;
        // Using Joshua Bloch's recipe:
        int result = 17;
        result = 37 * result + s.hashCode();
        result = 37 * result + id;
        result = 37 * result + c;
        return result;
    }

    public boolean equals(Object o) {
        return o instanceof CountedString2 && s.equals(((CountedString2) o).s)
            && id == ((CountedString2) o).id && c == ((CountedString2) o).c;
    }
}


public class E26_CountedString2 {
    public static void main(String[] args) {
        Map<CountedString2, Integer> map = new HashMap<CountedString2, Integer>();
        CountedString2[] cs = new CountedString2[5];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = new CountedString2("hi", 'c');
            map.put(cs[i], i); // Autobox int -> Integer
        }
        print(map);
        for (CountedString2 cstring : cs) {
            print("Looking up " + cstring);
            print(map.get(cstring));
        }
    }
}
