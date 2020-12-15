package com.freedomsoft.securemessenger.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.freedomsoft.securemessenger.Constants;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.Typefaces;
import com.freedomsoft.securemessenger.databinding.FragmentAddConversationBinding;

public class AddConversationFragment extends Fragment {
    private FragmentAddConversationBinding b;
    private Context context;
    private FragmentManager fragmentManager;

    public static AddConversationFragment newInstance() {
        Bundle args = new Bundle();
        AddConversationFragment fragment = new AddConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_conversation, container, false);
        b = FragmentAddConversationBinding.bind(view);
        b.newConversationFragmentRootLayout.setOnClickListener(v -> {
        });
        b.openConversationTitle.setTypeface(Typefaces.bold);
        b.openAesConversationTitle.setTypeface(Typefaces.medium);
        b.openAesConversationDescription.setTypeface(Typefaces.regular);
        b.openAesConversationLayout.setOnClickListener(v -> {
            closeSelf();
            openAesKeygenRoleFragment();
        });
        b.closeConversationButton.setOnClickListener(v -> closeSelf());

        return view;
    }

    private void closeSelf() {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(this).commitNow();
    }
    private void hideSelf() {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).hide(this).commitNow();
    }
    private void openAesKeygenRoleFragment() {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.main_activity_root_container, AesKeygenRoleFragment.newInstance(), Constants.AES_KEYGEN_ROLE_FRAGMENT_TAG).commit();
    }
}
