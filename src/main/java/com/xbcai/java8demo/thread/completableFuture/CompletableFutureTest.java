package com.xbcai.java8demo.thread.completableFuture;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * CompletableFuture一些例子
 */
public class CompletableFutureTest {
    @SuppressWarnings("all")
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private static Random rnd = new Random();
    static int delayRandom(int min,int max){
        int milli = max>min?rnd.nextInt(max-min):0;
        try {
            Thread.sleep(min+milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return milli;
    }
    /**
     * 在一个阶段正常完成后，再执行下一个任务,这里的join()是让主线程等待它的任务执行完才能往下走
     * 这里有三个异常的任务，通过thenRun自然的描述了他们的依赖关系，thenRun是同步版本，有对应的异步版本thenRunAsync
     * 在thenRun构建的任务流中，只有前一个阶段没有异常结束，下一个阶段的任务才会执行，如果前一个阶段发生了异常，
     * 所有后续阶段都不会运行，结果会被設置为相同的异常，调用join会抛出运行时异常CompletionException
     * 该例子中，下一个阶段任务的执行不需要前一个阶段的结果作为参数
     *
     * 场景：一个阶段完成再进行下一个阶段，下一个阶段不需要前一个阶段的返回值作为参数
     */
    public static void testThenRun(){
        System.out.println("==================main=======testThenRun========begin=======================");
        Runnable taskA = () -> System.out.println("task A");
        Runnable taskB = () -> System.out.println("task B");
        Runnable taskC = () -> System.out.println("task C");
        CompletableFuture.runAsync(taskA).thenRun(taskB).thenRun(taskC).join();
        System.out.println("================main=========testThenRun=========end======================");
    }

    /**
     * 任务A的输出结果传递给任务B，任务B转换成大写，将结果传给了任务C，任务C将处理后的结果输出
     *
     * 如果下一个任务需要前一个阶段的结果作为参数，可以使用thenAccept或thenApply方法，
     * thenAccept的任务类型时Consumer,它接受前一个阶段的结果作为参数，没有返回值；
     * thenApply的任务类型时Function，接受前一个阶段的结果作为参数，返回一个新的值，这个值会成为thenApply返回的CompletableFuture的结果值
     *
     * 场景：一个阶段完成再进行下一个阶段，下一个阶段需要前一个阶段的返回值作为参数
     */
    public static void testThenApplyAndThenAccept(){
        System.out.println("============================testThenApplyAndThenAccept==================================");
       Supplier<String> taskA = ()->"hello";
        Function<String,String> taskB = (t)->t.toUpperCase();
        Consumer<String> taskC = (t) -> System.out.println("consume:" + t);
        CompletableFuture.supplyAsync(taskA).thenApply(taskB).thenAccept(taskC).join();
    }

    /**
     * 任务A的输出结果传递给任务B，任务B转换成大写，将结果传给了任务C，任务C将处理后的结果输出，只不过任务B是一个转换函数，它自己也执行了异步的任务。
     *
     * thenCompose这个任务类型也是Function，也是接受前一个阶段的结果，返回一个新的结果，不过，这个转换函数fn的返回值类型是CompletionStage,也就是说它的返回值也是一个阶段。
     *
     * 场景：一个阶段完成再进行下一个阶段，下一个阶段需要前一个阶段的返回值作为参数
     */
    public static void testThenComposeAndThenAccept(){
        System.out.println("============================testThenComposeAndThenAccept==================================");
        Supplier<String> taskA = ()->"hello";
        Function<String,CompletableFuture<String>> taskB =(t)->CompletableFuture.supplyAsync(()->t.toUpperCase());
        Consumer<String> taskC = (t)-> System.out.println("consume:"+t);
        CompletableFuture.supplyAsync(taskA).thenCompose(taskB).thenAccept(taskC).join();
    }



    /**
     * 任务A和B执行结束后，执行任务C合并结果
     * thenCombine对应的任务类型是BiFunction，接受前两个阶段的结果作为参数，返回一个结果；
     *
     * 场景：两阶段都执行完毕后，再执行另一个任务
     */
    public static void testThenCombineAsyn(){
        System.out.println("============================testThenCombineAsyn==================================");
        Supplier<String> taskA = ()->"taskA";
        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->"taskB");
        BiFunction<String,String,String> taskC = (a,b)->a+","+b;
        String result = CompletableFuture.supplyAsync(taskA).thenCombineAsync(taskB, taskC).join();
        System.out.println(result);
    }

    /**
     * 只要任务A或者任务B任意一个完成，就执行任务C，这里的任务C是将前阶段的结果直接打印出来；
     * acceptEither 没有返回值，接受前两个任务任意一个完成的作为参数
     * 根据不同的场景，可以选用runAfterEither、applyToEither、acceptEither,只需要第三个任务根据他们的参数类型来构造就可以
     */
    public static void testAcceptEither(){
        System.out.println("============================testAcceptEither==================================");
        Supplier<String> taskA = ()->"taskA";
        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->"taskB");
        Consumer<String> taskC = (a) -> System.out.println("result:::" + a);
        CompletableFuture.supplyAsync(taskA).acceptEither(taskB,taskC).join();
    }

    /**
     * 如果依赖的阶段不止两个，可以利用CompletableFuture.allOf、CompletableFuture.anyOf来处理，前一个是要等待所有的任务都完成，后一个是只要有一个任务完成
     *
     * 对于allOf,当所有子CompletableFuture都完成时，它才完成，如果存在Completable-Future异常结束了，则新的CompletableFuture的结果也是异常，不过，它并不会因此有异常就提前结束，
     * 而是会等待所有阶段结束，如果有多个阶段异常结束，新的CompletableFutrue中保存的异常是最后一个，新的CompletableFuture会持有异常的结果，但不会保证正常结束的结果，如果有需要可以从每个阶段中获取。
     *
     *
     * 这里taskC首先异常结束，但新构建的CompletableFutrue会等待其他两个阶段结束，都结束后，可以通过子阶段（如task A）的方法检查子阶段的状态和结果
     *
     *
     * 对于anyOf返回的CompletableFuture，当第一个子CompletableFutrue完成或异常结束时，它相应的完成或异常结束，结果与第一个结束的子CompletableFuture一样。
     *
     * 带Async且指定Executor参数的方法，由指定的Executor执行，带Async但没有指定Executor的方法由默认Executor执行
     *
     */
    public static void testManyCompletableFuture(){
        System.out.println("============================testManyCompletableFuture==================================");
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            delayRandom(100, 1000);
            return "helloA";
        }, executor);

        CompletableFuture<Void> taskB = CompletableFuture.runAsync(() -> {
            delayRandom(2000, 3000);
        }, executor);

        CompletableFuture<Void> taskC = CompletableFuture.runAsync(() -> {
            delayRandom(30, 100);
            throw new RuntimeException("task C exception");
        }, executor);

        CompletableFuture.allOf(taskA,taskB,taskC).whenComplete((result,ex)->{
            if(ex!=null){
                System.out.println(ex.getMessage());
            }
            if(!taskA.isCompletedExceptionally()){
                System.out.println("task A"+taskA.join());
            }
        });

    }
    public static void main(String[] args) {
        testThenRun();
        testThenApplyAndThenAccept();
        testThenComposeAndThenAccept();
        testThenCombineAsyn();
        testAcceptEither();
        testManyCompletableFuture();
    }
}
