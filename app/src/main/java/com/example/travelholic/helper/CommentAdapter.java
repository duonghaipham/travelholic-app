package com.example.travelholic.helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelholic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Activity activity;
    private List<Comment> comments;

    public CommentAdapter(Activity activity, List<Comment> comments) {
        this.activity = activity;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.comment_block, null);

        ImageView ivAvatar = convertView.findViewById(R.id.iv_comment_avatar);
        TextView tvFullname = convertView.findViewById(R.id.tv_comment_fullname);
        TextView tvContent = convertView.findViewById(R.id.tv_comment_content);

        String url = "http://10.0.2.2/travelholic-app/server/" + comments.get(position).getAvatar();
        Picasso.get().load(url).into(ivAvatar);
        tvFullname.setText(comments.get(position).getFullname());
        tvContent.setText(comments.get(position).getContent());

        return convertView;
    }
}
