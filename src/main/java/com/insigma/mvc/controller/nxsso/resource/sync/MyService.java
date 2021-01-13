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
		// ���requestType��push_cancel����requestType��push����Դ״̬Ϊ2�ģ�����Դ������Ҫ���ٵ�
		String requestType = (String)resourceMap.get(Constants.REQUEST_TYPE);
		System.out.println("requestType="+requestType);
		System.out.println("11111111111111111111111111111111111111="+requestType);
		List<RmsOrganizationView> receivedOrgs = (List<RmsOrganizationView>) resourceMap.get(ResourceTypeEnum.Organization.name());
		if (receivedOrgs != null && !receivedOrgs.isEmpty()) {
			// TODO ������֯�ܹ�
		}

		List<RmsUserView> receivedUsers = (List<RmsUserView>) resourceMap.get(ResourceTypeEnum.User.name());
		if (receivedUsers != null && !receivedUsers.isEmpty()) {
			// TODO �����û���Դ
		}

		List<RmsApplicationView> receivedApps = (List<RmsApplicationView>) resourceMap.get(ResourceTypeEnum.Application.name());
		if (receivedApps != null && !receivedApps.isEmpty()) {
			// TODO ����Ӧ����Դ
		}

		List<RmsNetworkView> receivedNets = (List<RmsNetworkView>) resourceMap.get(ResourceTypeEnum.Network.name());
		if (receivedNets != null && !receivedNets.isEmpty()) {
			// TODO ����������Դ
		}

		List<Policy> receivedInfos = (List<Policy>) resourceMap.get(ResourceTypeEnum.Information.name());
		if (receivedInfos != null && !receivedInfos.isEmpty()) {
			// TODO ����Ȩ����Ϣ
		}

		List<Resource> receivedOthers = (List<Resource>) resourceMap.get(ResourceTypeEnum.Other.name());
		if (receivedOthers != null && !receivedOthers.isEmpty()) {
			// TODO ����������Դ
		}
		return true;
	}
}

