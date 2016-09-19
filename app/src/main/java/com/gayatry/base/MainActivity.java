package com.gayatry.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gayatry.R;
import com.gayatry.prelogin.LoginFragment;
import com.gayatry.prelogin.ProjectFragment;
import com.gayatry.storage.SharedPreferenceUtil;
import com.gayatry.utilz.AppUtils;

public class  MainActivity extends BaseActivity {

    private LoginFragment loginFragment;
    private ProjectFragment projectFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        /*SharedPreferenceUtil.putValue(AppUtils.IS_LOGIN, true);
        SharedPreferenceUtil.save();*/

        if(SharedPreferenceUtil.getBoolean(AppUtils.IS_LOGIN, false)){
            projectFragment = new ProjectFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.container, projectFragment, ProjectFragment.TAG).commit();
        }else{
            setStatusBarColor(R.color.app_bg_color);
            hideToolbar(toolbar);
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.container, loginFragment, LoginFragment.TAG).commit();
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void moveProjectListFragment() {
        setStatusBarColor(R.color.colorPrimaryDark);
        showToolbar(toolbar);
        projectFragment = new ProjectFragment();
        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                replace(R.id.container, projectFragment, ProjectFragment.TAG).commit();
    }

    private void logoutDialog(){
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setStatusBarColor(R.color.app_bg_color);
                SharedPreferenceUtil.putValue(AppUtils.IS_LOGIN, false);
                SharedPreferenceUtil.save();
                hideToolbar(toolbar);
                loginFragment = new LoginFragment();
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                        replace(R.id.container, loginFragment, LoginFragment.TAG).commit();;
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
