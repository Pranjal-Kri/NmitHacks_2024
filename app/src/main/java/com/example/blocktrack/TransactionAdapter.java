package com.example.blocktrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<EtherscanResponse.Transaction> transactions;
    public String address;
    public TransactionAdapter(List<EtherscanResponse.Transaction> transactions, String address) {
        this.transactions = transactions;
        this.address = address;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        EtherscanResponse.Transaction transaction = transactions.get(position);
        holder.bind(transaction, address);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView dateView;
        private TextView amountView;
        private TextView fromView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.date_view);
            amountView = itemView.findViewById(R.id.amount_view);
            fromView = itemView.findViewById(R.id.from_view);
        }

        public void bind(EtherscanResponse.Transaction transaction, String address) {
            dateView.setText(transaction.getFormattedDate()); // Convert timestamp to date
            if(transaction.to.equalsIgnoreCase(address)) {
                amountView.setText("ETH " + transaction.getFormattedValueInEther() + " cr.");
                amountView.setTextColor(itemView.getContext().getResources().getColor(R.color.credit_color));
            }// Convert value to Ether
            else {
                amountView.setText("ETH "+transaction.getFormattedValueInEther()+" deb.");// Convert value to Ether
                amountView.setTextColor(itemView.getContext().getResources().getColor(R.color.debit_color));
            }
            fromView.setText("From: "+transaction.from);
        }

    }
}
