package www.wangbin.com.securitycode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import rxjava2.qqj.com.securitycode.R;

public class MainActivity extends AppCompatActivity implements SecurityCodeView.InputCompleteListener{

    private SecurityCodeView mPhoneCodeEdit;
    private TextView mPhoneError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //软键盘自动弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mPhoneCodeEdit = (SecurityCodeView) findViewById(R.id.login_code_edit);
        mPhoneCodeEdit.setInputCompleteListener(this);
        mPhoneError = (TextView) findViewById(R.id.login_phone_error);
    }

    @Override
    public void inputComplete(String code) {
        //验证码输入完成回调处理
        mPhoneCodeEdit.clearEditText();
        mPhoneError.setVisibility(View.VISIBLE);
        mPhoneError.setText("验证码错误，请重新输入！");
    }
}
