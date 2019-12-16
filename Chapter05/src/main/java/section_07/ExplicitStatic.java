package section_07;

/**
 * @Author ZhangGJ
 * @Date 2019/04/03
 */
class Cup{
		Cup(int marker){
				System.out.println("Cup(" + marker + ")");
		}
		void f(int marker){
				System.out.println("f(" + marker + ")");
		}
}
class Cups{
		static Cup cup1;
		static Cup cup2;
		static{
				cup1 = new Cup(1);
				cup2 = new Cup(2);
		}
}
public class ExplicitStatic {
		public static void main(String[] args) {
				System.out.println("Inside main()");
				Cups.cup1.f(99);
		}
		static Cups cup1 = new Cups();
		static Cups cup2 = new Cups();
}
