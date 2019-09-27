package com.tiberiuciuc.projectbday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements ColorDialog.ColorDialogListener, SizeDialog.SizeDialogListener {

    public static final int MY_PERMISSION_REQUEST = 1;
    public static final int RESULT_LOAD_IMAGE = 2;

    public static Button load, save, share, go, color, size;

    TextView textView1, textView2;

    EditText editText1, editText2;

    ImageView imageView;

    String currentImage = "";

    Context context;

    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            //do nothing
        }

        context = this;

        imageView = findViewById(R.id.imageView);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        load = findViewById(R.id.load);
        save = findViewById(R.id.save);
        share = findViewById(R.id.share);
        go = findViewById(R.id.go);
        color = findViewById(R.id.colour);
        size = findViewById(R.id.size);

        save.setEnabled(false);
        share.setEnabled(false);


//        Bitmap image = BitmapFactory.decodeResource(context.getResources(), getIntent().getIntExtra("image_id", 00));
        imageView.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), getIntent().getIntExtra("image_id", 00), 512, 512));
        if (getIntent().getBooleanExtra("saveAndShareClickable", false)) {
//            imageView.setImageResource(getIntent().getIntExtra("image_id", 00));
            save.setEnabled(true);
            share.setEnabled(true);
        }

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, RESULT_LOAD_IMAGE);

                Intent intent = new Intent(context, AvailableImages.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View content = findViewById(R.id.lay);
                Bitmap bitmap = getScreenShot(content);
                currentImage = "meme" + System.currentTimeMillis() + ".png";
                store(bitmap, currentImage);
                share.setEnabled(true);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(currentImage);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setText(editText1.getText().toString().toUpperCase());
                textView2.setText(editText2.getText().toString().toUpperCase());

                editText1.setText("");
                editText2.setText("");
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorDialog();
            }
        });

        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSizeDialog();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openColorDialog() {
        ColorDialog colorDialog = new ColorDialog();
        colorDialog.show(getSupportFragmentManager(), "COLOR_DIALOG");
    }

    public void openSizeDialog() {
        SizeDialog sizeDialog = new SizeDialog();
        sizeDialog.show(getSupportFragmentManager(), "SIZE_DIALOG");
    }

    public static Bitmap getScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bitmap, String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEME";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, "Capodobera salvata!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Eroare! Esti prea urat ca sa fii salvat!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage(String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MEME";
        Uri uri = Uri.fromFile(new File(dirPath, fileName));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No app found!", Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            save.setEnabled(true);
//            share.setEnabled(false);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //do nothing
                    }
                } else {
                    Toast.makeText(this, "Nu mi-ai dat permisiune, UAI!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void applyColor(int position) {
        switch (position) {
            case 0:
                textView1.setTextColor(getResources().getColor(R.color.black));
                textView2.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                textView1.setTextColor(getResources().getColor(R.color.white));
                textView2.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                textView1.setTextColor(getResources().getColor(R.color.yellow));
                textView2.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 3:
                textView1.setTextColor(getResources().getColor(R.color.colorPrimary));
                textView2.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 4:
                textView1.setTextColor(getResources().getColor(R.color.blue));
                textView2.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 5:
                textView1.setTextColor(getResources().getColor(R.color.red));
                textView2.setTextColor(getResources().getColor(R.color.red));
                break;
            default:
//                textView1.setTextColor(getResources().getColor(R.color.black));
//                textView2.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    @Override
    public void applySize(int position) {

        switch (position) {
            case 0:
                textView1.setTextSize(20);
                textView2.setTextSize(20);
                break;
            case 1:
                textView1.setTextSize(25);
                textView2.setTextSize(25);
                break;
            case 2:
                textView1.setTextSize(30);
                textView2.setTextSize(30);
                break;
            case 3:
                textView1.setTextSize(35);
                textView2.setTextSize(35);
                break;
            case 4:
                textView1.setTextSize(40);
                textView2.setTextSize(40);
                break;
            default:
//                textView1.setTextSize(30);
//                textView2.setTextSize(30);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(context, "Butonul \"inapoi\" nu merge aici, uai!", Toast.LENGTH_SHORT).show();
    }
}