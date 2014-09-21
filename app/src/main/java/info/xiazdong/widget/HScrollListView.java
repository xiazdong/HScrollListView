package info.xiazdong.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by xiazdong on 14-9-19.
 */

/**
 * 横向的能够拖动的ListView
 */
public class HScrollListView extends HorizontalScrollView implements View.OnClickListener{
    private static final String TAG = HScrollListView.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout mRootContainer;
    private int mItemLayoutId;
    private int mLeftMargin;
    private int mRightMargin;
    private int mTopMargin;
    private int mBottomMargin;
    private int mHorizontalSpace;
    private int[] mIdArray;
    private int[] mImageArray;
    private String[] mTextArray;
    private OnItemClickListener mListener;
    private boolean mNeedSmoothScroll = true;
    private int mItemWidth;
    private int mItemHeight;
    private boolean mSingleSelectedEnabled;
    private boolean mMultiSelectedEnabled;
    private int mCurrentSelectedItem = -1;
    private ArrayList<Integer> mSelectedList = new ArrayList<Integer>();
    private boolean mToggleSelf;
    private int mDefaultSelectedIndex;
    private boolean mClipToMargin;
    private boolean mDistribute;
    public HScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        mRootContainer = new LinearLayout(context);
        mRootContainer.setOrientation(LinearLayout.HORIZONTAL);
        mRootContainer.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HScrollListView, defStyle, 0);
            mItemLayoutId = array.getResourceId(R.styleable.HScrollListView_itemLayoutId, 0);
            mLeftMargin = array.getDimensionPixelSize(R.styleable.HScrollListView_leftMargin, 0);
            mRightMargin = array.getDimensionPixelSize(R.styleable.HScrollListView_rightMargin, 0);
            mTopMargin = array.getDimensionPixelSize(R.styleable.HScrollListView_topMargin,0);
            mBottomMargin = array.getDimensionPixelSize(R.styleable.HScrollListView_bottomMargin,0);
            mHorizontalSpace = array.getDimensionPixelSize(R.styleable.HScrollListView_horizontalSpace, 0);
            mClipToMargin = array.getBoolean(R.styleable.HScrollListView_clipToMargin, false);
            mNeedSmoothScroll = array.getBoolean(R.styleable.HScrollListView_isSmoothScroll,true);
            mToggleSelf = array.getBoolean(R.styleable.HScrollListView_toggleSelf,false);
            mDistribute = array.getBoolean(R.styleable.HScrollListView_distribute,false);
            mSingleSelectedEnabled = array.getBoolean(R.styleable.HScrollListView_singleSelectable,false);
            mMultiSelectedEnabled = array.getBoolean(R.styleable.HScrollListView_multiSelectable,false);
            mDefaultSelectedIndex = array.getInt(R.styleable.HScrollListView_defaultSelectedIndex,-1);
            if(mDefaultSelectedIndex != -1){
                setDefaultItem(mDefaultSelectedIndex);
            }
            int textArrayResId = array.getResourceId(R.styleable.HScrollListView_textArray, 0);
            mTextArray = textArrayResId != 0 ? context.getResources().getStringArray(textArrayResId) : null;
            int imageArrayResId = array.getResourceId(R.styleable.HScrollListView_imageArray, 0);
            TypedArray imageArray = imageArrayResId != 0 ? context.getResources().obtainTypedArray(imageArrayResId) : null;
            if(imageArray != null) {
                mImageArray = new int[imageArray.length()];
                for (int i = 0; i < imageArray.length(); i++) {
                    mImageArray[i] = imageArray.getResourceId(i, 0);
                }
            }
            int idArrayResId = array.getResourceId(R.styleable.HScrollListView_idArray, 0);
            TypedArray idArray = idArrayResId != 0 ? context.getResources().obtainTypedArray(idArrayResId) : null;
            for(int i = 0; idArray != null && i < idArray.length(); i++){
                mIdArray[i] = idArray.getResourceId(i,0);
            }

            init(Math.max(mTextArray == null ? 0 : mTextArray.length, mImageArray == null ? 0 : mImageArray.length));

            if(array != null){
                array.recycle();
            }
            if(imageArray != null){
                imageArray.recycle();
            }
            if(idArray != null){
                idArray.recycle();
            }
        }
        setPadding(mLeftMargin,mTopMargin,mRightMargin,mBottomMargin);
        setClipToPadding(mClipToMargin);
        addView(mRootContainer);
    }

    /**
     * 根据已有的设置的信息对HScrollView进行初始化
     */
    private void init(int length){

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(mDistribute){
            lp.width = (mContext.getResources().getDisplayMetrics().widthPixels - mLeftMargin - mRightMargin ) / length;
        }
        else{
            lp.rightMargin = mHorizontalSpace;
        }
        for(int i = 0; i < length; i++){
            ViewGroup container = (ViewGroup)mInflater.inflate(mItemLayoutId,null);

            if((mSingleSelectedEnabled || mMultiSelectedEnabled) && mDefaultSelectedIndex == i){
                container.setSelected(true);
            }
            container.setTag(i);
            container.setOnClickListener(this);
            if(mIdArray != null){
                container.setId(mIdArray[i]);
            }
            TextView tv = (TextView)container.findViewById(R.id.text);
            ImageView image = (ImageView)container.findViewById(R.id.image);
            if(tv != null && mTextArray != null){
                tv.setText(mTextArray[i]);
            }
            if(image != null && mImageArray != null){
                image.setImageResource(mImageArray[i]);
            }
            mRootContainer.addView(container, lp);
        }
    }

    /**
     * 设置默认选中的索引
     */
    private void setDefaultItem(Integer index){
        if(!mSingleSelectedEnabled) return;
        if(mMultiSelectedEnabled){
            mSelectedList.clear();
            mSelectedList.add(index);
        }
        else{
            mCurrentSelectedItem = index;
        }
    }

    /**
     * 这个函数用户不能直接调用
     */
    @Override
    public void onClick(View view) {
        Integer index = (Integer) view.getTag();
        if (mNeedSmoothScroll) {
            if (!isLeftVisible(view)) {
                smoothScrollByPosition(-1);
            } else if (!isRightVisible(view)) {
                smoothScrollByPosition(1);
            }
        }
        if (mSingleSelectedEnabled) {
            if (mCurrentSelectedItem != -1) {
                mRootContainer.getChildAt(mCurrentSelectedItem).setSelected(false);
            }
            if (mToggleSelf && mCurrentSelectedItem == index) {
                mCurrentSelectedItem = -1;
            } else {
                mCurrentSelectedItem = index;
            }
            view.setSelected(true);
        } else if (mMultiSelectedEnabled) {
            view.setSelected(true);
            if (mSelectedList.contains(index)) {
                if (mToggleSelf) {
                    mSelectedList.remove(index);
                    view.setSelected(false);
                }
            } else {
                mSelectedList.add(index);
            }
        }
        if (mListener != null) {
            mListener.onItemClick(view, index);
        }
    }

    /**
     * 设置元素的监听器
     */
    public void setItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    /**
     * 设置是否可单选，这和 @method{setMultiSelected()} 函数只能设置一个，即不能既单选右多选
     */
    public void setSingleSelected(boolean singleSelectedEnabled){
        mSingleSelectedEnabled = singleSelectedEnabled;
    }

    /**
     * 设置是否可多选
     */
    public void setMultiSelected(boolean multiSelected){
        mMultiSelectedEnabled = multiSelected;
    }

    /**
     * 移动 distance 像素，如果 distance>0，则向右移，如果distance<0，则向左移
     */
    public void smoothScrollBy(int distance){
        smoothScrollBy(distance,0);
    }

    /**
     * 移动 position 个位置，如果position>0，则向右移，如果position<0，则向左移
     */
    public void smoothScrollByPosition(final int position){
        if(position == 0) return;
        int offset = Math.abs(position) * getItemWidth(null) + Math.abs(position) * mHorizontalSpace;
        if (position < 0) offset = -offset;
        smoothScrollBy(offset);
    }

    /**
     * 获得在屏幕中的item的范围
     * @return results[0]为最左边的在屏幕中的元素，results[1]为最右边的在屏幕中的元素
     */
    public int[] getInScreenItemsRange(){
        int[] results = {-1,-1};
        for(int i=0;i< mRootContainer.getChildCount();i++){
            if(isInScreen(i) && results[0] == -1){
                results[0] = i;
            }
            if(!isInScreen(i) && results[0] != -1 && results[1] == -1){
                results[1] = i - 1;
            }
        }
        if(results[1] == -1){
            results[1] = mRootContainer.getChildCount() - 1;
        }
        return results;
    }

    /**
     * 判断 v 是否在屏幕中
     */
    private boolean isInScreen(View v){
        int[] xy = {-1,-1};
        v.getLocationOnScreen(xy);
        int width = v.getWidth();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int x = xy[0];
        int y = xy[1];
        if((x >= 0 && x <= screenWidth) || (x + width > 0 && x < 0)){
            return true;
        }
        return false;
    }

    /**
     * 第 index 个item是否在屏幕中
     */
    public boolean isInScreen(int index){
        if(index < 0 || index >= mRootContainer.getChildCount()) return false;
        return isInScreen(mRootContainer.getChildAt(index));
    }

    /**
     * 获得 view 的宽度，如果 view = null，则表示每个item的宽度都是一样的
     */
    public int getItemWidth(View view){
        if(view == null && mRootContainer.getChildCount() > 0){
            view = mRootContainer.getChildAt(0);
        }
        if (mItemWidth == 0 || mItemHeight == 0) {
            mItemHeight = view.getHeight();
            mItemWidth = view.getWidth();
        }
        return mItemWidth;
    }

    /**
     * 获得 view 的高度，如果 view=null，则表示每个item的高度都是一样的
     */
    public int getItemHeight(View view){
        if(view == null && mRootContainer.getChildCount() > 0){
            view = mRootContainer.getChildAt(0);
        }
        if (mItemWidth == 0 || mItemHeight == 0) {
            mItemHeight = view.getHeight();
            mItemWidth = view.getWidth();
        }
        return mItemHeight;
    }

    /**
     * v是否完全在屏幕中
     */
    public boolean isEntirelyVisible(View v){
        Rect r = new Rect();
        v.getGlobalVisibleRect(r);
        if(v.getWidth() > r.width()){
            return false;
        }
        return true;
    }

    /**
     * v 的左侧是否在屏幕中
     */
    public boolean isLeftVisible(View v){
        Rect r = new Rect();
        v.getGlobalVisibleRect(r);
        if(r.right > v.getWidth()) return true;
        return false;
    }

    /**
     * v 的右侧是否在屏幕中
     */
    public boolean isRightVisible(View v){
        return !isEntirelyVisible(v) && isLeftVisible(v) ? false : true;
    }

    /**
     * 设置 HScrollListView 的每个item的内容
     */
    public void setAdapter(HScrollListViewAdapter adapter){
        init(adapter.getCount());
        for(int i = 0; i < mRootContainer.getChildCount(); i++){
            adapter.convert(mRootContainer.getChildAt(i),i,adapter.getDataByIndex(i));
        }
    }

    /**
     * 获得第 index 个 item 的 view
     */
    public View getItemViewByIndex(int index){
        return mRootContainer.getChildAt(index);
    }

    /**
     * 元素的监听器
     */
    public interface OnItemClickListener{
        public void onItemClick(View view, int index);
    }
}
