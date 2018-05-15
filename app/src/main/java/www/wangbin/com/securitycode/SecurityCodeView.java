package www.wangbin.com.securitycode;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import rxjava2.qqj.com.securitycode.R;

/**
 * Created by wangbin on 18/5/13.
 */

public class SecurityCodeView extends RelativeLayout {
    private EditText[] editViews;
    private int count = 6;
    private String mContent;
    private boolean mIsClear;

    public SecurityCodeView(Context context) {
        this(context, null);
    }

    public SecurityCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecurityCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        editViews = new EditText[count];
        View.inflate(context, R.layout.view_security_code, this);

        editViews[0] = (EditText) findViewById(R.id.item_code_iv1);
        editViews[1] = (EditText) findViewById(R.id.item_code_iv2);
        editViews[2] = (EditText) findViewById(R.id.item_code_iv3);
        editViews[3] = (EditText) findViewById(R.id.item_code_iv4);
        editViews[4] = (EditText) findViewById(R.id.item_code_iv5);
        editViews[5] = (EditText) findViewById(R.id.item_code_iv6);

        for (int i = 0; i < count; i++) {
            editViews[i].setEnabled(i == 0 ? true : false);
            editViews[i].addTextChangedListener(textWatch(i));
            editViews[i].setOnKeyListener(keyListener(i));
            editViews[i].setOnFocusChangeListener(focusListener(i));
        }
    }

    /**
     * 焦点获取事件
     *
     * @param index
     * @return
     */
    private OnFocusChangeListener focusListener(final int index) {
        OnFocusChangeListener focusListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (index == 5) {
                    if (hasFocus) {
                        editViews[index].setSelected(TextUtils.isEmpty(editViews[index].getText().toString()));
                    } else {
                        editViews[index].setSelected(false);
                    }
                } else {
                    editViews[index].setSelected(hasFocus);
                }
            }
        };
        return focusListener;
    }

    /**
     * 软键盘删除事件监听
     *
     * @param index
     * @return
     */
    private OnKeyListener keyListener(final int index) {
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (index == 5) {
                        if (editViews[5].getText().toString().length() == 0) {
                            editViews[index - 1].setText("");
                            editViews[index - 1].setFocusableInTouchMode(true);
                            editViews[index - 1].requestFocus();
                            editViews[index].setEnabled(false);
                        } else {
                            editViews[index].setCursorVisible(true);
                            editViews[index].setText("");
                            editViews[index].setFocusableInTouchMode(true);
                            editViews[index].requestFocus();
                            editViews[index].setSelected(true);
                        }
                    } else if (index > 0) {
                        editViews[index - 1].setText("");
                        editViews[index - 1].setFocusableInTouchMode(true);
                        editViews[index - 1].requestFocus();
                        editViews[index].setEnabled(false);
                    }
                    return true;
                }
                return false;
            }
        };
        return keyListener;
    }

    /**
     * EditText编辑事件
     *
     * @param index
     * @return
     */
    private TextWatcher textWatch(final int index) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mIsClear || (index == 0 && mIsClear)) {
                    if (s.toString().length() == 1) {
                        mIsClear = false;
                        if (index < 5) {
                            editViews[index].setFocusableInTouchMode(false);
                            editViews[index].setFocusable(false);
                            editViews[index + 1].setEnabled(true);
                            editViews[index + 1].setFocusableInTouchMode(true);
                            editViews[index + 1].requestFocus();
                            if (index == 4) editViews[index + 1].setCursorVisible(true);//显示光标
                        } else if (index == 5) {
                            editViews[index].setCursorVisible(false);//将光标隐藏
                            editViews[index].setSelected(false);
                            editViews[index].setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mIsClear) {
                    StringBuffer buffer = new StringBuffer();
                    if (TextUtils.isEmpty(s.toString())) {
                        if (index == 0) {
                            mContent = s.toString();
                        } else {
                            if (!TextUtils.isEmpty(mContent) && mContent.length() > 0) {
                                mContent = mContent.substring(0, index);
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(mContent)) {
                            mContent = buffer.append(s).toString();
                        } else {
                            mContent = buffer.append(mContent).append(s).toString();

                        }
                    }
                    if (mContent.length() == count && inputCompleteListener != null) {
                        inputCompleteListener.inputComplete(mContent);
                    }
                }
            }
        };
        return watcher;
    }

    /**
     * 清空输入内容
     */
    public void clearEditText() {
        mContent = "";
        mIsClear = true;
        for (int i = count - 1; i >= 0; i--) {
            editViews[i].setText("");
            if (i == 0) {
                editViews[i].setFocusableInTouchMode(true);
                editViews[i].setFocusable(true);
                editViews[i].setEnabled(true);
                editViews[i].requestFocus();
                editViews[i].setSelected(true);
            } else {
                editViews[i].setEnabled(false);
                editViews[i].setFocusableInTouchMode(false);
                editViews[i].setFocusable(false);
            }
        }
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {
        void inputComplete(String code);
    }

    /**
     * 获取输入文本
     *
     * @return
     */
    public String getEditContent() {
        return mContent;
    }


    /**
     * 该activity自动显示软键盘
     *
     * @param context
     */
    public void showInput(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    /**
     * 拦截软键盘上的完成键
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}