package me.zhixingye.salty.module.login.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salty.protos.ObtainTelephoneSMSCodeResp;
import com.salty.protos.SMSOperationType;
import com.salty.protos.VerifyTelephoneSMSCodeResp;

import me.zhixingye.base.component.mvvm.BasicViewModel;
import me.zhixingye.im.sdk.IMClient;
import me.zhixingye.salty.widget.listener.MVVMRequestCallback;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年02月04日.
 */
public class TelephoneSMSVerifyViewModel extends BasicViewModel {

    private final MutableLiveData<Integer> mSMSLengthData = new MutableLiveData<>();
    private final MutableLiveData<ErrorData> mObtainSMSErrorData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mVerifySMSSuccessData = new MutableLiveData<>();
    private final MutableLiveData<String> mVerifySMSSErrorData = new MutableLiveData<>();

    public void obtainTelephoneSMS(String telephone, SMSOperationType type) {
        IMClient.get().getSMSService().obtainVerificationCodeForTelephoneType(
                telephone,
                type,
                new MVVMRequestCallback<ObtainTelephoneSMSCodeResp>(this) {
                    @Override
                    protected void onSucceed(ObtainTelephoneSMSCodeResp result) {
                        postValue(mObtainSMSErrorData, null);
                        postValue(mSMSLengthData, result.getSmsCodeLength());
                    }

                    @Override
                    protected void showError(int code, String error) {
                        postValue(mObtainSMSErrorData, new ErrorData(code, error));
                    }
                });
    }

    public void verifyTelephoneSMS(String telephone, String smsCode, SMSOperationType type) {
        IMClient.get().getSMSService().verifyTelephoneSMSCode(
                telephone,
                smsCode,
                type,
                new MVVMRequestCallback<VerifyTelephoneSMSCodeResp>(this) {
                    @Override
                    protected void onSucceed(VerifyTelephoneSMSCodeResp result) {
                        postValue(mVerifySMSSuccessData, true);
                    }

                    @Override
                    protected void showError(int code, String error) {
                        postValue(mVerifySMSSErrorData, error);
                    }
                });
    }

    public LiveData<Integer> getSMSLengthData() {
        return mSMSLengthData;
    }

    public LiveData<ErrorData> getObtainSMSErrorData() {
        return mObtainSMSErrorData;
    }

    public LiveData<Boolean> getVerifySMSSuccessData() {
        return mVerifySMSSuccessData;
    }

    public LiveData<String> getVerifySMSSErrorData() {
        return mVerifySMSSErrorData;
    }
}
