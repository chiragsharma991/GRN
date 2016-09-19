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
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.base.BaseActivity;
import com.gayatry.grn.fragment.EditGRNGeneralFragment;
import com.gayatry.grn.fragment.EditGRNProductFragment;
import com.gayatry.grn.fragment.GRNGeneralFragment;
import com.gayatry.grn.fragment.GRNProductFragment;
import com.gayatry.grn.fragment.GRNTaxFragment;
import com.gayatry.model.EditGRNModel;
import com.gayatry.model.GRNListModel;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.AsyncTaskPostCommon;

import java.util.ArrayList;

/**
 * Created by Admin on 06-Apr-16.
 */
public class EditGRNActivity extends BaseActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView mTextToolbar;
    private EditGRNGeneralFragment editGRNGeneralFragment;
    private EditGRNProductFragment editGRNProductFragment;
    private final String ProjectId = "projectId";
    public String mProjectId;
    private CoordinatorLayout mCoordinator;
    private final String GRN_MODEL = "grn_model";
    private final String GRN_LIST_MODEL = "grn_list_model";
    public GRNListModel grnListModel;
    public ArrayList<EditGRNModel> mEditGRNModelList;
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
        grnListModel = (GRNListModel) getIntent().getSerializableExtra(GRN_MODEL);
        mEditGRNModelList = getIntent().getParcelableArrayListExtra(GRN_LIST_MODEL);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mProjectId = getIntent().getExtras().getString(ProjectId);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mTextToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTextToolbar.setText("Edit GRN");
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
        if(editGRNGeneralFragment != null) {
            if (AppUtils.isOnline(this)) {
                if(editGRNGeneralFragment.isValidate() && editGRNProductFragment.isValidate()) {
                    AppUtils.showProgressDialog(EditGRNActivity.this);
                    AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(String result)  {
                            deleteGrnList(mEditGRNModelList.get(0).getGRN_ID());
                           /* if(result.length() > 0){
                            }else
                                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));*/
                        }
                    });
                    asyncTaskCommon.execute(getString(R.string.update_grn_master), editGRNGeneralFragment.getAllValues());
                }
            } else {
                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_internet));
            }
        }
    }

    void deleteGrnList(final String grnId){
        AsyncTaskCommon asyncTaskCommon = new AsyncTaskCommon(this, new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    saveProduct(grnId);
                }
            });
            asyncTaskCommon.execute(getString(R.string.delete_grn_product_url)+"/"+grnId);
        }

    private void saveProduct(String grnId)  {
        AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result) {
                AppUtils.stopProgressDialog();
                if(result.length() > 0){
                    openAlertDialog("Success", "Update GRN insert successfully", false, new OnDialogClick() {
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
        asyncTaskCommon.execute(getString(R.string.insert_grn_product), editGRNProductFragment.getAllValues(grnId));
    }



    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                editGRNGeneralFragment = new EditGRNGeneralFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GRN_MODEL, grnListModel);
                editGRNGeneralFragment.setArguments(bundle);
                return editGRNGeneralFragment;
            }else if(position == 1){
                editGRNProductFragment = new EditGRNProductFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GRN_MODEL, grnListModel);
                editGRNProductFragment.setArguments(bundle);
                return editGRNProductFragment;
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
