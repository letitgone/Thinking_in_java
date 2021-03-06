# Thinking in java(Fourth Edition)
    thinking in java notes
## Chapter 8: 多态
1.在面向对象的程序设计语言中，多态是继数据抽象和继承之后的第三种基本特征。   
2.多态不但能够改善代码组织结构和可读性，还能够创建可扩展的程序--即无论在项目最初创建时还是在需要添加新功能时都可以
"生长"的程序。   
3.多态的作用就是消除类型之间的耦合关系。   
4.多态方法调用允许一种类型表现出与其他相似类型之间的区别，只要它们都是从同一基类导出而来的。
### 8.1 再论向上转型
1.把对某个对象的引用视为对其基类型的引用的做法被称为"向上转型"，----因为在继承树的画法中，基类是放置在上方的。
### 8.2 转机
1.将一个方法调用同一个方法主体关联起来被称作绑定。   
2.若在程序执行前进行绑定（如果有的话，由编译器和连接程序实现），叫做前期绑定。   
3.后期绑定，在运行时根据对象的类型进行绑定，后期绑定也称为动态绑定或者运行时绑定。   
4.Java中除了static方法和final方法（private方法属于final方法）之外，其他所有的方法都是后期绑定，这意味着通常情况下
我们不必判定是否应该进行后期绑定----它会自动发生。   
5.使用final可以有效的"关闭"动态绑定。  
6.```Shape s = new Circle();```   
这里创建了一个Circle对象，并把得到的引用立即赋值给Shape，这样做看似错误（将一种类型赋值给另一种类型），但实际上没问题，
因为通过继承，Circle就是一种Shape，假设你调用一个基类方法（它已在导出类中被覆盖）：   
```s.draw();```     
由于动态绑定（多态），还是正确调用了Circle.draw()方法。   
7.对于基类中的private方法，最好采用不同的名字。   
8.向上转型时，任何域访问操作都将由编译器解析，因此不是多态的，尽管这看起来好像会成为一个容易令人混淆的问题，但是实践中，
它实际上从来不会发生，首先你通常会将所有的域都设置成private，另外，你可能不会对基类中的域和导出类中的域赋予相同的名字，
因为这种做法容易令人混淆。   
9.如果某个方法时静态的，它的行为就不具有多态性，静态方法是与类，而非与单个的对象相关联的。   
### 8.3 构造器和多态
1.构造器并不具有多态性（它们实际上是static方法，只不过该static声明是隐式的）。   
2.构造器有一项特殊的任务：检查对象是否被正确的构造。   
3.构造器调用顺序：   
(1)调用基类构造器；   
(2)按声明顺序调用成员的初始化方法；
(3)调用导出类构造器的主体。   
4.销毁的顺序应该和初始化顺序相反，对于字段，则意味着与声明的顺序相反（因为字段的初始化是按照声明的顺序进行的），对于基类
应该首先对其导出类进行清理，然后才是基类。   
5.构造器的工作就是创建对象。    
6.上面的初始化顺序不完整，初始化的实际过程是：   
(1)在其他任何事物发生之前，将分配给对象的存储空间初始化成二进制的零；  被   
(2)如前所述那样调用基类构造器。此时，调用被覆盖后的draw()方法（要在调用RoundGlyph构造器之前调用），由于步骤一的缘故，
我们此时会发现radius的值为0；   
(3)按照声明顺序调用成员的初始化方法；   
(4)调用导出类的构造器主体。    
7.b编写构造器时有一条有效的准则：用尽可能简单的方法使对象进入正常状态，如果可以的话，避免调用其他方法。   
8.在构造器内唯一能够安全调用的那些方法是基类中的final方法（也适用于pribate方法，它们自动属于final方法）。
### 8.4 协变返回类型
1.导出类中的被覆盖方法可以返回基类方法的返回类型的某种导出类型。   
2.协变返回类型允许返回wheat类型。   
### 8.5 用继承进行设计
1.用继承表达行为间的差异，并用字段表达状态上的变化。   
2.在Java中，所有的转型都会得到检查。   
3.导出类中的接口的扩展部分不能被基类访问，因此，一旦我们向上转型，就不能调用那些新方。   
4.多态是一种不能单独来看待的特性，相反它只能作为类关系"全景"中的一部分，与其他特性协同工作。   
### 那么问题来了，到底什么是多态？（来自百度）
1.在面向对象语言中，接口的多种不同的实现方式即为多态。   
2.引用Charlie Calverts对多态的描述:多态性是允许你将父对象设置成为一个或更多的他的子对象相等的技术，赋值之后，父对
象就可以根据当前赋值给它的子对象的特性以不同的方式运作（摘自“Delphi4 编程技术内幕”）。   
3.简单的说，就是一句话：允许将子类类型的指针赋值给父类类型的指针。
