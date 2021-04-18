package com.example.prac3;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
Fragment for grid map
 */
public class MapFragment extends Fragment {
    private MapData actualMap = MapData.get();


    private MapAdapter adapter;
    private Callbacks mCallBacks;
    public interface Callbacks //callback to allow actvity to update displays, callback is for when something on screen is clicked
    {
        void onMapSelected();
    }
    @Override //callback method
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mCallBacks=(Callbacks)context;
    }
    @Override //callback method
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.frag_map, ui, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), actualMap.getHEIGHT(), GridLayoutManager.HORIZONTAL, false));

        MapAdapter mapAda= new MapAdapter();
        adapter = mapAda;
        rv.setAdapter(mapAda);
        return view;
    }

    public void refresh()
    {
        adapter.notifyDataSetChanged();
    } //for refreshing the recycler view


    private class MapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView topRight,topLeft,botRight,botLeft,coverAll;
        private MapElement myMapElement;
        public MapViewHolder(LayoutInflater li,ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cell,parent,false));
            topRight = itemView.findViewById(R.id.topRight);
            topLeft = itemView.findViewById(R.id.topLeft);
            botLeft = itemView.findViewById(R.id.botLeft);
            botRight = itemView.findViewById(R.id.botRight);
            coverAll = itemView.findViewById(R.id.coverAll);
            int size = parent.getMeasuredHeight() / actualMap.getHEIGHT() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;
            itemView.setOnClickListener(this);
        }
        public void bind(MapElement e,int col,int row)
        {
            topLeft.setImageResource(e.getNorthWest());
            topRight.setImageResource(e.getNorthEast());
            botLeft.setImageResource(e.getSouthWest());
            botRight.setImageResource(e.getSouthEast());
            e.setCol(col);
            e.setRow(row);
            if(e.getStructure()!=null) //if strucutre is null it should have no image
            {
                if(e.getImage()!=null) //checking for photo to display instead of normal image
                {
                    coverAll.setImageBitmap(e.getImage());
                }
                else
                {
                    coverAll.setImageResource(e.getStructure().getDrawableId());
                }
            }
            myMapElement = e;
        }

        @Override
        public void onClick(View view)//onclick for clicking on the screen
        {
            if(actualMap.getCurFunction()==null) //if no button funciton like construct demolish etc is selected
            {
                Toast.makeText(getActivity(),"ERROR: Must select option",Toast.LENGTH_SHORT).show();
            }
            else if(actualMap.getCurFunction().equals("construct")) //construct option is selected
            {
                if(myMapElement.getStructure()==null) //checking for the element being null, cant place object on already constucuted object
                {
                    if(myMapElement.isBuildable()) //not in ocean or coast
                    {
                        if(actualMap.getCurrStructure()!=null) //if their is a type of building currently selected
                        {
                            if(actualMap.getCurrStructure().getType().equals("Residential")) //if its a residential type
                            {
                                if(actualMap.getMoney()-actualMap.getSettings().getHouseBuildingCost()>=0) //have enough money
                                {
                                    if(actualMap.roadCheck(myMapElement)) //perform road checking
                                    {
                                        coverAll.setImageResource(actualMap.getCurrStructure().getDrawableId());
                                        myMapElement.setStructure(actualMap.getCurrStructure());
                                        actualMap.setMoney(actualMap.getMoney()-actualMap.getSettings().getHouseBuildingCost()); //taking away the build cost from current money
                                        actualMap.setnResidential(actualMap.getnResidential()+1); //incrementing the number of residential buildings
                                        actualMap.updateDBMap();
                                        actualMap.updateDBGrid();
                                        mCallBacks.onMapSelected(); //leting activity know something has been clicked
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),"ERROR: Building must be next to road",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"ERROR: Cant afford building",Toast.LENGTH_SHORT).show();
                                }

                            }
                            else if(actualMap.getCurrStructure().getType().equals("Commercial"))
                            {
                                if(actualMap.getMoney()-actualMap.getSettings().getCommBuildingCost()>=0) //have enough money
                                {
                                    if(actualMap.roadCheck(myMapElement))
                                    {
                                        coverAll.setImageResource(actualMap.getCurrStructure().getDrawableId());
                                        myMapElement.setStructure(actualMap.getCurrStructure());
                                        actualMap.setMoney(actualMap.getMoney()-actualMap.getSettings().getCommBuildingCost());
                                        actualMap.setnCommercial(actualMap.getnCommercial()+1);
                                        actualMap.updateDBMap();
                                        actualMap.updateDBGrid();
                                        mCallBacks.onMapSelected();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(),"ERROR: Building must be next to road",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"ERROR: Cant afford building",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(actualMap.getCurrStructure().getType().equals("Road"))
                            {
                                if(actualMap.getMoney()-actualMap.getSettings().getRoadBuildingCost()>=0) //have enough money
                                {
                                    coverAll.setImageResource(actualMap.getCurrStructure().getDrawableId());
                                    myMapElement.setStructure(actualMap.getCurrStructure());
                                    actualMap.setMoney(actualMap.getMoney()-actualMap.getSettings().getRoadBuildingCost());
                                    actualMap.updateDBMap();
                                    actualMap.updateDBGrid();
                                    mCallBacks.onMapSelected();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"ERROR: Cant afford building",Toast.LENGTH_SHORT).show();
                                }
                            }
                            refresh(); //refresh the layout
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"ERROR: No structure selected",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"ERROR: Invalid Build Location",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"ERROR: Building already in location",Toast.LENGTH_SHORT).show();
                }
            }
            else if(actualMap.getCurFunction().equals("demolish"))
            {
                if(myMapElement.getStructure()!=null) //is there a structure to demolish
                {
                    if(myMapElement.getStructure().getType().equals("Residential"))
                    {
                        actualMap.setnResidential(actualMap.getnResidential()-1);
                    }
                    else if(myMapElement.getStructure().getType().equals("Commercial"))
                    {
                        actualMap.setnCommercial(actualMap.getnCommercial()-1);
                    }
                    myMapElement.setStructure(null);
                    myMapElement.setImage(null);
                    refresh(); //calling refresh after demolish
                    actualMap.updateDBMap(); //updating map after building demolished
                    mCallBacks.onMapSelected();
                }
                else{
                    Toast.makeText(getActivity(),"ERROR: No Building in location",Toast.LENGTH_SHORT).show();
                }
            }
            else if(actualMap.getCurFunction().equals("detail")) //detail is selected
            {
                if(myMapElement.getStructure()!=null)
                {
                    actualMap.setCurrMapElement(myMapElement);
                    mCallBacks.onMapSelected();
                    Intent intent =new Intent(view.getContext(),DetailActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(),"ERROR: No Building in location",Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(getActivity(),"ERROR: Must select option",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MapAdapter extends RecyclerView.Adapter<MapViewHolder>
    {

        @Override
        public MapViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            LayoutInflater li = LayoutInflater.from(getActivity());

            return new MapViewHolder(li,viewGroup);
        }

        public int getItemCount()
        {
            return actualMap.getHEIGHT()*actualMap.getWIDTH();
        }

        @Override
        public void onBindViewHolder(MapViewHolder mapViewHolder, int i) {
            int row = i % actualMap.getHEIGHT();
            int col = i / actualMap.getHEIGHT();
            mapViewHolder.bind(actualMap.get(row,col),col,row);
        }


    }

}
