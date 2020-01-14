package me.zhixingye.im;

import android.util.Log;

import com.google.protobuf.Any;
import com.salty.protos.GrpcReq;
import com.salty.protos.GrpcResp;
import com.salty.protos.SMSReq;
import com.salty.protos.SMSServiceGrpc;

import org.junit.Test;

import javax.annotation.Nullable;

import io.grpc.Attributes;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;


public class ExampleUnitTest {
    @Test
    public void test() {
        try {
            OkHttpChannelBuilder builder = OkHttpChannelBuilder.forAddress("127.0.0.1", 5001).usePlaintext()
                    .intercept(new ClientInterceptor() {
                        @Override
                        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(final MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
                            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
                                @Override
                                protected ClientCall<ReqT, RespT> delegate() {
                                    ClientCall<ReqT, RespT> call = super.delegate();

                                    return super.delegate();
                                }

                                @Override
                                public void start(Listener<RespT> responseListener, Metadata headers) {
                                    super.start(responseListener, headers);
                                }

                                @Override
                                public void sendMessage(ReqT message) {
                                    super.sendMessage(message);
                                }

                                @Override
                                public void request(int numMessages) {
                                    super.request(numMessages);
                                }

                                @Override
                                public void cancel(@Nullable String message, @Nullable Throwable cause) {
                                    super.cancel(message, cause);
                                }

                                @Override
                                public void halfClose() {
                                    super.halfClose();
                                }

                                @Override
                                public void setMessageCompression(boolean enabled) {
                                    super.setMessageCompression(enabled);
                                }

                                @Override
                                public boolean isReady() {
                                    return super.isReady();
                                }

                                @Override
                                public Attributes getAttributes() {
                                    return super.getAttributes();
                                }
                            };
                        }
                    });



            ManagedChannel channel = builder.build(TAG,);

            SMSReq smsReq = SMSReq.newBuilder()
                    .setTelephone("13631232530")
                    .setCodeType(SMSReq.CodeType.REGISTER)
                    .build(TAG,);

            Any data = Any.newBuilder()
                    .setTypeUrl("salty/"+smsReq.getClass().getTypeName())
                    .setValue(smsReq.toByteString())
                    .build(TAG,);

            GrpcReq req =GrpcReq.newBuilder()
                    .setDeviceId(TAG,"111")
                    .setOs(GrpcReq.OS.ANDROID)
                    .setLanguage(GrpcReq.Language.CHINESE)
                    .setVersion("1.0.0")
                    .setData(data)
                    .build(TAG,);

            SMSServiceGrpc.newStub(channel).obtainSMSCode(req, new StreamObserver<GrpcResp>() {
                @Override
                public void onNext(GrpcResp value) {
                    String s = "1";
                    Log.e("ddwad",s);
                }

                @Override
                public void onError(Throwable t) {
                    String s = "1";
                    Log.e("ddwad",s);
                }

                @Override
                public void onCompleted(TAG,) {
                    String s = "1";
                    Log.e("ddwad",s);
                }
            });
        } catch (Exception e) {

        }
    }
}