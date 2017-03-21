package com.example.s525721.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class FileHelper {

  public static final String TAG = FileHelper.class.getSimpleName();

  public static final int SHORT_SIDE_TARGET = 1280;

  public static byte[] getByteArrayFromFile(Context context, Uri uri) {
    byte[] fileBytes = null;
    InputStream inStream = null;
    ByteArrayOutputStream outStream = null;

    if (uri.getScheme().equals("content")) {
      try {
        inStream = context.getContentResolver().openInputStream(uri);
        outStream = new ByteArrayOutputStream();

        byte[] bytesFromFile = new byte[1024 * 1024]; // buffer size (1 MB)
        int bytesRead = inStream.read(bytesFromFile);
        while (bytesRead != -1) {
          outStream.write(bytesFromFile, 0, bytesRead);
          bytesRead = inStream.read(bytesFromFile);
        }

        fileBytes = outStream.toByteArray();
      } catch (IOException e) {
        Log.e(TAG, e.getMessage());
      } finally {
        try {
          inStream.close();
          outStream.close();
        } catch (IOException e) { /*( Intentionally blank */ }
      }
    } else {
      try {
        java.net.URI tempUri = new URI(uri.toString());
        fileBytes = IOUtils.toByteArray(tempUri);
      } catch (IOException e) {
        Log.e(TAG, e.getMessage());
      }
      catch (URISyntaxException e) {
        Log.e(TAG, e.getMessage());
      }
    }

    return fileBytes;
  }

  public static byte[] reduceImageForUpload(byte[] imageData) {
    Bitmap bitmap = ImageResizer.resizeImageMaintainAspectRatio(imageData, SHORT_SIDE_TARGET);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    byte[] reducedData = outputStream.toByteArray();
    try {
      outputStream.close();
    } catch (IOException e) {
      // Intentionally blank
    }

    return reducedData;
  }

  public static String getFileName(Context context, Uri uri, String fileType) {
    String fileName = "image.";


    fileName += "png";

    return fileName;



  }
}