package be.kuleuven.gt.dogapp.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.kuleuven.gt.dogapp.R;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{

    private List<Reminder> reminderList;
    public ReminderAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }
    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View reminderView = layoutInflater.inflate(R.layout.reminder_view, parent, false);
        ViewHolder myViewHolder = new ViewHolder(reminderView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {

        Reminder reminder = reminderList.get(position);
        ((TextView) holder.reminder.findViewById(R.id.reminderDetails))
                .setText(reminder.getDetails());
        ((TextView) holder.reminder.findViewById(R.id.reminderTime))
                .setText(reminder.getTime());

    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public View reminder;
        public ViewHolder(View reminderView) {
            super(reminderView);
            reminder = (View) reminderView;
        }
    }

}
