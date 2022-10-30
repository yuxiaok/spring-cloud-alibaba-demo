package ltd.newbee.cloud.balancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>****************************************************************************
 * </p>
 * <p><b>Copyright © 2010-2022 shuncom team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : TODO </li>
 * <li>Version : 1.0.0</li>
 * <li>Creation : 2022年10⽉23⽇</li>
 * <li>@author : kai.yu</li>
 * </ul>
 * <p>****************************************************************************
 * </p>
 */
public class NewBeeCloudLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	private String serviceName;

	private AtomicInteger atomicCount = new AtomicInteger(0);

	private AtomicInteger atomicCurrentIndex = new AtomicInteger(0);

	public NewBeeCloudLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceName) {
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.serviceName = serviceName;
	}

	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		return serviceInstanceListSupplierProvider.getIfAvailable().get().next().map(this::getInstanceResponse);
	}

	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
		ServiceInstance serviceInstance = null;
		if (instances.isEmpty()) {
			System.out.println("注册中心无实例可用：" + serviceName);
			return new EmptyResponse();
		}

		//累加请求次数
		int requestNumber = atomicCount.incrementAndGet();

		//自定义算法
		if (requestNumber < 2) {
			serviceInstance = instances.get(atomicCurrentIndex.get());
		} else {
			//已经大于2了，重置
			atomicCount = new AtomicInteger(0);

			//实例下标+1
			atomicCurrentIndex.incrementAndGet();
			if (atomicCurrentIndex.get() >= instances.size()) {
				atomicCurrentIndex = new AtomicInteger(0);
				serviceInstance = instances.get(instances.size() - 1);
				return new DefaultResponse(serviceInstance);
			}
			serviceInstance = instances.get(atomicCurrentIndex.get() - 1);
		}
		return new DefaultResponse(serviceInstance);
	}
}