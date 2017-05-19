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
public class ThreadTest3 {
    
    public static void main(String[] args) throws Exception {
        final int EMIT_COUNT = 6;
        CountDownLatch countDownLatch = new CountDownLatch(EMIT_COUNT);
        
        Action1 observer = s -> {
            try {
                System.out.println(s + " :[Observing Thread] Start sleeping for a second on thread: " + Thread.currentThread().getName());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(s + " :[Observing Thread] End of sleeping for a second on thread: " + Thread.currentThread().getName());
            countDownLatch.countDown();
        };
        
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    String s = Long.toString(System.currentTimeMillis());
                    System.out.println(s + " :[Subscribing Thread] Start sleeping for a second on thread: " + Thread.currentThread().getName());
                    Thread.sleep(1000);
                    System.out.println(s + " :[Subscribing Thread] End of sleeping for a second on thread: " + Thread.currentThread().getName());
                    observer.onNext("a " + s);
                    observer.onNext("b " + s);

                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
        
        observable.observeOn(Schedulers.immediate()).subscribe(observer);
        
        observable.observeOn(Schedulers.newThread()).subscribeOn(Schedulers.computation()).subscribe(observer);
//        observable.observeOn(Schedulers.immediate()).subscribeOn(Schedulers.computation()).subscribe(observer);
        observable.observeOn(Schedulers.io()).subscribe(observer);
        
        countDownLatch.await();
    }
}
