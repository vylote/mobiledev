package com.vlt.lab2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactRowView extends LinearLayout {

    public CheckBox getCb() {
        return cb;
    }

    CheckBox cb;
    ImageView img;
    TextView name, phone;

    public ContactRowView(Context context) {
        super(context);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        int rowH = dp(96);
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                rowH
        ));

        setBackground(makeBorder(Color.BLACK));

        // ===== CHECKBOX =====
        LinearLayout cbBox = new LinearLayout(context);
        cbBox.setGravity(Gravity.CENTER);
        cbBox.setBackground(makeBorder(Color.RED));

        LayoutParams cbParams =
                new LayoutParams(dp(48), LayoutParams.MATCH_PARENT);

        cb = new CheckBox(context);
        cbBox.addView(cb);
        addView(cbBox, cbParams);

        // ===== IMAGE (VUÔNG) =====
        LinearLayout imgBox = new LinearLayout(context);
        imgBox.setPadding(4,4,4,4);
        imgBox.setBackground(makeBorder(Color.BLUE));

        LayoutParams imgParams =
                new LayoutParams(rowH, rowH); // 👈 VUÔNG

        img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setBackgroundColor(Color.LTGRAY);

        imgBox.addView(img, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));

        addView(imgBox, imgParams);

        // ===== TEXT (FLEX) =====
        LinearLayout txtBox = new LinearLayout(context);
        txtBox.setOrientation(VERTICAL);
        txtBox.setGravity(Gravity.CENTER_VERTICAL);
        txtBox.setPadding(16,0,0,0);
        txtBox.setBackground(makeBorder(Color.GREEN));

        LayoutParams txtParams =
                new LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        1   // 👈 CHỈ TEXT DÙNG WEIGHT
                );

        name = new TextView(context);
        name.setTextSize(16);
        phone = new TextView(context);
        phone.setTextSize(14);

        txtBox.addView(name);
        txtBox.addView(phone);
        addView(txtBox, txtParams);
    }

    public void bind(ContactItem item) {
        name.setText(item.name);
        phone.setText(item.phone);
        cb.setChecked(item.status);

        if (item.imagePath != null) {
            img.setImageURI(Uri.parse(item.imagePath));
        } else {
            img.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private GradientDrawable makeBorder(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(Color.TRANSPARENT);
        d.setStroke(2, color);
        return d;
    }

    /*Lấy mật độ điểm ảnh của màn hình hiện tại (Ví dụ: màn hình 2x, 3x, 4x...).
    Công thức: Pixel = dp * mật độ.
    Hàm này giúp bạn tư duy theo đơn vị dp (giống XML) nhưng trả về giá trị px để máy hiểu.*/
    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

}
