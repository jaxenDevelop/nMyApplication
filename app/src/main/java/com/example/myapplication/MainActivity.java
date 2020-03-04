package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.circle.CircleFragment;
import com.example.myapplication.home.HomeFragment;
import com.example.myapplication.mine.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Vector;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    //点击底部按钮切换viewpager
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        //i，判断登录账号身份（0：普通用户， 1：管理员）
        int i = intent.getIntExtra("IsManager", 0);

        /**保持屏幕常亮**/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /**导航栏监听**/
        if (i == 0) {
            bottomNavigationView = findViewById(R.id.navigation0);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else if (i == 1) {
//            bottomNavigationView = findViewById(R.id.navigation1);
//            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //塞入fragment
        List<Fragment> fragmentList = new Vector<>();
        if (i == 0) {
            fragmentList.add(new HomeFragment());
            fragmentList.add(new CircleFragment());
            fragmentList.add(new MineFragment());
        } else if (i == 1) {
//            fragmentList.add(new NotificationManageFragment());
//            fragmentList.add(new InfoManageFragment());
//            fragmentList.add(new StudentManageFragment());
        }


        //初始化viewpager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        /**版本大于6.0，动态获取位置服务及文件读取权限**/
        if (Build.VERSION.SDK_INT >= 23) {
            String perms[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, perms)) {
                /**do nothing**/
            } else {
                EasyPermissions.requestPermissions(this, "应用需要从本地读取csv文件，请授予权限以正常使用",
                        777, perms);
            }

        }

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case 777:
                Toast.makeText(this, "文件读取权限已获取！", Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case 777:
                Toast.makeText(this, "未授权，请在系统设置中手动授予文件读取权限！", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
