package com.melissa.sukonbugpt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ImageSelector {

    Toast currentImageToast = null;

    private static final int REQUEST_IMAGE_SELECTOR = 1001;
    private static final int MAX_IMAGE_COUNT = 5; // 최대 이미지 선택 개수

    @SuppressLint("IntentReset")
    public void selectImages(Activity activity) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = Intent.createChooser(galleryIntent, "카메라로 사진을 촬영하거나 갤러리에서 선택해주세요.");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        activity.startActivityForResult(chooser, REQUEST_IMAGE_SELECTOR);
    }

    public List<Bitmap> handleImageSelectionResult(Activity activity, int requestCode, int resultCode, Intent data) {
        List<Bitmap> selectedImages = new ArrayList<>();
        int selectedCount = 0;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_SELECTOR) {
                if (data != null) {
                    if (data.getData() != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                            selectedImages.add(bitmap);
                            selectedCount++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (data.getClipData() != null) {
                        int itemCount = data.getClipData().getItemCount();
                        for (int i = 0; i < itemCount; i++) {
                            if (selectedCount < MAX_IMAGE_COUNT) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getClipData().getItemAt(i).getUri());
                                    selectedImages.add(bitmap);
                                    selectedCount++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (currentImageToast != null) {
                                    currentImageToast.cancel();
                                }
                                currentImageToast = Toast.makeText(activity, "선택할 수 있는 이미지는 최대 5개입니다. 5개를 초과하는 이미지는 선택되지 않습니다.", Toast.LENGTH_SHORT);
                                currentImageToast.show();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return selectedImages;
    }
}
