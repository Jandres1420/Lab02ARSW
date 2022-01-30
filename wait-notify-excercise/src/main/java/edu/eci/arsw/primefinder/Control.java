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
    private int n;
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
            for(int i = 0; i < NTHREADS; i++ ) {
                exec = pft[i].isAlive();
                if(exec){
                    break;
                }
            }
            if(!exec){
                break;
            }
            if (System.currentTimeMillis() - start >= TMILISECONDS) {
                n = 0;
                PrimeFinderThread.setWaiting(false);
                synchronized (pivot) {
                    for(int i=0 ; i < NTHREADS; i++){
                        n += pft[i].getPrimes().size();
                    }
                    System.out.println("numero de Primos: "+ String.valueOf(n));
                    System.out.println("presione enter para continuar");
                    if (in.nextLine().equals("")) {
                        PrimeFinderThread.setWaiting(true);
                        start = System.currentTimeMillis();
                        pivot.notifyAll();
                    }
                }
            }

        }
    }
    
}
