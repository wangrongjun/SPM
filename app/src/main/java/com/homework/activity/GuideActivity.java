package com.homework.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.homework.R;
import com.homework.fragment.GuideOneFragment;
import com.homework.fragment.GuideThreeFragment;
import com.homework.fragment.GuideTwoFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class GuideActivity extends FragmentActivity {

    @Bind(R.id.iv_guide_point_one)
    ImageView ivGuidePointOne;
    @Bind(R.id.iv_guide_point_two)
    ImageView ivGuidePointTwo;
    @Bind(R.id.iv_guide_point_three)
    ImageView ivGuidePointThree;
    @Bind(R.id.vp_guide)
    ViewPager vpGuide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initViewPager();
        changePoint(0);
    }

    private void initViewPager() {

        final Fragment[] fragments = new Fragment[]{
                new GuideOneFragment(),
                new GuideTwoFragment(),
                new GuideThreeFragment()
        };

        vpGuide.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changePoint(int position) {
        ivGuidePointOne.setBackgroundResource(R.mipmap.ic_guide_point_normal);
        ivGuidePointTwo.setBackgroundResource(R.mipmap.ic_guide_point_normal);
        ivGuidePointThree.setBackgroundResource(R.mipmap.ic_guide_point_normal);

        switch (position) {
            case 0:
                ivGuidePointOne.setBackgroundResource(R.mipmap.ic_guide_point_select);
                break;
            case 1:
                ivGuidePointTwo.setBackgroundResource(R.mipmap.ic_guide_point_select);
                break;
            case 2:
                ivGuidePointThree.setBackgroundResource(R.mipmap.ic_guide_point_select);
                break;
        }
    }

}
