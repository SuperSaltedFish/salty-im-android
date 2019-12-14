package me.zhixingye.salty;

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

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
//        OkHttpChannelBuilder builder = OkHttpChannelBuilder.forAddress("127.0.0.1", 5001).usePlaintext();
//        ManagedChannel channel = builder.build();
//        HelloRequest request = HelloRequest.newBuilder().setName("123").build();
//        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
//        HelloReply reply = blockingStub.sayHello(request);

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

//            MetadataUtils.attachHeaders();

            GreeterGrpc.GreeterStub futureStub = GreeterGrpc.newStub(channel).build(channel, CallOptions.DEFAULT);
            futureStub.sayHello(new StreamObserver<HelloReply>() {
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
        } catch (Exception e) {

        }
    }
}