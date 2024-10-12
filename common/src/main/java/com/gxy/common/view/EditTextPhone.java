package com.gxy.common.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SHANG on 2019/4/25.
 */

public class EditTextPhone extends AppCompatEditText implements TextWatcher {
	// 特殊下标位置
	private static final int PHONE_INDEX_3 = 3;
	private static final int PHONE_INDEX_4 = 4;
	private static final int PHONE_INDEX_8 = 8;
	private static final int PHONE_INDEX_9 = 9;

	public EditTextPhone(Context context) {
		super(context);
		initView();
	}

	public EditTextPhone(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public EditTextPhone(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		setInputType(InputType.TYPE_CLASS_NUMBER);
		setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});//设置限制长度，多了输入不了
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		super.onTextChanged(s, start, before, count);
		if (s == null || s.length() == 0) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == ' ') {
				continue;
			} else {
				sb.append(s.charAt(i));
				if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != ' ') {
					sb.insert(sb.length() - 1, ' ');
				}
			}
		}
		if (!sb.toString().equals(s.toString())) {
			int index = start + 1;
			if (sb.charAt(start) == ' ') {
				if (before == 0) {
					index++;
				} else {
					index--;
				}
			} else {
				if (before == 1) {
					index--;
				}
			}

			setText(sb.toString());
			setSelection(index);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}


	// 获得不包含空格的手机号
	public String getPhoneText() {
		String str = getText().toString();
		return replaceBlank(str);
	}

	private String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			if (m.find()) {
				dest = m.replaceAll("");
			}
		}
		return dest;
	}
}