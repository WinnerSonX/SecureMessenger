package com.freedomsoft.securemessenger.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.Typefaces;
import com.freedomsoft.securemessenger.databinding.FragmentAesKeygenRoleBinding;
import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.services.ChatService;
import com.freedomsoft.securemessenger.security.KeyGen;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("ConstantConditions")
public class AesKeygenRoleFragment extends Fragment {
    private FragmentAesKeygenRoleBinding b;
    private Context context;
    private FragmentManager fragmentManager;

    public static AesKeygenRoleFragment newInstance() {
        Bundle args = new Bundle();
        AesKeygenRoleFragment fragment = new AesKeygenRoleFragment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                QRResult qrResult = QRTools.parseQrResult(intentResult.getContents());
                //СКАНИРОВАНИЕ ID Пользователя И КЛЮЧА
                Chat chat = new Chat();
                chat.setUserId(qrResult.getUserId());
                chat.setAesKey(qrResult.getAesKey());
                Log.d("SCANNED_QR_KEY", Arrays.toString(chat.getAesKey()));
                LiveData.getInstance().emergingChat.setValue(chat);
                openQrFragment(false);
                closeSelf();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aes_keygen_role, container, false);
        b = FragmentAesKeygenRoleBinding.bind(view);
        b.aesKeygenRoleFragmentRootLayout.setOnClickListener(v -> {
        });
        b.showKeyTitle.setTypeface(Typefaces.medium);
        b.showKeyDescription.setTypeface(Typefaces.regular);
        b.scanKeyTitle.setTypeface(Typefaces.medium);
        b.scanKeyDescription.setTypeface(Typefaces.regular);
        b.showKeyLayout.setOnClickListener(v -> {
            byte[] aesKey = KeyGen.generateRandom().getEncoded();
            Chat chat = new Chat();
            chat.setAesKey(aesKey);
            LiveData.getInstance().emergingChat.setValue(chat);
            LiveData.getInstance().aesKeyMutableLiveData.setValue(Optional.of(aesKey));

            closeSelf();
            openQrFragment(true);
        });
        b.scanKeyLayout.setOnClickListener(v -> IntentIntegrator.forSupportFragment(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setPrompt("Отсканируйте QR код друга")
                .setBeepEnabled(false)
                .initiateScan());

        b.closeAesKeygenRoleButton.setOnClickListener(v -> {
            closeSelf();
        });
        b.goBackToNewConversation.setOnClickListener(this::back);
        return view;
    }

    private void closeSelf() {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(this).commit();
    }

    private void openQrFragment(boolean aes) {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.main_activity_root_container, QrFragment.newInstance(aes), Constants.AES_QR_FRAGMENT_TAG).commit();
    }

    public void back(View v) {
        fragmentManager.beginTransaction().add(R.id.main_activity_root_container, AddConversationFragment.newInstance(), Constants.NEW_CONVERSATION_FRAGMENT_TAG).commit();

        closeSelf();

    }
}
