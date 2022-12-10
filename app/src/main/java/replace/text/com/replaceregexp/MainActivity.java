package replace.text.com.replaceregexp;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends AppCompatActivity {

    String[] specialTag = {"&lt;", "<", "&gt;", ">", "&quot;", "\"", "&acute;", "'", "&#45;", "-", "&#44;", ",", "&#40;", "(", "&#41;", ")", "&#124;", "￦", "&#nbsp;", " ", "&amp;", "&"};
    public static Handler sHan;
    static public EditText[] editArr = new ClearEditText[7];
    static public TextView[] resultArr = new TextView[7];
    public static Context mcon;
    static public ViewPager vp;
    static NestedScrollView scroll;
    private final long FINISH_INTERVAL_TIME = 2000;
    public TextView textsss;
    String cont = "";
    LinearLayout mainLayout, ToolR;
    EditText editS, editR, editS2;
    CheckBox isReg, RepAll;
    TextView result;
    Button btnR, btnC, btnT;
    Toolbar toolbar;
    ImageView ivMain;
    int Sel2;
    @SuppressLint("HandlerLeak")
    Handler myHan = new Handler();
    String RT2 = "";
    Boolean isConsole = false;
    Function MyResponder = null;
    TextView mtext = null;
    private long backPressedTime = 0;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onStop() {
        Thread.currentThread().interrupt();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(ctx, "ㅇㅇ", Toast.LENGTH_SHORT).show();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        ClearEditText.setUseClearIcon(mPref.getBoolean("clearButton", true));
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mcon = this;

        sHan = new Handler();


        toolbar = new Toolbar(this);

        toolbar.setTitle("Name Tool");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor("#3f51b5"));
        setSupportActionBar(toolbar);

        vp = new ViewPager(this);
        SlidingTabLayout slidingTab = new SlidingTabLayout(this);

        MyAdapter adapter = new MyAdapter();
        vp.setAdapter(adapter);


        slidingTab.setViewPager(vp);
        slidingTab.setSelectedIndicatorColors(Color.parseColor("#e5526f"));
        slidingTab.setBackgroundColor(Color.parseColor("#3f51b5"));

        ToolR = new LinearLayout(MainActivity.this);
        ToolR.setOrientation(1);
        ToolR.addView(toolbar);
        ToolR.addView(slidingTab);
        ToolR.addView(vp);


        //ToolR.addView(scroll);
        setContentView(ToolR);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActionModeStarted(android.view.ActionMode mode) {
        //mode.getMenu().clear();
        Menu menus = mode.getMenu();
        onInitializeMenu(menus);
        super.onActionModeStarted(mode);
    }

    private List<ResolveInfo> getSupportedActivities() {
        PackageManager packageManager =
                MainActivity.this.getPackageManager();
        return
                packageManager.queryIntentActivities(createProcessTextIntent(),
                        0);
    }

    private Intent createProcessTextIntent() {
        return new Intent()
                .setAction(Intent.ACTION_PROCESS_TEXT)
                .setType("text/plain");
    }

    private Intent createProcessTextIntentForResolveInfo(ResolveInfo info) {
        return createProcessTextIntent()
                .putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
                .setClassName(info.activityInfo.packageName,
                        info.activityInfo.name);
    }

    public void onInitializeMenu(Menu menu) {
        int menuItemOrder = 100;
        for (ResolveInfo resolveInfo : getSupportedActivities()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                menu.add(Menu.NONE, Menu.NONE,
                        menuItemOrder++,
                        resolveInfo.loadLabel(this.getPackageManager()))//.setIcon(resolveInfo.loadIcon(getPackageManager()))
                        .setIntent(createProcessTextIntentForResolveInfo(resolveInfo))
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Snackbar snack = Snackbar.make(MainActivity.this.getWindow().getDecorView().getRootView(),
                    "뒤로가기 키를 한 번 더 누르거나 바로 종료를 누르시면 종료 됩니다.", 2000).setActionTextColor(Color.WHITE);
            snack.setAction("바로 종료", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.this.finish();
                }
            });
            snack.getView().setBackgroundColor(Color.parseColor("#3eb489"));
            snack.show();
        }
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String Decryption(String str, String pw, int keys) throws Exception {
//        byte[] bytes = pw.getBytes("utf-8");
//        SecureRandom sr = new SecureRandom();
//        sr.setSeed(bytes);
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        kgen.init(keys, sr);
//
//        SecretKey skey = kgen.generateKey();
//        SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
//
//        Cipher c = Cipher.getInstance("AES");
//        c.init(Cipher.DECRYPT_MODE, skeySpec);
//        byte[] decrypted = c.doFinal(Base64.decode(str, Base64.DEFAULT));
//        return new String(decrypted);
        SecretKeyFactory secKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(pw.toCharArray(), pw.getBytes(), 50, keys);
        SecretKey pbeSecretKey = secKeyFactory.generateSecret(spec);
        SecretKey skeySpec = new SecretKeySpec(pbeSecretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(str));
        return new String(decrypted);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("ResourceType")
    public LinearLayout decr() {
        final LinearLayout L1 = new LinearLayout(MainActivity.this);
        L1.setOrientation(1);
        TextInputLayout E1L = new TextInputLayout(MainActivity.this);
        final ClearEditText E1 = new ClearEditText(MainActivity.this);
        E1.setHint("Decode Target");
        editArr[3] = E1;
        ScrollingView scrollingView = new ScrollingView(MainActivity.this);
        scrollingView.setMaxLines(4);
        scrollingView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        E1L.addView(E1);
        scrollingView.addView(E1L);

        LinearLayout L2 = new LinearLayout(MainActivity.this);

        final String[] str = {"Selection", "Decode", "Base64", "Eval", "AES", "HTML_Arrow"};

        final EditText edit = new EditText(MainActivity.this);
        final EditText edit2 = new EditText(MainActivity.this);
        edit.setHint("Key");
        edit2.setHint("256/192/128");
        edit.setId(123);
        edit2.setId(456);

        Spinner spin = new Spinner(MainActivity.this);
        spin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, str);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                Sel2 = i;
                if ((EditText) L1.findViewById(123) != null) {
                    L1.removeView((EditText) L1.findViewById(123));
                    L1.removeView((EditText) L1.findViewById(456));
                }
                if (Sel2 == 4) {
                    L1.addView(edit, 1);
                    L1.addView(edit2, 2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });


        L2.addView(scrollingView);
        L2.addView(spin);

        final TextView T2 = new TextView(MainActivity.this);
        T2.setTextIsSelectable(true);
        T2.setTextSize(30);
        resultArr[3] = T2;

        Button B2 = new Button(MainActivity.this);
        B2.setTransformationMethod(null);
        B2.setText("Continue Decode");
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targ = E1.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                if (prefs.getBoolean("auto", false) && RT2.equals(targ) && T2.getText().toString().length() != 0)
                    targ = T2.getText().toString();
                if (Sel2 == 0) T2.setText(targ);
                else if (Sel2 == 1) {
                    //Decode
                    try {
                        T2.setText(URLDecoder.decode(targ, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (Sel2 == 2) {
                    //Base64
                    try {
                        T2.setText(new String(Base64.decode(targ.getBytes("UTF-8"), Base64.NO_WRAP), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (Sel2 == 3) {
                    //Eval
                    T2.setText(endless(
                            targ.replace("\\\\", "\\").replace("\\n", "\n").replace("\\\"", "\"")
                                    .replace("\\\'", "\'")));
                } else if (Sel2 == 4) {
                    try {
                        T2.setText(Decryption(targ, edit.getText().toString(), Integer.parseInt(edit2.getText().toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Sel2 == 5) {
                    String res = targ;
                    for (int i = 0; i < specialTag.length; i += 2) {
                        res = res.replaceAll(specialTag[i], specialTag[i + 1]);
                    }
                    T2.setText(res);
                }
                RT2 = E1.getText().toString();
            }
        });

        Button B3 = new Button(MainActivity.this);
        B3.setText("Copy Result");
        B3.setTransformationMethod(null);
        B3.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                if (T2.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Decode", T2.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Copyed Decode", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Can't Copy", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button B5 = new Button(MainActivity.this);
        B5.setText("Clear Result");
        B5.setTransformationMethod(null);
        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T2.setText("");
            }
        });
        Button B4 = new Button(MainActivity.this);
        B4.setText("Result to Target");
        B4.setTransformationMethod(null);
        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                E1.setText(T2.getText().toString());
            }
        });

        L1.addView(L2);
        L1.addView(B2);
        L1.addView(B3);
        L1.addView(B5);
        L1.addView(B4);
        L1.addView(T2);


        NestedScrollView scr = new NestedScrollView(MainActivity.this);
        LinearLayout li = new LinearLayout(MainActivity.this);
        li.setOrientation(1);
        scr.addView(L1);
        li.addView(scr);

        return li;
    }

    private String endless(String a) {
        Pattern p = Pattern.compile("\\\\([0-7]{1,3})");
        Matcher m = p.matcher(a);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            int b = Integer.parseInt(m.group(1));
            m.appendReplacement(sb, String.valueOf(b < 378 ? (char) (Integer.parseInt(b + "", 8)) : (char) (Integer.parseInt((b + "").substring(0, 2), 8))));
        }
        m.appendTail(sb);
        a = sb.toString();
        p = Pattern.compile("\\\\x([a-fA-F\\d]{2})");
        m = p.matcher(a);
        sb = new StringBuffer();
        while (m.find()) {
            try {
                m.appendReplacement(sb, String.valueOf((char) Integer.parseInt(m.group(1), 16)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        m.appendTail(sb);
        a = sb.toString();
        p = Pattern.compile("\\\\u([a-fA-F\\d]{4})");
        m = p.matcher(a);
        sb = new StringBuffer();
        while (m.find()) {
            try {
                m.appendReplacement(sb, String.valueOf((char) Integer.parseInt(m.group(1), 16)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        m.appendTail(sb);
        a = sb.toString();
        return a;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            ivMain.setImageURI(data.getData());
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LinearLayout rhino() {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(1);
        TextInputLayout editL = new TextInputLayout(MainActivity.this);
        ScrollingView scrollingView = new ScrollingView(MainActivity.this);
        scrollingView.setMaxLines(15);
        final ClearEditText edit = new ClearEditText(MainActivity.this);
        edit.setHint("Input Code");
        editArr[5] = edit;
        editL.addView(edit);
        scrollingView.addView(editL);
        Button btn = new Button(MainActivity.this);
        btn.setText("Run");
        btn.setTransformationMethod(null);
        final TextView text = new TextView(this);
        text.setTextIsSelectable(true);
        text.setTextSize(18);
        resultArr[5] = text;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                runJS(edit.getText().toString(), text, edit);
            }
        });
        layout.addView(scrollingView);
        layout.addView(btn);
        layout.addView(text);
        NestedScrollView scr = new NestedScrollView(MainActivity.this);
        LinearLayout li = new LinearLayout(MainActivity.this);
        li.setOrientation(1);
        scr.addView(layout);
        li.addView(scr);
        return li;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void runJS(final String str, final TextView text, final EditText edit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strs = null;
                try {
                    isConsole = false;
                    strs = initializeJS(str, text, edit);
                    org.mozilla.javascript.Context.exit();

                    if (!isConsole) {
                        final String finalStrs = strs;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(finalStrs);
                            }
                        });
                    }
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            e.printStackTrace(pw);
                            String sStack = sw.toString();
                            text.setText(sStack);
                            e.printStackTrace();
                        }
                    });
                }
            }
        }).start();
    }

    private String initializeJS(String code, TextView text, EditText edit) {

        mtext = text;
        System.gc();

        org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
        rhino.setOptimizationLevel(-1);
        rhino.setLanguageVersion(org.mozilla.javascript.Context.VERSION_ES6);

        try {
            ScriptableObject scope = rhino.initStandardObjects();

            //addMethod
            ScriptableObject.putProperty(scope, "ctx", this);
            ScriptableObject.putProperty(scope, "printTable", text);
            ScriptableObject.putProperty(scope, "scriptTable", edit);
            ScriptableObject.putProperty(scope, "console", new console());
            scope.defineFunctionProperties(new String[]{"print"}, function.class, ScriptableObject.DONTENUM);

            //assets
            //InputStream iss = am.open("MoreMath.js");
            //rhino.evaluateReader(scope, new InputStreamReader(iss), "", 1, null);
            //Main

            Function str = null;
            if (MyResponder == null)
                str = rhino.compileFunction(scope, "function response(str){return eval(str)+'';}", "MyScript", 0, null);
            else str = MyResponder;
            //Function str = rhino.compileFunction(scope, "function response(str){return eval(str)+'';}", "MyScript", 0, null);
            MyResponder = str;
            return org.mozilla.javascript.Context.toString(str.call(rhino, scope, scope, new Object[]{code}));
            //str.exec(rhino, scope);
            //Object obj = str.get("response", (Scriptable) scope);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStack = sw.toString();
            e.printStackTrace();
            return sStack;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LinearLayout ParseLayout() {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(1);
        LinearLayout layout1 = new LinearLayout(MainActivity.this);
        layout1.setOrientation(1);
        NestedScrollView scroll = new NestedScrollView(MainActivity.this);
        TextInputLayout editL = new TextInputLayout(MainActivity.this);
        ScrollingView scrollingView = new ScrollingView(MainActivity.this);
        scrollingView.setMaxLines(15);
        final ClearEditText edit = new ClearEditText(MainActivity.this);
        edit.setHint("Input Parse URL");
        editArr[6] = edit;
        edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        edit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String naverHtml = getNaverHtml(edit.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textsss.setText(naverHtml);
                            }
                        });
                    }
                }).start();
                return true;
            }
        });
        editL.addView(edit);
        scrollingView.addView(editL);
        Button btn = new Button(MainActivity.this);
        btn.setTransformationMethod(null);
        btn.setText("Continue Pasing");
        textsss = new TextView(MainActivity.this);
        textsss.setTextIsSelectable(true);
        textsss.setTextSize(18);
        resultArr[6] = textsss;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String naverHtml = getNaverHtml(edit.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textsss.setText(naverHtml);
                            }
                        });
                    }
                }).start();
            }
        });
        layout.addView(scrollingView);
        layout.addView(btn);
        layout.addView(textsss);
        scroll.addView(layout);
        layout1.addView(scroll);
        return layout1;


    }

    private String getNaverHtml(String as) {
        String naverHtml = "";

        if (as.indexOf("http") != 0) as = "http://" + as;
        try {
            URL url = new URL(as);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1500);
            con.setReadTimeout(1500);

            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                naverHtml += str + "\n";
            }
            con.disconnect();
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return naverHtml;
    }

    public static class function {
        @JSStaticFunction
        public static void print(String str) {
            Looper.prepare();
            Toast.makeText(mcon, str, Toast.LENGTH_SHORT).show();
            Looper.loop();

        }

//        @JSStaticFunction
//        public void runOnUiThread(Runnable runnable) {
//            runOnUi(runnable);
//        }
    }

    public class console {
        @SuppressLint("SetTextI18n")
        public String log(final String str) {
            myHan.post(new Runnable() {
                @Override
                public void run() {
                    mtext.setText(mtext.getText() + str + "\n");
                }
            });
            if (!isConsole) isConsole = true;
            return "";
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class MyAdapter extends PagerAdapter {

        String[] title = {"    Replace    ", "    Search    ", "    Encode    ", "    Decode    ", "    Source    ", "    Rhino    ", "    Parse    "};
        ArrayList<LinearLayout> lists = new ArrayList<>();
        int Sel = 0;
        String RT = "";

        public MyAdapter() {


            scroll = new NestedScrollView(MainActivity.this);


            mainLayout = new LinearLayout(MainActivity.this);
            mainLayout.setOrientation(1);
            TextInputLayout editSL = new TextInputLayout(MainActivity.this);
            editSL.setCounterEnabled(true);
            ScrollingView scrollingView = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(15);
            ScrollingView scrollingView2 = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(15);
            ScrollingView scrollingView3 = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(15);
            editS = new ClearEditText(MainActivity.this);
            editArr[0] = editS;
            editS.setHint("Replace Target");
            editSL.addView(editS);
            scrollingView.addView(editSL);
            TextInputLayout editRL = new TextInputLayout(MainActivity.this);
            editR = new ClearEditText(MainActivity.this);
            editR.setHint("Replace RegExp");
            editRL.addView(editR);
            scrollingView2.addView(editRL);
            TextInputLayout editS2L = new TextInputLayout(MainActivity.this);
            editS2 = new ClearEditText(MainActivity.this);
            editS2.setHint("Replace Content");
            editS2L.addView(editS2);
            scrollingView3.addView(editS2L);
            isReg = new CheckBox(MainActivity.this);
            isReg.setText("Use RegExp");
            isReg.setChecked(true);
            RepAll = new CheckBox(MainActivity.this);
            final CheckBox Reo = new CheckBox(MainActivity.this);
            LinearLayout lll = new LinearLayout(MainActivity.this);
            lll.addView(RepAll);
            lll.addView(Reo);
            RepAll.setText("Replace All      ");
            RepAll.setChecked(true);
            RepAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        isReg.setEnabled(true);
                        Reo.setChecked(false);
                    } else {
                        isReg.setChecked(true);
                        Reo.setChecked(true);
                    }
                }
            });
            Reo.setText("Replace One");
            Reo.setChecked(false);
            Reo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        isReg.setEnabled(false);
                        RepAll.setChecked(false);
                    } else RepAll.setChecked(true);
                }
            });
            btnR = new Button(MainActivity.this);
            btnR.setText("Continue Replace");
            btnR.setTransformationMethod(null);
            btnR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        if (prefs.getBoolean("auto", false) && cont.equals(editS.getText().toString()) && result.getText().toString().length() != 0)
                            cont = result.getText().toString();
                        else cont = editS.getText().toString();

                        if (RepAll.isChecked()) {
                            if (isReg.isChecked())
                                result.setText(cont.replaceAll(editR.getText() + "", editS2.getText() + ""));
                            else
                                result.setText(cont.replace(editR.getText() + "", editS2.getText() + ""));
                        } else
                            result.setText(cont.replaceFirst(Pattern.quote(editR.getText() + ""), editS2.getText() + ""));
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
            btnC = new Button(MainActivity.this);
            btnC.setText("Copy Result");
            btnC.setTransformationMethod(null);
            btnC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result.getText().toString().length() != 0) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Replace", result.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MainActivity.this, "Copyed Result", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "Can't Copy", Toast.LENGTH_SHORT).show();
                }
            });
            btnT = new Button(MainActivity.this);
            btnT.setText("Result to Target");
            btnT.setTransformationMethod(null);
            btnT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editS.setText(result.getText() + "");
                    result.setText("");
                }
            });

            final Button cl = new Button(MainActivity.this);
            cl.setText("Clear Result");
            cl.setTransformationMethod(null);
            cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    result.setText("");
                }
            });


            result = new TextView(MainActivity.this);
            result.setTextIsSelectable(true);
            result.setTextSize(30);
            resultArr[0] = result;

            mainLayout.addView(scrollingView);
            mainLayout.addView(scrollingView2);
            mainLayout.addView(scrollingView3);
            mainLayout.addView(isReg);
            //mainLayout.addView(RepAll);
            //mainLayout.addView(Reo);
            mainLayout.addView(lll);
            mainLayout.addView(btnR);
            mainLayout.addView(btnC);
            mainLayout.addView(cl);
            mainLayout.addView(btnT);
            mainLayout.addView(result);

            scroll.addView(mainLayout);


            LinearLayout ll = new LinearLayout(MainActivity.this);
            ll.setOrientation(1);


            MainActivity.scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ll.addView(MainActivity.scroll);
            lists.add(ll);
            shit();
            dec();
            lists.add(decr());
            lists.add(sourceEncode.sourceEn(MainActivity.this));
            lists.add(rhino());
            lists.add(ParseLayout());
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void shit() {

            final EditText target, ment, conS;
            final Button btnF, btnC, btnT;
            final NestedScrollView scroll;
            final LinearLayout layout;
            //TextView result;

            final TextView result = new TextView(MainActivity.this);
            result.setTextIsSelectable(true);
            result.setTextSize(30);
            resultArr[1] = result;


            scroll = new NestedScrollView(MainActivity.this);
            layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(1);

            ScrollingView scrollingView = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(15);
            ScrollingView scrollingView2 = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(15);
            ScrollingView scrollingView3 = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(15);

            TextInputLayout targetL = new TextInputLayout(MainActivity.this);
            targetL.setCounterEnabled(true);
            target = new ClearEditText(MainActivity.this);
            target.setHint("Search Target");
            editArr[1] = target;
            targetL.addView(target);
            scrollingView.addView(targetL);
            final TextInputLayout mentL = new TextInputLayout(MainActivity.this);
            ment = new ClearEditText(MainActivity.this);
            ment.setHint("Search RegExp");
            mentL.addView(ment);
            scrollingView2.addView(mentL);
            final TextInputLayout conSL = new TextInputLayout(MainActivity.this);
            conS = new ClearEditText(MainActivity.this);
            conSL.setHint("Connect to Search Content");
            conSL.addView(conS);
            scrollingView3.addView(conSL);

            LinearLayout lll = new LinearLayout(MainActivity.this);
            final CheckBox isReg = new CheckBox(MainActivity.this);
            final CheckBox RepAll = new CheckBox(MainActivity.this);
            lll.addView(isReg);
            lll.addView(RepAll);
            isReg.setText("Search All       ");
            isReg.setChecked(true);
            isReg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        RepAll.setChecked(false);
                        conS.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        layout.addView(mentL, 1);
                        conSL.setHint("Connect to Search Content");
                    } else RepAll.setChecked(true);
                }
            });


            RepAll.setText("Search Line");
            RepAll.setChecked(false);
            RepAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        isReg.setChecked(false);
                        conS.setInputType(InputType.TYPE_CLASS_NUMBER);
                        layout.removeView(mentL);
                        conSL.setHint("Which Line to Search");
                    } else isReg.setChecked(true);
                }
            });

            btnF = new Button(MainActivity.this);
            btnF.setText("Continue Search");
            btnF.setTransformationMethod(null);
            btnF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isReg.isChecked()) {
                        SpannableStringBuilder ssb = new SpannableStringBuilder(target.getText().toString());
                        ssb.setSpan(new android.text.style.ForegroundColorSpan(Color.BLACK), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Pattern pat = Pattern.compile("(" + ment.getText() + ")");
                        Matcher mat = pat.matcher(ssb);
                        String res = "";
                        while (mat.find()) {
                            res += (conS.getText().toString()) + mat.group(1);
                            ssb.setSpan(new android.text.style.ForegroundColorSpan(Color.RED), mat.start(), mat.end(), android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        target.setText(ssb);
                        result.setText(res.replaceFirst(Pattern.quote(conS.getText() + ""), ""));
                    } else {
                        try {
                            result.setText(target.getText().toString().split("\n")[new Integer(conS.getText().toString()) - 1]);
                        } catch (Exception e) {
                            result.setText("");//invalid line
                        }
                    }
                }
            });


            btnC = new Button(MainActivity.this);
            btnC.setText("Copy Result");
            btnC.setTransformationMethod(null);
            btnC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result.getText().toString().length() != 0) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Search", result.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MainActivity.this, "Copyed Result", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "Can't Copy", Toast.LENGTH_SHORT).show();
                }
            });

            btnT = new Button(MainActivity.this);
            btnT.setText("Result to Target");
            btnT.setTransformationMethod(null);
            btnT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    target.setText(result.getText() + "");
                    result.setText("");
                }
            });


            layout.addView(scrollingView);
            layout.addView(scrollingView2);
            layout.addView(scrollingView3);
            //layout.addView(isReg);
            //layout.addView(RepAll);
            layout.addView(lll);
            layout.addView(btnF);
            layout.addView(btnC);
            layout.addView(btnT);
            layout.addView(result);

            scroll.addView(layout);
            LinearLayout ll = new LinearLayout(MainActivity.this);
            ll.setOrientation(1);
            scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ll.addView(scroll);
            lists.add(ll);


        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(lists.get(position));
            return lists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @SuppressLint("ResourceType")
        public void dec() {

            Sel = 0;
            NestedScrollView scroll = new NestedScrollView(MainActivity.this);
            final LinearLayout L1 = new LinearLayout(MainActivity.this);
            L1.setOrientation(1);
            TextInputLayout E1L = new TextInputLayout(MainActivity.this);
            ScrollingView scrollingView = new ScrollingView(MainActivity.this);
            scrollingView.setMaxLines(4);
            final ClearEditText E1 = new ClearEditText(MainActivity.this);
            E1.setHint("Encode Target");
            editArr[2] = E1;
            scrollingView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            E1L.addView(E1);
            scrollingView.addView(E1L);
            LinearLayout L2 = new LinearLayout(MainActivity.this);

            final EditText edit = new EditText(MainActivity.this);
            final EditText edit2 = new EditText(MainActivity.this);
            edit.setHint("Key");
            edit2.setHint("256/192/128");
            edit.setId(123);
            edit2.setId(456);

            final String[] str = {"Selection", "Encode", "Base64", "Uneval", "Radix", "Md5", "Sha-256", "AES", "Choose"};
            final String[] Code = {"Input", "ASCII", "Hex", "Dec", "Oct", "Bin"};
            final String[] Code2 = {"Output", "ASCII", "Hex", "Dec", "Oct", "Bin"};

            final Spinner spinC = new Spinner(MainActivity.this);
            spinC.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            ArrayAdapter adapterC = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, Code);
            spinC.setAdapter(adapterC);

            final Spinner spinC2 = new Spinner(MainActivity.this);
            spinC2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            ArrayAdapter adapterC2 = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, Code2);
            spinC2.setAdapter(adapterC2);

            final LinearLayout Rli = new LinearLayout(MainActivity.this);
            Rli.addView(spinC);
            Rli.addView(spinC2);
            Rli.setId(32);

            final EditText rE = new EditText(MainActivity.this);
            rE.setHint("Input 2~36 Asc=0");
            rE.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            rE.setInputType(InputType.TYPE_CLASS_NUMBER);
            final EditText rE2 = new EditText(MainActivity.this);
            rE2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            rE2.setHint("Output 2~36 Asc=0");
            rE2.setInputType(InputType.TYPE_CLASS_NUMBER);

            final LinearLayout Rli2 = new LinearLayout(MainActivity.this);
            Rli2.addView(rE);
            Rli2.addView(rE2);
            Rli2.setId(34);

            final LinearLayout lays2 = new LinearLayout(MainActivity.this);

            final CheckBox check2 = new CheckBox(MainActivity.this);
            check2.setId(391);
            check2.setText("Use Hexadecimal");
            check2.setChecked(false);
            check2.setEnabled(false);
            check2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            Spinner spin = new Spinner(MainActivity.this);

            final CheckBox check = new CheckBox(MainActivity.this);
            check.setId(39);
            check.setChecked(false);
            check.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (Sel == 4) {
                        if ((LinearLayout) L1.findViewById(34) != null)
                            L1.removeView((LinearLayout) L1.findViewById(34));
                        if ((LinearLayout) L1.findViewById(32) != null)
                            L1.removeView((LinearLayout) L1.findViewById(32));
                        if (isChecked)
                            L1.addView(Rli2, 1);
                        else
                            L1.addView(Rli, 1);
                    } else {
                        if (isChecked)
                            check2.setEnabled(true);
                        else check2.setEnabled(false);
                    }
                }
            });

            lays2.setId(237);
            lays2.addView(check2);

            spin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, str);
            spin.setAdapter(adapter);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                    Sel = i;
                    if ((CheckBox) lays2.findViewById(39) != null)
                        lays2.removeView((CheckBox) lays2.findViewById(39));
                    if ((LinearLayout) L1.findViewById(34) != null)
                        L1.removeView((LinearLayout) L1.findViewById(34));
                    if ((LinearLayout) L1.findViewById(32) != null)
                        L1.removeView((LinearLayout) L1.findViewById(32));
                    if ((EditText) L1.findViewById(123) != null) {
                        L1.removeView((EditText) L1.findViewById(123));
                        L1.removeView((EditText) L1.findViewById(456));
                    }
                    if ((CheckBox) lays2.findViewById(39) != null)
                        lays2.removeView((CheckBox) lays2.findViewById(39));
                    if ((CheckBox) L1.findViewById(39) != null)
                        L1.removeView((CheckBox) L1.findViewById(39));
                    if ((LinearLayout) L1.findViewById(237) != null)
                        L1.removeView((LinearLayout) L1.findViewById(237));


                    if (Sel == 3) {
                        check.setText("String Uneval");
                        lays2.addView(check, 0);
                        L1.addView(lays2, 1);
                        check.setChecked(false);
                    }
                    if (Sel == 4) {
                        check.setText("Custom");
                        L1.addView(check, 1);
                        check.setChecked(false);
                        if ((LinearLayout) L1.findViewById(32) == null)
                            L1.addView(Rli, 1);
                    }
                    if (Sel == 7) {
                        L1.addView(edit, 1);
                        L1.addView(edit2, 2);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView adapterView) {

                }
            });

            L2.addView(scrollingView);
            L2.addView(spin);

            final TextView T2 = new TextView(MainActivity.this);
            T2.setTextIsSelectable(true);
            T2.setTextSize(30);
            resultArr[2] = T2;

            Button B2 = new Button(MainActivity.this);
            B2.setTransformationMethod(null);
            B2.setText("Continue Encode");

            B2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String targ = E1.getText().toString();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    if (prefs.getBoolean("auto", false) && RT.equals(targ) && T2.getText().toString().length() != 0)
                        targ = T2.getText().toString();
                    if (Sel == 0) T2.setText(targ);
                    else if (Sel == 1) {
                        //Encode
                        try {
                            T2.setText(URLEncoder.encode(targ, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else if (Sel == 2) {
                        //Base64
                        try {
                            T2.setText(Base64.encodeToString(new String(targ).getBytes("UTF-8"), Base64.NO_WRAP));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else if (Sel == 3) {
                        //uneval
                        if (check.isChecked())
                            if (check2.isChecked()) T2.setText(StrUn(targ));
                            else T2.setText(StrUn(targ).replace("\\x", "\\u00"));
                        else T2.setText(
                                targ.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
                                        .replace("\'", "\\\'"));
                    } else if (Sel == 4) {
                        if (check.isChecked())
                            T2.setText(Radix2(targ, Integer.parseInt(rE.getText().toString()), Integer.parseInt(rE2.getText().toString())));
                        else
                            T2.setText(Radix(targ, spinC.getSelectedItemPosition(), spinC2.getSelectedItemPosition()));
                    } else if (Sel == 5) {
                        //Md5
                        String MD5 = "";
                        try {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(targ.getBytes());
                            byte byteData[] = md.digest();
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < byteData.length; i++) {
                                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                            }
                            MD5 = sb.toString();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            MD5 = "";
                        }
                        T2.setText(MD5);
                    } else if (Sel == 6) {
                        //Sha-256
                        String SHA = "";
                        try {
                            MessageDigest sh = MessageDigest.getInstance("SHA-256");
                            sh.update(targ.getBytes());
                            byte byteData[] = sh.digest();
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < byteData.length; i++) {
                                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                            }
                            SHA = sb.toString();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            SHA = "";
                        }
                        T2.setText(SHA);
                    } else if (Sel == 7) {
                        try {
                            T2.setText(Encryption(targ, edit.getText().toString(), Integer.parseInt(edit2.getText().toString())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose an image"), 1);
                    }
                    RT = E1.getText().toString();
                }
            });

            Button B3 = new Button(MainActivity.this);
            B3.setText("Copy Result");
            B3.setTransformationMethod(null);
            B3.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View view) {
                    if (T2.getText().toString().length() != 0) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Encode", T2.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MainActivity.this, "Copyed Encode", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Can't Copy", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button B5 = new Button(MainActivity.this);
            B5.setText("Clear Result");
            B5.setTransformationMethod(null);
            B5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    T2.setText("");
                    ivMain.setImageBitmap(null);
                }
            });
            Button B4 = new Button(MainActivity.this);
            B4.setText("Result to Target");
            B4.setTransformationMethod(null);
            B4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    E1.setText(T2.getText().toString());
                }
            });
            ivMain = new ImageView(MainActivity.this);


            L1.addView(L2);
            L1.addView(B2);
            L1.addView(B3);
            L1.addView(B5);
            L1.addView(B4);
            L1.addView(T2);
            L1.addView(ivMain);

            scroll.addView(L1);
            LinearLayout L3 = new LinearLayout(MainActivity.this);
            L3.setOrientation(1);
            L3.addView(scroll);
            lists.add(L3);

        }

        public String StrUn(String str) {
            char[] strs = str.toCharArray();
            StringBuilder result = new StringBuilder();
            for (int chr : strs) {
                if (chr != '\"' && chr != '\'') {
                    if (chr > 0x999)
                        result.append("\\u").append(String.valueOf(Integer.toString((int) chr, 16)));
                    else if (chr < 0x100)
                        result.append("\\x").append(String.valueOf(Integer.toString((int) chr, 16)));
                    else {
                        result.append("\\u0").append(String.valueOf(Integer.toString((int) chr, 16)));
                    }
                } else result.append(Character.toString((char) chr));
            }
            return result.toString();
        }

        public String Radix(String str, int inp, int out) {
            int[] rad = {0, 0, 16, 10, 8, 2};
            StringBuilder result = new StringBuilder();
            try {
                switch (inp) {
                    case 0:
                        return "";
                    case 1://asc pass
                        switch (out) {
                            case 0:
                                return "";
                            case 1:
                                return str;
                            default:
                                char[] strs = str.toCharArray();
                                for (int chr : strs)
                                    result.append(String.valueOf(Integer.toString((int) chr, rad[out]))).append(" ");
                        }
                        break;
                    default:
                        switch (out) {
                            case 0:
                                return "";
                            case 1:
                                String[] strs = str.split(" ");
                                for (String str2 : strs)
                                    result.append((char) Integer.parseInt(str2, rad[inp]));
                                break;
                            default:
                                String[] strs2 = str.split(" ");
                                for (String str2 : strs2)
                                    result.append(new BigInteger(str2, rad[inp]).toString(rad[out])).append(" ");
                                break;
                        }
                        break;
                }
            } catch (Exception e) {
                return "오류 발생 Error: " + e;
            }
            return String.valueOf(result);
        }

        public String Radix2(String str, int inp, int out) {
            StringBuilder result = new StringBuilder();
            try {
                switch (inp) {
                    case 0:
                        switch (out) {
                            case 0:
                                return str;
                            default:
                                char[] strs = str.toCharArray();
                                for (int chr : strs)
                                    result.append(String.valueOf(Integer.toString((int) chr, out))).append(" ");
                        }
                        break;
                    default:
                        switch (out) {
                            case 0:
                                String[] strs = str.split(" ");
                                for (String str2 : strs)
                                    result.append(String.valueOf((char) Integer.parseInt(str2, inp)));
                                break;
                            default:
                                String[] strs2 = str.split(" ");
                                for (String str2 : strs2)
                                    result.append(new BigInteger(str2, inp).toString(out)).append(" ");
                                break;
                        }
                        break;
                }
            } catch (Exception e) {
                return "오류 발생 Error: " + e;
            }
            return String.valueOf(result);
        }

        public String Encryption(String str, String pw, int keys) throws Exception {
//            byte[] bytes = pw.getBytes("utf-8");
//
//            SecureRandom sr = new SecureRandom();
//            sr.setSeed(bytes);
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(keys, sr);
//
//            SecretKey skey = kgen.generateKey();
//            SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
//            Cipher c = Cipher.getInstance("AES");
//            c.init(Cipher.ENCRYPT_MODE, skeySpec);
//            byte[] encrypted = c.doFinal(str.getBytes("utf-8"));
//            return Base64.encodeToString(encrypted, Base64.DEFAULT);
            SecretKeyFactory secKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(pw.toCharArray(), pw.getBytes(), 50, keys);
            SecretKey pbeSecretKey = secKeyFactory.generateSecret(spec);
            SecretKey skeySpec = new SecretKeySpec(pbeSecretKey.getEncoded(), "AES");
            //SecretKeySpec skeySpec = new SecretKeySpec(pw.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes());
            return bytesToHex(encrypted);
        }

        private String bytesToHex(byte[] hashInBytes) {

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        }

    }

}


