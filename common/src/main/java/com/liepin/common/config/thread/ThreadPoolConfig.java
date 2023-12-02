package com.liepin.common.config.thread;



import com.liepin.common.config.exception.AssertUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);
    // 核心线程池大小
    private int corePoolSize = 20;

    // 最大可创建的线程数
    private int maxPoolSize = 50;

    // 队列最大长度
    private int queueCapacity = 300;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    // 线程池
    @Bean(name = "myAsyncExecutor")
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        //executor.setThreadNamePrefix("main-pool");
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        logger.info("ThreadPool Inited, CorePoolSize: {} MaxPoolSize: {} ThreadNamePrefix: {}",corePoolSize,maxPoolSize,executor.getThreadNamePrefix());
        return executor;
    }

    // 定时任务线程池
    @Bean(name = "mySchedulerExecutor")
    protected ScheduledExecutorService scheduledExecutorService()
    {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);

                // Future类型的Runnable直接通过Future返回异常
                if (t == null && r instanceof Future<?>){
                    try {
                        if (((Future<?>) r).isDone())
                            ((Future<?>) r).get();
                    } catch (CancellationException ce) {
                        t = ce;
                    } catch (ExecutionException ee) {
                        t = ee.getCause();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }

                //非Future类型
                if (t != null){
                    AssertUtils.throwException(t.getMessage());
                }
            }
        };
    }
}
