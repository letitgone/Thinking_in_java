/**
 * @Author ZhangGJ
 * @Date 2019/07/30
 */
interface FactoryI<T> {
    T create(int arg);
}


class Foo<T> {
    private T x;

    public Foo(FactoryI<T> factory) {
        x = factory.create(1);
    }
    // ...
}


class IntegerFactory implements FactoryI<Integer> {
    public Integer create(int arg) {
        return new Integer(arg);
    }
}


class Widget {
    private final int id;

    Widget(int ident) {
        id = ident;
    }

    public String toString() {
        return "Widget " + id;
    }

    public static class Factory implements FactoryI<Widget> {
        public Widget create(int arg) {
            return new Widget(arg);
        }
    }
}


public class E23_FactoryConstraint2 {
    public static void main(String[] args) {
        new Foo<Integer>(new IntegerFactory());
        new Foo<Widget>(new Widget.Factory());
    }
}