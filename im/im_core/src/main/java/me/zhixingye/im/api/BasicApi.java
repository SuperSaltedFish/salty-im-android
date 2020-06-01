package me.zhixingye.im.api;

import android.text.TextUtils;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;
import com.salty.protos.StatusCode;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import me.zhixingye.im.BuildConfig;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.DeviceService;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.service.impl.ServiceAccessor;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.StringUtil;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public abstract class BasicApi {

    private static final String TAG = "BasicApi";

    public abstract void onBindManagedChannel(ManagedChannel channel);

    GrpcReq createReq(MessageLite message) {
        Any data = Any.newBuilder()
                .setTypeUrl("type.googleapis.com/" + message.getClass().getCanonicalName())
                .setValue(message.toByteString())
                .build();

        DeviceService deviceService = ServiceAccessor.get(DeviceService.class);
        AccountService accountService = ServiceAccessor.get(AccountService.class);

        GrpcReq req = GrpcReq.newBuilder()
                .setDeviceId(StringUtil.checkNull(deviceService.getDeviceId()))
                .setOs(GrpcReq.OS.ANDROID)
                .setLanguage(GrpcReq.Language.CHINESE)
                .setVersion(StringUtil.checkNull(deviceService.getAppVersion()))
                .setToken(StringUtil.checkNull(accountService.getCurrentUserToken()))
                .setData(data)
                .build();

        printRequest(req, message);
        return req;
    }

    @SuppressWarnings("unchecked")
    static class DefaultStreamObserver<T extends GeneratedMessageLite> implements StreamObserver<GrpcResp> {

        private RequestCallback<T> mCallback;
        private GrpcResp mResponse;
        private T mDefaultInstance;

        DefaultStreamObserver(T defaultInstance, RequestCallback<T> callback) {
            mCallback = callback;
            mDefaultInstance = defaultInstance;
        }

        @Override
        public void onNext(GrpcResp value) {
            mResponse = value;
        }

        @Override
        public void onError(Throwable t) {
            Status status = Status.fromThrowable(t);

            String error;
            switch (status.getCode()) {
                case DEADLINE_EXCEEDED:
                    error = "连接超时，请检查当前网络状态是否正常！";
                    break;
                default:
                    error = status.getDescription();
                    break;
            }
            if (TextUtils.isEmpty(error) && status.getCause() != null) {
                error = status.getCause().getMessage();
            }
            callError(-status.getCode().value(), error);
        }

        @Override
        public void onCompleted() {
            if (mResponse == null) {
                onError(new Throwable("GrpcResp == null"));
                return;
            }

            T resultMessage = null;
            try {
                if (ResponseCode.isErrorCode(mResponse.getCode())) {
                    callError(mResponse.getCode().getNumber(), mResponse.getMessage());
                    return;
                }

                Any anyData = mResponse.getData();
                if (anyData == null) {
                    onError(new Throwable("anyData == null"));
                    return;
                }

                ByteString protoData = anyData.getValue();
                if (protoData == null) {
                    onError(new Throwable("protoData == null"));
                    return;
                }

                if (mCallback == null) {
                    return;
                }

                resultMessage = (T) mDefaultInstance.getParserForType().parseFrom(protoData);
                if (resultMessage == null) {
                    onError(new Throwable("resultMessage == null"));
                } else {
                    mCallback.onCompleted(resultMessage);
                }
            } catch (Exception e) {
                onError(e);
            } finally {
                printResponse(mResponse, resultMessage, mDefaultInstance);
            }
        }

        private void callError(int code, String error) {
            if (mCallback != null) {
                mCallback.onFailure(code, error);
            }
        }
    }


    private static void printRequest(GrpcReq req, MessageLite data) {
        if (BuildConfig.DEBUG) {
            String title = "发起请求：" + getRequestName(data);
            printGrpcData(
                    title,
                    req.toString(),
                    data.toString());
        }
    }

    private static void printResponse(GrpcResp resp, MessageLite data, MessageLite defaultDataInstance) {
        if (BuildConfig.DEBUG) {
            String title = "收到响应：" + getRequestName(defaultDataInstance);
            printGrpcData(
                    title,
                    resp.toString(),
                    data == null ? "" : data.toString());
        }
    }

    private static void printGrpcData(String title, String grpcStr, String dataContent) {
        Pattern firstLinePattern = Pattern.compile("#.*?\\n");
        String respStr = firstLinePattern.matcher(grpcStr).replaceAll("");
        String responseData;

        if (!TextUtils.isEmpty(dataContent) && dataContent.startsWith("#") && dataContent.indexOf("\n") > 0) {
            dataContent = firstLinePattern.matcher(dataContent).replaceAll("");
            dataContent = "  " + dataContent;
            dataContent = dataContent.replace("\n", "\n  ");

            Pattern dataRegionPattern = Pattern.compile("\\{[\\s\\S]*?\\}");
            Matcher dataRegionMatcher = dataRegionPattern.matcher(respStr);

            responseData = dataRegionMatcher.replaceAll("{\n" + dataContent + "\n}");
        } else {
            responseData = respStr;
        }

        responseData = "  " + responseData;
        responseData = responseData.replace("\n", "\n  ");

        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n%s\n{\n%s\n}",
                title,
                responseData
        ));
    }

    private static String getRequestName(MessageLite message) {
        String className = message.getClass().getSimpleName();
        int endIndex = className.indexOf("Req");
        if (endIndex < 0) {
            endIndex = className.indexOf("Resp");
        }
        if (endIndex < 0) {
            return "unknown";
        }
        return className.substring(0, endIndex);
    }
}

