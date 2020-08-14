package com.unis.kotlin

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.*

class MyCrashHandler : Thread.UncaughtExceptionHandler{
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (t != null) {
            Log.e("程序出现异常了", "Thread = " + t.getName() + "\nThrowable = " + e!!.message)
        }
        val stackTraceInfo = getStackTraceInfo(e)
        Log.e("stackTraceInfo", stackTraceInfo)
        saveThrowableMessage(stackTraceInfo)
    }



    private fun getStackTraceInfo(throwable: Throwable?) : String{

        var pw: PrintWriter? = null
        val writer = StringWriter()
        try {
            pw = PrintWriter(writer)
            throwable!!.printStackTrace(pw)
        } catch (e: Exception) {
            return ""
        } finally {
            pw?.close()
        }
        return writer.toString()


    }

    private val logFilePath = Environment.getExternalStorageDirectory().toString() + File.separator + "Android" +
            File.separator + "data" + File.separator + BaseApplication.getInstance()!!.getPackageName() + File.separator + "crashLog"


    private fun saveThrowableMessage(errorMessage: String) {
        if (TextUtils.isEmpty(errorMessage)) {
            return
        }
        val file = File(logFilePath)
        if (!file.exists()) {
            val mkdirs = file.mkdirs()
            if (mkdirs) {
                writeStringToFile(errorMessage, file)
            }
        } else {
            writeStringToFile(errorMessage, file)
        }
    }

    private fun writeStringToFile(errorMessage: String, file: File) {
        Thread(Runnable {
            var outputStream: FileOutputStream? = null
            try {
                val inputStream = ByteArrayInputStream(errorMessage.toByteArray())

                outputStream = FileOutputStream(File(file, System.currentTimeMillis().toString() + ".txt"))
                var len : Int = 0
                val bytes = ByteArray(1024)
                do {
                    len = inputStream.read(bytes)
                    outputStream.write(bytes, 0, len)
                }while (len != -1)
                outputStream.flush()
                Log.e("程序出异常了", "写入本地文件成功：" + file.absolutePath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }).start()
    }
    
}