package com.example.g04_project;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Create_newgame_page extends AppCompatActivity implements OnMapReadyCallback{
    private WebSocketClient client;
    private GoogleMap mymap;
    static float zoomLevel = 14.8f;
    private RadioGroup radioGroup, privacyGroup;
    private EditText numericPasswordEditText;
    private NumberPicker numberPicker;
    private Button goto_activity_main;
    private TextView tvPlayerCount, tvCatCount, tvMouseCount, tvDuration;
    private LinearLayout layout_cat_mouse, layoutMap, layoutPrivatePassword;
    private Spinner location_name;
    public String final_location = "null";
    public TimePicker timePicker;
    public double[] final_coor = new double[]{-37.79917143220294, 144.96114830091392};
    public String final_mode = "null", finalNumericPassword, final_duration, final_startTime;
    public boolean isPrivate;
    public boolean isOngoing = false;
    public Integer final_player, final_cat, final_mouse;
    public String user_id, userName, room_id = "000001", final_privacy = "null";
    public ConcurrentHashMap<String, Player> cat_list, rat_list, ready_list;
    private Player host;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newgame_page);
        MyApp app = (MyApp) getApplication();
        client = app.getWebSocketClient();
        user_id = client.getAccount().getUserID();
        userName = client.getAccount().getUserName();

        System.out.println(user_id);
        System.out.println(userName);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        goto_activity_main = findViewById(R.id.back);

        goto_activity_main.setOnClickListener(view ->{
            open_activity_main(view);
        });

        numericPasswordEditText = findViewById(R.id.numericPasswordEditText);

        layout_cat_mouse = findViewById(R.id.choose_cat_mouse_number);
        layoutMap = findViewById(R.id.layout_map);
        radioGroup = findViewById(R.id.modelGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.classic) {
                    final_mode = "classic";
//                    Toast.makeText(Create_newgame_page.this, "You selected Classic", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.zombie) {
//                    Toast.makeText(Create_newgame_page.this, "You selected Zombie", Toast.LENGTH_SHORT).show();
                    final_mode = "zombie";
                }
            }
        });

        layoutPrivatePassword = findViewById(R.id.private_password);
        privacyGroup = findViewById(R.id.privacyGroup);
        privacyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.public_privacy) {
                    isPrivate = false;
                    final_privacy = "Public";
                    layoutPrivatePassword.setVisibility(View.GONE);
                } else if (checkedId == R.id.private_privacy) {
                    isPrivate = true;
                    final_privacy = "Private";
                    layoutPrivatePassword.setVisibility(View.VISIBLE);
                }
//                Toast.makeText(Create_newgame_page.this, "You selected " + final_privacy, Toast.LENGTH_SHORT).show();
            }
        });

        tvPlayerCount = findViewById(R.id.tv_player_count);
        tvCatCount = findViewById(R.id.tv_cat_count);
        final_cat = Integer.parseInt(tvCatCount.getText().toString());
        tvMouseCount = findViewById(R.id.tv_mouse_count);
        final_mouse = Integer.parseInt(tvMouseCount.getText().toString());
        tvDuration = findViewById(R.id.tv_duration);

        findViewById(R.id.btn_player_increase).setOnClickListener(v -> {
            updatePlayerCount(tvPlayerCount, true);
//            onRoomCountChanged();
        });
        findViewById(R.id.btn_player_decrease).setOnClickListener(v -> {
            updatePlayerCount(tvPlayerCount, false);
//            onRoomCountChanged();
        });
//
        findViewById(R.id.btn_cat_increase).setOnClickListener(v -> updateCatCount(tvCatCount, true));
        findViewById(R.id.btn_cat_decrease).setOnClickListener(v -> updateCatCount(tvCatCount, false));

        findViewById(R.id.btn_mouse_increase).setOnClickListener(v -> updateMouseCount(tvMouseCount, true));
        findViewById(R.id.btn_mouse_decrease).setOnClickListener(v -> updateMouseCount(tvMouseCount, false));

        findViewById(R.id.btn_duration_increase).setOnClickListener(v -> updateDuration(tvDuration, true));
        findViewById(R.id.btn_duration_decrease).setOnClickListener(v -> updateDuration(tvDuration, false));

        location_name = findViewById(R.id.spinner_location_name);


// 设置选择监听器
        location_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 获取所选项的值
                String selectedValue = parent.getItemAtPosition(position).toString();
                final_location = selectedValue;
                if (!final_location.equals("Choose Place")){
                    layoutMap.setVisibility(View.VISIBLE);
                }
                if (final_location.equals("Unimelb")){
                    final_coor = new double[]{-37.79917143220294, 144.96114830091392};
                } else if (final_location.equals("Monash")) {
                    final_coor = new double[]{-37.9105, 145.1362};
                }else if (final_location.equals("RMIT")) {
                    final_coor = new double[]{-37.8082139,144.9637764};
                }
                // 在这里，您可以根据选择的值执行任何操作。例如，显示一个Toast消息：
