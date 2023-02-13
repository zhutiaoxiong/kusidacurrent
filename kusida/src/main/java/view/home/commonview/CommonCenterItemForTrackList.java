package view.home.commonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class CommonCenterItemForTrackList extends ConstraintLayout {
    public TextView today;
    public TextView lastday;
    public TextView select_time;
    public CommonCenterItemForTrackList(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_for_track_list, this, true);
        today =  findViewById(R.id.today);
        lastday =  findViewById(R.id.lastday);
        select_time = findViewById(R.id.select_time);
    }
}
