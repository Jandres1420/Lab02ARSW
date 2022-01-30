package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	private static boolean waiting;

	private List<Integer> primes;
	private Object lock;
	
	public PrimeFinderThread(int a, int b, Object lock ) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;

		this.lock = lock;
		this.waiting = true;
	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){
            	synchronized (lock){
            		while(!waiting){
						System.out.println("Hilo detenido");

            			try{
            				lock.wait();

						}catch (InterruptedException ex){
            				ex.printStackTrace();
						}
					}
				}

                if (isPrime(i)){
                    primes.add(i);
                    //System.out.println(i);
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
	public static void setWaiting(boolean b){
		waiting = b;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
}
