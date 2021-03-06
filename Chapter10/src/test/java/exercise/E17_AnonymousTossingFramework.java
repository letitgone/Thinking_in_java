package exercise;

interface Tossing {
    boolean event();
}


interface TossingFactory {
    Tossing getTossing();
}


class CoinTossing implements Tossing {
    private int events;
    private static final int EVENTS = 2;

    public boolean event() {
        System.out.println("Coin tossing event " + events);
        return ++events != EVENTS;
    }

    public static TossingFactory factory = new TossingFactory() {
        public CoinTossing getTossing() {
            return new CoinTossing();
        }
    };
}


class DiceTossing implements Tossing {
    private int events;
    private static final int EVENTS = 6;

    public boolean event() {
        System.out.println("Dice tossing event " + events);
        return ++events != EVENTS;
    }

    public static TossingFactory factory = new TossingFactory() {
        public DiceTossing getTossing() {
            return new DiceTossing();
        }
    };
}


/**
 * @Author ZhangGJ
 * @Date 2019/05/09
 */
public class E17_AnonymousTossingFramework {
    public static void simulate(TossingFactory fact) {
        Tossing t = fact.getTossing();
        while (t.event())
            ;
    }

    public static void main(String[] args) {
        simulate(CoinTossing.factory);
        simulate(DiceTossing.factory);
    }
}
