package com.gayatry.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.base.BaseActivity;
import com.gayatry.grn.fragment.GRNGeneralFragment;
import com.gayatry.grn.fragment.GRNProductFragment;
import com.gayatry.report.fragment.AgingReportFragment;
import com.gayatry.report.fragment.PaymentReportFragment;
import com.gayatry.report.fragment.StockReportFragment;

/**
 * Created by Admin on 01-May-16.
 */
public class ReportActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView mTextToolbar;
    private StockReportFragment stockReportFragment;
    private PaymentReportFragment paymentReportFragment;
    private AgingReportFragment agingReportFragment;
    private final String ProjectId = "projectId";
    private final String ProjectName = "projectName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grn);
        initView();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        initToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mTextToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTextToolbar.setText("Report");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                stockReportFragment = new StockReportFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ProjectName,""+getIntent().getExtras().getString(ProjectName));
                bundle.putString(ProjectId,""+getIntent().getExtras().getString(ProjectId));
                stockReportFragment.setArguments(bundle);
                return stockReportFragment;
            }else if(position == 1){
                paymentReportFragment = new PaymentReportFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ProjectId,""+getIntent().getExtras().getString(ProjectId));
                paymentReportFragment.setArguments(bundle);
                return paymentReportFragment;
            }else if(position == 2){
                agingReportFragment = new AgingReportFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ProjectId,""+getIntent().getExtras().getString(ProjectId));
                agingReportFragment.setArguments(bundle);
                return agingReportFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Stock";
            }else if(position == 1){
                return "Payment";
            }else if(position == 2){
                return "Aging";
            }
            return "";
        }
    }

}
