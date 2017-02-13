package com.lxm.smartbutler.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.lxm.smartbutler.MainActivity;
import com.lxm.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager mViewPager;
    private ImageView mSkip;
    private ImageView mPagerPoint1,mPagerPoint2,mPagerPoint3;
    private List<View> lists = new ArrayList<>();
    private View view1,view2,view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        view1 = View.inflate(this,R.layout.pager_item_one,null);
        view2 = View.inflate(this,R.layout.pager_item_two,null);
        view3 = View.inflate(this,R.layout.pager_item_three,null);

        ((Button) view3.findViewById(R.id.btn_start)).setOnClickListener(this);

        lists.add(view1);
        lists.add(view2);
        lists.add(view3);

        mViewPager.setAdapter(new GuideAdapter());

        mPagerPoint1 = (ImageView) findViewById(R.id.point_1);
        mPagerPoint2 = (ImageView) findViewById(R.id.point_2);
        mPagerPoint3 = (ImageView) findViewById(R.id.point_3);
        setImagePoint(true,false,false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setImagePoint(true,false,false);
                        mSkip.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setImagePoint(false,true,false);
                        mSkip.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setImagePoint(false,false,true);
                        mSkip.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSkip = (ImageView) findViewById(R.id.btn_skip);
        mSkip.setOnClickListener(this);
    }

    private void setImagePoint(boolean isChecked1, boolean isChecked2, boolean isChecked3) {
        if (isChecked1) {
            mPagerPoint1.setBackgroundResource(R.drawable.point_on);
        } else {
            mPagerPoint1.setBackgroundResource(R.drawable.point_off);
        }
        if (isChecked2) {
            mPagerPoint2.setBackgroundResource(R.drawable.point_on);
        } else {
            mPagerPoint2.setBackgroundResource(R.drawable.point_off);
        }
        if (isChecked3) {
            mPagerPoint3.setBackgroundResource(R.drawable.point_on);
        } else {
            mPagerPoint3.setBackgroundResource(R.drawable.point_off);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
            case R.id.btn_skip:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(lists.get(position));
            return lists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(lists.get(position));
        }
    }
}
