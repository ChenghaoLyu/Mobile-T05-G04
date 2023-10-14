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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;
import java.util.List;

public class Create_newgame_page extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mymap;
    static float zoomLevel = 14.8f;
    private RadioGroup radioGroup, privacyGroup;
    private EditText numericPasswordEditText;
    private NumberPicker numberPicker;
    private Button goto_activity_main;
    private TextView tvPlayerCount, tvCatCount, tvMouseCount, tvDuration;
    private LinearLayout layout_cat_mouse, layoutMap, layoutPrivatePassword;
    private Spinner location_name;
    public String final_location = "none";
    public TimePicker timePicker;
    public double[] final_coor = new double[]{-37.79917143220294, 144.96114830091392};
    public String final_mode, final_privacy, finalNumericPassword;
    public Integer final_player, final_duration, final_cat, final_mouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newgame_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        goto_activity_main = findViewById(R.id.back);

        goto_activity_main.setOnClickListener(view ->{
            open_activity_main(view);
        });
        numericPasswordEditText = findViewById(R.id.numericPasswordEditText);
        finalNumericPassword = numericPasswordEditText.getText().toString();

        layout_cat_mouse = findViewById(R.id.choose_cat_mouse_number);
        layoutMap = findViewById(R.id.layout_map);
        radioGroup = findViewById(R.id.modelGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.classic) {
                    final_mode = "classic";
                    Toast.makeText(Create_newgame_page.this, "You selected Classic", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.zombie) {
                    Toast.makeText(Create_newgame_page.this, "You selected Zombie", Toast.LENGTH_SHORT).show();
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
                    final_privacy = "public";
                    layoutPrivatePassword.setVisibility(View.GONE);
                } else if (checkedId == R.id.private_privacy) {
                    final_privacy = "private";
                    layoutPrivatePassword.setVisibility(View.VISIBLE);
                }
                Toast.makeText(Create_newgame_page.this, "You selected " + final_privacy, Toast.LENGTH_SHORT).show();
            }
        });

        tvPlayerCount = findViewById(R.id.tv_player_count);
        tvCatCount = findViewById(R.id.tv_cat_count);
        tvMouseCount = findViewById(R.id.tv_mouse_count);
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
                if (final_location.equals("unimelb")){
                    final_coor = new double[]{-37.79917143220294, 144.96114830091392};
                } else if (final_location.equals("Monash")) {
                    final_coor = new double[]{-37.9105, 145.1362};
                }
                // 在这里，您可以根据选择的值执行任何操作。例如，显示一个Toast消息：
                Toast.makeText(parent.getContext(), "Selected: " + final_coor[0], Toast.LENGTH_SHORT).show();
                if (mymap != null) {  // 确保地图已经初始化
                    mymap.clear();
                    if(!final_location.equals("Choose Place")) {
                        List<LatLng> melbUniCorners = Arrays.asList(
                                new LatLng(-37.793523320527385, 144.95857232520177),
                                new LatLng(-37.801602929501136, 144.95750868453575),
                                new LatLng(-37.806267618431896, 144.9597094759837),
                                new LatLng(-37.80663432876381, 144.9628192124287),
                                new LatLng(-37.79445857240261, 144.96497746234934),
                                new LatLng(-37.793780045017094, 144.96483822041955),
                                new LatLng(-37.79330323826143, 144.96460615053638),
                                new LatLng(-37.792991478333924, 144.9643276666747),
                                new LatLng(-37.792698056024555, 144.96390994088318),
                                new LatLng(-37.79238629354342, 144.9634690081034),
                                new LatLng(-37.79209288389333, 144.96284241056208),
                                new LatLng(-37.79194617109964, 144.9621694078973),
                                new LatLng(-37.7919094928559,144.961542819211),
                                new LatLng(-37.79194617109964, 144.96098585148962),
                                new LatLng(-37.79205620572242, 144.96045209075658),
                                new LatLng(-37.79223959639573, 144.95998795098842),
                                new LatLng(-37.792441325610504, 144.95954701820864),
                                new LatLng(-37.792716410015515, 144.9591989133839),
                                new LatLng(-37.793523320527385, 144.95857232520177)
                        );
                        // 在地图上绘制多边形
                        LatLng chosen_location = new LatLng(final_coor[0], final_coor[1]);
                        mymap.addPolygon(new PolygonOptions()
                                .addAll(melbUniCorners)
                                .strokeColor(Color.RED)  // 边框颜色
                                .fillColor(Color.argb(50, 255, 0, 0)));  // 填充颜色（半透明红色）


//                        mymap.addMarker(new MarkerOptions().position(chosen_location).title("unimelb"));
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
                        Toast.makeText(Create_newgame_page.this, "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(Create_newgame_page.this, "You selected " + final_player + " players", Toast.LENGTH_SHORT).show();
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

        Toast.makeText(Create_newgame_page.this, "You selected " + final_cat + " cats", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(Create_newgame_page.this, "You selected " + final_mouse + " Mouses", Toast.LENGTH_SHORT).show();
    }
    private void updateDuration(TextView textView, boolean increase) {
        int count = Integer.parseInt(textView.getText().toString());
        if (increase) {
            count = Math.min(120, count + 10);
        } else {
            count = Math.max(30, count - 10); // 确保数量不小于1
        }
        textView.setText(String.valueOf(count));
        final_duration = count;
        Toast.makeText(Create_newgame_page.this, "You selected " + final_duration + " mins", Toast.LENGTH_SHORT).show();
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
}
