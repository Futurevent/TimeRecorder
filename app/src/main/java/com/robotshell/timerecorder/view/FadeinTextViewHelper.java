package com.robotshell.timerecorder.view;

import android.text.TextUtils;
import android.widget.TextView;

public class FadeinTextViewHelper {
    private final TextView tv;
    private String text;
    private int length;
    private long duration;
    private int n = 0;
    private int nn;
    private Thread curWorkThread;

    public FadeinTextViewHelper(TextView tv, String text, long duration) {
        this.tv = tv;
        this.text = text;
        this.duration = duration;
        this.length = text.length();
    }

    public void setText(String text) {
        synchronized (this.text) {
            this.text = text;
        }
        if (curWorkThread != null) {
            curWorkThread.interrupt();
        }
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void startTv(final int n) {
        curWorkThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (TextUtils.isEmpty(text)) {
                                return;
                            }
                            final String stv = text.substring(0, n);//截取要填充的字符串

                            tv.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(stv);
                                }
                            });

                            Thread.sleep(duration);//休息片刻
                            nn = n + 1;//n+1；多截取一个
                            if (nn <= length) {//如果还有汉字，那么继续开启线程，相当于递归的感觉
                                startTv(nn);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

        );
        curWorkThread.start();
    }


}