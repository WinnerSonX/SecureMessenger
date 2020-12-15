package com.freedomsoft.securemessenger.ui;

import androidx.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRResult {
    private String userId;
    @Nullable
    private byte[] aesKey;
}
