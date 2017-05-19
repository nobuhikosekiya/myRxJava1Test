/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myrxjava1tests;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 * @author nsekiya
 */
public class ThreadTest_partlambda1 {

    public static void main(String[] args) throws Exception {
        final int EMIT_COUNT = 6;
        CountDownLatch countDownLatch = new CountDownLatch(EMIT_COUNT);
        
        Action1 observer = (Action1<String>) (String s) -> {
            try {
                System.out.println(s + " :Start sleeping for a second on thread: " + Thread.currentThread().getName() );
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadTest_partlambda1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(s + " :End of sleeping for a second on thread: " + Thread.currentThread().getName() );
            countDownLatch.countDown();
        };
        
        Observable.just("a", "b").observeOn(Schedulers.immediate()).subscribe(observer);
        

        Observable.just("c", "d").observeOn(Schedulers.immediate()).subscribeOn(Schedulers.computation()).subscribe(observer);

        Observable.just("e", "f").observeOn(Schedulers.computation()).subscribe(observer);

        countDownLatch.await();
//        Observable.just("a", "b").subscribe(s -> System.out.println(s) );
    }
}
