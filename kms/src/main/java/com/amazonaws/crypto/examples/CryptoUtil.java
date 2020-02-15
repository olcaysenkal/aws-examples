package com.amazonaws.crypto.examples;

import java.nio.ByteBuffer;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;

public class CryptoUtil {
    private static String keyArn;
    private static String data;

    public static void main(final String[] args) {
        keyArn = args[0];
        data = args[1];

        ByteBuffer ciphertext = encryptData(keyArn,data);

        ByteBuffer plainText = decrypt(ciphertext);

        System.out.println(new String(plainText.array()));

    }

    private static ByteBuffer encryptData(String keyArn, String data){
        AWSKMS kmsClient = AWSKMSClientBuilder.standard().withRegion("eu-west-2").build();

        EncryptRequest req = new EncryptRequest().withKeyId(keyArn).withPlaintext(ByteBuffer.wrap(data.getBytes()));
        return kmsClient.encrypt(req).getCiphertextBlob();
    }

    private static ByteBuffer decrypt(ByteBuffer ciphertext){
        AWSKMS kmsClient = AWSKMSClientBuilder.standard().withRegion("eu-west-2").build();

        DecryptRequest decryptRequest = new DecryptRequest().withCiphertextBlob(ciphertext);
        return kmsClient.decrypt(decryptRequest).getPlaintext();
    }
}