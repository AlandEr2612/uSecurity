package com.arrg.android.app.usecurity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.norbsoft.typefacehelper.TypefaceHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.arrg.android.app.usecurity.USecurity.LOCKED_APPS_PREFERENCES;
import static com.arrg.android.app.usecurity.USecurity.PACKAGES_APPS_PREFERENCES;

public class AppListFragment extends Fragment {

    private static final String ARG_APPS = "apps";
    private static final String ARG_POSITION = "position";

    private SharedPreferences lockedAppsPreferences;
    private SharedPreferences packagesAppsPreferences;
    private SharedPreferencesUtil preferencesUtil;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public AppListFragment() {
    }

    public static AppListFragment newInstance(int position, ArrayList<App> apps) {
        AppListFragment appListFragment = new AppListFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putSerializable(ARG_APPS, apps);

        appListFragment.setArguments(args);

        return appListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtil = new SharedPreferencesUtil(getActivity());
        lockedAppsPreferences = getActivity().getSharedPreferences(LOCKED_APPS_PREFERENCES, Context.MODE_PRIVATE);
        packagesAppsPreferences = getActivity().getSharedPreferences(PACKAGES_APPS_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);

        ButterKnife.bind(this, rootView);
        TypefaceHelper.typeface(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();

        ArrayList<App> apps = (ArrayList<App>) bundle.getSerializable(ARG_APPS);
        Integer index = bundle.getInt(ARG_POSITION);

        setAdapter(index, apps);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setAdapter(int position, ArrayList<App> apps) {
        AppAdapter appAdapter;
        ArrayList<App> appArrayList = new ArrayList<>();

        for (App app : apps) {
            if (!preferencesUtil.getBoolean(lockedAppsPreferences, app.getAppPackage(), false)) {
                appArrayList.add(app);
            }
        }

        appAdapter = new AppAdapter(getActivity(), appArrayList, position, lockedAppsPreferences, packagesAppsPreferences, preferencesUtil);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(appAdapter);
    }
}
