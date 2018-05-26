package com.scheduled.excutorservice;

import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class ScheduledExcutorServiceTest {
	public static void main(String[] args) throws InterruptedException {
		new BeeperControl().beep();
	}
}

class BeeperControl {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3); // 원하 만큼 쓰레드 생성 현재는 3
	private int count = 0;

	public void beep() {
		final Runnable beeper = new Runnable() {
			public void run() {
				count++;
				System.out.format("beep! count=%d : currTh=%s : activeTh=%d %s \n", count, Thread.currentThread(), Thread.activeCount(), Thread.currentThread().getName());
			}
		};
		
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 30, 30, SECONDS);

		scheduler.schedule(new Runnable() {
			public void run() {
				beeperHandle.cancel(true);
				scheduler.shutdown();
				try {
					scheduler.awaitTermination(3, SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("스케줄 익스큐터 3초 후 종료!");					
				}
			}
		}, 30 * 5, SECONDS);
	}
	
}

// 30초 지연 후 task 스케줄링 시작
// 30초 마다 task 동작
// 150초 후 스케줄러 정지
// 3초 후 ScheduledExecutorService 종료