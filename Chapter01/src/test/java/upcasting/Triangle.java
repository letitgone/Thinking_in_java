package upcasting;

/**
 * @Author ZhangGJ
 * @Date 2019/12/27 06:21
 */
public class Triangle extends Shape{

    @Override
    public void draw(){
        System.out.println("Triangle draw");
    }

    @Override
    public void erase(){
        System.out.println("Triangle erase");
    }
}