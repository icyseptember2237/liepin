package com.liepin.common.config.thread;

import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 异步任务执行器
public class AsyncExecutor {

    private static final Logger logger = LoggerFactory.getLogger(AsyncExecutor.class);

    private ScheduledExecutorService scheduledExecutorService;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private AsyncExecutor(){
        this.scheduledExecutorService = SpringUtil.getBean("mySchedulerExecutor");
        this.threadPoolTaskExecutor = SpringUtil.getBean("myAsyncExecutor");
    }

    // 使用static创造单例模式
    private static AsyncExecutor executor = new AsyncExecutor();

    // 使用static创造单例模式
    public static AsyncExecutor getExecutor(){
        return executor;
    }

    public void execute(Thread thread){
        threadPoolTaskExecutor.execute(thread);
    }

    public void schedule(Thread thread){
        scheduledExecutorService.schedule(thread,10,TimeUnit.MILLISECONDS);
    }

    public void schedule(Thread thread,int delayMilliSeconds){
        scheduledExecutorService.schedule(thread,delayMilliSeconds,TimeUnit.MILLISECONDS);
    }

    public void shutdown(){
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()){
            // 关闭线程池
            scheduledExecutorService.shutdown();
            try {
                // 等待30秒以便线程池中的所有任务完成执行
                if (!scheduledExecutorService.awaitTermination(30,TimeUnit.SECONDS)){
                    // 30秒后线程池还未停止则再次关池并等待
                    logger.info("ScheduledExecutor didn't end normally.");
                    scheduledExecutorService.shutdown();
                    if (!scheduledExecutorService.awaitTermination(30,TimeUnit.SECONDS)){
                        // 线程池依旧未关闭则强行关闭
                        logger.info("Shut The ScheduledExecutor Forcefully!");
                        scheduledExecutorService.shutdownNow();
                    }

                }
            } catch (InterruptedException ie) {
                // 强行关闭线程池
                scheduledExecutorService.shutdownNow();
                logger.info("ScheduledExecutor Forced to Shutdown!");
                Thread.currentThread().interrupt();
            }
        }
    }

}
