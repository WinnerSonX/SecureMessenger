package com.freedomsoft.securemessenger.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.freedomsoft.securemessenger.LiveData;
import com.freedomsoft.securemessenger.R;
import com.freedomsoft.securemessenger.databinding.FragmentAesQrBinding;
import com.freedomsoft.securemessenger.room.models.Chat;
import com.freedomsoft.securemessenger.room.models.User;
import com.freedomsoft.securemessenger.room.services.ChatService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

@SuppressWarnings("ConstantConditions")
public class QrFragment extends Fragment {
    private FragmentAesQrBinding b;
    private Context context;
    private FragmentManager fragmentManager;
    private byte[] generatedAesKey;
    private User user;
    private boolean aes;

    public static QrFragment newInstance(boolean aes) {
        Bundle args = new Bundle();
        args.putBoolean("aes", aes);
        QrFragment fragment = new QrFragment();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                QRResult qrResult = QRTools.parseQrResult(intentResult.getContents());
                //СКАНИРОВАНИЕ ID Пользователя И КЛЮЧА
                Chat chat = LiveData.getInstance().emergingChat.getValue();
                chat.setUserId(qrResult.getUserId());
                ChatService.save(chat);
                closeSelf();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.aes = args.getBoolean("aes");
        if (aes)
            generatedAesKey = LiveData.getInstance().aesKeyMutableLiveData.getValue().get();
        user = LiveData.getInstance().userMutableLiveData.getValue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aes_qr, container, false);
        b = FragmentAesQrBinding.bind(view);

        b.scanFriendId.setOnClickListener(v -> {
            if (aes) {
                IntentIntegrator.forSupportFragment(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                        .setPrompt("Отсканируйте QR код друга")
                        .setBeepEnabled(false)
                        .initiateScan();
            } else {
                ChatService.save(LiveData.getInstance().emergingChat.getValue());
                closeSelf();
            }
        });

        b.aesQrFragmentRootLayout.setOnClickListener(v -> {
        });
        b.closeQrButton.setOnClickListener(v -> closeSelf());
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(QRTools.encodeQrJson(user.getId(), generatedAesKey), BarcodeFormat.QR_CODE, 600, 600);
            b.qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        b.goBackToRole.setOnClickListener(this::back);
        return view;
    }

    private void closeSelf() {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(this).commit();
    }

    private void openAesKeygenRoleFragment() {
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.main_activity_root_container, AesKeygenRoleFragment.newInstance(), Constants.AES_KEYGEN_ROLE_FRAGMENT_TAG).commit();
    }

    public void back(View v) {
        fragmentManager.beginTransaction().add(R.id.main_activity_root_container, AesKeygenRoleFragment.newInstance(), Constants.AES_KEYGEN_ROLE_FRAGMENT_TAG).commit();
        closeSelf();

    }

    private String getQrString() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (aes)
                jsonObject.put("aes", new String(generatedAesKey, StandardCharsets.UTF_8));
            jsonObject.put("user_id", user.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
