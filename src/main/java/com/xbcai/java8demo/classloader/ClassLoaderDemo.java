package com.xbcai.java8demo.classloader;

import com.xbcai.java8demo.utils.BinaryFileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 自定义类加载器demo
 */
public class ClassLoaderDemo {
    private static final String CLASS_NAME="com.xbcai.java8demo.classloader.HelloServiceImpl";
    private static final String FILE_NAME="C:\\Users\\46143\\IdeaProjects\\java8-demo\\src\\main\\java\\com\\xbcai\\java8demo\\classloader\\";
    private static volatile HelloService helloService;

    public static HelloService getHelloService(){
        if (helloService!=null){
            return helloService;
        }
        synchronized (ClassLoaderDemo.class){
            if(helloService==null){
                helloService = createHelloService();
            }
            return helloService;
        }
    }

    private static HelloService createHelloService(){
        try {
            MyClassLoader c1 = new MyClassLoader();
            Class<?> cls = c1.loadClass(CLASS_NAME);
            if(cls!=null){
                return (HelloService) cls.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void client(){
        try {
            new Thread(()->{
                while (true){
                    HelloService helloService = getHelloService();
                    helloService.sayHello("xbcai");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void monitor(){
        Thread t = new Thread(){
            long lastModified = new File(FILE_NAME).lastModified();

            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long now = new File(FILE_NAME).lastModified();
                    if(now!=lastModified){
                        lastModified = now;
                        reloadHelloService();
                    }

                }
            }
        };
      t.start();
    }

    private static void reloadHelloService(){
        helloService = createHelloService();
    }
    public static void main(String[] args) throws Exception {
//        System.out.println(ClassLoader.getSystemClassLoader());
//        MyClassLoader my1 = new MyClassLoader();
//        Class<?> aClass = my1.loadClass("com.xbcai.java8demo.classloader.HelloServiceImpl");
//        MyClassLoader my2 = new MyClassLoader();
//        Class<?> aClass1 = my2.loadClass("com.xbcai.java8demo.classloader.HelloServiceImpl");
//        System.out.println(aClass==aClass1);
//        HelloService cast = (HelloService)(aClass1.newInstance());
//        cast.sayHello("xbcai");

        monitor();
        client();

    }



}

/**
 * 类加载性
 */
class MyClassLoader extends ClassLoader{
    private static final String BASE_DIR="C:\\Users\\46143\\IdeaProjects\\java8-demo\\src\\main\\java\\com\\xbcai\\java8demo\\classloader\\";
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = name.replaceAll("\\.", "/");
        fileName = BASE_DIR+fileName+".class";
        try {
            byte[] bytes = BinaryFileUtils.readFileToByteArray(fileName);
            return  defineClass(name,bytes,0,bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("failed to load class "+name,e);
        }
    }
}
