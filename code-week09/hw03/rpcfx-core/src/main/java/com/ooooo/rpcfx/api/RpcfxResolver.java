package com.ooooo.rpcfx.api;

public interface RpcfxResolver {

    <T> T resolve(Class<T> clazz);

}
