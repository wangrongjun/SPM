package com.spm.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spm.R;
import com.wang.android_lib.util.DimensionUtil;
import com.wang.java_util.TextUtil;

/**
 * by 王荣俊 on 2016/8/15.
 */
public class ToolBarView extends RelativeLayout {

    private Context context;
    private TextView tvTitle;
    private ImageView btnOne;
    private ImageView btnTwo;
    private TextView btnRight;

    private String titleText;
    private int titleColor;
    private Drawable btnOneIcon;
    private float btnOneAlpha;
    private Drawable btnTwoIcon;
    private float btnTwoAlpha;
    private String btnRightText;
    private float btnRightTextSize;
    private int btnRightTextColor;
    private float underDividerHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                DimensionUtil.dipToPx(context, 50),
                MeasureSpec.EXACTLY));
    }

    public ToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToolBarView);

        titleText = ta.getString(R.styleable.ToolBarView_titleText);
        titleColor = ta.getColor(R.styleable.ToolBarView_titleColor, Color.BLACK);

        btnOneIcon = ta.getDrawable(R.styleable.ToolBarView_btnOneIcon);
        btnOneAlpha = ta.getFloat(R.styleable.ToolBarView_btnOneAlpha, 1);
        btnTwoIcon = ta.getDrawable(R.styleable.ToolBarView_btnTwoIcon);
        btnTwoAlpha = ta.getFloat(R.styleable.ToolBarView_btnTwoAlpha, 1);

        btnRightText = ta.getString(R.styleable.ToolBarView_btnRightText);
        btnRightTextSize = ta.getDimension(R.styleable.ToolBarView_btnRightTextSize, 14);
        btnRightTextColor = ta.getColor(R.styleable.ToolBarView_btnRightTextColor, Color.BLACK);

        underDividerHeight = ta.getDimension(R.styleable.ToolBarView_underDividerHeight,
                DimensionUtil.dipToPx(context, 0.5));

        initView();
        ta.recycle();
    }

    private void initView() {

//        返回按钮
        ImageView btnBack = new ImageView(context);
        btnBack.setImageResource(R.mipmap.ic_btn_back_white);
        int px = DimensionUtil.dipToPx(context, 12);
        btnBack.setPadding(px, 0, px, 0);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
        px = DimensionUtil.dipToPx(context, 50);
        LayoutParams params = new LayoutParams(px, px);
        params.addRule(ALIGN_PARENT_LEFT, TRUE);
        addView(btnBack, params);


//        中间标题
        tvTitle = new TextView(context);
        if (TextUtil.isEmpty(titleText)) titleText = "ECar";
        tvTitle.setText(titleText);
        tvTitle.setTextColor(titleColor);
        tvTitle.setTextAppearance(context, R.style.ToolBarTitle);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT, TRUE);
        addView(tvTitle, params);


//        右边第一个按钮
        if (btnOneIcon != null) {
            btnOne = new ImageView(context);
            btnOne.setImageDrawable(btnOneIcon);
            btnOne.setAlpha(btnOneAlpha);
            px = DimensionUtil.dipToPx(context, 10);
            btnOne.setPadding(0, px, 0, px);
            px = DimensionUtil.dipToPx(context, 50);
            params = new LayoutParams(px, px);
            params.addRule(ALIGN_PARENT_RIGHT, TRUE);
            addView(btnOne, params);
        }


//        右边第二个按钮
        if (btnTwoIcon != null) {
            btnTwo = new ImageView(context);
            btnTwo.setImageDrawable(btnTwoIcon);
            btnTwo.setAlpha(btnTwoAlpha);
            px = DimensionUtil.dipToPx(context, 10);
            btnTwo.setPadding(0, px, 0, px);
            px = DimensionUtil.dipToPx(context, 50);
            params = new LayoutParams(px, px);
            params.rightMargin = px;
            params.addRule(ALIGN_PARENT_RIGHT, TRUE);
            addView(btnTwo, params);
        }


//        右边文字按钮
        if (!TextUtil.isEmpty(btnRightText)) {
            btnRight = new TextView(context);
            btnRight.setText(btnRightText);
            btnRight.setTextColor(btnRightTextColor);
            btnRight.setGravity(Gravity.CENTER);
//            btnRight.setTextSize(btnRightTextSize);
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.addRule(ALIGN_PARENT_RIGHT, TRUE);
            params.addRule(CENTER_VERTICAL, TRUE);
            params.rightMargin = DimensionUtil.dipToPx(context, 10);
            params.leftMargin = DimensionUtil.dipToPx(context, 10);
            addView(btnRight, params);
        }


//        下边的分割线
        ImageView ivBottomBar = new ImageView(context);
        ivBottomBar.setBackgroundResource(R.color.light_gray);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) underDividerHeight);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(ivBottomBar, params);

    }

    public void setOnBtnOneClickListener(OnClickListener listener) {
        btnOne.setOnClickListener(listener);
    }

    public void setOnBtnTwoClickListener(OnClickListener listener) {
        btnTwo.setOnClickListener(listener);
    }

    public void setOnBtnRightTextClickListener(OnClickListener listener) {
        btnRight.setOnClickListener(listener);
    }

    public void setTitleText(String text) {
        tvTitle.setText(text);
    }

    public TextView getRightBtnTextView() {
        return tvTitle;
    }

}
