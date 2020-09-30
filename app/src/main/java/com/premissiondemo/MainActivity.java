package com.premissiondemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.lib.weight.dashboard.type.DialChart05View;
import com.lib.weight.svgmap.SvgMapView;
import com.lib.weight.svgmap.GetSvgProperties;
import com.lib.weight.toast.CentreToast;
import com.lib.weight.treelist.TreeEntity;
import com.lib.weight.treelist.TreeListAdapter;
import com.lib.weight.treelist.TreeOnItemClickListener;
import com.premissionannotationlib.PermissionDenied;
import com.premissionannotationlib.PermissionGrant;
import com.premissionannotationlib.PermissionRational;
import com.premissionhelper.PermissionHelper;
import com.premissionhelper.PermissionRationalCallback;
import com.premissionhelper.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TreeOnItemClickListener {

    private static final int RESULT_CODE_PERMISSION = 100;
    private TreeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionHelper.requestPermission(this,RESULT_CODE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_NUMBERS);

        initview();
    }

    private void initview() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TreeListAdapter();
        mAdapter.setOnItemClickListenr(this);
        recyclerView.setAdapter(mAdapter);
        testData();
    }

    @Override
    public void onItemClick() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, VewpagerTestActivity.class);
        startActivity(intent);
    }

    private void testData() {
        List<TreeEntity> entities1 = new ArrayList<>();
        List<TreeEntity> entities2 = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            TreeEntity treeEntity = new TreeEntity();
            treeEntity.setTitle("副标题" + i);
            treeEntity.setLevel(1);
            entities1.add(treeEntity);
        }

        for (int i = 0; i <= 5; i++) {
            TreeEntity treeEntity = new TreeEntity();
            treeEntity.setTitle("主标题" + i);
            treeEntity.setLevel(0);
            treeEntity.setChilds(entities1);
            entities2.add(treeEntity);
        }
        mAdapter.setDatas(entities2);

    }

    @PermissionGrant(RESULT_CODE_PERMISSION)
    public void onRequestGranted(String[] grantPermissions) {

        CentreToast.showText(this, "权限申请成功", true);

    }

    @PermissionDenied(RESULT_CODE_PERMISSION)
    public void onRequestDenied(String[] deniedPermission) {
        StringBuilder sb = new StringBuilder();
        for (String permission : deniedPermission) {
            sb.append(permission + "\n");
        }

        new AlertDialog.Builder(this).setTitle("权限授权提示")
                .setMessage("很遗憾，一下权限的申请被拒绝，功能将无法使用， 请到设置--应用程序--当前程序中授予以下权限\n\n" + sb.toString())
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();



                    }
                }).create().show();
    }

    @PermissionRational(RESULT_CODE_PERMISSION)
    public void onRequestRational(String[] rationalPermisson, final PermissionRationalCallback callback) {

        new AlertDialog.Builder(this).setTitle("权限授权提示")
                .setMessage("请授予一下权限，以继续功能的使用\n\n" + PermissionUtil.getChineseByPremission(rationalPermisson))
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (callback != null) {
                            callback.onRationalExecute();
                        }
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}