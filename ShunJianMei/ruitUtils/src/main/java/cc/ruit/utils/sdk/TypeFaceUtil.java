package cc.ruit.utils.sdk;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TypeFaceUtil {
	/**
	 * 字体样式修改
	 * @param context
	 * @param view 支持多textview edittext button 三种view的修改，可以一次传多参
	 */
	public static void setTypeFace(Context context,View... view) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(),"DINCondensedBold.ttf");
		for (int i = 0;view!=null&& i < view.length; i++) {
			View v = view[i];
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			}
		}
//         else if (v instanceof ViewGroup) {  
//            changeFonts((ViewGroup) v, act);  
//         }  
	}
}
