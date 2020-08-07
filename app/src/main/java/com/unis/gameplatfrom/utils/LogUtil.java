package com.unis.gameplatfrom.utils;

import android.util.Log;

import com.unis.gameplatfrom.Constant;

public class LogUtil {

    private final static String APP_TAG = "appName";

    public static void v(String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.v(APP_TAG, getMsgFormat(msg));
        }
    }

    public static void v(String tag, String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.v(tag, getMsgFormat(msg));
        }
    }


    public static void d(String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.d(APP_TAG, getMsgFormat(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.d(tag, getMsgFormat(msg));
        }
    }


    public static void i(String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.i(APP_TAG, getMsgFormat(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.i(tag, getMsgFormat(msg));
        }
    }


    public static void w(String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.w(APP_TAG, getMsgFormat(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.w(tag, getMsgFormat(msg));
        }
    }


    public static void e(String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.e(APP_TAG, getMsgFormat(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (Constant.IS_PRINT_LOG) {
            Log.e(tag, getMsgFormat(msg));
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (Constant.IS_PRINT_LOG) {
            Log.e(tag, getMsgFormat(msg), e);
        }
    }

    public static void e(String msg, Throwable e) {
        if (Constant.IS_PRINT_LOG) {
            Log.e(APP_TAG, getMsgFormat(msg), e);
        }
    }

    private static String getMsgFormat(String msg) {
        return getFunctionName() + " : " + msg;
    }

    /**
     * 格式 Thread:线程名 类名.方法名
     *
     * @return
     */
    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            int index = -1;
            String className = "";
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }

                if (st.getClassName().equals(LogUtil.class.getName())) {
                    continue;
                }

                //com.aa.bb.类名->类名
                if ((index = st.getClassName().lastIndexOf(".")) != -1) {
                    className = st.getClassName().substring(index + 1);
                }

                return "Thread:" + Thread.currentThread().getName()
                        + " " + className
                        + "." + st.getMethodName();
            }
        }
        return null;
    }
}
