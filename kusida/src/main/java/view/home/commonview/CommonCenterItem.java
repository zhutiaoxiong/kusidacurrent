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

public class CommonCenterItem extends ConstraintLayout {
    public TextView all_device;
    public TextView online_device;
    public TextView offline_device;
    public CommonCenterItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_center_item, this, true);
        all_device =  findViewById(R.id.all_device);
        online_device =  findViewById(R.id.online_device);
        offline_device = findViewById(R.id.offline_device);
    }
}
