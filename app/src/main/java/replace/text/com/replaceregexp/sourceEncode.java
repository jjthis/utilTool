package replace.text.com.replaceregexp;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.InputType;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mozilla.javascript.tools.jsc.Main;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Me on 2019-05-09.
 */

class sourceEncode {

    private static WebView web;

    private static boolean isLoaded = false;

    private static TextView textsss;
    private static String RTB = "";

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    static LinearLayout sourceEn(final Context ctx) {
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(1);
        LinearLayout layout1 = new LinearLayout(ctx);
        layout1.setOrientation(1);
        NestedScrollView scroll = new NestedScrollView(ctx);
        TextInputLayout editL = new TextInputLayout(ctx);
        ScrollingView scrollingView = new ScrollingView(ctx);
        scrollingView.setMaxLines(15);
        final ClearEditText edit = new ClearEditText(ctx);
        edit.setHint("Source Target");
        MainActivity.editArr[4] = edit;
        editL.addView(edit);
        scrollingView.addView(editL);
        Button btn = new Button(ctx);
        btn.setTransformationMethod(null);
        btn.setText("Continue Encoding");

        Button btn1 = new Button(ctx);
        btn1.setTransformationMethod(null);
        btn1.setText("Copy Result");
        Button btn2 = new Button(ctx);
        btn2.setTransformationMethod(null);
        btn2.setText("Clear Result");
        Button btn3 = new Button(ctx);
        btn3.setTransformationMethod(null);
        btn3.setText("Result to Target");

        final EditText edits = new EditText(ctx);

        final LinearLayout lay = new LinearLayout(ctx);

        edits.setHint("Key");
        edits.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

        final LinearLayout lay2 = new LinearLayout(ctx);
        final EditText edits2 = new EditText(ctx);
        edits2.setHint("1st");
        edits2.setText("뀨");
        edits2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5));
        final EditText edits3 = new EditText(ctx);
        edits3.setHint("2nd");
        edits3.setText("!");
        edits3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5));
        final EditText edits4 = new EditText(ctx);
        edits4.setHint("3rd");
        edits4.setText("?");
        edits4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5));
        lay2.addView(edits2);
        lay2.addView(edits3);
        lay2.addView(edits4);
        final CheckBox checkBox = new CheckBox(ctx);
        checkBox.setChecked(true);
        final CheckBox checkBox2 = new CheckBox(ctx);
        checkBox2.setChecked(false);
        textsss = new TextView(ctx);
        textsss.setTextIsSelectable(true);
        textsss.setTextSize(18);
        MainActivity.resultArr[4] = textsss;
        String[] str = {"Caeser Cipher", "Hex Encryptor", "Kkyuly Encryptor", "Mangle Properties", "Japanese Encoding", "Dollar Encoding", "JSFuck", "Beautifier", "JSPack", "BarCodelize", "Hex Beautifier(Temp)"};
        final Spinner spin = new Spinner(ctx);
        spin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        final ArrayAdapter adapter = new ArrayAdapter(ctx, android.R.layout.simple_list_item_1, str);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lay.removeAllViewsInLayout();
                if (!isLoaded) {
                    isLoaded = true;
                    web.loadUrl("file:///android_asset/all.html");
                }
                if (position == 0) {
                    edits.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
                    lay.addView(edits);
                    edits.setText("10");
                } else if (position == 2)
                    lay.addView(lay2);
                else if (position == 3) {
                    lay.addView(checkBox);
                    checkBox.setText("Only private");
                } else if (position == 5) {

                    edits.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    lay.addView(edits);
                    edits.setText("$");
                } else if (position == 6) {
                    lay.addView(checkBox);
                    checkBox.setText("Use Eval");
                } else if (position == 8) {
                    lay.addView(checkBox);
                    lay.addView(checkBox2);
                    checkBox.setText("Base62");
                    checkBox.setChecked(false);
                    checkBox2.setText("Shrink");
                }

                lay.addView(spin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targ = edit.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                if (prefs.getBoolean("auto", false) && RTB.equals(targ) && textsss.getText().toString().length() != 0)
                    targ = textsss.getText().toString();
                switch (spin.getSelectedItemPosition()) {
                    case 0:
                        textsss.setText(encrypt(targ, Integer.parseInt(edits.getText().toString())));
                        break;
                    case 1:
                        textsss.setText(encryptH(targ));
                        break;
                    case 2:
                        textsss.setText(encryptK(targ, edits2.getText().toString(), edits3.getText().toString(), edits4.getText().toString()));
                        break;
                    case 3:
                        textsss.setText(mangle(targ, checkBox.isChecked()));
                        break;
                    case 4:
                        textsss.setText(aaencode(targ));
                        break;
                    case 5:
                        textsss.setText(jjencode(edits.getText().toString(), targ));
                        break;
                    case 6:
                        calculate("JSFuck.encode(\"" + targ.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
                                .replace("\'", "\\\'") + "\", " + checkBox.isChecked() + ")");
                        break;
                    case 7://beatifier
                        calculate("js_beautify(\"" + targ.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
                                .replace("\'", "\\\'") + "\")");
                        break;
                    case 8://jspack
                        calculate("packer.pack('" + targ.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
                                .replace("\'", "\\\'") + "', " + checkBox.isChecked() + ", " + checkBox2.isChecked() + ")");
                        break;
                    case 9:
                        textsss.setText(barcode(targ));
                        break;
                    case 10:
                        textsss.setText(endless(targ));
                        break;
                }
                RTB = edit.getText().toString();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textsss.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Source", textsss.getText().toString());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ctx, "Copyed Result", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ctx, "Can't Copy", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textsss.setText("");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText(textsss.getText().toString());
                textsss.setText("");
            }
        });


        web = new WebView(ctx);


        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JSLinker(), "android");


        lay.addView(spin);
        layout.addView(scrollingView);
        layout.addView(lay);
        layout.addView(btn);
        layout.addView(btn1);
        layout.addView(btn2);
        layout.addView(btn3);
        layout.addView(textsss);

        //layout.addView(web);

        scroll.addView(layout);
        layout1.addView(scroll);
        return layout1;
    }

    private static String barcode(String str){
        //"function name(a,b)".match(/function (([^\s\(]+)?\()/);
//"this.name = function(a,b)".match(/(^|\.|\])([^\s\(\.\]]+)\s?\=\s?function/)
//"[\"name\"] = function(a,b)".match(/(\[(\"|\')([^\"\']+)(\"|\')\])\s?\=\s?function/)
        Pattern p = Pattern.compile("function (([^\\s\\(]+)?\\()");
        Matcher m = p.matcher(str);
        while (m.find()) {
            str = str.replace(m.group(1), str2Bin(m.group(2))+"(");
        }
        p = Pattern.compile("(^|\\.|\\])([^\\s\\(\\.\\]]+)(\\s?[=:]\\s?function)");
        m = p.matcher(str);
        while (m.find()) {
            str = str.replace(m.group(2)+m.group(3), str2Bin(m.group(2))+m.group(3));
            str = str.replace(m.group(1)+m.group(2)+"(", m.group(1)+str2Bin(m.group(2))+"(");
        }
        p = Pattern.compile("([\"\'])([^\"\']+)(([\"\'])\\]\\s?[=:]\\s?function)");
        m = p.matcher(str);
        while (m.find()) {
            str = str.replace(m.group(2)+m.group(3), str2Bin(m.group(2))+m.group(3));
            str = str.replace(m.group(2)+m.group(4)+"](", str2Bin(m.group(2))+m.group(4)+"](");
        }
        return str;
    }

    private static String str2Bin(String str){
        StringBuilder result = new StringBuilder();
        char[] strs = str.toCharArray();
        for (int chr : strs)
            result.append(String.valueOf(Integer.toString((int) chr, 2)).replace("0", "l").replace("1", "I"));
        return result.toString();
    }

    private static void calculate(String source) {
        web.loadUrl("javascript:window.android.sendData(" + source + ");");
    }

    private static String encrypt(String textarea, int key) {
        char[] c = textarea.toCharArray();
        StringBuilder d = new StringBuilder();
        int b = 0;
        for (int f = c.length; b < f; b++) {
            int a = c[b] + key;
            if (!"\n".equals(Character.toString(c[b])))
                d.append(34 != a && 39 != a && 92 != a ? Character.toString((char) a) : "\\" + (char) a);
        }
        return "var code=\"" + d.toString() + "\";_code=[];for(let i=code.length;i--;){_code[i]=String.fromCharCode(code.charCodeAt(i)-" + key + ");}eval(_code.join(\"\"));";
    }

    private static String getRandomChar() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("")[(int) Math.floor(52 * Math.random())];
    }

    private static String getRandomStr(Stack<String> usedChar) {
        String a = "";
        for (; -1 < usedChar.indexOf(a) || a.length() == 0; ) {
            for (int b = (int) Math.floor(2 * Math.random()) + 1; 0 != (b--); )
                a += getRandomChar();
        }
        return a;
    }

    private static String mangle(String a, boolean b) {
        Stack<String> usedChar = new Stack<String>();
        Pattern p = Pattern.compile(b ? "([A-z_$][\\w$]*)\\.(_[\\w$]*)" : "([A-z_$][\\w$]*)\\.([A-z_$][\\w$]{2,})");
        Matcher m = p.matcher(a);
        for (; m.find(); ) {
            String res = getRandomStr(usedChar);
            usedChar.push(res);
            a = a.replace(m.group(0), m.group(1) + "." + res);
            //m=p.matcher(a);
        }
        return a;
    }

    private static String endless(String a) {
        Pattern p = Pattern.compile("\\\\([0-7]{1,3})");
        Matcher m = p.matcher(a);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            int b = Integer.parseInt(m.group(1));
            m.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf(b < 378 ? (char) (Integer.parseInt(b + "", 8)) : (char) (Integer.parseInt((b + "").substring(0, 2), 8)))));
        }
        m.appendTail(sb);
        a = sb.toString();
        p = Pattern.compile("\\\\x([a-fA-F\\d]{2})");
        m = p.matcher(a);
        sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf((char) Integer.parseInt(m.group(1), 16))));
        }
        m.appendTail(sb);
        a = sb.toString();
        p = Pattern.compile("\\\\u([a-fA-F\\d]{4})");
        m = p.matcher(a);
        sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf((char) Integer.parseInt(m.group(1), 16))));
        }
        m.appendTail(sb);
        a = sb.toString();
        return a;
    }

    private static String str2Uni(String a) {
        String b = "";
        for (int d = 0; d < a.length(); d++) {
            b += Integer.toString(a.charAt(d), 16).length()==2?"\\x":"\\u";
            b += Integer.toString(a.charAt(d), 16).replace("\\%\\u", "");
        }
        return b;
    }

    private static String encryptH(String a) {
        String b = "";
        int d = 0;
        String e = "_code";
        for (; -1 < a.indexOf(e); )
            e = "_" + e;
        for (; 5 > d; ) {
            Pattern p = Pattern.compile("\"[^\"]*\"");
            Matcher m = p.matcher(a);
            Pattern p1 = Pattern.compile("\\.[a-zA-Z_]+[a-zA-Z0-9_]*");
            Matcher m1 = p.matcher(a);
            String c = "";
            if (m.find()) {
                c = str2Uni(m.group(0).replaceAll("\"", ""));
                c = c.replaceAll("\\x5c\\x6e", "\\x0a").replaceAll("\\x5c\\x74", "\\x09");
                c = '"' + c + '"';
                if (0 > b.indexOf(c)) b += ", " + c;
                a = a.replaceFirst("\"[^\"]*\"", e + "[" + (java.util.Arrays.asList(b.split(", ")).indexOf(c) - 1) + "]");
            } else if (m1.find()) {
                c = "\"" + str2Uni(m1.group(0).split("\\.")[1]) + "\"";
                if (0 > b.indexOf(c)) {
                    b += ", " + c;
                    a = a.replace("\\.[a-zA-Z_]+[a-zA-Z0-9_]*", "[" + e + "[" + (java.util.Arrays.asList(b.split(", ")).indexOf(c) - 1) + "]]");
                }
            } else d++;
        }
        return "var " + e + " = [" + b.replaceFirst(", ", "") + "];\n\n" + a;
    }

    private static String encryptK(String f, String c, String d, String e) {
        if (f.length() == 0) return "";
        int a = 1;
        String b = Integer.toString(f.charAt(0), 2);
        for (int g = f.length(); a < g; a++)
            b += e + Integer.toString(f.charAt(a), 2);
        return "var arr=\"" + b.replace("0", d).replace("1", c) + "\".split(\"" + d + "\").join(\"0\").split(\"" + c + "\").join(\"1\").split(\""
                + e + "\");for(var i=arr.length;i--;){arr[i]=String.fromCharCode(parseInt(arr[i],2).toString(10));}eval(arr.join(\"\"));";
    }

    private static String aaencode(String text) {
        String t;
        String[] b = {
                "(c^_^o)",
                "(ﾟΘﾟ)",
                "((o^_^o) - (ﾟΘﾟ))",
                "(o^_^o)",
                "(ﾟｰﾟ)",
                "((ﾟｰﾟ) + (ﾟΘﾟ))",
                "((o^_^o) +(o^_^o))",
                "((ﾟｰﾟ) + (o^_^o))",
                "((ﾟｰﾟ) + (ﾟｰﾟ))",
                "((ﾟｰﾟ) + (ﾟｰﾟ) + (ﾟΘﾟ))",
                "(ﾟДﾟ) .ﾟωﾟﾉ",
                "(ﾟДﾟ) .ﾟΘﾟﾉ",
                "(ﾟДﾟ) ['c']",
                "(ﾟДﾟ) .ﾟｰﾟﾉ",
                "(ﾟДﾟ) .ﾟДﾟﾉ",
                "(ﾟДﾟ) [ﾟΘﾟ]"
        };
        String r = "ﾟωﾟﾉ= /｀ｍ´）ﾉ ~┻━┻   //*´∇｀*/ ['_']; o=(ﾟｰﾟ)  =_=3; c=(ﾟΘﾟ) =(ﾟｰﾟ)-(ﾟｰﾟ); ";

        if (text.matches("ひだまりスケッチ×(365|３５６)\\s*来週も見てくださいね[!！]")) {
            r += "X=_=3; ";
            r += "\r\n\r\n    X / _ / X < \"来週も見てくださいね!\";\r\n\r\n";
        }
        r += "(ﾟДﾟ) =(ﾟΘﾟ)= (o^_^o)/ (o^_^o);" +
                "(ﾟДﾟ)={ﾟΘﾟ: '_' ,ﾟωﾟﾉ : ((ﾟωﾟﾉ==3) +'_') [ﾟΘﾟ] " +
                ",ﾟｰﾟﾉ :(ﾟωﾟﾉ+ '_')[o^_^o -(ﾟΘﾟ)] " +
                ",ﾟДﾟﾉ:((ﾟｰﾟ==3) +'_')[ﾟｰﾟ] }; (ﾟДﾟ) [ﾟΘﾟ] =((ﾟωﾟﾉ==3) +'_') [c^_^o];" +
                "(ﾟДﾟ) ['c'] = ((ﾟДﾟ)+'_') [ (ﾟｰﾟ)+(ﾟｰﾟ)-(ﾟΘﾟ) ];" +
                "(ﾟДﾟ) ['o'] = ((ﾟДﾟ)+'_') [ﾟΘﾟ];" +
                "(ﾟoﾟ)=(ﾟДﾟ) ['c']+(ﾟДﾟ) ['o']+(ﾟωﾟﾉ +'_')[ﾟΘﾟ]+ ((ﾟωﾟﾉ==3) +'_') [ﾟｰﾟ] + " +
                "((ﾟДﾟ) +'_') [(ﾟｰﾟ)+(ﾟｰﾟ)]+ ((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+" +
                "((ﾟｰﾟ==3) +'_') [(ﾟｰﾟ) - (ﾟΘﾟ)]+(ﾟДﾟ) ['c']+" +
                "((ﾟДﾟ)+'_') [(ﾟｰﾟ)+(ﾟｰﾟ)]+ (ﾟДﾟ) ['o']+" +
                "((ﾟｰﾟ==3) +'_') [ﾟΘﾟ];(ﾟДﾟ) ['_'] =(o^_^o) [ﾟoﾟ] [ﾟoﾟ];" +
                "(ﾟεﾟ)=((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+ (ﾟДﾟ) .ﾟДﾟﾉ+" +
                "((ﾟДﾟ)+'_') [(ﾟｰﾟ) + (ﾟｰﾟ)]+((ﾟｰﾟ==3) +'_') [o^_^o -ﾟΘﾟ]+" +
                "((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+ (ﾟωﾟﾉ +'_') [ﾟΘﾟ]; " +
                "(ﾟｰﾟ)+=(ﾟΘﾟ); (ﾟДﾟ)[ﾟεﾟ]='\\\\'; " +
                "(ﾟДﾟ).ﾟΘﾟﾉ=(ﾟДﾟ+ ﾟｰﾟ)[o^_^o -(ﾟΘﾟ)];" +
                "(oﾟｰﾟo)=(ﾟωﾟﾉ +'_')[c^_^o];" +//TODO
                "(ﾟДﾟ) [ﾟoﾟ]='\\\"';" +
                "(ﾟДﾟ) ['_'] ( (ﾟДﾟ) ['_'] (ﾟεﾟ+";
        r += "(ﾟДﾟ)[ﾟoﾟ]+ ";
        for (int i = 0; i < text.length(); i++) {
            char n = text.charAt(i);
            t = "(ﾟДﾟ)[ﾟεﾟ]+";
            if (n <= 127) {
                String a = Integer.toString(n, 8);
                Pattern p = Pattern.compile("[0-7]");
                Matcher m = p.matcher(a);
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    m.appendReplacement(sb, Matcher.quoteReplacement(b[Integer.parseInt(m.group(0))] + "+ "));
                }
                m.appendTail(sb);
                t += sb.toString();
            } else {
                Pattern p = Pattern.compile("[0-9a-fA-F]{4}$");
                Matcher m = p.matcher("000" + Integer.toString(n, 16));
                StringBuffer sb = new StringBuffer();
                m.find();
                String s = m.group(0);

                p = Pattern.compile("[0-9a-fA-F]");
                m = p.matcher(s);
                sb = new StringBuffer();
                while (m.find()) {
                    m.appendReplacement(sb, Matcher.quoteReplacement(b[Integer.parseInt(m.group(0), 16)] + "+ "));
                }
                m.appendTail(sb);
                t += "(oﾟｰﾟo)+ " + sb.toString();
            }
            r += t;

        }
        r += "(ﾟДﾟ)[ﾟoﾟ]) (ﾟΘﾟ)) ('_');";
        return r;


    }

    private static String jjencode(String gv, String text) {
        StringBuilder r = new StringBuilder();
        char n;
        String t;
        String[] b = {
                "___", "__$", "_$_", "_$$", "$__", "$_$", "$$_", "$$$", "$___", "$__$", "$_$_", "$_$$", "$$__", "$$_$", "$$$_", "$$$$",};
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            n = text.charAt(i);
            if (n == 0x22 || n == 0x5c) {
                s.append("\\\\\\").append(n);
            }else if ((0x20 <= n && n <= 0x2f) /*|| (0x3A <= n && n == 0x40)*/ || (0x5b <= n && n <= 0x60) || (0x7b <= n && n <= 0x7f)) {
                s.append(text.charAt(i));
            } else if ((0x30 <= n && n <= 0x39) || (0x61 <= n && n <= 0x66)) {
                if (s.length() > 0) r.append("\"").append(s).append("\"+");
                r.append(gv).append(".").append(b[n < 0x40 ? n - 0x30 : n - 0x57]).append("+");
                s = new StringBuilder();
            } else if (n == 0x6c) { // 'l'
                if (s.length() > 0) r.append("\"").append(s).append("\"+");
                r.append("(![]+\"\")[").append(gv).append("._$_]+");
                s = new StringBuilder();
            } else if (n == 0x6f) { // 'o'
                if (s.length() > 0) r.append("\"").append(s).append("\"+");
                r.append(gv).append("._$+");
                s = new StringBuilder();
            } else if (n == 0x74) { // 'u'
                if (s.length() > 0) r.append("\"").append(s).append("\"+");
                r.append(gv).append(".__+");
                s = new StringBuilder();
            } else if (n == 0x75) { // 'u'
                if (s.length() > 0) r.append("\"").append(s).append("\"+");
                r.append(gv).append("._+");
                s = new StringBuilder();
            } else if (n < 128) {
                if (s.length() > 0) r.append("\"").append(s);
                else r.append("\"");
                r.append("\\\\\"+");
                Pattern p = Pattern.compile("[0-7]");
                Matcher m = p.matcher(Integer.toString(n, 8));
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    m.appendReplacement(sb, Matcher.quoteReplacement(gv + "." + b[Integer.parseInt(m.group(0))] + "+"));
                }
                m.appendTail(sb);
                //t += sb.toString();
                r.append(sb.toString());
                s = new StringBuilder();
            } else {
                if (s.length() > 0) r.append("\"").append(s);
                else r.append("\"");
                r.append("\\\\\"+").append(gv).append("._+");
                Pattern p = Pattern.compile("[0-9a-fA-F]");
                Matcher m = p.matcher(Integer.toString(n, 16));
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    m.appendReplacement(sb, Matcher.quoteReplacement(gv + "." + b[Integer.parseInt(m.group(0), 16)] + "+"));
                }
                m.appendTail(sb);
                //t += sb.toString();
                r.append(sb.toString());
                s = new StringBuilder();
            }
        }
        if (s.length() > 0) r.append("\"").append(s).append("\"+");
        r = new StringBuilder(gv + "=~[];" +
                gv + "={___:++" + gv + ",$$$$:(![]+\"\")[" + gv + "],__$:++" + gv + ",$_$_:(![]+\"\")[" + gv + "],_$_:++" +
                gv + ",$_$$:({}+\"\")[" + gv + "],$$_$:(" + gv + "[" + gv + "]+\"\")[" + gv + "],_$$:++" + gv + ",$$$_:(!\"\"+\"\")[" +
                gv + "],$__:++" + gv + ",$_$:++" + gv + ",$$__:({}+\"\")[" + gv + "],$$_:++" + gv + ",$$$:++" + gv + ",$___:++" + gv + ",$__$:++" + gv + "};" +
                gv + ".$_=" +
                "(" + gv + ".$_=" + gv + "+\"\")[" + gv + ".$_$]+" +
                "(" + gv + "._$=" + gv + ".$_[" + gv + ".__$])+" +
                "(" + gv + ".$$=(" + gv + ".$+\"\")[" + gv + ".__$])+" +
                "((!" + gv + ")+\"\")[" + gv + "._$$]+" +
                "(" + gv + ".__=" + gv + ".$_[" + gv + ".$$_])+" +
                "(" + gv + ".$=(!\"\"+\"\")[" + gv + ".__$])+" +
                "(" + gv + "._=(!\"\"+\"\")[" + gv + "._$_])+" +
                gv + ".$_[" + gv + ".$_$]+" +
                gv + ".__+" +
                gv + "._$+" +
                gv + ".$;" +
                gv + ".$$=" +
                gv + ".$+" +
                "(!\"\"+\"\")[" + gv + "._$$]+" +
                gv + ".__+" +
                gv + "._+" +
                gv + ".$+" +
                gv + ".$$;" +
                gv + ".$=(" + gv + ".___)[" + gv + ".$_][" + gv + ".$_];" +
                gv + ".$(" + gv + ".$(" + gv + ".$$+\"\\\"\"+" + r.toString() + "\"\\\"\")())();");

        return r.toString();
    }

