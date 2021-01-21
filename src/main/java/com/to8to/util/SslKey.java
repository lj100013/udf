package com.to8to.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import sun.misc.BASE64Encoder;

public class SslKey {

    public static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    public static PrivateKey getPrivateKey() {
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            KeyStore ks = getKeyStore("D:\\presto_cer\\presto_keystore.jks", "presto");
            PrivateKey key = (PrivateKey) ks.getKey("presto_keystore", "presto".toCharArray());
            String encoded = encoder.encode(key.getEncoded());
            System.out.println("-----BEGIN RSA PRIVATE KEY-----");
            System.out.println(encoded);
            System.out.println("-----END RSA PRIVATE KEY-----");
            return key;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        getPrivateKey();
    }

}