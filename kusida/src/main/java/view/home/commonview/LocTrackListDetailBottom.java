package view.home.commonview;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class LocTrackListDetailBottom extends ConstraintLayout {
    public TextView txt_top_left;
    public TextView txt_start;
    public TextView txt_end;
    public TextView txt_the_speed;
    public TextView txt_mileage;
    public TextView txt_loc_mode;
    public TextView txt_direction;
    public TextView time_start;
    public TextView time_end;
    public ImageView play_img;
    public SeekBar seek_bar;

    public LocTrackListDetailBottom(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.loc_track_list_detail_bottom, this, true);
        txt_top_left=findViewById(R.id.txt_top_left);
        txt_start=findViewById(R.id.txt_start);
        txt_end=findViewById(R.id.txt_end);
        txt_the_speed=findViewById(R.id.txt_the_speed);
        txt_mileage=findViewById(R.id.txt_mileage);
        txt_loc_mode=findViewById(R.id.txt_loc_mode);
        txt_direction=findViewById(R.id.txt_direction);
        time_start=findViewById(R.id.time_start);
        time_end=findViewById(R.id.time_end);
        play_img=findViewById(R.id.play_img);
        seek_bar=findViewById(R.id.seek_bar);
    }
}
