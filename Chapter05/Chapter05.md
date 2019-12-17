# Thinking in java(Fourth Edition)
    thinking in java notes
## Chapter 5: 初始化与清理
### 5.1 用构造器确保初始化
1.C++引入了构造器（constructor）的概念，这是一个在创建对象时被自动调用的特殊方法，Java中也采用了构造器，并额外提供了
"垃圾回收器"，对于不再使用的内存资源，垃圾回收器能自动将其释放。   
2.在Java中，通过提供构造器，类的设计者可确保每个对象都会得到初始化，创建对象时，如果其类具有构造器，Java就会在用户
有能力操作对象之前自动调用相应的构造器。   
3.构造器的命名：即构造器采用与类相同的名称。命名涉及两个问题：1）所取得的任何名字都可能与类的某个成员名称相冲突；2）
调用构造器是编译器的责任，所以必须让编译器知道应该调用哪个方法。   
4.在创建对象时，将会为对象分配存储空间，并调用相应的构造器，这就确保了你能操作对象之前，它已经被恰当地初始化了，请注意
，由于构造器的名称必须与类名完全相同，所以"每个方法首字母小写"的编码风格并不适用于构造器。   
5.不接受任何参数的构造器叫做无参构造器，构造器也能带有形式参数，以便指定如何创建对象。   
6.从概念上讲，"初始化"与"创建"是彼此独立的，但是你却找不到对initialize（）方法的明确调用，Java中，"初始化"与"创建"
是捆绑在一起的，两者不能分离。  
7.构造器没有返回值，这与void明显不同。
### 5.2 方法重载
1.为了让方法名相同而形式参数不同的构造器同时存在，必须用到方法重载。   
2.每个重载的方法都必须有一个独一无二的参数类型列表（参数顺序的不同也可以区分两个方法，但是这样做代码很难维护）。      
3.根据方法的返回值来区分重载方法是行不通的。
### 5.3 默认构造器
1.默认构造器即无参构造器，如果你写的类中没有构造器，则编译器会自动帮你创建一个默认构造器，但是如果已经定义了一个构造
器（无论是否有参数），编译器就不会帮你自动创建默认构造器。
### 5.4 this关键字
1.一个类的两个对象a和b，同时调用peel()方法，怎么才能知道是哪个引用调用的peel()方法？编译器做了一些后台工作，它暗自
把"所操作对象的引用"作为第一个参数传递给了peel()，所以就变成：  
```
Banana.peel(a, 1);
Banana.peel(b, 2);
```  
但是实际不能这么用，为此有一个专门的关键词：this。   
2.this关键字只能在方法内部使用，表示对"调用方法的那个对象"的引用（实际上this就是引用），this的用法和其他对象引用并
无不同，但是如果在方法内部调用同一个类的另一个方法，就不必使用this了。   
3.可能想在一个构造器中调用另一个构造器，避免重复，可以用this关键字做到这一点。   
4.构造器 Flower(String s, int petals)表明：尽管可以用this调用一个构造器，但却不能调用两个，此外，必须将构造器
调用置于最起始处，否则编译会报错。   
5.由于参数s和数据成员s名字相同，所以使用thi.s来代表数据成员。   
6.除构造器之外，编译器禁止在其他任何方法中调用构造器。   
7.static方法就是没有this的方法，在static方法的内部不能调用非静态方法，反过来倒是可以的，而且可以在没有创建任何对象
的前提下，仅仅通过类本身来调用static方法，这是static方法的主要用途。   
8.Java中禁止使用全局方法，但是你在类中置入static方法就可以访问其他static方法和static域，有些人认为static方法不是
"面向对象"的，因为它们的确具有全局函数的含义，使用static方法时，由于不存在this，所以不是通过"向对象发送消息"的方式
来完成的。   
### 5.5 清理：终结处理和垃圾回收
1.Java垃圾回收器只知道释放那些经由new分配的内存，不知道如何处理并非使用new创建的特殊内存区域，因此使用finalize()。   
2.(1)对象可能不被垃圾回收；(2)垃圾回收并不等于"析构"；(3)垃圾回收只与内存有关。   
3.使用垃圾回收器的唯一原因是为了回收程序不再使用的内存。   
4.垃圾回收器可以明显提高对象的创建速度，当它工作时，一面回收空间，一面使堆中的对象紧凑排列，这样"堆指针"就可以很容易
移动到更靠近传送带的开始处，也就尽量避免了页面错误，通过垃圾回收器对对象重新排列，实现了一种高速的，有无限空间可供分配
的堆模型。   
### 5.6 成员初始化
1.对于方法的局部变量，必须初始化，而类的数据成员（即字段），可以不用初始化。
### 5.7 构造器初始化
1.无法阻止自动初始化的进行，他将在构造器被调用之前发生。   
2.在类的内部，变量定义的先后顺序决定了初始化的顺序，即使变量定义散布于方法定义之间，它们仍旧会在任何方法（包括构造器）
被调用之前的到初始化(包括new对象)。   
3.无论创建多少个对象，静态数据都只占用一份存储区域，static关键字不能应用于局部变量，因此它只能作用于域。   
4.静态初始化只有在必要的时候才会进行，如果不创建Table对象，也不引用Table.b1或者Table.b2，那么静态的Bowl b1和b2
永远都不会被创建，只有在第一个Table对象被创建（或者第一次访问静态数据）的时候，它们才会被初始化，此后，静态对象不会
再次被初始化。   
5.初始化的顺序是先静态对象（如果它们尚未因前面的对象创建过程而被初始化），而后是"非静态"对象。   
6.要执行main()（静态方法）方法，必须加载StaticInitialization类，然后其静态域table和cupbo被初始化，这将导致它们
对应的类也被加载，并且由于它们也都包含静态的Bowl对象，因此Bowl随后也被加载，这样，在这个特殊的程序中的所有类在main（）
开始之前都被加载了，实际情况并非如此，因为在典型的程序中，不会把所有的事物都通过static联系起来。   
7.对象的创建过程：   
(1)即使没有显示地使用static关键字，构造器实际上也是静态方法，因此，当首次创建类型为Dog的对象是时（构造器可以看成静
态方法），或者Dog类的静态方法/静态域首次被访问时，Java解释器必须查找类路径，以定位Dog.class文件；   
(2)然后载入Dog.class（这将创建一个Class对象），有关静态初始化的所有动作都会执行，因此静态初始化只在Class对象首次
加载的时候进行一次；   
(3)当用new Dog()创建对象时，首先将在堆上为Dog对象分配足够的存储空间；   
(4)这块存储空间会被清零，这就自动地将Dog对象中的所有基本类型数据都设置成了默认值，而引用则被设置成null；   
(5)执行所有出现于字段定义处的初始化动作；   
(6)执行构造器。
8.实例初始化子句是在调用构造器之前执行。   
### 5.8 数组初始化
1.数组是相同类型的，用一个标识符名称封装到一起的一个对象序列或基本类型数据序列。   
2.数组的创建是在运行时刻进行的。   
### 5.9 枚举类型
1.枚举类型的实例是常量，因此按照命名惯例它们都用大写字母表示（如果在一个名字中有多个单词，用下划线隔开）。   
### 总结
1.没有正确的构造器调用，编译器就不允许创建对象。
2.构造器增加了运行时的开销，速度问题仍是Java涉足某些特定编程领域的障碍。