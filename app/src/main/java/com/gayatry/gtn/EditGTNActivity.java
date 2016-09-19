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
import com.gayatry.gtn.fragment.EditGTNGeneralFragment;
import com.gayatry.gtn.fragment.EditGTNProductFragment;
import com.gayatry.model.EditGTNModel;
import com.gayatry.model.GTNListModel;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.AsyncTaskCommon;
import com.gayatry.utilz.AsyncTaskPostCommon;

import java.util.ArrayList;

/**
 * Created by Admin on 23-Apr-16.
 */
public class EditGTNActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditGTNGeneralFragment editGTNGeneralFragment;
    private EditGTNProductFragment editGTNProductFragment;
    private TextView mTextToolbar;
    private CoordinatorLayout mCoordinator;
    private String GTN_MODEL="gtn_model";
    private GTNListModel gtnListModel;
    public String mAssesableAmount = "0";
    public String mProjectId;
    private final String ProjectId = "projectId";
    private String GTN_LIST_MODEL = "gtn_list";
    public ArrayList<EditGTNModel> mEditGTNModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grn);
        gtnListModel = (GTNListModel) getIntent().getSerializableExtra(GTN_MODEL);
        mEditGTNModelList = getIntent().getParcelableArrayListExtra(GTN_LIST_MODEL);
        mProjectId = getIntent().getExtras().getString(ProjectId);
        initView();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        initToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mTextToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mTextToolbar.setText("Edit GTN");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    public void onSaveGtnClick() {
        if(editGTNProductFragment != null) {
            if (AppUtils.isOnline(this)) {
                if(editGTNProductFragment.isValidate() && editGTNGeneralFragment.isValidate()) {
                    AppUtils.showProgressDialog(EditGTNActivity.this);
                    AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(String result)  {
                            deleteGrnList(mEditGTNModelList.get(0).getGTN_ID());
                            /* if(result.length() > 0){
                                saveProduct(result);
                            }else
                                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));*/
                        }
                    });
                    asyncTaskCommon.execute(getString(R.string.update_gtn_master), editGTNGeneralFragment.getAllValues());
                }
            } else {
                AppUtils.showSnakbar(mCoordinator, getString(R.string.error_internet));
            }
        }
    }

    void deleteGrnList(final String gtnId){
        if(AppUtils.isOnline(this)) {
            AsyncTaskCommon asyncTaskCommon = new AsyncTaskCommon(this, new AsyncTaskCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                   saveProduct(gtnId);
                }
            });
            asyncTaskCommon.execute(getString(R.string.delete_gtn_product_url)+"/"+gtnId);
        }else{
            AppUtils.showSnakbar(mCoordinator, getString(R.string.error_internet));
        }
    }

    private void saveProduct(String gtnId)  {
        AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result) {
                AppUtils.stopProgressDialog();
                openAlertDialog("Success", "GTN update successfully", false, new OnDialogClick() {
                    @Override
                    public void onPositiveClick() {
                        AppUtils.stopProgressDialog();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
               /* if(result.length() > 0){

                }else
                    AppUtils.showSnakbar(mCoordinator, getString(R.string.error_server));*/
            }
        });
        asyncTaskCommon.execute(getString(R.string.insert_gtn_product), editGTNProductFragment.getAllValues(gtnListModel.getGTN_ID()));
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                editGTNGeneralFragment = new EditGTNGeneralFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GTN_MODEL, gtnListModel);
                editGTNGeneralFragment.setArguments(bundle);
                return editGTNGeneralFragment;
            }else if(position == 1){
                editGTNProductFragment = new EditGTNProductFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GTN_MODEL, gtnListModel);
                editGTNProductFragment.setArguments(bundle);
                return editGTNProductFragment;
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
