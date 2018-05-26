package com.scheduled.executorservice.more;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author victor
 * Thread Scheduling with ScheduledExecutorService
 * execute sh script
 * cat a file and read result of that
 * create a json file called 10000product.json with 10000 lines under the directory, "~/documents/csvtest/"
 */
public class ScheduledExecutorServiceTest {
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private int count = 0;

	public static void main(String[] args) {
		new ScheduledExecutorServiceTest().exec();
	}

	/**
	 * 1. create runnable and define it 
	 * 2. task: read 10000 lines of json file
	 * 3. scheduling task with executor
	 * 4. every 3secs, task is scheduled to be executed
	 * 5. stopScheduler method stops scheduler
	 * 
	 * ??? check how to solve thread which stops during scheduling
	 */
	void exec() {
		Runnable runnable = new Runnable() {
			Process p = null;
			@Override
			public void run() {
				System.out.format("beep! >> currTh=%s : activeTh=%d %s \n", Thread.currentThread(), Thread.activeCount(), Thread.currentThread().getName());

				try {
					String cmdArray[] = { "sh", "-c", "cat ~/documents/csvtest/1000product.json" };
					p = Runtime.getRuntime().exec(cmdArray);
//					p.waitFor(); // waiFor() here pauses Thread 
					printHistory(p);
					count++;
					System.out.println("task # : " + count);
					p.waitFor();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				} finally {
					p.destroy();
				}
			}
		};

		final ScheduledFuture<?> runnableHandler = scheduler.scheduleAtFixedRate(runnable, 3, 3, TimeUnit.SECONDS);
		this.stopScheduler(runnableHandler, 3 * 5);
	}

	/**
	 * @param runnableHandler
	 * @param timeToStop
	 * accept runnableHandler, managing ScheduledExecutorService, and timeToStop, controlling when to stop scheduling
	 * scheduler stops working after 3 seconds from timeToStop
	 */
	void stopScheduler(ScheduledFuture<?> runnableHandler, long timeToStop) {
		scheduler.schedule(new Runnable() {
			public void run() {
				runnableHandler.cancel(true);
				scheduler.shutdown();
				try {
					scheduler.awaitTermination(3, SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("Schedule Executor stops after 3 secs!");
				}
			}
		}, timeToStop, SECONDS);
	}

	/**
	 * @param process
	 *            get the result of p, and then read executed history from shell script
	 * @throws IOException
	 */
	void printHistory(Process p) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ((line = br.readLine()) != null) System.out.println(line);
		if (br != null) br.close();

	}

}