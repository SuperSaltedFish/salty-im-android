package me.zhixingye.im.service;

import com.google.protobuf.Any;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import me.zhixingye.im.constant.ErrorCode;
import me.zhixingye.im.listener.RequestCallback;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
class BasicService {

    private static final String TAG = "BasicService";

    private NetworkService mNetworkService = NetworkService.get();

    protected ManagedChannel getChannel() {
        return mNetworkService.getChannel();
    }

    static GrpcReq createReq(MessageLite message) {
        Any data = Any.newBuilder()
                .setTypeUrl("salty/" + message.getClass().getCanonicalName())
                .setValue(message.toByteString())
                .build();

        return GrpcReq.newBuilder()
                .setDeviceID("111")
                .setOs(GrpcReq.OS.ANDROID)
                .setLanguage(GrpcReq.Language.CHINESE)
                .setVersion("1.0.0")
                .setData(data)
                .build();
    }

    static class DefaultStreamObserver<T extends GeneratedMessageLite> implements StreamObserver<GrpcResp> {
        private RequestCallback<T> mCallback;
        private GrpcResp mResponse;
        private T mDefaultInstance;

        DefaultStreamObserver(T defaultInstance, RequestCallback<T> callback) {
            mDefaultInstance = defaultInstance;
            mCallback = callback;
        }

        @Override
        public void onNext(GrpcResp value) {
            mResponse = value;
        }

        @Override
        public void onError(Throwable t) {
            Status status = Status.fromThrowable(t);
            if (status == null) {
                LogService.getLogger().e(TAG, "Status == null");
                callError(mCallback, ErrorCode.INTERNAL_UNKNOWN);
            } else {
                switch (status.getCode()){
                    case OK:
                    case ABORT ED:
                }
                callError(mCallback,new ErrorCode(status.getCode().value(),status.getDescription()));
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCompleted() {
            if (mResponse == null) {
                LogService.getLogger().e(TAG, "GrpcResp == null");
                callError(mCallback, ErrorCode.INTERNAL_UNKNOWN);
                return;
            }

            if (ErrorCode.isErrorCode(mResponse.getCode())) {
                callError(mCallback, new ErrorCode(mResponse.getCode(), mResponse.getMessage()));
                return;
            }

            Any data = mResponse.getData();
            if (data != null) {
                try {
                    T original = (T) mDefaultInstance.getParserForType().parseFrom(data.getValue());
                    if (original != null) {
                        callComplete(mCallback, original);
                    }
                } catch (Exception e) {
                    LogService.getLogger().e(TAG, "parse 'ProtocolBuffer' fail", e);
                    callError(mCallback, ErrorCode.INTERNAL_UNKNOWN_RESP_DATA);
                }
            }
        }
    }

    private static void callError(RequestCallback<?> callback, ErrorCode errorCode) {
        if (callback != null) {
            callback.onFailure(errorCode);
        }
    }

    private static <T> void callComplete(RequestCallback<T> callback, T response) {
        if (callback != null) {
            callback.onCompleted(response);
        }
    }
}

