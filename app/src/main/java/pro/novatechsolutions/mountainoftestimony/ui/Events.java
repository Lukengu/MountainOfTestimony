package pro.novatechsolutions.mountainoftestimony.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.novatechsolutions.mountainoftestimony.R;

public class Events  extends Fragment {

    RecyclerView events_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.events, container, false);
        events_list = view.findViewById(R.id.event_list);
        return view;
    }
}
