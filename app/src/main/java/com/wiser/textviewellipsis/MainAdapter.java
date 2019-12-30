package com.wiser.textviewellipsis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wiser.textellipsis.TextEllipsisLayout;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

	private Context context;

	private List<String> list;

	public MainAdapter(Context context,List<String> list) {
		this.context = context;
		this.list = list;
	}

	@NonNull @Override public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new MainHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
	}

	@Override public void onBindViewHolder(@NonNull MainHolder holder, int position) {
		holder.textView.setText(list.get(position));
	}

	@Override public int getItemCount() {
		return list == null ? 0 : list.size();
	}

	class MainHolder extends RecyclerView.ViewHolder {

		TextEllipsisLayout	ellipsisLayout;

		TextView			textView;

		Button				btn;

		MainHolder(@NonNull View itemView) {
			super(itemView);
			ellipsisLayout = itemView.findViewById(R.id.tel);
			textView = itemView.findViewById(R.id.tv);
			btn = itemView.findViewById(R.id.btn);
		}
	}

}
