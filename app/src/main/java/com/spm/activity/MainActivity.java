package com.spm.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spm.R;
import com.spm.fragment.MainClassFragment;
import com.spm.fragment.MainCourseFragment;
import com.spm.fragment.MainHomeworkFragment;
import com.spm.fragment.MainMeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.iv_course)
    ImageView ivCourse;
    @Bind(R.id.tv_course)
    TextView tvCourse;
    @Bind(R.id.iv_homework)
    ImageView ivHomework;
    @Bind(R.id.tv_homework)
    TextView tvHomework;
    @Bind(R.id.iv_class)
    ImageView ivClass;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.iv_me)
    ImageView ivMe;
    @Bind(R.id.tv_me)
    TextView tvMe;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private MainCourseFragment courseFragment;
    private MainHomeworkFragment homeworkFragment;
    private MainClassFragment classFragment;
    private MainMeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        changePage(0);
    }

    private void init() {

        courseFragment = new MainCourseFragment();
        homeworkFragment = new MainHomeworkFragment();
        classFragment = new MainClassFragment();
        meFragment = new MainMeFragment();

        final Fragment[] fragments = new Fragment[]{
                courseFragment,
                homeworkFragment,
                classFragment,
                meFragment
        };
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置左右缓存页面数为3，避免重复加载
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * 换页
     */
    private void changePage(int position) {
        viewPager.setCurrentItem(position, true);
        changeTab(position);
    }

    private void changeTab(int position) {

        tvCourse.setTextColor(Color.GRAY);
        ivCourse.setImageResource(R.mipmap.ic_tab_course_nor);
        tvHomework.setTextColor(Color.GRAY);
        ivHomework.setImageResource(R.mipmap.ic_tab_homework_nor);
        tvClass.setTextColor(Color.GRAY);
        ivClass.setImageResource(R.mipmap.ic_tab_class_nor);
        tvMe.setTextColor(Color.GRAY);
        ivMe.setImageResource(R.mipmap.ic_tab_me_nor);

        switch (position) {
            case 0:
                tvCourse.setTextColor(Color.BLACK);
                ivCourse.setImageResource(R.mipmap.ic_tab_course_pre);
                break;
            case 1:
                tvHomework.setTextColor(Color.BLACK);
                ivHomework.setImageResource(R.mipmap.ic_tab_homework_pre);
                break;
            case 2:
                tvClass.setTextColor(Color.BLACK);
                ivClass.setImageResource(R.mipmap.ic_tab_class_pre);
                break;
            case 3:
                tvMe.setTextColor(Color.BLACK);
                ivMe.setImageResource(R.mipmap.ic_tab_me_pre);
                break;
        }

    }

    @OnClick({R.id.btn_test, R.id.btn_course, R.id.btn_homework, R.id.btn_class, R.id.btn_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                break;
            case R.id.btn_course:
                changePage(0);
                break;
            case R.id.btn_homework:
                changePage(1);
                break;
            case R.id.btn_class:
                changePage(2);
                break;
            case R.id.btn_me:
                changePage(3);
                break;
        }
    }
}
