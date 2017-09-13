package com.robotshell.timerecorder.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.bean.Wisdom;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * Created by zhaojie on 17-9-13.
 */

public class WisdomDataManager {
    private static WisdomDataManager gInstance;

    Context context;

    private WisdomDataManager(Context context) {
        this.context = context;
    }

    public static WisdomDataManager getInstance(Context context) {
        if (gInstance == null) {
            gInstance = new WisdomDataManager(context);
        }

        return gInstance;
    }

    public void getWisdom(int id, final GetWisdomCallback callback) {
        BmobQuery<Wisdom> query = new BmobQuery<>();
        query.addWhereEqualTo("id", id);
        query.findObjects(new FindListener<Wisdom>() {
            @Override
            public void done(List<Wisdom> list, BmobException e) {
                if (callback != null) {
                    if (e == null) {
                        if (list.size() > 0) {
                            callback.onGetWisdom(list.get(0));
                        } else {
                            callback.onGetWisdom(getDefaultWisdom());
                        }
                    } else {
                        callback.onGetWisdom(getDefaultWisdom());
                    }
                }
            }
        });
    }

    private Wisdom getDefaultWisdom() {
        Wisdom wisdom = new Wisdom();
        wisdom.author = context.getString(R.string.wisdom_author);
        wisdom.wisdom = context.getString(R.string.wisdom);
        return wisdom;
    }

    public void getWisdomCount(final GetWisdomCountCallback callback) {
        String bql = "select count(*), * from Wisdom";
        new BmobQuery<Wisdom>().doSQLQuery(bql, new SQLQueryListener<Wisdom>() {

            @Override
            public void done(BmobQueryResult<Wisdom> result, BmobException e) {
                int count = 0;
                if (e == null) {
                    count = result.getCount();
                }
                if (callback != null) {
                    callback.onGetCount(count);
                }
            }
        });
    }

    public void randomWisdom(final GetWisdomCallback callback) {
        getWisdomCount(new GetWisdomCountCallback() {
            @Override
            public void onGetCount(int count) {
                if (callback != null) {
                    if (count > 0) {
                        Random random = new Random();
                        int id = random.nextInt(count);
                        getWisdom(id, callback);
                    } else {
                        callback.onGetWisdom(getDefaultWisdom());
                    }
                }
            }
        });
    }

    public interface GetWisdomCallback {
        void onGetWisdom(Wisdom wisdom);
    }

    public interface GetWisdomCountCallback {
        void onGetCount(int count);
    }
}
