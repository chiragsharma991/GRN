package com.gayatry.gtn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import com.gayatry.grn.fragment.GRNTaxFragment;
import com.gayatry.gtn.fragment.GTNGeneralFragment;
import com.gayatry.gtn.fragment.GTNProductFragment;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskPostCommon;

/**
 * Created by Admin on 23-Apr-16.
 */
public class AddGTNActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GTNProductFragment gtnProductFragment;
    private GTNGeneralFragment gtnGeneralFragment;
    private TextView mTextToolbar;
    private CoordinatorLayout mCoordinator;
    private final String ProjectId = "projectId";
    public String mProjectId;
    public String mAssesableAmount = "0";

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
        mProjectId = getIntent().getExtras().getString(ProjectId);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mTextToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mTextToolbar.setText("ADD GTN");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    public void onSaveGtnClick() {
        if(gtnGeneralFragment != null) {
            if (AppUtils.isOnline(this)) {
                if(gtnGeneralFragment.isValidate() && gtnProductFragment.isValidate()) {
                    AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(String result)  {
                            if(result.length() > 0){
                                saveProduct(result);
                            }else
                                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));
                        }
                    });
                    asyncTaskCommon.execute(getString(R.string.insert_gtn_master), gtnGeneralFragment.getAllValues());
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
                openAlertDialog("Success", "GTN insert successfully", false, new OnDialogClick() {
                    @Override
                    public void onPositiveClick() {
                        AppUtils.stopProgressDialog();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });

               /* if(result.length() > 0){
                    openAlertDialog("Success", "GRN insert successfully", false, new OnDialogClick() {
                        @Override
                        public void onPositiveClick() {
                            AppUtils.stopProgressDialog();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    });
                }else
                    AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));*/
            }
        });
        asyncTaskCommon.execute(getString(R.string.insert_gtn_product), gtnProductFragment.getAllValues(grnId));
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                gtnGeneralFragment = new GTNGeneralFragment();
                return gtnGeneralFragment;
            }else if(position == 1){
                gtnProductFragment = new GTNProductFragment();
                return gtnProductFragment;
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
            }
            return "";
        }
    }
}
