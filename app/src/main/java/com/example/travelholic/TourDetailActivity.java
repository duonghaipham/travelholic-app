package com.example.travelholic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.travelholic.helper.Comment;
import com.example.travelholic.helper.CommentAdapter;
import com.example.travelholic.helper.Session;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TourDetailActivity extends AppCompatActivity {

    private Session session;
    private String Id;

    private EditText txtTourName;
    private Spinner spnTourType;
    private Spinner spnTourStatus;
    private EditText txtTourDeparture;
    private EditText txtTourDestination;
    private EditText txtTourDuring;
    private EditText txtTourMembers;
    private EditText txtTourNote;
    private Button btnTourImage;
    private ImageView ivTourImage;
    private Button btnSubmit;
    private ListView lvComment;
    private EditText txtComment;
    private Button btnComment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        map();

        session = new Session(TourDetailActivity.this);
        int position = getIntent().getIntExtra("position", -1);
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2/travelholic-app/server/index.php?controller=Tour&action=load_by_position&position="
                + String.valueOf(position);
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                TourDetailActivity.this.runOnUiThread(() -> {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Id = jsonObject.getString("id");
                        txtTourName.setText(jsonObject.getString("tour_name"));
                        txtTourDeparture.setText(jsonObject.getString("departure"));
                        txtTourDestination.setText(jsonObject.getString("destination"));
                        txtTourDuring.setText(jsonObject.getString("during"));
                        txtTourNote.setText(jsonObject.getString("note"));
                        String urlImage = "http://10.0.2.2/travelholic-app/server/" + jsonObject.getString("image");
                        Picasso.get().load(urlImage).into(ivTourImage);

                        if (session.getUsername().equals(jsonObject.getString("creator"))) {
                            btnSubmit.setText("Update");
                        }
                        else {
                            btnSubmit.setText("Apply");
                        }
                    } catch (JSONException | IOException jsonException) {
                        jsonException.printStackTrace();
                    }
                });
            }
        });

        btnComment.setOnClickListener(v -> {
            if (!txtComment.getText().toString().isEmpty()) {
                RequestBody body = new FormBody.Builder()
                        .add("tour_id", Id)
                        .add("username", session.getUsername())
                        .add("content", txtComment.getText().toString())
                        .build();
                String commentUrl = "http://10.0.2.2/travelholic-app/server/index.php?controller=Tour&action=comment";
                Request commentRequest = new Request.Builder()
                        .post(body)
                        .url(commentUrl)
                        .build();
                client.newCall(commentRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) { }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        TourDetailActivity.this.runOnUiThread(() -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getBoolean("success")) {
                                    txtComment.clearFocus();
                                    txtComment.setText("");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            }
        });

        refreshComments();
    }
    
    private void map() {
        txtTourName = findViewById(R.id.txt_tour_name);
        spnTourType = findViewById(R.id.spn_tour_type);
        spnTourStatus = findViewById(R.id.spn_tour_status);
        txtTourDeparture = findViewById(R.id.txt_tour_departure);
        txtTourDestination = findViewById(R.id.txt_tour_destination);
        txtTourDuring = findViewById(R.id.txt_tour_during);
        txtTourMembers = findViewById(R.id.txt_tour_members);
        txtTourNote = findViewById(R.id.txt_tour_note);
        btnTourImage = findViewById(R.id.btn_tour_image);
        ivTourImage = findViewById(R.id.iv_tour_image);
        btnSubmit = findViewById(R.id.btn_tour_submit);
        lvComment = findViewById(R.id.lv_comment);
        txtComment = findViewById(R.id.txt_comment);
        btnComment = findViewById(R.id.btn_comment);
    }

    private void refreshComments() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2/travelholic-app/server/index.php?controller=Tour&action=load_comments&id=" + Id;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                TourDetailActivity.this.runOnUiThread(() -> {
                    List<Comment> comments = new Vector<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            comments.add(new Comment(jsonObject.getString("avatar"), jsonObject.getString("fullname"), jsonObject.getString("content")));
                        }
                        CommentAdapter adapter = new CommentAdapter(TourDetailActivity.this, comments);
                        lvComment.setAdapter(adapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}