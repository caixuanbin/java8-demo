package com.xbcai.java8demo.classloader;

public interface HelloService {
    void sayHello(String name);
}

class HelloServiceImpl implements HelloService{
    @Override
    public void sayHello(String name) {
        System.out.println("hello "+name);
    }

}

class HelloServiceImpl2 implements HelloService{
    @Override
    public void sayHello(String name) {
        System.out.println("the two sayHello "+name);
    }
}
