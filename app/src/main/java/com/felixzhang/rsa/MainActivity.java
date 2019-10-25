package com.felixzhang.rsa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            generateKey();
        }catch (Exception e1){
            e1.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button enButton =  findViewById(R.id.enbutton);
        final Button deButton = findViewById(R.id.debutton);
        final EditText input = findViewById(R.id.Input);
        final EditText raw = findViewById(R.id.raw);
        final EditText output = findViewById(R.id.OriginText);

        enButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{
                   raw.setText(encrypt(input.getText().toString()));
               }catch(Exception e){
                   e.printStackTrace();
               }
            }
        });

        deButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                      output.setText(decrypt(encrypt(input.getText().toString(),publicKey),privateKey));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private final static String RSA = "RSA";
    public static PublicKey publicKey;
    public static PrivateKey privateKey;

    public static void generateKey()throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(2048, new SecureRandom());

        KeyPair keyPair = generator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    private static byte[] encrypt(String text, PublicKey publicKey) throws  Exception{
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(text.getBytes());
    }

    public static String encrypt(String text) throws  Exception{

            byte[] cipherText = encrypt(text,publicKey);
            return Base64.getEncoder().encodeToString(cipherText);

    }

    public static String decrypt(byte[] cipherText, PrivateKey privateKey)throws Exception{
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);

        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }
}
