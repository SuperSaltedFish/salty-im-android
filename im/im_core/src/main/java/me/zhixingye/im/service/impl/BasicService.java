package me.zhixingye.im.service.impl;

import com.google.protobuf.Any;
import com.google.protobuf.MessageLite;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import me.zhixingye.im.IMCore;
import me.zhixingye.im.constant.ResponseCode;
import me.zhixingye.im.listener.RequestCallback;
import me.zhixingye.im.tool.Logger;

/**
 * Created by zhixingye on 2020年01月10日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
class BasicService {

    private static final String TAG = "BasicService";

    private NetworkServiceImpl mNetworkService = NetworkServiceImpl.get();

    protected ManagedChannel getChannel() {
        return mNetworkService.getChannel();
    }

    static GrpcReq createReq(MessageLite message) {
        Any data = Any.newBuilder()
                .setTypeUrl("salty/" + message.getClass().getCanonicalName())
                .setValue(message.toByteString())
                .build();

        return GrpcReq.newBuilder()
                .setDeviceId(IMCore.get().getDeviceID())
                .setOs(GrpcReq.OS.ANDROID)
                .setLanguage(GrpcReq.Language.CHINESE)
                .setVersion(IMCore.get().getAppVersion())
                .setData(data)
                .build();
    }

    static class DefaultStreamObserver<T extends MessageLite> implements StreamObserver<GrpcResp> {
        private RequestCallback<T> mCallback;
        private GrpcResp mResponse;

        DefaultStreamObserver(RequestCallback<T> callback) {
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
                Logger.e(TAG, "Status == null", t);
                callError(mCallback, ResponseCode.INTERNAL_UNKNOWN);
            } else {
                Logger.e(TAG, status.toString());
                callError(mCallback, status.getCode().value(), status.getDescription());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onCompleted() {
            if (mResponse == null) {
                Logger.e(TAG, "GrpcResp == null");
                callError(mCallback, ResponseCode.INTERNAL_UNKNOWN);
                return;
            }

            if (ResponseCode.isErrorCode(mResponse.getCode())) {
                callError(mCallback, mResponse.getCode(), mResponse.getMessage());
                return;
            }

            Any anyData = mResponse.getData();
            if (anyData == null) {
                Logger.e(TAG, "anyData == null");
                callError(mCallback, ResponseCode.INTERNAL_UNKNOWN_RESP_DATA);
                return;
            }

            byte[] protoData = anyData.toByteArray();
            if (protoData == null) {
                Logger.e(TAG, "protoData == null");
                callError(mCallback, ResponseCode.INTERNAL_UNKNOWN_RESP_DATA);
                return;
            }

            if (mCallback == null) {
                return;
            }

            ParameterizedType pType = null;
            try {
                Type[] types = mCallback.getClass().getGenericInterfaces();
                for (Type t : types) {
                    if (t instanceof ParameterizedType && ((ParameterizedType) t).getRawType() == RequestCallback.class) {
                        pType = (ParameterizedType) t;
                        break;
                    }
                }
                if (pType == null) {
                    Logger.e(TAG, "pType == null");
                    callError(mCallback, ResponseCode.INTERNAL_UNKNOWN);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                callError(mCallback, ResponseCode.INTERNAL_UNKNOWN);
                return;
            }

            try {
                Class type = (Class) pType.getActualTypeArguments()[0];
                Method method = type.getMethod("parseFrom", byte[].class);
                T resultMessage = (T) method.invoke(null, (Object) protoData);
                if (resultMessage == null) {
                    Logger.e(TAG, "resultMessage == null");
                    callError(mCallback, ResponseCode.INTERNAL_UNKNOWN);
                } else {
                    callComplete(mCallback, resultMessage);
                }

            } catch (Exception e) {
                e.printStackTrace();
                callError(mCallback, ResponseCode.INTERNAL_UNKNOWN);
            }
        }
    }

    private static void callError(RequestCallback<?> callback, ResponseCode responseCode) {
        callError(callback, responseCode.getCode(), responseCode.getMsg());
    }

    private static void callError(RequestCallback<?> callback, int code, String error) {
        if (callback != null) {
            callback.onFailure(code, error);
        }
    }

    private static <T> void callComplete(RequestCallback<T> callback, T response) {
        if (callback != null) {
            callback.onCompleted(response);
        }
    }
}

