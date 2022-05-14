package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecoverPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecoverPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText mail;
    Button recover_password;
    FirebaseAuth auth;
    AuthActivity activity;

    public RecoverPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecoverPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecoverPasswordFragment newInstance(String param1, String param2) {
        RecoverPasswordFragment fragment = new RecoverPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_recover_password, container, false);
        activity=(AuthActivity)getActivity();
        mail=(EditText) root.findViewById(R.id.login);
        recover_password=(Button) root.findViewById(R.id.button2);
        auth=FirebaseAuth.getInstance();
        recover_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecoverPassword();
            }
        });
        return root;
    }
    private void RecoverPassword(){
        if(mail.getText().toString().isEmpty()){
            mail.setError("Введите электронную почту");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()){
            mail.setError("Введите корректную почту");
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        auth.sendPasswordResetEmail(mail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Cсылка для смены пароля отправлена на вашу почту",Toast.LENGTH_SHORT).show();
                    Button button1=(Button)activity.findViewById(R.id.button);
                    Button button2=(Button)activity.findViewById(R.id.recover_password_button);
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    getParentFragmentManager().beginTransaction().remove(RecoverPasswordFragment.this).commit();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Что-то пошло не так, попробуйте снова",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}