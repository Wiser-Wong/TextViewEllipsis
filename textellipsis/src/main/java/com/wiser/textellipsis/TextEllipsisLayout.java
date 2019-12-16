package com.wiser.textellipsis;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Wiser
 * 
 *         文本+布局 文本超过1行展示省略号...
 */
public class TextEllipsisLayout extends ViewGroup {

	private int		lastWidth;

	private LView	line;

	public TextEllipsisLayout(Context context) {
		super(context);
		init();
	}

	public TextEllipsisLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextEllipsisLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		line = new LView();
	}

	@Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int top = getPaddingTop();
		int left = getPaddingLeft();
		line.reMeasure();// 在摆放之前从新测量
		line.layout(left, top);// 确定摆放的位置
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
				if (!(childView instanceof TextView)) {
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
	}

	// 测量子View尺寸
	private void childMeasure(View childView, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		LayoutParams params = childView.getLayoutParams();
		int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width);
		int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height);
		childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	class LView {

		private ArrayList<View> views = new ArrayList<>();

		void addLineView(View childView) {
			if (!views.contains(childView)) {
				views.add(childView);
			}
		}

		void layout(int left, int top) {
			for (int i = 0; i < views.size(); i++) {
				View childView = views.get(i);
				MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
				childView.layout(left + params.leftMargin, top + params.topMargin, left + params.leftMargin + childView.getMeasuredWidth(), top + params.topMargin + childView.getMeasuredHeight());
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

	//需要重写该方法 否则报(MarginLayoutParams) childView.getLayoutParams()转换异常
	@Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			if (getChildCount() > 0 && getChildAt(0) instanceof TextView) {
				TextView childView = ((TextView) getChildAt(0));
				MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
				childView.setMaxWidth(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - params.leftMargin - params.rightMargin - lastWidth);
				childView.setEllipsize(TextUtils.TruncateAt.END);
				childView.setMaxLines(1);
			}
		}
	}
}
