# Spring

## IoC
### 一个例子
```java
// 接口类
package com.yuhang.ioc.bean;

public interface Geli {
    void say();
}

// ------ 实现类
package com.yuhang.ioc.bean;




public class LiuDeHua implements Geli {
    public void say() {
        System.out.println("墨者革离");
    }
}

// --- 应用
package com.yuhang.ioc.bean;


public class MoAttack {

    private Geli geli;

    // 两种注入方式

    public MoAttack() {}

    // 1.构造注入
    public MoAttack(Geli geli) {
        this.geli = geli;
    }

    // 2.属性注入

    public void setGeli(Geli geli) {
        this.geli = geli;
    }


    public void cityGreatAsk() {
        geli.say();
    }
}

//--- 装配(setter注入)
    <bean id="geli" class="com.yuhang.ioc.bean.LiuDeHua" />
    <bean id="moAttack" class="com.yuhang.ioc.bean.MoAttack">
        <property name="geli" ref="geli" />
    </bean>
    
//--- 测试
package com.yuhang;


import com.yuhang.ioc.bean.MoAttack;
import com.yuhang.ioc.reflect.Car;
import com.yuhang.ioc.reflect.ReflectTest;
import org.springframework.context.ApplicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        MoAttack moAttack = applicationContext.getBean("moAttack",MoAttack.class);
        moAttack.cityGreatAsk();
    }
}


```
### 原理分析 
对于Car类
```java
package com.yuhang.ioc.reflect;

public class Car {
    private String brand;
    private String color;
    private int maxSpeed;

    public Car() {
    }

    public Car(String brand, String color, int maxSpeed) {
        this.brand = brand;
        this.color = color;
        this.maxSpeed = maxSpeed;
    }


    public void introduce() {
        System.out.println( "Car{" +
                "brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", maxSpeed=" + maxSpeed +
                '}');
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
```

可以这样装配 
```xml
<bean id="car" class="com.yuhang.ioc.reflect.Car">
        <property name="brand" value="红旗CA72" />
        <property name="color" value="RED" />
        <property name="maxSpeed" value="200"/>
    </bean>
```
Sprin内部对于装配的实现原理大概是这个样子
```java
public static Car initByDefaultConst() throws  Exception {
//        ClassLoader loader = Thread.currentThread().getContextClassLoader();
//        Class clazz = loader.loadClass("com.yuhang.ioc.reflect.Car");
//
//        /*
//        <bean id="car2" class="com.yuhang.ioc.reflect.Car">
//            <constructor-arg value="红旗CA72" index="0" type="java.lang.String" />
//            <constructor-arg value="RED" index="1" type="java.lang.String"/>
//            <constructor-arg value="200" index="2" type="int"/>
//        </bean>
//         */
//        Constructor cons = clazz.getConstructor(String.class,String.class,int.class);
//        Car car2 = (Car)cons.newInstance("红旗CA72","RED",200);
//        return car2;





        // <bean id="car" class="com.yuhang.ioc.reflect.Car">
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class clazz = loader.loadClass("com.yuhang.ioc.reflect.Car");

        Constructor cons = clazz.getConstructor((Class[])null);
        Car car = (Car)cons.newInstance();

        // <property name="brand" value="红旗CA72" />
        Method setBrand = clazz.getDeclaredMethod("setBrand",String.class);
        setBrand.invoke(car,"红旗CA72");
        // <property name="color" value="RED" />
        Method setColor = clazz.getDeclaredMethod("setColor",String.class);
        setColor.invoke(car,"RED");
        // <property name="maxSpeed" value="200"/>
        Method setMaxSpeed = clazz.getDeclaredMethod("setMaxSpeed",int.class);
        setMaxSpeed.invoke(car,200);

        return car;
    }
```
#### ClassLoader
工作过程

装载（导入Class文件） - 校验（检查正确性） - 准备（给类静态变量分配空间） - 解析（符号引用转为直接引用） - 初始化（对类静态变量，类静态代码初始化）

类装载器

根装载器，ExtClassLoader， AppClassLoader

“全盘负责委托机制”  

全盘负责 ： 除显示使用外，一个ClassLoader加载某类及其依赖和引用类

委托机制 ： 先委托父装载器寻找目标类，找不到才从自己类路径下查找并装载。

### 生命周期
* Bean级生命周期接口（直接Bean实现即可）
	* BeanNameAware 
	* BeanFactoryAware
	* ApplicationContextAware
	* InitializingBean
	* DisposableBean
* 容器级生命周期接口（自主实现接口，并注册到容器中）
	* InstantiationAwareBeanPostProcessor
	* BeanPostProcessor

#### 大致流程
启动容器 - 调用BeanFactoryPostProcessor的postProcessBeanFacoty() - (通过getBean()调用某个Bean) - 调用InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation() - 实例化 - 调用InstantiationAwareBeanPostProcessor的postProcessAfterInstantiation() - 调用InstantiationAwareBeanPostProcessor的postProcessPropertyValues() - 设置属性值 - 调用BeanNameAware的setBeanName() - 调用BeanFactoryAware的setBeanFactory() - 调用ApplciationContextAware的setApplicationContext() - 调用BeanPostProcessor的postProcessBeforeInitialization() - InitializingBean的afterPropertiesSet() - 通过init-method 属性配置的初始化方法 - 调用BeanPostProcessor的postProcessAfterInitialization() - ... ... - 当容器销毁时 - 调用DisposableBea的dedstory() - 调用通过destroy-method属性配置的销毁方法
### 使用xml文件定义Bean
Car.class
```java
package com.yuhang.ioc.bean;

public class Car {
    private String brand;
    private String color;

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
// 省略 getter/setter
}

```

