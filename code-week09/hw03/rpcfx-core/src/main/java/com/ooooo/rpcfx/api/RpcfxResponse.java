package com.ooooo.rpcfx.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcfxResponse {
    private Object result;
    private boolean status;
    private Exception exception;
}
