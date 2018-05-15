package www.wangbin.com.securitycode;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * 有软键盘时让其点击手机返回键失效
 * Created by wangbin on 2018/5/13.
 */

public class BackEditText extends AppCompatEditText {

    public BackEditText(Context context) {
        super(context);
    }

    public BackEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //不让其返回键隐藏软键盘
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
