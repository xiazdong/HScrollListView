package info.xiazdong.widget;

import android.view.View;

/**
 * Created by xiazdong on 2014-09-21.
 */

/**
 * HScrollListView 的内容适配器
 * @param <T> 每个item的封装类型
 */
public abstract class HScrollListViewAdapter<T>{
    private T[] mDatas;
    public HScrollListViewAdapter(T[] datas){
        this.mDatas = datas;
    }

    /**
     * 需要自己实现的函数，即将第 index 的 view 自定义赋值
     * @param view 第index个item的view
     * @param index 索引
     * @param data 元素数据
     */
    public abstract void convert(View view, int index, T data);

    /**
     * 根据索引获取元素的数据
     */
    public T getDataByIndex(int index){
        return mDatas[index];
    }

    /**
     * 元素个数
     */
    public int getCount(){
        return mDatas.length;
    }
}