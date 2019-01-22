package com.cdkj.token.trade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityOrderDetailsBinding;
import com.cdkj.token.model.OrderListDetailsModel;
import com.cdkj.token.model.submitOrdeMakeMoneyModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import retrofit2.Call;

public class OrderDetailsActivity extends AbsLoadActivity {
    private ActivityOrderDetailsBinding mBinding;
    private String currentCode;
    private OrderListDetailsModel orderListDetailsModel;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String code) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN2, code);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_order_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setShowTitle(false);
        init();
        initData();
        initOnClickListener();
    }

    private void init() {
        if (getIntent() != null) {
            currentCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);
        }
    }

    private void initData() {
        if (TextUtils.isEmpty(currentCode))
            return;
        HashMap<String, String> map = new HashMap<>();
        map.put("code", currentCode);
        Call<BaseResponseModel<OrderListDetailsModel>> ordetListDetails = RetrofitUtils.createApi(MyApi.class).getOrdetListDetails("625286", StringUtils.getRequestJsonString(map));
        addCall(ordetListDetails);
        showLoadingDialog();
        ordetListDetails.enqueue(new BaseResponseModelCallBack<OrderListDetailsModel>(this) {
            @Override
            protected void onSuccess(OrderListDetailsModel data, String SucMessage) {
                if (data == null) {
                    return;
                }
                orderListDetailsModel = data;
                setViewData();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private void initOnClickListener() {
        mBinding.ivBack.setOnClickListener(v -> finish());
        mBinding.tvFuyanCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvFuyan.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvOnekeyCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, "一键复制");
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvPayeeCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvPayee.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvBankNumberCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvBankNumber.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvBankNameCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvBankName.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvBankBranchCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvBankBranch.getText().toString());
            ToastUtil.show(this, "复制成功");
        });

        mBinding.tvTitleRight.setOnClickListener(v -> {
            submitOrder(false);
        });

        mBinding.btnConfirm.setOnClickListener(v -> {
            submitOrder(true);
        });


    }

    private void setViewData() {
        if (TextUtils.equals("0", orderListDetailsModel.getStatus())) {
            mBinding.llType0.setVisibility(View.VISIBLE);
            mBinding.llType3.setVisibility(View.GONE);
            mBinding.tvTitleRight.setVisibility(View.VISIBLE);
        } else {
            mBinding.llType3.setVisibility(View.VISIBLE);
            mBinding.llType0.setVisibility(View.GONE);
            mBinding.tvTitleRight.setVisibility(View.GONE);
        }
        switch (orderListDetailsModel.getStatus()) {
            case "0":
                //待支付是支付宝  还是银行卡
                mBinding.tvFuyan.setText(orderListDetailsModel.getPostscript());
                mBinding.tvUnpaidAmount.setText("¥" + orderListDetailsModel.getTradeAmount().toString());
                //1  是支付宝  0是银行卡
                if (TextUtils.equals("1", orderListDetailsModel.getReceiveType())) {
                    mBinding.ivQr.setVisibility(View.VISIBLE);
                    mBinding.llBack.setVisibility(View.GONE);
                    mBinding.ivPayLogo.setImageResource(R.mipmap.icon_ali_logo);
                    mBinding.tvPayName.setText("支付宝");
                } else {
                    mBinding.ivQr.setVisibility(View.GONE);
                    mBinding.llBack.setVisibility(View.VISIBLE);
                    mBinding.ivPayLogo.setImageResource(R.mipmap.icon_pay_bank_logo);
                    mBinding.tvPayName.setText("银行卡转账");
                    mBinding.tvPayee.setText(orderListDetailsModel.getReceiveName());
                    mBinding.tvBankNumber.setText(orderListDetailsModel.getReceiveCardNo());
                    mBinding.tvBankName.setText(orderListDetailsModel.getReceiveBank());
                    mBinding.tvBankBranch.setText(orderListDetailsModel.getReceiveSubbranch());
                }
                break;
            case "1":
            case "2":
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_success);
                mBinding.tvType.setText("已完成");
                mBinding.tvTypeMassage.setText("币已转入您的钱包账户，交易完成");
                setOrderDetalisView();
                break;
            case "3":
            case "4":
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_cancel);
                mBinding.tvType.setText("已取消");
                mBinding.tvTypeMassage.setText("用户已取消");
                setOrderDetalisView();
                break;
            case "5":
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_timeout);
                mBinding.tvType.setText("已超时");
                mBinding.tvTypeMassage.setText("未在规定时间内完成付款,订单被关闭");
                setOrderDetalisView();
                break;
            default:
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_cancel);
                mBinding.tvType.setText("");
        }
    }

    /**
     * 设置公共部分的view
     */
    private void setOrderDetalisView() {
        mBinding.tvTypeMsg.setText(TextUtils.equals("0", orderListDetailsModel.getType()) ? "买入" : "卖出" + orderListDetailsModel.getCount() + orderListDetailsModel.getTradeCoin());
        mBinding.tvTotalPrice.setText("总价" + orderListDetailsModel.getTradeAmount().toString());
        mBinding.tvOrderNumber.setText(currentCode);
        mBinding.tvBuyOrPay.setText(TextUtils.equals("0", orderListDetailsModel.getType()) ? "买入" : "卖出");
        mBinding.tvMoney.setText("¥" + orderListDetailsModel.getTradeAmount().toString());
        mBinding.tvTime.setText(DateUtil.format(orderListDetailsModel.getCreateDatetime(), DateUtil.DEFAULT_DATE_FMT));
    }

    /**
     * 提交打款标记 或者  取消订单
     */
    private void submitOrder(boolean isSubmit) {
        if (TextUtils.isEmpty(currentCode))
            return;
        HashMap<String, String> map = new HashMap<>();
        map.put("code", currentCode);
        map.put("userId", SPUtilHelper.getUserId());
        String msg;
        String code;
        if (isSubmit) {
            msg = "提交成功";
            code = "625273";
        } else {
            msg = "取消成功";
            code = "625272";
        }
        Call<BaseResponseModel<submitOrdeMakeMoneyModel>> ordetList = RetrofitUtils.createApi(MyApi.class).submitOrCancelOrdeMakeMoney(code, StringUtils.getRequestJsonString(map));
        addCall(ordetList);
        showLoadingDialog();
        ordetList.enqueue(new BaseResponseModelCallBack<submitOrdeMakeMoneyModel>(this) {
            @Override
            protected void onSuccess(submitOrdeMakeMoneyModel data, String SucMessage) {

                UITipDialog.showSuccess(OrderDetailsActivity.this, msg, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                        //刷新上个界面的数据  订单列表数据
                        EventBus.getDefault().post("刷新订单");
                    }
                });
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }
}
