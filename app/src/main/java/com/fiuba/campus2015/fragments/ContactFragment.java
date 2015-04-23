package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.ContactItem;
import com.fiuba.campus2015.adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.LinearLayoutManager;

public class ContactFragment extends Fragment {
    private ListView listViewContacts;
    private List<ContactItem> myContacts;
    private ContactsAdapter contactsAdapter;
    private RecyclerView recyclerView;
    private View myView;

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.contact_fragment, container, false);

        myContacts = new ArrayList<>();

        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactsAdapter = new ContactsAdapter(getActivity());
        //addAllContacts(fillContacts());
        recyclerView.setAdapter(contactsAdapter);

        myContacts.addAll(fillContacts());
        contactsAdapter.setContacts(myContacts);

        // hardcoded...

        return myView;
    }

    public void addAllContacts(List<ContactItem> contactItems) {
        myContacts.addAll(contactItems);
        contactsAdapter.sortMark();
        contactsAdapter.notifyDataSetChanged();
    }

    public void addContact(ContactItem contact) {
        myContacts.add(contact);
        contactsAdapter.sortMark();
        contactsAdapter.notifyDataSetChanged();
    }

    public List<ContactItem> getContacts() {
        return myContacts;
    }

    private List<ContactItem> fillContacts() {
        List<ContactItem> contacts = new ArrayList<>();
        contacts.add(new ContactItem("Alberto Rodriguez","1"));
        contacts.add(new ContactItem("Luis Fernandez","1"));
        contacts.add(new ContactItem("Laura Gonzales","1"));
        contacts.add(new ContactItem("Amelia Lopez","1"));
        contacts.add(new ContactItem("Armando Garcia", "1"));
        contacts.add(new ContactItem("Julia Donohue", "1"));
        contacts.add(new ContactItem("Lionel Messi", "1"));
        contacts.add(new ContactItem("Beto Sanzhez", "1"));
        contacts.add(new ContactItem("Barbara Macri", "1"));
        contacts.add(new ContactItem("Jose Gonzales", "1"));
        contacts.add(new ContactItem("Renzo Perez", "1"));
        contacts.add(new ContactItem("Raul Fernandez", "1"));
        contacts.add(new ContactItem("Romina Arias", "1"));
        contacts.add(new ContactItem("Ignacio Zapata", "1"));
        contacts.add(new ContactItem("Sebastian Durand", "1"));
        return contacts;
    }
}
