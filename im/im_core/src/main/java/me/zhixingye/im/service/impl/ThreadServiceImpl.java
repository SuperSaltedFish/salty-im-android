package me.zhixingye.im.service.impl;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.zhixingye.im.service.ThreadService;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月17日.
 */
public class ThreadServiceImpl implements ThreadService {

    private Handler mUIHandler = new Handler();

    private Executor mWorkExecutor = new ThreadPoolExecutor(
            1,
            Runtime.getRuntime().availableProcessors() + 1,
            30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void runOnUIThread(Runnable runnable) {
        mUIHandler.post(runnable);
    }

    @Override
    public void runOnWorkThread(Runnable runnable) {
        mWorkExecutor.execute(runnable);
    }
}
