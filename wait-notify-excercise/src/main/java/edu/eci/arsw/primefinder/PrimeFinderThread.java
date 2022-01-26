package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b,waitingTime;
	private long start = System.currentTimeMillis();
	private List<Integer> primes;
	private Object lock;
	
	public PrimeFinderThread(int a, int b,int waitingTime, Object lock ) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		this.waitingTime = waitingTime;
		this.lock = lock;
	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){
            	synchronized (lock){
            		while(System.currentTimeMillis() - start >  waitingTime){
            			try{
            				lock.wait();
							System.out.println("Hilo detenido");
						}catch (InterruptedException ex){
            				ex.printStackTrace();
						}
					}
				}

                if (isPrime(i)){
                    primes.add(i);
                    System.out.println(i);
                }

            }
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
}
