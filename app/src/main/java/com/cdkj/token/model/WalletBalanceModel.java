package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

/**
 * Created by cdkj on 2018/7/1.
 */

public class WalletBalanceModel implements Parcelable {

    private String coinImgUrl;
    private String coinName;
    private String amount;
    private String marketPriceCNY;
    private String marketPriceUSD;
    private String amountCny;
    private String amountUSD;

    private String accountNumber;

    private String address;

    private String coinBalance="0";
    private String frozenAmountString="0";
    private String amountString="0";

    public String getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(String coinBalance) {
        this.coinBalance = coinBalance;
    }

    public String getFrozenAmountString() {
        return frozenAmountString;
    }

    public void setFrozenAmountString(String frozenAmountString) {
        this.frozenAmountString = frozenAmountString;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmountUSD() {
        return amountUSD;
    }

    public void setAmountUSD(String amountUSD) {
        this.amountUSD = amountUSD;
    }

    public String getCoinImgUrl() {
        return coinImgUrl;
    }

    public void setCoinImgUrl(String coinImgUrl) {
        this.coinImgUrl = coinImgUrl;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarketPriceCNY() {
        return marketPriceCNY;
    }

    public void setMarketPriceCNY(String marketPriceCNY) {
        this.marketPriceCNY = marketPriceCNY;
    }

    public String getMarketPriceUSD() {
        return marketPriceUSD;
    }

    public void setMarketPriceUSD(String marketPriceUSD) {
        this.marketPriceUSD = marketPriceUSD;
    }

    public String getAmountCny() {
        return amountCny;
    }

    public void setAmountCny(String amountCny) {
        this.amountCny = amountCny;
    }

    public WalletBalanceModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coinImgUrl);
        dest.writeString(this.coinName);
        dest.writeString(this.amount);
        dest.writeString(this.marketPriceCNY);
        dest.writeString(this.marketPriceUSD);
        dest.writeString(this.amountCny);
        dest.writeString(this.amountUSD);
        dest.writeString(this.accountNumber);
        dest.writeString(this.address);
        dest.writeString(this.coinBalance);
        dest.writeString(this.frozenAmountString);
        dest.writeString(this.amountString);
    }

    protected WalletBalanceModel(Parcel in) {
        this.coinImgUrl = in.readString();
        this.coinName = in.readString();
        this.amount = in.readString();
        this.marketPriceCNY = in.readString();
        this.marketPriceUSD = in.readString();
        this.amountCny = in.readString();
        this.amountUSD = in.readString();
        this.accountNumber = in.readString();
        this.address = in.readString();
        this.coinBalance = in.readString();
        this.frozenAmountString = in.readString();
        this.amountString = in.readString();
    }

    public static final Creator<WalletBalanceModel> CREATOR = new Creator<WalletBalanceModel>() {
        @Override
        public WalletBalanceModel createFromParcel(Parcel source) {
            return new WalletBalanceModel(source);
        }

        @Override
        public WalletBalanceModel[] newArray(int size) {
            return new WalletBalanceModel[size];
        }
    };
}