在配置文件中定义一个单例car Bean。并且属性注入。

spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="car" class="com.yuhang.ioc.bean.Car"
        p:brand="123"
          p:color="red"
    />
</beans>
```

启动spring，从容器中获得car Bean。

```java
package com.yuhang;

import com.yuhang.ioc.bean.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Car car = (Car)applicationContext.getBean("car");
        Car car2 = (Car)applicationContext.getBean("car");
        System.out.println(car);
        System.out.println(car==car2);
    }
}
// output: 
//Car{brand='123', color='red'}
//true

```

#### 其他注入方式
##### 构造函数注入

```java
public class Car {
    private String brand;
    private String color;

    public Car(String brand, String color) {
        this.brand = brand;
        this.color = color;
    }
    
    

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

// getter .. setter..
}

```

```xml
<bean id="car" class="com.yuhang.ioc.bean.Car">
        <constructor-arg index="0" value="123"/>
        <constructor-arg index="1" value="red"/>
</bean>
```
还可以通过指定type参数来确定入参。但例子中入参为两个同为String的类型，故需要通过index指定顺序。

##### 静态工厂方法注入
```java
package com.yuhang.ioc.bean;

public class CarFactory {
    public static Car createCar() {
        Car car = new Car();
        car.setBrand("123");
        car.setColor("red");
        return car;
    }
}
```

```xml
<bean id="car2" class="com.yuhang.ioc.bean.CarFactory" factory-method="createCar" />
```
只需要指定工厂类和工厂方法即可，不要指定生成类。

##### 非静态工厂方法注入
```java
package com.yuhang.ioc.bean;

public class CarFactory {
    public Car createCar() {
        Car car = new Car();
        car.setBrand("123");
        car.setColor("red");
        return car;
    }
}

```

```xml
    <bean id="carFactory" class="com.yuhang.ioc.bean.CarFactory" />
    <bean id="car2" factory-bean="carFactory" factory-method="createCar" />
```
对于非静态工厂方法，只需要构造工厂Bean，再从工厂Bean中生成需要的Bean。

### 使用注解配置

Bean类加注解
```java
package com.yuhang.ioc.bean;

import org.springframework.stereotype.Component;
@Component
public class Car {
    private String brand;
    private String color;

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
// getter  setter  
}

```

xml中配置包扫描
```xml
<context:component-scan base-package="com.yuhang.ioc.bean" />
```
在需要使用的地方加@Autowired注解（配置了scan之后就不需要声明`<context:annotation-config/>` ）
### 注解配置类

```java
package com.yuhang;

import com.yuhang.ioc.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConf {

    @Bean
    public Car car() {
        Car car = new Car();
        car.setColor("red");
        car.setBrand("1234");
        return car;
    }
}

```
启动类
```java
package com.yuhang;

import com.yuhang.ioc.bean.Car;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.stereotype.Component;

@Component
public class Application {



    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConf.class);
        Car car = (Car)applicationContext.getBean("car");
        System.out.println(car);


    }
}

```

## AOP
AOP的底层实现原理有两种：
1. JDK的动态代理   InvocationHandler、Proxy.newInstancec(targetClassLoader,targetInterface,InvocationHandlerImpl)
2. CGLib动态代理   Enhance.create(targetClass, MethodInterceptorImpl)

### 增强
1. 前置增强   
2. 后置增强   
3. 环绕增强   
4. 异常抛出增强
5. 引介增强
6. Final增强

### 使用注解配置

Aspect注解类
```java
package com.yuhang.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PreGreetingAspect {

    @Before("execution(* greetTo(..))")
    public void beforeGreeting() {
        System.out.println("Hello");
    }
}
```
xml文件中设置AOP自动装配
```xml
    <aop:aspectj-autoproxy />
```
### 切点表达式函数

类别 | 函数 | 入参 | 说明
:----: | :---: | :---: | :---:
方法切点函数 | execution() | 方法匹配模式串 | execution(* greetTo(..))任何目标类中的任何返回值任何入参函数名为greetTo的。
 | @annotation() | 方法注解类名 | @annotation(com.smart.anno.NeedTest)  所有注解@NeedTest的方法
 方法入参函数 | args() | 类名 | args(com.smart.Waiter) 匹配 只有一个入参且入参类型匹配Waiter的方法
 | @Args() | 类型注解类名 | @args(com.smart.Monitorable) 匹配 只有一个入参且入参标注了@monitorable的方法
 目标类切点函数 | within() | 类名匹配串 | within(com.smart.service.*) 表示com.smart.service包下的所有方法。
 |target() | 类名 | target(com.smart.Waiter) 匹配 Waiter及Waiter所有子类的所有方法)
 | @within(0 | 类型注解类名 | @within(com.smart.Monitorable) 匹配 标注了@Monitorable注解的类及子类的所有方法
 | @target（） | 类型注解类名 | @within(com.smart.Monitorable) 匹配 标注了@Monitorable注解的类

### 注解配置示例

#### 环绕增强
```java
@Aspect
public class TestAspect {
	@Around("execute(* greetTo(..))")
    public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable{
    	// ...
        pjp.proceed();
        // ...
    
    }

}
```

#### 异常增强
```java
@Aspect
public class TestAspect {
	@AfterThrowing(value="target(com.smart.SmartSeller)",throwing="iae)
    public void bindExceptation(Exception iae) {
    	// ....
    
    }

}
```

## Spring 事务
* 原子性 一个事务中的所有操作执行成功，整个事务才会提交
* 一致性  数据不会被破坏
* 隔离性  不同隔离级别不同设定
* 持久性  数据被持久化到数据库中

## Spring MVC
