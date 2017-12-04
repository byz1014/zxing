package lianlian.com.testzxing;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apple on 2016/12/3.
 */

public class CheckPermissionUtils {
    public static int MISS_CODE = 100;
    private static CheckPermissionUtils checkPermissionUtils;

    private CheckPermissionUtils() {
    }

    public static CheckPermissionUtils getCheckPermissionUtils() {

        if (checkPermissionUtils == null) {
            synchronized (CheckPermissionUtils.class) {
                if (checkPermissionUtils == null) {
                    checkPermissionUtils = new CheckPermissionUtils();
                }
            }
        }
        return checkPermissionUtils;
    }


    //检测权限
    public String[] checkPermission(String[] permissions, Context context) {
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(context, permission);
            if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {//未申请
                data.add(permission);
            }
        }
        return data.toArray(new String[data.size()]);
    }


    public void onResult(int requestCode, String[] permissions, int[] grantResults, Activity activity, PermissionListener permissionListener) {
        if (MISS_CODE == requestCode) {
            boolean state = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    state = false;
                }
            }
            if (state) {
                permissionListener.ontask();
            } else {
                showMissingPermissionDialog(activity);
            }
        }
    }

    public interface PermissionListener {
        void ontask();
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setMessage("缺少必要的权限");
        // 拒绝, 退出应用
        builder.setNegativeButton("返回",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });
        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(activity);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings(Activity activity) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }


}
