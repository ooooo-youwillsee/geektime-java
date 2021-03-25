package com.ooooo.rpcfx.demo.consumer;

import com.ooooo.rpcfx.client.Rpcfx;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author leizhijie
 * @since 2021/3/25 21:00
 */
@Component
public class RpcfxBeanPostProcessro implements InstantiationAwareBeanPostProcessor {
	
	// 通过反射进行设置
	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
		Class<?> userClass = ClassUtils.getUserClass(bean.getClass());
		ReflectionUtils.doWithFields(userClass, field -> {
			RpcfxReference reference = AnnotatedElementUtils.findMergedAnnotation(field, RpcfxReference.class);
			if (reference != null) {
				ReflectionUtils.makeAccessible(field);
				String value = reference.value();
				Object proxy = Rpcfx.create(field.getType(), value);
				field.set(bean, proxy);
			}
		});
		return null;
	}
}
