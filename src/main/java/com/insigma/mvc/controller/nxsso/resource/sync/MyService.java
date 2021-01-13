package com.insigma.mvc.controller.nxsso.resource.sync;

import java.util.List;
import java.util.Map;

import com.westone.middleware.toolkit.trustService.authorization.support.vo.Permission.Policy;
import com.westone.middleware.toolkit.trustService.commons.constant.Constants;
import com.westone.middleware.toolkit.trustService.commons.data.TrustServiceResponse;
import com.westone.middleware.toolkit.trustService.resource.AbstractResourceSynchronizationTemplate;
import com.westone.middleware.toolkit.trustService.resource.support.enums.ResourceTypeEnum;
import com.westone.middleware.toolkit.trustService.resource.support.view.RmsApplicationView;
import com.westone.middleware.toolkit.trustService.resource.support.view.RmsNetworkView;
import com.westone.middleware.toolkit.trustService.resource.support.view.RmsOrganizationView;
import com.westone.middleware.toolkit.trustService.resource.support.view.RmsUserView;
import com.westone.middleware.toolkit.trustService.resource.support.vo.Resource;

class MyService extends AbstractResourceSynchronizationTemplate {
	@Override
	public boolean resourcesPersistence(Map<String, Object> resourceMap, TrustServiceResponse tsResponse) {
		// 如果requestType是push_cancel或者requestType是push且资源状态为2的，该资源就是需要销毁的
		String requestType = (String)resourceMap.get(Constants.REQUEST_TYPE);
		System.out.println("requestType="+requestType);
		System.out.println("11111111111111111111111111111111111111="+requestType);
		List<RmsOrganizationView> receivedOrgs = (List<RmsOrganizationView>) resourceMap.get(ResourceTypeEnum.Organization.name());
		if (receivedOrgs != null && !receivedOrgs.isEmpty()) {
			// TODO 处理组织架构
		}

		List<RmsUserView> receivedUsers = (List<RmsUserView>) resourceMap.get(ResourceTypeEnum.User.name());
		if (receivedUsers != null && !receivedUsers.isEmpty()) {
			// TODO 处理用户资源
		}

		List<RmsApplicationView> receivedApps = (List<RmsApplicationView>) resourceMap.get(ResourceTypeEnum.Application.name());
		if (receivedApps != null && !receivedApps.isEmpty()) {
			// TODO 处理应用资源
		}

		List<RmsNetworkView> receivedNets = (List<RmsNetworkView>) resourceMap.get(ResourceTypeEnum.Network.name());
		if (receivedNets != null && !receivedNets.isEmpty()) {
			// TODO 处理网络资源
		}

		List<Policy> receivedInfos = (List<Policy>) resourceMap.get(ResourceTypeEnum.Information.name());
		if (receivedInfos != null && !receivedInfos.isEmpty()) {
			// TODO 处理权限信息
		}

		List<Resource> receivedOthers = (List<Resource>) resourceMap.get(ResourceTypeEnum.Other.name());
		if (receivedOthers != null && !receivedOthers.isEmpty()) {
			// TODO 处理其他资源
		}
		return true;
	}
}