//    private static String runJS(String code) {
//        org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
//        rhino.setLanguageVersion(org.mozilla.javascript.Context.VERSION_ES6);
//        rhino.setOptimizationLevel(-1);
//
//        try {
//            Scriptable scope = rhino.initStandardObjects();
//            String ev = org.mozilla.javascript.Context.toString(rhino.evaluateString(scope, code, "JavaScript", 1, null));
//            org.mozilla.javascript.Context.exit();
//            return ev;
//        } catch (Exception e) {
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String sStack = sw.toString();
//            org.mozilla.javascript.Context.exit();
//            return "";
//        }
//    }

    private static class JSLinker {

        @JavascriptInterface
        public void sendData(final String data) {
            MainActivity.sHan.post(new Runnable() {
                @Override
                public void run() {
                    textsss.setText(data);
                }
            });
        }
    }

    //뻘짓
    static class Beautifier {
        static String beautify(String a) {
            Pattern p = Pattern.compile("([A-z$_][\\w$]*)\\[\"([A-z$_][\\w$]*)\"\\]");
            Matcher m = p.matcher(a);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, Matcher.quoteReplacement(m.group(1) + "." + m.group(2)));
            }
            m.appendTail(sb);
            return sb.toString();
        }

        static String[] split(String a) {
            String[] ab = a.split("\\s*\\,\\s*");
            String[] c = {};
            for (int b = 0, d = ab.length; b < d; b++) {
                String e = ab[b];
                if ("\"".equals(e.substring(0, 1)) && "\"".equals(e.substring(e.length() - 1)))
                    c[b] = (e.substring(1, e.length() - 1));
            }
            return c;
        }

        static String unescape(String a) {
            Pattern p = Pattern.compile("\\\\([0-7]{1,3})");
            Matcher m = p.matcher(a);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                int b = Integer.parseInt(m.group(1));
                m.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf(b < 378 ? (char) (Integer.parseInt(b + "", 8)) : (char) (Integer.parseInt((b + "").substring(0, 2), 8)))));
            }
            m.appendTail(sb);
            a = sb.toString();
            p = Pattern.compile("\\\\x([a-fA-F\\d]{2})");
            m = p.matcher(a);
            sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf((char) Integer.parseInt(m.group(1), 16))));
            }
            m.appendTail(sb);
            a = sb.toString();
            p = Pattern.compile("\\\\u([a-fA-F\\d]{4})");
            m = p.matcher(a);
            sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, Matcher.quoteReplacement(String.valueOf((char) Integer.parseInt(m.group(1), 16))));
            }
            m.appendTail(sb);
            a = sb.toString();
            return "\"" + a + "\"";
        }

        static String unpack(String a) {
            if (a.matches("^var\\s_0x[a-f\\d]+\\s*\\=\\s*\\[")) {
                Pattern p = Pattern.compile("var\\s(_0x[a-f\\d]+)\\s *\\=\\s *\\[(. * ?)\\][;,]?");
                Matcher m = p.matcher(a);
                if (m.find()) {
                    String b = m.group(1);
                    String[] c = Beautifier.split(m.group(2));
                    a = a.replaceFirst("var(_0x[a-f\\d]+) ?\\= ?\\[(. * ?)\\][;,]?", "");
                    for (int d = 0, e = c.length; d < e; d++)
                        a = a.replaceAll(b + "\\[" + d + "\\]", Beautifier.unescape(c[d]));
                    a = Beautifier.beautify(a);
                }
            }
            return a;
        }
    }
}
