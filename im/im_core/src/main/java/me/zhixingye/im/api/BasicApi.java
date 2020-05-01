package me.zhixingye.im.api;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.service.AccountService;
import me.zhixingye.im.service.DeviceService;
import me.zhixingye.im.service.impl.ApiServiceImpl;
import me.zhixingye.im.service.impl.ServiceAccessor;
import me.zhixingye.im.tool.Logger;
import me.zhixingye.im.util.StringUtil;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class BasicApi {

    private static final String TAG = "BasicApi";

    GrpcReq createReq(MessageLite message) {
        Any data = Any.newBuilder()
                .setTypeUrl("salty/" + message.getClass().getCanonicalName())
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
            if (status == null) {
                Logger.e(TAG, "Status == null", t);
                callError(ResponseCode.INTERNAL_UNKNOWN);
            } else {
                Logger.e(TAG, status.toString());
                callError(status.getCode().value(), status.getDescription());
            }
        }

        @Override
        public void onCompleted() {
            if (mResponse == null) {
                Logger.e(TAG, "GrpcResp == null");
                callError(ResponseCode.INTERNAL_UNKNOWN);
                return;
            }

            if (ResponseCode.isErrorCode(mResponse.getCode())) {
                callError(mResponse.getCode(), mResponse.getMessage());
                return;
            }

            Any anyData = mResponse.getData();
            if (anyData == null) {
                Logger.e(TAG, "anyData == null");
                callError(ResponseCode.INTERNAL_UNKNOWN_RESP_DATA);
                return;
            }

            ByteString protoData = anyData.getValue();
            if (protoData == null) {
                Logger.e(TAG, "protoData == null");
                callError(ResponseCode.INTERNAL_UNKNOWN_RESP_DATA);
                return;
            }

            if (mCallback == null) {
                return;
            }

            try {
                T resultMessage = (T) mDefaultInstance.getParserForType().parseFrom(protoData);
                if (resultMessage == null) {
                    Logger.e(TAG, "resultMessage == null");
                    callError(ResponseCode.INTERNAL_UNKNOWN);
                } else {
                    callComplete(resultMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                callError(ResponseCode.INTERNAL_UNKNOWN);
            }
        }

        private void callError(ResponseCode responseCode) {
            callError(responseCode.getCode(), responseCode.getMsg());
        }

        private void callError(int code, String error) {
            printErrorResponse(mDefaultInstance, code, error);
            if (mCallback != null) {
                mCallback.onFailure(code, error);
            }
        }

        private void callComplete(T response) {
            printResponse(mResponse, response);
            if (mCallback != null) {
                mCallback.onCompleted(response);
            }
        }
    }


    private static void printRequest(GrpcReq req, MessageLite data) {
        printGrpcData("发起请求", req.toString(), data);
    }

    private static void printResponse(GrpcResp resp, MessageLite data) {
        printGrpcData("收到响应", resp.toString(), data);
    }

    private static void printGrpcData(String title, String grpcStr, MessageLite data) {
        Pattern firstLinePattern = Pattern.compile("#.*?\\n");
        String respStr = firstLinePattern.matcher(grpcStr).replaceAll("");
        String dataStr = data.toString();
        String responseData;
        if (dataStr.startsWith("#") && dataStr.indexOf("\n") > 0) {
            dataStr = firstLinePattern.matcher(dataStr).replaceAll("");
            dataStr = "  " + dataStr;
            dataStr = dataStr.replace("\n", "\n  ");

            Pattern dataRegionPattern = Pattern.compile("\\{[\\s\\S]*?\\}");
            Matcher dataRegionMatcher = dataRegionPattern.matcher(respStr);

            responseData = dataRegionMatcher.replaceAll("{\n" + dataStr + "\n}");
        } else {
            responseData = respStr;
        }

        responseData = "  " + responseData;
        responseData = responseData.replace("\n", "\n  ");

        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n%s：Method = %s\n{\n%s\n}",
                title,
                getRequestName(data),
                responseData
        ));
    }

    private static void printErrorResponse(MessageLite data, int code, String error) {
        Logger.e(TAG, String.format(Locale.getDefault(),
                "\n收到响应：Method = %s\n{\n  code：%d\n  error：%s\n}",
                getRequestName(data),
                code,
                error
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

