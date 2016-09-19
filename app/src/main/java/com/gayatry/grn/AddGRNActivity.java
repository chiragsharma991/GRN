package com.gayatry.grn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.gayatry.R;
import com.gayatry.base.BaseActivity;
import com.gayatry.grn.fragment.GRNGeneralFragment;
import com.gayatry.grn.fragment.GRNProductFragment;
import com.gayatry.grn.fragment.GRNTaxFragment;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.AsyncTaskPostCommon;

import org.json.JSONException;

/**
 * Created by Admin on 06-Apr-16.
 */
public class AddGRNActivity extends BaseActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GRNGeneralFragment grnGeneralFragment;
    private GRNProductFragment grnProductFragment;
    private GRNTaxFragment grnTaxFragment;
    private final String ProjectId = "projectId";
    public String mProjectId;
    private CoordinatorLayout mCoordinator;
    public String mAssessableAmount = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        mProjectId = getIntent().getExtras().getString(ProjectId);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onSaveGrnClick() {
        if(grnGeneralFragment != null) {
            if (AppUtils.isOnline(this)) {
                if(grnGeneralFragment.isValidate() && grnProductFragment.isValidate()) {
                    AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(String result)  {
                            if(result.length() > 0){
                                saveProduct(result);
                            }else
                                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));
                        }
                    });
                    asyncTaskCommon.execute(getString(R.string.insert_grn_master),grnGeneralFragment.getAllValues());
                }
            } else {
                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_internet));
            }
        }
    }

    private void saveProduct(String grnId)  {
            AppUtils.showProgressDialog(this);
            AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if(result.length() > 0){
                        openAlertDialog("Success", "GRN insert successfully", false, new OnDialogClick() {
                            @Override
                            public void onPositiveClick() {
                                AppUtils.stopProgressDialog();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                        });
                    }else
                        AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));
                }
            });
            asyncTaskCommon.execute(getString(R.string.insert_grn_product), grnProductFragment.getAllValues(grnId));
    }



    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                grnGeneralFragment = new GRNGeneralFragment();
                return grnGeneralFragment;
            }else if(position == 1){
                grnProductFragment = new GRNProductFragment();
                return grnProductFragment;
            }else if(position == 2){
                grnTaxFragment = new GRNTaxFragment();
                return grnTaxFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "General";
            }else if(position == 1){
                return "Product";
            }else if(position == 2){
                return "Tax";
            }
            return "";
        }
    }
}
