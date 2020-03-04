package com.example.myapplication.user;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public class Login extends Activity {
    private EditText et_user;
    private EditText et_password;
    private Button btn_login, manager_login;
    private TextView icon_show;
    private boolean show = false;
    private TextView register;
    private Boolean has_register;
    private Handler handler;
    private int IsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //初始化控件
        initView();
        //初始化管理员账号与预置课程
        initManagerAccount();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        login(IsManager);
                        break;
                }
                return false;
            }
        });
    }

    private void initManagerAccount() {
        SqlHelper myDatabaseHelper = new SqlHelper(getApplicationContext());
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("manager", null, null, null, null, null, null);
        //初始化管理员账号：判断表中是否有数据，如果没有就进行初始化
        if (cursor.getCount() < 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "admin");
            contentValues.put("password", "admin");
            db.insert("manager", null, contentValues);


//            "CREATE TABLE information(id INTEGER PRIMARY KEY AUTOINCREMENT,editor TEXT,title TEXT,time TEXT, img BLOB, read_number TEXT, content TEXT)";

            //定义数组
            List<String> editor = new ArrayList<>();
            List<String> title = new ArrayList<>();
            List<String> time = new ArrayList<>();
            List<Integer> img = new ArrayList<>();
            List<Integer> read_number = new ArrayList<>();
            List<String> content = new ArrayList<>();

            //添加第一条数据
            editor.add("小二农技");
            title.add("让人头疼的柑橘黄化，病害？虫害？缺素？");
            time.add("2020-03-02");
            img.add(R.drawable.t0);
            read_number.add(43);
            content.add("柑橘黄化会引起叶片失绿发黄，大大减低光合作用能力，树势变差，从而引发一系列问题，如抗病能力减弱，吸收水肥的能力变差等，最终导致产量低、果实品质差，直接影响果农收益。目前大多数柑橘黄化并不由单一因素引起，而是多种因素叠加所致。\n" +
                    "\n" +
                    "柑橘受到病菌、病毒和类菌质体等病原体的侵入，会产生的各种分泌物，堵塞输导系统，阻断水分和养分吸收，阻碍光合作用必需原料的供应，叶片即出现失绿黄化。\n" +
                    "\n" +
                    "今天田小胖给大家梳理了一些由病害引起柑橘黄化的原因，带领果农朋友挖掘黄化表象下的根源，对症下药。\n" +
                    "\n" +
                    "柑橘艾滋病·黄龙病\n" +
                    "\n" +
                    "号称“柑橘艾滋”的柑橘黄龙病，病原体是细菌，通过嫁接和柑橘木虱转播。症状表现为柑橘整株叶片发黄，果实病变，最终死亡。基于柑橘黄龙病危害性，好多果农朋友一旦发现自家柑橘叶片发黄，就会害怕感染了黄龙病，田小盼为大家整理了几个区分黄龙病黄化的小技巧：\n" +
                    "\n" +
                    "斑驳状黄化叶\n" +
                    "\n" +
                    "病叶左右不对称；叶基先黄化；斑块交界不明显；病叶脆硬而滑多发生在已经转绿的老熟叶片上，最初从叶脉或叶缘开始退绿黄化，并逐渐向叶肉扩散，形成斑驳黄化，此斑驳黄化细看似一团黄色的烟雾弥散叶片上，其黄化颜色深浅不一，与健康组织无清晰边界。\n" +
                    "\n" +
                    "\n" +
                    " 缺素状黄化\n" +
                    "\n" +
                    "类似缺素黄化，这种症状经常出现却难以判断。多发生在新稍叶片刚转绿，并一直保持缺素状黄化，即使补充足够的营养元素也无法逆转。\n" +
                    "\n" +
                    "产生原因是病菌堵塞树干韧皮部后，根系难以获得营养，部分根系饥饿而枯死，难以吸收土壤中的营养物质，从而引起新稍缺素状黄化，是病菌间接原因造成的黄化症状，不同于斑驳黄化，是病菌直接堵塞叶脉造成的斑驳黄化。\n" +
                    "\n" +
                    "\n" +
                    "嫩梢插金花\n" +
                    "\n" +
                    "秋冬季新梢不转绿，表现均匀黄化的症状，其特点是病树上一束或少数几丛新梢不转色，呈现亮黄色，像一朵黄花俏立枝头，此症状明显，易于普查识别。\n" +
                    "\n" +
                    "初发病果园病树零星分布。不同病树症状不对称、不统一。初期只有1-2个枝梢发病，大部分枝叶果正常。同一病树上不同病枝症状不对称、不一样。同一病枝上不同病叶症状不对称、差异大。同一病叶上的不同病斑形状、颜色均不对称。\n" +
                    "\n" +
                    "\n" +
                    "“红鼻子果”或“青果”\n" +
                    "\n" +
                    "红鼻果：正常的果实着色是先从中下部再向上转色或均匀着色，而黄龙病果是先在柄处着色，而中下部不再着色。\n" +
                    "\n" +
                    "青果：橙柚类感染黄龙病多表现为青果、小果又小又硬，僵果皱缩不能膨大，有些能长成大果，但果实很软，不能着色影响糖酸度，口感酸涩。");


            //添加第一条数据
            editor.add("小二农技");
            title.add("让人头疼的柑橘黄化，病害？虫害？缺素？");
            time.add("2020-03-02");
            img.add(R.drawable.t0);
            read_number.add(43);
            content.add("柑橘黄化会引起叶片失绿发黄，大大减低光合作用能力，树势变差，从而引发一系列问题，如抗病能力减弱，吸收水肥的能力变差等，最终导致产量低、果实品质差，直接影响果农收益。目前大多数柑橘黄化并不由单一因素引起，而是多种因素叠加所致。\n" +
                    "柑橘受到病菌、病毒和类菌质体等病原体的侵入，会产生的各种分泌物，堵塞输导系统，阻断水分和养分吸收，阻碍光合作用必需原料的供应，叶片即出现失绿黄化。\n" +
                    "今天田小胖给大家梳理了一些由病害引起柑橘黄化的原因，带领果农朋友挖掘黄化表象下的根源，对症下药。\n" +
                    "柑橘艾滋病·黄龙病\n" +
                    "号称“柑橘艾滋”的柑橘黄龙病，病原体是细菌，通过嫁接和柑橘木虱转播。症状表现为柑橘整株叶片发黄，果实病变，最终死亡。基于柑橘黄龙病危害性，好多果农朋友一旦发现自家柑橘叶片发黄，就会害怕感染了黄龙病，田小盼为大家整理了几个区分黄龙病黄化的小技巧：\n" +
                    "斑驳状黄化叶\n" +
                    "病叶左右不对称；叶基先黄化；斑块交界不明显；病叶脆硬而滑多发生在已经转绿的老熟叶片上，最初从叶脉或叶缘开始退绿黄化，并逐渐向叶肉扩散，形成斑驳黄化，此斑驳黄化细看似一团黄色的烟雾弥散叶片上，其黄化颜色深浅不一，与健康组织无清晰边界。\n" +
                    " 缺素状黄化\n" +
                    "类似缺素黄化，这种症状经常出现却难以判断。多发生在新稍叶片刚转绿，并一直保持缺素状黄化，即使补充足够的营养元素也无法逆转。\n" +
                    "产生原因是病菌堵塞树干韧皮部后，根系难以获得营养，部分根系饥饿而枯死，难以吸收土壤中的营养物质，从而引起新稍缺素状黄化，是病菌间接原因造成的黄化症状，不同于斑驳黄化，是病菌直接堵塞叶脉造成的斑驳黄化。\n" +
                    "嫩梢插金花\n" +
                    "秋冬季新梢不转绿，表现均匀黄化的症状，其特点是病树上一束或少数几丛新梢不转色，呈现亮黄色，像一朵黄花俏立枝头，此症状明显，易于普查识别。\n" +
                    "初发病果园病树零星分布。不同病树症状不对称、不统一。初期只有1-2个枝梢发病，大部分枝叶果正常。同一病树上不同病枝症状不对称、不一样。同一病枝上不同病叶症状不对称、差异大。同一病叶上的不同病斑形状、颜色均不对称。\n" +
                    "“红鼻子果”或“青果”\n" +
                    "红鼻果：正常的果实着色是先从中下部再向上转色或均匀着色，而黄龙病果是先在柄处着色，而中下部不再着色。\n" +
                    "青果：橙柚类感染黄龙病多表现为青果、小果又小又硬，僵果皱缩不能膨大，有些能长成大果，但果实很软，不能着色影响糖酸度，口感酸涩");


            //添加第一条数据
            editor.add("小二农技");
            title.add("让人头疼的柑橘黄化，病害？虫害？缺素？");
            time.add("2020-03-02");
            img.add(R.drawable.t0);
            read_number.add(43);
            content.add("柑橘黄化会引起叶片失绿发黄，大大减低光合作用能力，树势变差，从而引发一系列问题，如抗病能力减弱，吸收水肥的能力变差等，最终导致产量低、果实品质差，直接影响果农收益。目前大多数柑橘黄化并不由单一因素引起，而是多种因素叠加所致。\n" +
                    "柑橘受到病菌、病毒和类菌质体等病原体的侵入，会产生的各种分泌物，堵塞输导系统，阻断水分和养分吸收，阻碍光合作用必需原料的供应，叶片即出现失绿黄化。\n" +
                    "今天田小胖给大家梳理了一些由病害引起柑橘黄化的原因，带领果农朋友挖掘黄化表象下的根源，对症下药。\n" +
                    "柑橘艾滋病·黄龙病\n" +
                    "号称“柑橘艾滋”的柑橘黄龙病，病原体是细菌，通过嫁接和柑橘木虱转播。症状表现为柑橘整株叶片发黄，果实病变，最终死亡。基于柑橘黄龙病危害性，好多果农朋友一旦发现自家柑橘叶片发黄，就会害怕感染了黄龙病，田小盼为大家整理了几个区分黄龙病黄化的小技巧：\n" +
                    "斑驳状黄化叶\n" +
                    "病叶左右不对称；叶基先黄化；斑块交界不明显；病叶脆硬而滑多发生在已经转绿的老熟叶片上，最初从叶脉或叶缘开始退绿黄化，并逐渐向叶肉扩散，形成斑驳黄化，此斑驳黄化细看似一团黄色的烟雾弥散叶片上，其黄化颜色深浅不一，与健康组织无清晰边界。\n" +
                    " 缺素状黄化\n" +
                    "类似缺素黄化，这种症状经常出现却难以判断。多发生在新稍叶片刚转绿，并一直保持缺素状黄化，即使补充足够的营养元素也无法逆转。\n" +
                    "产生原因是病菌堵塞树干韧皮部后，根系难以获得营养，部分根系饥饿而枯死，难以吸收土壤中的营养物质，从而引起新稍缺素状黄化，是病菌间接原因造成的黄化症状，不同于斑驳黄化，是病菌直接堵塞叶脉造成的斑驳黄化。\n" +
                    "嫩梢插金花\n" +
                    "秋冬季新梢不转绿，表现均匀黄化的症状，其特点是病树上一束或少数几丛新梢不转色，呈现亮黄色，像一朵黄花俏立枝头，此症状明显，易于普查识别。\n" +
                    "初发病果园病树零星分布。不同病树症状不对称、不统一。初期只有1-2个枝梢发病，大部分枝叶果正常。同一病树上不同病枝症状不对称、不一样。同一病枝上不同病叶症状不对称、差异大。同一病叶上的不同病斑形状、颜色均不对称。\n" +
                    "“红鼻子果”或“青果”\n" +
                    "红鼻果：正常的果实着色是先从中下部再向上转色或均匀着色，而黄龙病果是先在柄处着色，而中下部不再着色。\n" +
                    "青果：橙柚类感染黄龙病多表现为青果、小果又小又硬，僵果皱缩不能膨大，有些能长成大果，但果实很软，不能着色影响糖酸度，口感酸涩");


            //添加第一条数据
            editor.add("小二农技");
            title.add("让人头疼的柑橘黄化，病害？虫害？缺素？");
            time.add("2020-03-02");
            img.add(R.drawable.t0);
            read_number.add(43);
            content.add("柑橘黄化会引起叶片失绿发黄，大大减低光合作用能力，树势变差，从而引发一系列问题，如抗病能力减弱，吸收水肥的能力变差等，最终导致产量低、果实品质差，直接影响果农收益。目前大多数柑橘黄化并不由单一因素引起，而是多种因素叠加所致。\n" +
                    "柑橘受到病菌、病毒和类菌质体等病原体的侵入，会产生的各种分泌物，堵塞输导系统，阻断水分和养分吸收，阻碍光合作用必需原料的供应，叶片即出现失绿黄化。\n" +
                    "今天田小胖给大家梳理了一些由病害引起柑橘黄化的原因，带领果农朋友挖掘黄化表象下的根源，对症下药。\n" +
                    "柑橘艾滋病·黄龙病\n" +
                    "号称“柑橘艾滋”的柑橘黄龙病，病原体是细菌，通过嫁接和柑橘木虱转播。症状表现为柑橘整株叶片发黄，果实病变，最终死亡。基于柑橘黄龙病危害性，好多果农朋友一旦发现自家柑橘叶片发黄，就会害怕感染了黄龙病，田小盼为大家整理了几个区分黄龙病黄化的小技巧：\n" +
                    "斑驳状黄化叶\n" +
                    "病叶左右不对称；叶基先黄化；斑块交界不明显；病叶脆硬而滑多发生在已经转绿的老熟叶片上，最初从叶脉或叶缘开始退绿黄化，并逐渐向叶肉扩散，形成斑驳黄化，此斑驳黄化细看似一团黄色的烟雾弥散叶片上，其黄化颜色深浅不一，与健康组织无清晰边界。\n" +
                    " 缺素状黄化\n" +
                    "类似缺素黄化，这种症状经常出现却难以判断。多发生在新稍叶片刚转绿，并一直保持缺素状黄化，即使补充足够的营养元素也无法逆转。\n" +
                    "产生原因是病菌堵塞树干韧皮部后，根系难以获得营养，部分根系饥饿而枯死，难以吸收土壤中的营养物质，从而引起新稍缺素状黄化，是病菌间接原因造成的黄化症状，不同于斑驳黄化，是病菌直接堵塞叶脉造成的斑驳黄化。\n" +
                    "嫩梢插金花\n" +
                    "秋冬季新梢不转绿，表现均匀黄化的症状，其特点是病树上一束或少数几丛新梢不转色，呈现亮黄色，像一朵黄花俏立枝头，此症状明显，易于普查识别。\n" +
                    "初发病果园病树零星分布。不同病树症状不对称、不统一。初期只有1-2个枝梢发病，大部分枝叶果正常。同一病树上不同病枝症状不对称、不一样。同一病枝上不同病叶症状不对称、差异大。同一病叶上的不同病斑形状、颜色均不对称。\n" +
                    "“红鼻子果”或“青果”\n" +
                    "红鼻果：正常的果实着色是先从中下部再向上转色或均匀着色，而黄龙病果是先在柄处着色，而中下部不再着色。\n" +
                    "青果：橙柚类感染黄龙病多表现为青果、小果又小又硬，僵果皱缩不能膨大，有些能长成大果，但果实很软，不能着色影响糖酸度，口感酸涩");


            //添加第一条数据
            editor.add("小二农技");
            title.add("让人头疼的柑橘黄化，病害？虫害？缺素？");
            time.add("2020-03-02");
            img.add(R.drawable.t0);
            read_number.add(43);
            content.add("柑橘黄化会引起叶片失绿发黄，大大减低光合作用能力，树势变差，从而引发一系列问题，如抗病能力减弱，吸收水肥的能力变差等，最终导致产量低、果实品质差，直接影响果农收益。目前大多数柑橘黄化并不由单一因素引起，而是多种因素叠加所致。\n" +
                    "柑橘受到病菌、病毒和类菌质体等病原体的侵入，会产生的各种分泌物，堵塞输导系统，阻断水分和养分吸收，阻碍光合作用必需原料的供应，叶片即出现失绿黄化。\n" +
                    "今天田小胖给大家梳理了一些由病害引起柑橘黄化的原因，带领果农朋友挖掘黄化表象下的根源，对症下药。\n" +
                    "柑橘艾滋病·黄龙病\n" +
                    "号称“柑橘艾滋”的柑橘黄龙病，病原体是细菌，通过嫁接和柑橘木虱转播。症状表现为柑橘整株叶片发黄，果实病变，最终死亡。基于柑橘黄龙病危害性，好多果农朋友一旦发现自家柑橘叶片发黄，就会害怕感染了黄龙病，田小盼为大家整理了几个区分黄龙病黄化的小技巧：\n" +
                    "斑驳状黄化叶\n" +
                    "病叶左右不对称；叶基先黄化；斑块交界不明显；病叶脆硬而滑多发生在已经转绿的老熟叶片上，最初从叶脉或叶缘开始退绿黄化，并逐渐向叶肉扩散，形成斑驳黄化，此斑驳黄化细看似一团黄色的烟雾弥散叶片上，其黄化颜色深浅不一，与健康组织无清晰边界。\n" +
                    " 缺素状黄化\n" +
                    "类似缺素黄化，这种症状经常出现却难以判断。多发生在新稍叶片刚转绿，并一直保持缺素状黄化，即使补充足够的营养元素也无法逆转。\n" +
                    "产生原因是病菌堵塞树干韧皮部后，根系难以获得营养，部分根系饥饿而枯死，难以吸收土壤中的营养物质，从而引起新稍缺素状黄化，是病菌间接原因造成的黄化症状，不同于斑驳黄化，是病菌直接堵塞叶脉造成的斑驳黄化。\n" +
                    "嫩梢插金花\n" +
                    "秋冬季新梢不转绿，表现均匀黄化的症状，其特点是病树上一束或少数几丛新梢不转色，呈现亮黄色，像一朵黄花俏立枝头，此症状明显，易于普查识别。\n" +
                    "初发病果园病树零星分布。不同病树症状不对称、不统一。初期只有1-2个枝梢发病，大部分枝叶果正常。同一病树上不同病枝症状不对称、不一样。同一病枝上不同病叶症状不对称、差异大。同一病叶上的不同病斑形状、颜色均不对称。\n" +
                    "“红鼻子果”或“青果”\n" +
                    "红鼻果：正常的果实着色是先从中下部再向上转色或均匀着色，而黄龙病果是先在柄处着色，而中下部不再着色。\n" +
                    "青果：橙柚类感染黄龙病多表现为青果、小果又小又硬，僵果皱缩不能膨大，有些能长成大果，但果实很软，不能着色影响糖酸度，口感酸涩");


            //将数据塞入表
            for (int i = 0; i < editor.size(); i++) {
                contentValues.clear();
                contentValues.put("editor", editor.get(i));
                contentValues.put("title", title.get(i));
                contentValues.put("time", time.get(i));
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), img.get(i));
                int size = bitmap1.getWidth() * bitmap1.getHeight() * 4;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imagedata1 = baos.toByteArray();

                contentValues.put("img", imagedata1);
                contentValues.put("read_number", read_number.get(i));
                contentValues.put("content", content.get(i));
                db.insert("information", null, contentValues);
            }


            /**以下是帖子初始化**/
            contentValues.clear();
            contentValues.put("username", "王翠超");
            contentValues.put("time", "2020-01-23 17:55");
            contentValues.put("content", "药害用什么药？");
            contentValues.put("discuss_number", 20);
            db.insert("comment", null, contentValues);

            contentValues.clear();
            contentValues.put("username", "王翠超");
            contentValues.put("time", "2020-01-25 10:35");
            contentValues.put("content", "红苗怎么回事");
            contentValues.put("discuss_number", 36);
            db.insert("comment", null, contentValues);

            /**以下是评论初始化**/
            String sql4 = "CREATE TABLE discuss(id INTEGER PRIMARY KEY AUTOINCREMENT,circle_id Integer,username TEXT, reply_people_name TEXT,time TEXT, content TEXT)";


            contentValues.clear();
            contentValues.put("circle_id", 1);
            contentValues.put("username", "张三丰");
            contentValues.put("reply_people_name", "王翠超");
            contentValues.put("time", "2020-01-23 18:55");
            contentValues.put("content", "到水稻病虫害资料库查看，如果不满意可以在问题下回复追问");
            db.insert("discuss", null, contentValues);

            contentValues.clear();
            contentValues.put("circle_id", 1);
            contentValues.put("username", "雷潘");
            contentValues.put("reply_people_name", "张三丰");
            contentValues.put("time", "2020-01-23 20:35");
            contentValues.put("content", "谢谢，我去查下资料");
            db.insert("discuss", null, contentValues);


        }

        cursor.close();
        db.close();

    }

    protected void initView() {
        TextView icon_user = findViewById(R.id.icon_user);
        TextView icon_pass = findViewById(R.id.icon_pass);
        et_user = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);

        //带删除
        et_user.setText("123");
        et_password.setText("123");

        btn_login = findViewById(R.id.btn_login);
        manager_login = findViewById(R.id.manager_login);

        icon_show = findViewById(R.id.icon_show);
        register = findViewById(R.id.register);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        icon_user.setTypeface(font);
        icon_pass.setTypeface(font);
        icon_show.setTypeface(font);
        icon_user.setText(getResources().getString(R.string.user));
        icon_pass.setText(getResources().getString(R.string.password));
        icon_show.setText(getResources().getString(R.string.eye_close));

        //学生登录
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IsManager = 0;

                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
        });

        //管理员登录
        manager_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IsManager = 1;
                login(IsManager);
            }
        });

        //显示、隐藏密码
        icon_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show = !show;
                if (show) {
                    icon_show.setText(getResources().getString(R.string.eye_open));
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    icon_show.setText(getResources().getString(R.string.eye_close));
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //注册按钮
        register.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

    }

    //获取账号
    public String getUser() {
        return et_user.getText().toString().trim();
    }

    //获取密码
    public String getPassword() {
        return et_password.getText().toString().trim();
    }

    //点击登录按钮后的逻辑判断
    public void login(int i) {
        if (getUser().isEmpty()) {
            showToast("请输入用户名！");
            return;
        }
        if (getPassword().isEmpty()) {
            showToast("请输入密码！");
            return;
        }
        setLoginBtnClickable(false);
        //判断账号密码
        SqlHelper sqlHelper = new SqlHelper(this);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        String sql = null;
        if (i == 0)
            sql = "select * from userInfo where username = ? and password = ?";
        else if (i == 1)
            sql = "select * from manager where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{getUser(), getPassword()});
        if (cursor.moveToFirst()) {
            cursor.close();
            showToast("登陆成功");
            SaveUserInfo(i);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("IsManager", i);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

//            finish();
        } else {
            showToast("账号或密码不正确");
        }
        setLoginBtnClickable(true);
    }

    //保存当前登录账号
    private void SaveUserInfo(int i) {
        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", getUser());
        if (i == 0)
            editor.putInt("IsManager", 0);
        else if (i == 1)
            editor.putInt("IsManager", 1);

        editor.apply();
    }

    public void setLoginBtnClickable(Boolean clickable) {
        btn_login.setClickable(clickable);
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
