package com.hp.hp.imagetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;


public class MainActivity extends AppCompatActivity {
    byte[] byteArray;ImageView imageView;
     Bitmap selectedImage,newbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         imageView = (ImageView) findViewById(R.id.img);

//        KeyGenerator keyGenerator = null;
//        try {
//            keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128);
//            Key key = keyGenerator.generateKey();
//            System.out.println(key);
//
//            byte[] content = imagetobytarray();
//            System.out.println(content);
//
//            byte[] encrypted = encryptPdfFile(key, content);
//            System.out.println(encrypted);
//
//            byte[] decrypted = decryptPdfFile(key, encrypted);
//            System.out.println(decrypted);
//
//            saveFile(decrypted);
//            System.out.println("Done");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public  byte[] getFile() {

        //File f = new File("/home/bridgeit/Desktop/Olympics.jpg");
        File f = new File("/sdcard/harish.jpg");

        if(f.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            Toast.makeText(MainActivity.this, ""+f.getAbsolutePath(), Toast.LENGTH_SHORT).show();

           // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

         //   myImage.setImageBitmap(myBitmap);

          //  Bitmap bmp = intent.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            myBitmap.recycle();

        }else
        {
            Toast.makeText(this, "image not exist", Toast.LENGTH_SHORT).show();
        }

        return  byteArray;


    }

    public byte[] imagetobytarray()
    {
        //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

       Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.file2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;
    }

    public static byte[] encryptPdfFile(Key key, byte[] content) {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;

    }

    public static byte[] decryptPdfFile(Key key, byte[] textCryp) {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(textCryp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public static void saveFile(byte[] bytes) throws IOException {

        FileOutputStream fos = new FileOutputStream("/sdcard/harishnew.jpg");
        fos.write(bytes);
        fos.close();

    }


    public void imageclicked(View view) {
        Intent galleryintent=new Intent();
        galleryintent.setType("image/*");
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryintent,"SELECT IMAGE"),1);

    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
          byte[] newtest=      convertimagetobyteonclick(selectedImage);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(MainActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public void startbtn(View view) {
        newbitmap=selectedImage;
        convertimagetobyteonclick(newbitmap);

        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            Key key = keyGenerator.generateKey();
            System.out.println(key);

            byte[] content =         convertimagetobyteonclick(newbitmap);

            System.out.println(content);

            byte[] encrypted = encryptPdfFile(key, content);
            System.out.println(encrypted);

            byte[] decrypted = decryptPdfFile(key, encrypted);
            System.out.println(decrypted);

            saveFile(decrypted);
            System.out.println("Done");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] convertimagetobyteonclick(Bitmap newbitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;


    }
}
