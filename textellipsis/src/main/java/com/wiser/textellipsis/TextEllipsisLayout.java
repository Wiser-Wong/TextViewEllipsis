package com.wiser.textellipsis;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Wiser
 * 
 *         文本+布局 文本超过1行展示省略号...
 */
public class TextEllipsisLayout extends ViewGroup implements ViewTreeObserver.OnGlobalLayoutListener {

	private int			lastWidth;

	private LView		line;

	private final int	SINGLE	= 1;		// 单独样式

	private final int	LIST	= 2;		// 列表样式

	private int			type	= SINGLE;

	public TextEllipsisLayout(Context context) {
		super(context);
		init(context, null);
	}

	public TextEllipsisLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TextEllipsisLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		line = new LView();

		if (attrs == null) return;

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextEllipsisLayout);
		type = ta.getInt(R.styleable.TextEllipsisLayout_tel_type, type);
		ta.recycle();

		if (type == LIST) getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = getPaddingLeft();
		line.reMeasure();// 在摆放之前从新测量
		line.layout(left);// 确定摆放的位置
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getChildCount() > 2) throw new InflateException("必须先添加的是TextView，并且只有两位子View");
		reset();
		int height = 0;
		int temp = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			// 测量子View尺寸
			childMeasure(childView, widthMeasureSpec, heightMeasureSpec);
			// 获取childView的宽度
			int childWidth = childView.getMeasuredWidth();
			line.addLineView(childView);
			if (i == 0) {
				if (!(childView instanceof AppCompatTextView)) {
					throw new InflateException("必须先添加的是TextView，并且只有两位子View");
				}
			} else {
				MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
				lastWidth = lastWidth + childWidth + params.leftMargin + params.rightMargin;
			}
			height = temp > childView.getMeasuredHeight() ? temp : childView.getMeasuredHeight();
			temp = childView.getMeasuredHeight();
		}
		// 确定控件的高度
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void reset() {
		lastWidth = 0;
		if (line != null) line.clear();
	}

	// 测量子View尺寸
	private void childMeasure(View childView, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		LayoutParams params = childView.getLayoutParams();
		int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width);
		int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height);
		childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@Override public void onGlobalLayout() {
		if (type == LIST) setTextMaxWidth();
	}

	class LView {

		private ArrayList<View> views = new ArrayList<>();

		void addLineView(View childView) {
			if (!views.contains(childView)) {
				views.add(childView);
			}
		}

		void clear() {
			if (views != null) views.clear();
		}

		void layout(int left) {
			for (int i = 0; i < views.size(); i++) {
				View childView = views.get(i);
				MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
				// 居中显示
				childView.layout(left + params.leftMargin, getMeasuredHeight() / 2 - childView.getMeasuredHeight() / 2, left + params.leftMargin + childView.getMeasuredWidth(),
						getMeasuredHeight() / 2 + childView.getMeasuredHeight() / 2);
				left = left + params.leftMargin + childView.getMeasuredWidth() + childView.getPaddingLeft();
			}
		}

		// 从新测量
		void reMeasure() {
			for (int i = 0; i < views.size(); i++) {
				View childView = views.get(i);
				int measuredWidth = childView.getMeasuredWidth();
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
				int measuredHeight = childView.getMeasuredHeight();
				int measuredHeightSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.AT_MOST);
				childView.measure(widthMeasureSpec, measuredHeightSpec);
			}
		}
	}

	// 需要重写该方法 否则报(MarginLayoutParams) childView.getLayoutParams()转换异常
	@Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	private void setTextMaxWidth() {
		if (getChildCount() > 0 && getChildAt(0) instanceof AppCompatTextView) {
			AppCompatTextView childView = ((AppCompatTextView) getChildAt(0));
			MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
			childView.setMaxWidth(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - params.leftMargin - params.rightMargin - lastWidth);
			childView.setEllipsize(TextUtils.TruncateAt.END);
			childView.setMaxLines(1);
		}
	}

	@Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (type == SINGLE) setTextMaxWidth();
	}
}
