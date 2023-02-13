package view.home.commonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.client.proj.kusida.R;

import org.jetbrains.annotations.NotNull;

public class MessageTopItem extends ConstraintLayout {
    public EditText  device_input;
    public ImageView iv;
    public ImageView  delete;
    public ImageView  select;
    public MessageTopItem(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.message_top_item, this, true);
        device_input =  findViewById(R.id.device_input);
        iv =  findViewById(R.id.iv);
        delete = findViewById(R.id.delete);
        select =  findViewById(R.id.select);
    }
}
