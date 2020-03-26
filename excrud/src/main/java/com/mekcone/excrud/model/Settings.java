package com.mekcone.excrud.model;

import cn.hutool.core.codec.Base64;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
class KeyPair {
    private String publicKey;
    private String privateKey;

    public static KeyPair pair(String publicKey, String privateKey) {
        KeyPair keyPair = new KeyPair();
        keyPair.setPublicKey(publicKey);
        keyPair.setPrivateKey(privateKey);
        return keyPair;
    }
}

@Component
@Data
public class Settings {
    private List<KeyPair> rsaKeyPairs;

    public void addRsaKeyPair(String publicKey, String privateKey) {
        if (rsaKeyPairs == null) {
            rsaKeyPairs = new ArrayList<>();
        }
        rsaKeyPairs.add(KeyPair.pair(publicKey, privateKey));
    }

    @JsonIgnore
    public byte[] getRsaPrivateKeyBytes(String rsaPublicKey) {
        if (rsaKeyPairs != null) {
            for (KeyPair rsaKeyPair: rsaKeyPairs) {
                if (rsaKeyPair.getPublicKey().equals(rsaPublicKey)) {
                    return Base64.decode(rsaKeyPair.getPrivateKey());
                }
            }
        }
        return null;
    }

    @JsonIgnore
    public byte[] getDefaultRsaPublicKeyBytes() {
        if (rsaKeyPairs != null && !rsaKeyPairs.isEmpty()) {
            return Base64.decode(rsaKeyPairs.get(0).getPublicKey());
        }
        return null;
    }
}
