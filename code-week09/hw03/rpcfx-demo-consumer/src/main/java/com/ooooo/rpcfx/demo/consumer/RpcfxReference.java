package com.ooooo.rpcfx.demo.consumer;

import java.lang.annotation.*;
import org.springframework.stereotype.Indexed;

/**
 * @author leizhijie
 * @since 2021/3/25 21:49
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface RpcfxReference {
	
	String value();
}
