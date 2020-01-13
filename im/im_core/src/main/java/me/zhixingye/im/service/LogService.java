package me.zhixingye.im.service;

import android.util.Log;

/**
 * Created by zhixingye on 2020年01月13日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class LogService {

    private static LogInterface sLogger;

    private static final LogInterface DEFAULT_LOGGER = new AndroidLogImpl();

    public static LogInterface getLogger() {
        return sLogger == null ? DEFAULT_LOGGER : sLogger;
    }

    public static void setLogger(LogInterface logger) {
        LogService.sLogger = logger;
    }

    public interface LogInterface {
        void v(String tag, String msg);

        void v(String tag, String msg, Throwable tr);

        void d(String tag, String msg);

        void d(String tag, String msg, Throwable tr);

        void i(String tag, String msg);

        void i(String tag, String msg, Throwable tr);

        void w(String tag, String msg);

        void w(String tag, String msg, Throwable tr);

        void e(String tag, String msg);

        void e(String tag, String msg, Throwable tr);

        void wtf(String tag, String msg);

        void wtf(String tag, String msg, Throwable tr);
    }

    public static class AndroidLogImpl implements LogInterface {

        public void v(String tag, String msg) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.v(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg));
        }


        public void v(String tag, String msg, Throwable tr) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.v(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg), tr);
        }

        public void d(String tag, String msg) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.d(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg));
        }

        public void d(String tag, String msg, Throwable tr) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.d(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg), tr);
        }

        public void i(String tag, String msg) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.i(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg));
        }

        public void i(String tag, String msg, Throwable tr) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.i(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg), tr);
        }

        public void w(String tag, String msg) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.w(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg));
        }

        public void w(String tag, String msg, Throwable tr) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.w(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg), tr);
        }

        public void e(String tag, String msg) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.e(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg));
        }

        public void e(String tag, String msg, Throwable tr) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.e(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg), tr);
        }

        public void wtf(String tag, String msg) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.wtf(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg));
        }

        public void wtf(String tag, String msg, Throwable tr) {
            if (msg == null) {
                msg = "null";
            }
            String[] stackInfo = generateStackInfo();
            Log.wtf(tag, String.format("%s(%s.java:%s) ==>  %s", stackInfo[1], stackInfo[0], stackInfo[2], msg), tr);
        }

        private String[] generateStackInfo() {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
            String className = stackTraceElement.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            return new String[]{
                    className,
                    stackTraceElement.getMethodName(),
                    String.valueOf(stackTraceElement.getLineNumber())};
        }
    }
}
