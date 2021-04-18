package com.example.prac3;

import android.widget.ImageView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/*
fragment for the building selector
 */
public class SelectorFragment extends Fragment {

    private StructureData myStrucData = StructureData.get();
    private SelectorAdapter selectorAda;

    MapData myMap = MapData.get();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {

        View view = inflater.inflate(R.layout.frag_selector, ui, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        selectorAda= new SelectorFragment.SelectorAdapter();
        rv.setAdapter(selectorAda);
        return view;
    }
    public void refresh()
    {
        selectorAda.notifyDataSetChanged();
    }

    private class SelectorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView structureImage;
        private TextView structureDescription;
        private Structure myStructure;
        public SelectorViewHolder(LayoutInflater li,ViewGroup parent)
        {
            super(li.inflate(R.layout.list_selection,parent,false));
            structureImage = (ImageView)itemView.findViewById(R.id.selectorImageView);
            structureDescription = (TextView)itemView.findViewById(R.id.selectorText);
            itemView.setOnClickListener(this);
        }
        public void bind(Structure e)
        {
            if(myMap.getCurrStructure()==null) //if no stucuture is selected normal operation
            {
                myStructure = e;
                structureImage.setImageResource(e.getDrawableId());
                structureDescription.setText(e.getLabel());
            }
            else
            {
                if(e.equals(myMap.getCurrStructure())) //if its currently the selected object highlight it
                {
                    myStructure = e;
                    structureImage.setImageResource(e.getDrawableId());
                    structureDescription.setText(e.getLabel());
                    structureImage.setBackgroundColor(0xFF99cc00);
                }
                else //not currently selected so no highlight
                {
                    myStructure = e;
                    structureImage.setImageResource(e.getDrawableId());
                    structureDescription.setText(e.getLabel());
                    structureImage.setBackgroundColor(0xFFFFFFFF);
                }
            }
        }
        @Override
        public void onClick(View view){
            myMap.setCurrStructure(myStructure);
            refresh();
        }
    }

    private class SelectorAdapter extends RecyclerView.Adapter<SelectorFragment.SelectorViewHolder>
    {

        @Override
        public SelectorFragment.SelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new SelectorFragment.SelectorViewHolder(li,viewGroup);
        }

        public int getItemCount()
        {
            return myStrucData.size();
        }

        @Override
        public void onBindViewHolder(SelectorFragment.SelectorViewHolder mapViewHolder, int i) {
            mapViewHolder.bind(myStrucData.get(i));
        }

    }

}
