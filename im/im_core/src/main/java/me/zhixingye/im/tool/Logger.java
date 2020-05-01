package me.zhixingye.im.tool;

import android.util.Log;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public class Logger {

    private static LogInterface sLogger;

    private static final LogInterface DEFAULT_LOGGER = new AndroidLogImpl();

    private static LogInterface getLogger() {
        return sLogger == null ? DEFAULT_LOGGER : sLogger;
    }

    public static void setLogger(LogInterface logger) {
        Logger.sLogger = logger;
    }

    public static void v(String tag, String msg) {
        getLogger().v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        getLogger().v(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        getLogger().d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        getLogger().d(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        getLogger().i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        getLogger().i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        getLogger().w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        getLogger().w(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        getLogger().e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        getLogger().e(tag, msg, tr);
    }

    public static void wtf(String tag, String msg) {
        getLogger().wtf(tag, msg);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        getLogger().wtf(tag, msg, tr);
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
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
            String className = stackTraceElement.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            return new String[]{
                    className,
                    stackTraceElement.getMethodName(),
                    String.valueOf(stackTraceElement.getLineNumber())};
        }
    }
}
