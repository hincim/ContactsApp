package com.example.contactsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.CardDesignHolder> {

    private Context mContext;
    private List<Persons> personsList;
    private Database db;

    public PersonsAdapter(Context mContext, List<Persons> personsList, Database db) {
        this.mContext = mContext;
        this.personsList = personsList;
        this.db = db;
    }

    @NonNull
    @Override
    public CardDesignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View design = LayoutInflater.from(mContext).inflate(R.layout.person_card_design,parent,false);
        return new CardDesignHolder(design);
    }

    @Override
    public void onBindViewHolder(@NonNull CardDesignHolder holder, int position) {

        Persons person = personsList.get(position);
        holder.textViewPersonInformation.setText(person.getPerson_name()+" - "+person.getPerson_phone());
        holder.imageViewPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewPoint);

                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.action_delete:
                                    Snackbar.make(holder.imageViewPoint,"Delete the person?",Snackbar.LENGTH_SHORT)
                                            .setAction("Yes", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    new Personsdao().personDelete(db,person.getPerson_id());

                                                    personsList = new Personsdao().allPersons(db);
                                                    notifyDataSetChanged();
                                                }
                                            }).show();
                                    return true;
                                case R.id.action_update:
                                    alertShow(person);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

    public class CardDesignHolder extends RecyclerView.ViewHolder{

        private TextView textViewPersonInformation;
        private ImageView imageViewPoint;

        public CardDesignHolder(@NonNull View itemView) {
            super(itemView);

            textViewPersonInformation = itemView.findViewById(R.id.textViewPersonInformation);
            imageViewPoint = itemView.findViewById(R.id.imageViewPoint);
        }
    }

    public void alertShow(Persons person){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.alert_design,null);
        EditText editTextPersonName = view.findViewById(R.id.editTextPersonName);
        EditText editTextPersonPhone = view.findViewById(R.id.editTextPersonPhone);

        editTextPersonName.setText(person.getPerson_name());
        editTextPersonPhone.setText(person.getPerson_phone());

        AlertDialog.Builder name = new AlertDialog.Builder(mContext);

        name.setTitle("Update contact");
        name.setView(view);
        name.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String person_name = editTextPersonName.getText().toString().trim();
                String person_phone = editTextPersonPhone.getText().toString().trim();

                new Personsdao().personUpdate(db,person.getPerson_id(),person_name,person_phone);
                personsList = new Personsdao().allPersons(db);
                notifyDataSetChanged();
            }
        });
        name.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        name.create().show();
    }
}
