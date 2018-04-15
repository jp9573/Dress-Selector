package in.co.jaypatel.dressselector;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import java.io.File;
import java.util.List;

/**
 * Created by jay on 14/04/18.
 */

public class ChooseImageDialog extends Dialog {

    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;

    private static int PICK_IMAGE = 511;
    private static int CAMERA_REQUEST = 512;
    public int flag = 0;

    public ChooseImageDialog(Context context, Fragment fragment) {
        super(context);
        mFragment = fragment;
        mActivity = fragment.getActivity();
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_image_dialog);

        Button mChooseGalleryImageButton = findViewById(R.id.button_choose_image_from_gallery);
        Button mTakeCameraImageButton = findViewById(R.id.button_take_camera_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        mChooseGalleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                mFragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                dismiss();
            }
        });


        mTakeCameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
                mFragment.startActivityForResult(intent, CAMERA_REQUEST);
                dismiss();
            }
        });

    }

}
