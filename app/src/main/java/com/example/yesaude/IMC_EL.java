package com.example.yesaude;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMC_EL extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> childColecao;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_el);
        createGroupList();
        createCollection();
        expandableListView = findViewById(R.id.listaDatas);
        expandableListAdapter = new MyExpandableListAdapter(this, groupList, childColecao);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i,i1).toString();
                Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void createCollection() {
        String[] childExemplo1 = {"Item 1", "Item 2",
                "Item 3"};
        String[] childExemplo2 = {"Item 4", "Item 5",
                "Item 6"};
        String[] childExemplo3 = {"Item 7", "Item 8",
                "Item 9"};
        String[] childExemplo4 = {"Item 10", "Item 11",
                "Item 12"};
        String[] childExemplo5 = {"Item 13", "Item 14",
                "Item 15"};
        String[] childExemplo6 = {"Item 16", "Item 17",
                "Item 18"};
        childColecao = new HashMap<String, List<String>>();
        for(String group : groupList){
            if (group.equals("childExemplo1")){
                loadChild(childExemplo1);
            } else if (group.equals("childExemplo2"))
                loadChild(childExemplo2);
            else if (group.equals("childExemplo3"))
                loadChild(childExemplo3);
            else if (group.equals("childExemplo4"))
                loadChild(childExemplo4);
            else if (group.equals("childExemplo5"))
                loadChild(childExemplo5);
            else
                loadChild(childExemplo6);
            childColecao.put(group, childList);
        }
    }

    private void loadChild(String[] mobileModels) {
        childList = new ArrayList<>();
        for(String model : mobileModels){
            childList.add(model);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("childExemplo1");
        groupList.add("childExemplo2");
        groupList.add("childExemplo3");
        groupList.add("childExemplo4");
        groupList.add("childExemplo5");
        groupList.add("childExemplo6");
    }
}