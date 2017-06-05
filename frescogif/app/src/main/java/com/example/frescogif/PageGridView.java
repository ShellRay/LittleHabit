package com.example.frescogif;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuanwei on 17/3/17.
 */
public class PageGridView extends AdapterView<ListAdapter>
{

    public interface OnPageChangeListener
    {
        public void onPageChange(int page);
    }

    class DataObserver extends DataSetObserver
    {
        @Override
        public void onChanged()
        {
            destroyview(true);

            if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0)
            {
                refreshview();
            }
            else
            {
                rebuild = true;
                requestLayout();
            }
        }

        @Override
        public void onInvalidated()
        {
            destroyview(false);
            mAdapter = null;
        }
    }

    class ChildView
    {
        final int position;

        final int viewType;

        final View content;

        public ChildView(int position, int viewType, View content)
        {
            this.position = position;
            this.viewType = viewType;
            this.content = content;
        }
    }

    int gridSpacingX = 0;

    int gridSpacingY = 0;

    int gridRowNumber = 2;

    int gridColNumber = 2;

    int selectItem = -1;

    int selectPage = -1;

    boolean rebuild;

    Scroller scroller;

    ListAdapter mAdapter;

    DataObserver observer;

    List<ChildView> activeslist;

    List<ChildView> recylerlist;

    OnPageChangeListener pageListener;

    public PageGridView(Context context)
    {
        this(context, null);
    }

    public PageGridView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PageGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        this(context, attrs, defStyleAttr);
    }

    public PageGridView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        observer = new DataObserver();
        activeslist = new ArrayList<ChildView>();
        recylerlist = new ArrayList<ChildView>();

        ViewConfiguration conf = ViewConfiguration.get(context);
        minVelocity = conf.getScaledMinimumFlingVelocity();
        maxVelocity = conf.getScaledMaximumFlingVelocity();
        mTouchSlop = conf.getScaledTouchSlop();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageGridView);
        gridSpacingX = a.getDimensionPixelSize(R.styleable.PageGridView_gridSpaceX, 0);
        gridSpacingY = a.getDimensionPixelSize(R.styleable.PageGridView_gridSpceY, 0);
        gridRowNumber = a.getInteger(R.styleable.PageGridView_gridRowNumber, 2);
        gridColNumber = a.getInteger(R.styleable.PageGridView_gridColNumber, 2);
        a.recycle();
    }

    public void setOnPageChangeListener(OnPageChangeListener pageListener)
    {
        this.pageListener = pageListener;
    }

    @Override
    public ListAdapter getAdapter()
    {
        return mAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter)
    {
        if (mAdapter != null)
        {
            mAdapter.unregisterDataSetObserver(observer);
        }

        mAdapter = adapter;

        if (mAdapter != null)
        {
            mAdapter.registerDataSetObserver(observer);
        }

        destroyview(true);

        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0)
        {
            refreshview();
        }
        else
        {
            rebuild = true;
            requestLayout();
        }
    }

    @Override
    public View getSelectedView()
    {
        return getItemView(selectItem);
    }

    @Override
    public void setSelection(int position)
    {
        int count = getItemCount();

        if (position >= 0 && position < count)
        {
            selectItem = position;

            View view = getItemView(position);

            if (view != null)
            {
                view.setSelected(true);
            }
        }
    }

    public int getSelection()
    {
        return selectItem;
    }

    public void cancleSelect()
    {
        View view = getItemView(selectItem);

        if (view != null)
        {
            view.setSelected(false);
        }
    }

    public int getPageCount()
    {

        int pageUnit = getOneNumber();
        int datasize = getItemCount();
        int result = datasize / pageUnit;

        if (datasize % pageUnit != 0)
        {
            result++;
        }

        return result;
    }

    public int getOneNumber()
    {
        return gridColNumber * gridRowNumber;
    }

    public int getItemCount()
    {
        return mAdapter != null ? mAdapter.getCount() : 0;
    }

    public int getMaxScroll()
    {
        int max = (getPageCount() - 1) * getMeasuredWidth();
        return Math.max(max, 0);
    }

    public int getItemWidth()
    {
        int pageWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        return (pageWidth + gridSpacingX) / gridColNumber - gridSpacingX;
    }

    public int getItemHeight()
    {
        int pageHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        return (pageHeight + gridSpacingY) / gridRowNumber - gridSpacingY;
    }

    public int getItemLeft(int position)
    {
        int value = position % getOneNumber();
        int page = position / getOneNumber(); //1
        int cols = value / gridRowNumber;
        return page * getMeasuredWidth() + (getItemWidth() + gridSpacingX) * cols + getPaddingLeft();
    }

    public int getItemTop(int position)
    {
        int value = position % getOneNumber();
        int rows = value % gridRowNumber;
        return (getItemHeight() + gridSpacingY) * rows + getPaddingTop();
    }

    public int getPageFocus()
    {
        int pageWidth = getMeasuredWidth(), result = 0;

        if (pageWidth > 0)
        {
            result = (getScrollX() + pageWidth / 2) / pageWidth;
        }
        return result;
    }

    public View getItemView(int position)
    {
        ChildView result = null;

        int datasize = activeslist.size();

        for (int i = datasize - 1; i >= 0; i--)
        {
            ChildView c = activeslist.get(i);

            if (c.position == position)
            {
                result = c;
                break;
            }
        }

        return result == null ? null : result.content;

    }

    private View getRecycler(int viewType)
    {
        ChildView result = null;

        int datasize = recylerlist.size();

        for (int i = datasize - 1; i >= 0; i--)
        {
            ChildView c = recylerlist.get(i);
            if (c.viewType == viewType)
            {
                result = recylerlist.remove(i);
            }
        }

        return result == null ? null : result.content;
    }

    private void destroyview(boolean recycle)
    {
        int size = activeslist.size();

        for (int i = size - 1; i >= 0; i--)
        {
            ChildView child = activeslist.remove(i);
            removeViewInLayout(child.content);

            if (recycle)
            {
                recylerlist.add(child);
            }
        }
    }

    private void refreshview()
    {
        int maxScrollX = getMaxScroll();

        if (getScrollX() > maxScrollX)
        {
            int scrollY = getScrollY();
            scrollTo(maxScrollX, scrollY);
        }

        int pagecount = getPageCount();
        int pagefocus = getPageFocus();

        int pagefrom = Math.max(pagefocus - 1, 0);
        int pagestop = Math.min(pagefocus + 1, pagecount - 1);

        int onenumber = getOneNumber();
        int itemfrom = pagefrom * onenumber;
        int itemstop = (pagestop + 1) * onenumber;

        itemfrom = Math.max(itemfrom, 0);
        itemstop = Math.min(itemstop, getItemCount());

        int datasize = activeslist.size();

        for (int index = datasize - 1; index >= 0; index--)
        {
            ChildView child = activeslist.get(index);

            if (child.position < itemfrom || child.position >= itemstop)
            {
                removeViewInLayout(child.content);
                activeslist.remove(index);
                recylerlist.add(child);
            }
        }

        for (int index = itemfrom; index < itemstop; index++)
        {
            if (!isContain(index))
            {
                addItemView(index);
            }
        }

    }

    private boolean isContain(int position)
    {
        boolean contain = false;

        for (ChildView child : activeslist)
        {
            if (child.position == position)
            {
                contain = true;
                break;
            }
        }

        return contain;
    }

    private void addItemView(int position)
    {
        int viewType = mAdapter.getItemViewType(position);
        View contentView = getRecycler(viewType);

        if (contentView != null)
        {
            cleanupLayoutState(contentView);
        }

        contentView = mAdapter.getView(position, contentView, this);

        contentView.setSelected(position == selectItem);

        int itemWidth = getItemWidth(), itemHeight = getItemHeight();

        LayoutParams params = contentView.getLayoutParams();

        if (params == null)
        {
            params = new LayoutParams(itemWidth, itemHeight);
        }
        else
        {
            params.width = itemWidth;
            params.height = itemHeight;
        }

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
        addViewInLayout(contentView, getChildCount(), params);

        ChildView child = new ChildView(position, viewType, contentView);
        activeslist.add(child);

        int top = getItemTop(position);
        int left = getItemLeft(position);

        contentView.measure(widthMeasureSpec, heightMeasureSpec);
        contentView.layout(left, top, left + itemWidth, top + itemHeight);
    }

    private int findTouchItem(int x, int y)
    {

        int result = -1;

        int viewWidth = getMeasuredWidth();
        int itemWidth = getItemWidth();
        int itemHeight = getItemHeight();

        int page = x / viewWidth, t = x % viewWidth;

        int guesscol = (t - getPaddingLeft()) / (itemWidth + gridSpacingX);
        int guessrow = (y - getPaddingTop()) / (itemHeight + gridSpacingY);

        if (guesscol >= 0 && guessrow >= 0)
        {
            //            int position = getOneNumber() * page + guessrow * gridColNumber + guesscol;

            int position = getOneNumber() * page + guesscol * gridRowNumber + guessrow;

            int left = getItemLeft(position), top = getItemTop(position);
            int right = left + itemWidth, bottom = top + itemHeight;

            if (x >= left && x <= right && y >= top && y <= bottom && position >= 0 && position < getItemCount())
            {
                result = position;
            }
        }

        return result;

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (rebuild)
        {
            rebuild = false;
            refreshview();
        }

        Log.i("---", "onmeasure");
        int count = getChildCount();

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getItemWidth(), MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getItemHeight(), MeasureSpec.EXACTLY);
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int count = activeslist.size();

        int itemWidth = getItemWidth();

        int itemHeight = getItemHeight();

        for (int i = 0; i < count; i++)
        {
            ChildView cv = activeslist.get(i);

            int t = getItemTop(cv.position);
            int l = getItemLeft(cv.position);

            int r = l + itemWidth;

            int b = t + itemHeight;

            cv.content.layout(l, t, r, b);


        }
    }

    int             pointer_id;
    int             minVelocity;
    int             maxVelocity;
    boolean         haveSloped;
    int             mTouchSlop;
    int             slideFromX;
    float           touchFromX;
    VelocityTracker ev_tracker;

    private void onSecondaryPointerUp(MotionEvent ev)
    {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int id = ev.getPointerId(pointerIndex);

        if (id == pointer_id)
        {
            if (ev_tracker != null)
            {
                ev_tracker.clear();
            }

            int newIndex = pointerIndex == 0 ? 1 : 0;
            pointer_id = ev.getPointerId(newIndex);
            touchFromX = ev.getX(newIndex);
            slideFromX = getScrollX();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        boolean intercept = false;
        int action = ev.getActionMasked();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                pointer_id = ev.getPointerId(0);

                if (scroller.isFinished())
                {
                    touchFromX = ev.getX();
                    slideFromX = getScrollX();
                }
                else
                {
                    intercept = true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                int index = ev.findPointerIndex(pointer_id);

                int diffX = (int) Math.abs(touchFromX - ev.getX(index));

                if (diffX > mTouchSlop)
                {
                    ViewParent parent = getParent();

                    if (parent != null)
                    {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    intercept = true;
                    haveSloped = true;
                    slideFromX = getScrollX();
                    touchFromX = ev.getX(index);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {
                onSecondaryPointerUp(ev);
                break;
            }
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {

        if (ev_tracker == null)
        {
            ev_tracker = VelocityTracker.obtain();
        }

        ev_tracker.addMovement(ev);

        int action = ev.getActionMasked();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                if (!scroller.isFinished())
                {
                    ViewParent parent = getParent();
                    if (parent != null)
                    {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    scroller.abortAnimation();
                    haveSloped = true;
                }
                else
                {
                    haveSloped = false;
                }

                pointer_id = ev.getPointerId(0);
                slideFromX = getScrollX();
                touchFromX = ev.getX();
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                int index = ev.findPointerIndex(pointer_id);

                int diffx = (int) (touchFromX - ev.getX(index));

                if (haveSloped)
                {
                    int scrollX = slideFromX + diffx;
                    int min = 0, max = getMaxScroll();

                    scrollX = Math.max(scrollX, min);
                    scrollX = Math.min(scrollX, max);

                    int scrollY = getScrollY();
                    scrollTo(scrollX, scrollY);
                    refreshview();
                }
                else
                {
                    ViewParent parent = getParent();

                    if (parent != null)
                    {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }

                    haveSloped = Math.abs(diffx) > mTouchSlop;
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {
                onSecondaryPointerUp(ev);
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            {
                scrollToPage(getPageFocus());
                break;
            }

            case MotionEvent.ACTION_UP:
            {

                ev_tracker.computeCurrentVelocity(1000, maxVelocity);
                int velocity = (int) ev_tracker.getXVelocity();
                ev_tracker.recycle();
                ev_tracker = null;

                if (haveSloped)
                {
                    int pagefocus = getPageFocus();

                    if (Math.abs(velocity) > minVelocity)
                    {
                        if (velocity < 0)
                        {
                            pagefocus = getScrollX() / getMeasuredWidth() + 1;
                        }
                        else
                        {
                            pagefocus--;
                        }
                    }

                    scrollToPage(pagefocus);
                    invalidate();
                }
                else if (getOnItemClickListener() != null)
                {
                    int pointerIndex = ev.findPointerIndex(pointer_id);
                    int x = (int) ev.getX(pointerIndex);
                    int y = (int) ev.getY(pointerIndex);

                    int position = findTouchItem(getScrollX() + x, y);
                    View content = getItemView(position);

                    if (position >= 0 && content != null)
                    {
                        getOnItemClickListener().onItemClick(this, content, position, mAdapter.getItemId(position));
                    }
                }

                break;
            }
        }

        return true;

    }

    private void scrollToPage(int page)
    {

        int pagecount = getPageCount();
        page = Math.min(page, pagecount - 1);
        page = Math.max(0, page);

        if (selectPage != page)
        {
            selectPage = page;

            if (pageListener != null)
            {
                pageListener.onPageChange(page);
            }
        }


        int finalX = page * getMeasuredWidth();
        int min = 0, max = getMaxScroll();
        finalX = Math.max(finalX, min);
        finalX = Math.min(finalX, max);

        int startX = getScrollX();
        int length = Math.abs(finalX - startX);
        int duration = Math.min(360, length * 2);
        scroller.startScroll(startX, getScrollY(), finalX - startX, 0, duration);

        invalidate();
    }

    @Override
    public void computeScroll()
    {
        if (scroller.computeScrollOffset())
        {
            int cx = scroller.getCurrX();
            int cy = scroller.getCurrY();
            scrollTo(cx, cy);
            refreshview();
            invalidate();
        }
    }

}
