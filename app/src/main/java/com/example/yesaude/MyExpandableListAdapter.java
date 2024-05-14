package com.example.yesaude;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, List<String>> childColecao;
    private List<String> groupList;

    public MyExpandableListAdapter(Context context, List<String> groupList,
                                   Map<String, List<String>> childColecao){
        this.context = context;
        this.childColecao = childColecao;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return childColecao.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childColecao.get(groupList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childColecao.get(groupList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String mobileName = getGroup(i).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.groupitem_imc, null);
        }
        TextView item = view.findViewById(R.id.mobile);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(mobileName);
        return view;
    }
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        String model = getChild(i, i1).toString();
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.childitem_imc, null);
        }
        TextView item = view.findViewById(R.id.model);
        ImageView delete = view.findViewById(R.id.delete);
        item.setText(model);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Quer deletar esse item?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        List<String> child = childColecao.get(groupList.get(i));
                        child.remove(i1);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}