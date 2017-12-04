package lianlian.com.testzxing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_1, tv_2, tv_3, tv_4, tv_result;
    Button tv_btn, tv_btn_2;
    ImageView iv_img;
    EditText et_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        et_text = (EditText) findViewById(R.id.et_text);
        tv_btn = (Button) findViewById(R.id.tv_btn);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        tv_btn_2 = (Button) findViewById(R.id.tv_btn_2);
        tv_result = (TextView) findViewById(R.id.tv_result);

        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_btn.setOnClickListener(this);
        tv_btn_2.setOnClickListener(this);
        permissions = CheckPermissionUtils.getCheckPermissionUtils().checkPermission(permissions1, this);
    }

    String[] permissions;
    //需要申请的权限
    String permissions1[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    public static final int REQUEST_IMAGE = 112;
    public static final int REQUEST_CODE = 111;

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_1:
                intent = new Intent(getApplication(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tv_2:

                break;
            case R.id.tv_3:
                intent = new Intent(MainActivity.this, OrdinaryActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tv_4:
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.tv_btn:
                String textContent = et_text.getText().toString();
                if (TextUtils.isEmpty(textContent)) {
                    Toast.makeText(MainActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_text.setText("");
                Bitmap mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.mm));
                iv_img.setImageBitmap(mBitmap);
                break;
            case R.id.tv_btn_2:
                Bitmap mBitmap1 = CodeUtils.createImage(et_text.getText().toString(), 400, 400, null);
                iv_img.setImageBitmap(mBitmap1);

                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            tv_result.setText(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    tv_result.setText(result);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissions = CheckPermissionUtils.getCheckPermissionUtils().checkPermission(permissions, this);
        CheckPermissionUtils.getCheckPermissionUtils().onResult(requestCode, permissions, grantResults, this, new CheckPermissionUtils.PermissionListener() {
            @Override
            public void ontask() {

                Toast.makeText(MainActivity.this, "申请权限成功，执行任务。。。。", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
