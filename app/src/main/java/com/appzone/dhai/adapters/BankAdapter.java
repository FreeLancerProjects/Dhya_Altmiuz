package com.appzone.dhai.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.models.BankAccountDataModel;

import java.util.List;
import java.util.Locale;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyHolder> {

    private List<BankAccountDataModel.BankModel> bankModelList;
    private Context context;

    public BankAdapter(List<BankAccountDataModel.BankModel> bankModelList, Context context) {
        this.bankModelList = bankModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bank_row,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        BankAccountDataModel.BankModel bankModel = bankModelList.get(position);
        holder.BindData(bankModel);

    }

    @Override
    public int getItemCount() {
        return bankModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_number;

        public MyHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_number = itemView.findViewById(R.id.tv_number);

        }

        public void BindData(BankAccountDataModel.BankModel bankModel)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_name.setText(bankModel.getTitle_ar());
            }else
                {
                    tv_name.setText(bankModel.getTitle_en());

                }

            tv_number.setText(bankModel.getAccount());



        }
    }




}
