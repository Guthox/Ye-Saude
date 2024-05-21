package com.example.yesaude;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewList extends RecyclerView.ViewHolder {

    TextView med, horario;
    public ViewList(@NonNull View itemView) {
        super(itemView);
        med = itemView.findViewById(R.id.med);
        horario = itemView.findViewById(R.id.horario);
    }
}
