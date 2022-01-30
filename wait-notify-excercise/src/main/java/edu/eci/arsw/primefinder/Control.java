/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.*;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;
    private Scanner in;

    private final int NDATA = MAXVALUE / NTHREADS;
    private long start = System.currentTimeMillis();
    private Object pivot;
    private boolean exec;

    private PrimeFinderThread pft[];

    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        in = new Scanner(System.in);
        pivot = new Object();
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, pivot);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, pivot);
        exec = true;

    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        while(exec) {
            if (System.currentTimeMillis() - start >= TMILISECONDS) {
                PrimeFinderThread.setWaiting(false);
                for(int i=0; i < NTHREADS; i++){
                    System.out.println(i);
                    System.out.println(pft[i].getPrimes());
                }
                System.out.println("presione enter para continuar");
                synchronized (pivot) {
                    if (in.nextLine().equals("")) {
                        PrimeFinderThread.setWaiting(true);
                        start = System.currentTimeMillis();
                        pivot.notifyAll();
                    }
                }
            }
            for(int i = 0; i < NTHREADS; i++ ) {
                exec = pft[i].isAlive();
                if(exec){
                    break;
                }
            }
        }
    }
    
}
