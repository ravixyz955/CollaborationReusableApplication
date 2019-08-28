package com.example.user.collaboration.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.collaboration.R;
import com.example.user.collaboration.network.model.AuthenticateUserRequest;
import com.example.user.collaboration.network.model.User;
import com.example.user.collaboration.network.service.UserAPIService;
import com.example.user.collaboration.utils.DataUtils;
import com.example.user.collaboration.utils.NetworkUtils;
import com.example.user.collaboration.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SigninFragment extends Fragment {
    private EditText emailText;
    private EditText passwordText;

    private UserAPIService userAPIService;
    private CompositeDisposable compositeDisposable;

    public SigninFragment() {
        // Required empty public constructor
    }

    public static SigninFragment newInstance(String param1, String param2) {
        return new SigninFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getActivity().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        userAPIService = NetworkUtils.provideUserAPIService(getActivity(), "https://auth.");
        compositeDisposable = new CompositeDisposable();
        Utils.createProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signin, container, false);
        emailText = root.findViewById(R.id.email_txt);
        passwordText = root.findViewById(R.id.pwd_txt);
        Button signinBtn = root.findViewById(R.id.signin_btn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignin();
            }
        });
        return root;
    }

    private void doSignin() {
        AuthenticateUserRequest request = new AuthenticateUserRequest();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        request.setEmail(email);
        request.setPassword(password);

        if (NetworkUtils.isConnectingToInternet(getActivity())) {
            Utils.showProgress();

            compositeDisposable.add(
                    userAPIService.authenticate(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getDisposableObserver(password)));
        } else {
            Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_LONG).show();
        }

    }

    private DisposableSingleObserver<User> getDisposableObserver(String password) {
        return new DisposableSingleObserver<User>() {
            @Override
            public void onSuccess(User user) {
                DataUtils.saveId(getActivity(), user.getId());
                DataUtils.saveUser(getActivity(), user.toString());
                DataUtils.saveEmail(getActivity(), user.getEmail());
                DataUtils.saveName(getActivity(), user.getFullName());
                DataUtils.saveMobile(getActivity(), user.getMobile());
                DataUtils.savePassword(getActivity(), password);
                DataUtils.setActive(getActivity(), false);
                DataUtils.saveCountryCode(getActivity(), user.getCountryCode());
                DataUtils.saveToken(getActivity(), user.getToken());
                startActivity(new Intent(getActivity(), CollaborationActivity.class));
                getActivity().finish();
                Utils.dissmisProgress();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "Failed to signin!", Toast.LENGTH_LONG).show();
                Utils.dissmisProgress();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}