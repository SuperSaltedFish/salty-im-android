package me.zhixingye.im;

import org.junit.Test;

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
import me.zhixingye.im.grpc.GreeterGrpc;
import me.zhixingye.im.grpc.HelloReply;
import me.zhixingye.im.grpc.HelloRequest;


public class ExampleUnitTest {
    @Test
    public void test() {
        try {
            OkHttpChannelBuilder builder = OkHttpChannelBuilder.forAddress("127.0.0.1", 5001).usePlaintext().intercept(new ClientInterceptor() {
                @Override
                public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
                    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)){
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

            HelloRequest request = HelloRequest.newBuilder().setName("123").build();

            GreeterGrpc.GreeterStub futureStub = GreeterGrpc.newStub(channel);
            StreamObserver<HelloRequest> observer =    futureStub.sayHello(new StreamObserver<HelloReply>() {
                @Override
                public void onNext(HelloReply value) {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            });
            observer.onNext(request);
        } catch (Exception e) {

        }
    }
}