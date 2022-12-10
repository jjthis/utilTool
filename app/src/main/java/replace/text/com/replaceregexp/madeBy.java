package replace.text.com.replaceregexp;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class madeBy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);//, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setMessage("COPYRIGHT© 2018-2019 ? ALL RIGHTS RESERVED.");
        dialog.setTitle("개발자 정보");
        dialog.setCancelable(false);
        AlertDialog alert = dialog.create();
        alert.show();
    }
}