//                Toast.makeText(parent.getContext(), "Selected: " + final_coor[0], Toast.LENGTH_SHORT).show();
                if (mymap != null) {  // 确保地图已经初始化
                    mymap.clear();
                    if(final_location.equals("Unimelb")) {
                        String[] stringArray = getResources().getStringArray(R.array.melb_uni_corners);
                        List<LatLng> melbUniCorners = new ArrayList<>();
// 解析字符串为LatLng对象
                        for (String coord : stringArray) {
                            String[] latLng = coord.split(",");
                            double latitude = Double.parseDouble(latLng[0]);
                            double longitude = Double.parseDouble(latLng[1]);
                            melbUniCorners.add(new LatLng(latitude, longitude));
                        }
                        // 在地图上绘制多边形
                        LatLng chosen_location = new LatLng(final_coor[0], final_coor[1]);
                        mymap.addPolygon(new PolygonOptions()
                                .addAll(melbUniCorners)
                                .strokeColor(Color.RED)  // 边框颜色
                                .fillColor(Color.argb(50, 255, 0, 0)));  // 填充颜色（半透明红色）


//                        mymap.addMarker(new MarkerOptions().position(chosen_location).title("unimelb"));
                        mymap.addMarker(new MarkerOptions().position(chosen_location).title("Unimelb"));
                        mymap.moveCamera(CameraUpdateFactory.newLatLng(chosen_location));
                        mymap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                    } else if (final_location.equals("Monash")) {
                        LatLng chosen_location = new LatLng(final_coor[0], final_coor[1]);
                        mymap.addMarker(new MarkerOptions().position(chosen_location).title("Monash"));
                        mymap.moveCamera(CameraUpdateFactory.newLatLng(chosen_location));
                        mymap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                    }else if (final_location.equals("RMIT")) {
                        LatLng chosen_location = new LatLng(final_coor[0], final_coor[1]);
                        mymap.addMarker(new MarkerOptions().position(chosen_location).title("RMIT"));
                        mymap.moveCamera(CameraUpdateFactory.newLatLng(chosen_location));
                        mymap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                    }
                }

                timePicker = findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true); // 设置为24小时制
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        // 这里处理用户选择的时间
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        final_startTime = selectedTime;
//                        Toast.makeText(Create_newgame_page.this, "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 这个方法在用户没有选择任何项或选择了当前已选中的项时被调用。
                // 在大多数情况下，您可能不需要在这里做任何事情。
            }
        });
    }

    private void updatePlayerCount(TextView textView, boolean increase) {
        int count = Integer.parseInt(textView.getText().toString());
        if (increase) {
            count++;
        } else {
            count = Math.max(3, count - 1); // 确保数量不小于1
        }
        textView.setText(String.valueOf(count));
        final_player = count;
        double cat_number = (double) count * 2.5 / 10;
        final_cat = (int) Math.round(cat_number);
        final_mouse = final_player - final_cat;
        tvCatCount.setText(String.valueOf(final_cat));
        tvMouseCount.setText(String.valueOf(final_mouse));
        layout_cat_mouse.setVisibility(View.VISIBLE);
//        Toast.makeText(Create_newgame_page.this, "You selected " + final_player + " players", Toast.LENGTH_SHORT).show();
    }

    private void updateCatCount(TextView textView, boolean increase) {
        int count = Integer.parseInt(textView.getText().toString());
        int mouse_count = Integer.parseInt(tvMouseCount.getText().toString());
        if (increase) {
            count++;
            if (!final_mouse.equals(1)) {
                textView.setText(String.valueOf(count));
                final_cat = count;
                tvMouseCount.setText(String.valueOf(mouse_count - 1));
                final_mouse = mouse_count - 1;
            }
        } else {
            if (count != 1) {
                textView.setText(String.valueOf(count - 1));
                final_cat = count - 1;
                tvMouseCount.setText(String.valueOf(mouse_count + 1));
                final_mouse = mouse_count + 1;
            }
        }

//        Toast.makeText(Create_newgame_page.this, "You selected " + final_cat + " cats", Toast.LENGTH_SHORT).show();
    }

    private void updateMouseCount(TextView textView, boolean increase) {
        int count = Integer.parseInt(textView.getText().toString());
        int cat_count = Integer.parseInt(tvCatCount.getText().toString());
        if (increase) {
            count++;
            if (!final_cat.equals(1)) {
                textView.setText(String.valueOf(count));
                final_mouse = count;
                tvCatCount.setText(String.valueOf(cat_count - 1));
                final_cat = cat_count - 1;
            }
        } else {
            if (count != 1) {
                textView.setText(String.valueOf(count - 1));
                final_mouse = count - 1;
                tvCatCount.setText(String.valueOf(cat_count + 1));
                final_cat = cat_count + 1;
            }
        }
//        Toast.makeText(Create_newgame_page.this, "You selected " + final_mouse + " Mouses", Toast.LENGTH_SHORT).show();
    }
    private void updateDuration(TextView textView, boolean increase) {
        int count = Integer.parseInt(textView.getText().toString());
        if (increase) {
            count = Math.min(120, count + 10);
        } else {
            count = Math.max(30, count - 10); // 确保数量不小于1
        }
        textView.setText(String.valueOf(count));
        final_duration = String.valueOf(count);
//        Toast.makeText(Create_newgame_page.this, "You selected " + final_duration + " mins", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mymap = googleMap;
        try {
            // 使用 raw resource JSON 文件
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        LatLng chosen_location = new LatLng(final_coor[0], final_coor[1]);
        mymap.moveCamera(CameraUpdateFactory.newLatLng(chosen_location));
        mymap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }
    public void open_activity_main(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
    public void verifyRoomInfo(View view) {
//        final_location
//        final_mode
//        final_privacy
        final_player = Integer.parseInt(tvPlayerCount.getText().toString());
        final_duration = tvDuration.getText().toString();
        final_cat = Integer.parseInt(tvCatCount.getText().toString());
        final_mouse = Integer.parseInt(tvMouseCount.getText().toString());
        finalNumericPassword = numericPasswordEditText.getText().toString();
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); // 设置为24小时制
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // 这里处理用户选择的时间
                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                final_startTime = selectedTime;
//                        Toast.makeText(Create_newgame_page.this, "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
            }
        });
        // Check empty input
        if (final_privacy.equals("null") || final_mode.equals("null") || final_location.equals("null")) {
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.item_toast, null);

            TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
            customToastTextView.setText("Please enter all required fields!");

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();
            //Toast.makeText(getApplicationContext(), "Please enter all required fields!", Toast.LENGTH_SHORT).show();
            return;

            // Invalid email format, show an error message or take appropriate action.
        } else if (isPrivate && finalNumericPassword.length() != 6) {
            System.out.println("missing password");
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.item_toast, null);

            TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
            customToastTextView.setText("Private Room Password must be at least 6 digits long" + String.valueOf(finalNumericPassword.length()));

            Toast customToast = new Toast(getApplicationContext());
            customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
            customToast.setView(customToastView);

            customToast.show();
            return;
            //Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();

            // Passwords do not match
        }
        else {
            ready_list = new ConcurrentHashMap<>();
            cat_list = new ConcurrentHashMap<>();
            rat_list = new ConcurrentHashMap<>();
            host = new Player(user_id, userName);
            host.setRoomID(room_id);
            host.setHost();
            System.out.println("sending");
            client.sendRoomInformation(room_id, user_id, final_location, final_mode, final_duration, finalNumericPassword,
                    final_cat, 0, final_mouse, 0, final_startTime, isPrivate, isOngoing, cat_list, rat_list, ready_list);
        }
