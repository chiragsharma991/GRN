package com.gayatry.prelogin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gayatry.R;
import com.gayatry.grn.GRNListActivity;
import com.gayatry.gtn.GTNListActivity;
import com.gayatry.model.ProjectModel;
import com.gayatry.prelogin.adapter.ProjectGridAdapter;
import com.gayatry.report.ReportActivity;
import com.gayatry.rest.JsonParser;
import com.gayatry.storage.SharedPreferenceUtil;
import com.gayatry.utilz.AppUtils;
import com.gayatry.utilz.DialogUtils;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Admin on 04-Apr-16.
 */
public class ProjectFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ProjectFragment";
    private RecyclerView recyclerView;
    private DialogUtils dialogUtils;
    private RelativeLayout mRelParent;
    private ProgressBar mProgressBar;
    private LinearLayout mLinInternet;
    private ImageView mImgRetry;
    private TextView mTxtErrorMsg;
    private final String ProjectId = "projectId";
    private final String ProjectName = "projectName";
    private GetProjectAsync getProjectAsync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_project, container, false);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(true);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getProjectList();
        dialogUtils = new DialogUtils(getContext());
    }

    private void getProjectList() {
        if (AppUtils.isOnline(getContext())){
            getProjectAsync = new GetProjectAsync();
            getProjectAsync.execute();
        }
        else {
            visibleErrorMsg(getString(R.string.error_internet));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getProjectAsync != null && getProjectAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getProjectAsync.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getProjectAsync != null && getProjectAsync.getStatus() != AsyncTask.Status.FINISHED) {
            getProjectAsync.cancel(true);
        }
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRelParent = (RelativeLayout) view.findViewById(R.id.relParent);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mLinInternet = (LinearLayout) view.findViewById(R.id.linInternet);
        mImgRetry = (ImageView) view.findViewById(R.id.imgRetry);
        mTxtErrorMsg = (TextView) view.findViewById(R.id.txtErrorMsg);
        mImgRetry.setOnClickListener(this);
    }

    private void setAdapter(ArrayList<ProjectModel> arrayList) {
        //recyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);

        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_project_activity, recyclerView, false);
        TextView txtUser = (TextView) header.findViewById(R.id.txtUser);
        txtUser.setText(SharedPreferenceUtil.getString(AppUtils.USER_FULL_NAME, ""));
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final ProjectGridAdapter adapter = new ProjectGridAdapter(header, arrayList, ProjectFragment.this);
        recyclerView.setAdapter(adapter);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });
    }

    public void openMenu(final ProjectModel model) {
        final Dialog dialog = dialogUtils.setupCustomeDialogFromBottom(R.layout.dialog_project_menu);
        LinearLayout linGtn = (LinearLayout) dialog.findViewById(R.id.linGtn);
        LinearLayout linGrn = (LinearLayout) dialog.findViewById(R.id.linGrn);
        LinearLayout linReport = (LinearLayout) dialog.findViewById(R.id.linReport);
        linGtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().startActivity(new Intent(getActivity(), GRNListActivity.class).
                        putExtra(ProjectId, model.getProjectId()));
            }
        });
        linGrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().startActivity(new Intent(getActivity(), GTNListActivity.class).
                        putExtra(ProjectId, model.getProjectId()));
            }
        });
        linReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().startActivity(new Intent(getActivity(), ReportActivity.class).
                        putExtra(ProjectId, model.getProjectId()).
                        putExtra(ProjectName, model.getProjectName()));
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgRetry:
                hideErrorMsg();
                getProjectList();
                break;
        }
    }

    private class GetProjectAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JsonParser jsonParser = new JsonParser();
                return jsonParser.getResponse(getString(R.string.project_url) + "/" + SharedPreferenceUtil.getString(AppUtils.USER_ID, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            showProgress(false);
            handleResponse(response);
        }
    }

    private void handleResponse(String response) {
        try {
            if (response != "") {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    hideErrorMsg();
                    ArrayList<ProjectModel> arrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProjectModel projectModel = new ProjectModel();
                        projectModel.setProjectId(jsonArray.getJSONObject(i).getString("Project_ID"));
                        projectModel.setProjectName(jsonArray.getJSONObject(i).getString("Project_Name"));
                        arrayList.add(projectModel);
                    }
                    setAdapter(arrayList);
                } else {
                    visibleErrorMsg(getString(R.string.no_data_available));
                }
            } else {
                visibleErrorMsg(getString(R.string.error_server));
            }
        } catch (Exception e) {
            e.printStackTrace();
            visibleErrorMsg(getString(R.string.error_server));
        }
    }

    private void hideErrorMsg() {
        mLinInternet.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void visibleErrorMsg(String msg) {
        mLinInternet.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        mTxtErrorMsg.setText(msg);
        AppUtils.showSnakbar(mRelParent, "" + msg);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            recyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
