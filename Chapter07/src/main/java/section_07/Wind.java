package section_07;


class Instrument {
    public void play() {
    }

    static void tune(Instrument i) {
        // ...
        i.play();
    }
}

/**
 * // Wind objects are instruments
 * // because they have the same interface:
 * @Author ZhangGJ
 * @Date 2019/04/08
 */
public class Wind extends Instrument {
    public static void main(String[] args) {
        Wind flute = new Wind();
        Instrument.tune(flute); // Upcasting
    }
}