//        Toast.makeText(Create_newgame_page.this, "finish fill the form", Toast.LENGTH_SHORT).show();

        client.setOnMessageReceivedListener(new WebSocketClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(String message) {
                // Successful registration
                if (message.equals("Successfully create room")) {
                    System.out.println(message);
                    RoomInformation roomInformation = new RoomInformation(room_id, user_id, final_location, final_mode, final_duration,
                            finalNumericPassword, final_cat, 0, final_mouse, 0, final_startTime,
                            isPrivate, isOngoing, cat_list, rat_list, ready_list);
                    RoomManager.getInstance().setRoom(roomInformation);
                    PlayerManager.getInstance().setPlayer(host);
//                    Toast.makeText(Create_newgame_page.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), DisplayGameRoomPage.class);
                    startActivity(intent);

                    // Incorrect validation, show an error message or take appropriate action.
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View customToastView = inflater.inflate(R.layout.item_toast, null);

                    TextView customToastTextView = customToastView.findViewById(R.id.customToastText);
                    customToastTextView.setText("Create Room fails. Please try again!");

                    Toast customToast = new Toast(getApplicationContext());
                    customToast.setDuration(Toast.LENGTH_SHORT); // Set the duration as needed
                    customToast.setView(customToastView);

                    customToast.show();
                    //Toast.makeText(getApplicationContext(), "Invalid user name or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
