package com.vlt.multiactivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter implements Filterable {
    //khai báo đối tượng Activity để xác định Activity chứa Listview
    private Activity activity;
    //khai báo đối tượng ArrayList<User> là nguồn dữ liệu cho Adapter
    private ArrayList<User> data;
    //khai báo đối tương LayoutInflater để phân tích giao diện cho một phần tử
    private LayoutInflater inflater;
    private ArrayList<User> databackup;
    public MyAdapter(Activity activity, ArrayList<User> data) {
        this.activity = activity;
        this.data = data;
//        databackup = data;
        inflater = (LayoutInflater)activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
        {
            v = inflater.inflate(R.layout.contact_layout, null);
            //tham chiếu tới imageview trên contact_layout
            ImageView image = v.findViewById(R.id.ivAva);
            //sau đó có thể thay đổi ảnh cho phần tử bằng đường dẫn ở đây
            //tham chiếu tới textview để hiển thị tên
            TextView name = v.findViewById(R.id.tvName);
            //thiết lập thuộc tính text của name là tên của phần tử thứ position
            name.setText(data.get(position).getName());
            //tham chiếu tới textview để hiển thị số điện thoại
            TextView phone = v.findViewById(R.id.tvPhoneNum);
            phone.setText(data.get(position).getPhoneNum());

            CheckBox cbCheck = v.findViewById(R.id.cbCheck);

            // Lấy User hiện tại
            User u = data.get(position);

            // --- XỬ LÝ HIỂN THỊ ẢNH ---
            if (u.getImgUri() != null && !u.getImgUri().isEmpty()) {
                // Nếu có đường dẫn ảnh thì load ảnh
                try {
                    image.setImageURI(android.net.Uri.parse(u.getImgUri()));
                } catch (Exception e) {
                    // Nếu lỗi (ảnh bị xóa...) thì để mặc định
                    image.setImageResource(R.mipmap.ic_launcher);
                }
            } else {
                // Nếu không có ảnh thì để mặc định
                image.setImageResource(R.mipmap.ic_launcher);
            }

            // --- XỬ LÝ CHECKBOX ---
            // 1. Gỡ lắng nghe sự kiện cũ để tránh lỗi vòng lặp khi setChecked
            cbCheck.setOnCheckedChangeListener(null);

            // 2. Set trạng thái hiển thị dựa trên dữ liệu User
            cbCheck.setChecked(u.isChecked());

            // 3. Gắn lắng nghe sự kiện mới để cập nhật ngược lại vào User
            cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    u.setChecked(isChecked);
                }
            });

        }
        return v;
    }
    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults fr = new FilterResults();
                //Backup dữ liệu: lưu tạm data vào databackup
                if(databackup==null)
                    databackup = new ArrayList<>(data);
                //Nếu chuỗi để filter là rỗng thì khôi phục dữ liệu
                if(charSequence==null || charSequence.length()==0)
                {
                    fr.count = databackup.size();
                    fr.values = databackup;
                }
                //Còn nếu không rỗng thì thực hiện filter
                else{
                    ArrayList<User> newdata = new ArrayList<>();
                    for(User u:databackup)
                        if(u.getName().toLowerCase().contains(
                                charSequence.toString().toLowerCase()))
                            newdata.add(u);
                    fr.count=newdata.size();
                    fr.values=newdata;
                }
                return fr;
            }
            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                data = new ArrayList<User>();
                ArrayList<User> tmp =(ArrayList<User>)filterResults.values;
                for(User u: tmp)
                    data.add(u);
                notifyDataSetChanged();
            }
        };
        return f;
    }
}
