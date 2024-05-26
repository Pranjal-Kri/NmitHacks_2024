package com.example.blocktrack;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EtherscanResponse {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public List<Transaction> result;

    public static class Transaction {
        @SerializedName("blockNumber")
        public String blockNumber;
        @SerializedName("timeStamp")
        public String timeStamp;
        @SerializedName("hash")
        public String hash;
        @SerializedName("from")
        public String from;
        @SerializedName("to")
        public String to;
        @SerializedName("value")
        public String value;
        @SerializedName("contractAddress")
        public String contractAddress;
        @SerializedName("gas")
        public String gas;
        @SerializedName("gasUsed")
        public String gasUsed;
        @SerializedName("isError")
        public String isError;
        @SerializedName("errCode")
        public String errCode;
        public String getFormattedDate() {
            long timeStampLong = Long.parseLong(timeStamp) * 1000; // Convert seconds to milliseconds
            Date date = new Date(timeStampLong);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        }

        public String getFormattedValueInEther() {
            // Convert value from String to BigInteger to perform arithmetic
            BigInteger weiValue = new BigInteger(value);
            // Define the conversion factor (Wei to Ether)
            BigInteger conversionFactor = new BigInteger("1000000000000000000"); // 10^18
            // Perform division to convert Wei to Ether
            BigDecimal etherValue = new BigDecimal(weiValue).divide(new BigDecimal(conversionFactor), 18, RoundingMode.DOWN);
            // Return the result as a String
            return etherValue.toPlainString();
        }

    }

}
