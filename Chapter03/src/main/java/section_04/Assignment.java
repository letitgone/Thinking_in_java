package section_04;

class Tank{
		int level;
}
/**
 * 对象赋值
 * @Author ZhangGJ
 * @Date 2019/03/18
 */
public class Assignment {
		public static void main(String[] args) {
				Tank t1 = new Tank();
				Tank t2 = new Tank();
				t1.level = 9;
				t2.level = 47;
				System.out.println("1: t1.level: " + t1.level + ", t2.level: " + t2.level);
				t1 = t2;
				System.out.println("1: t1.level: " + t1.level + ", t2.level: " + t2.level);
				t1.level = 27;
				System.out.println("1: t1.level: " + t1.level + ", t2.level: " + t2.level);
		}
}