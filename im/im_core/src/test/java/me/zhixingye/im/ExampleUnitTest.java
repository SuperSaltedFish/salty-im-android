package me.zhixingye.im;

import com.google.protobuf.Any;
import com.salty.protos.BasicReq;
import com.salty.protos.BasicResp;
import com.salty.protos.UserServiceGrpc;

import org.junit.Test;

import javax.annotation.Nullable;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;


public class ExampleUnitTest {
    @Test
    public void test() {
        try {
            OkHttpChannelBuilder builder = OkHttpChannelBuilder.forAddress("127.0.0.1", 5001).usePlaintext().intercept(new ClientInterceptor() {
                @Override
                public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
                    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
                        @Override
                        public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {

                            super.start(responseListener, headers);
                        }

                        @Override
                        public void sendMessage(ReqT message) {
                            super.sendMessage(message);
                        }
                    };
                }
            });

            ManagedChannel channel = builder.build();
       
        } catch (Exception e) {

        }
    }
}