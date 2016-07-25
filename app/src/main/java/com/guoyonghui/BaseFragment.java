package com.guoyonghui;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.guoyonghui.todo.BasePresenter;

public abstract class BaseFragment extends Fragment {

    protected abstract BasePresenter getPresenter();

    @Override
    public void onResume() {
        super.onResume();

        getPresenter().start();
    }

    public void showMessage(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showMessage(int msgResId) {
        Snackbar.make(getView(), msgResId, Snackbar.LENGTH_SHORT).show();
    }

}
