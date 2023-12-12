package org.ammbra.compute.subnet;

import com.pulumi.oci.Core.*;
import com.pulumi.oci.Core.inputs.RouteTableRouteRuleArgs;
import org.ammbra.compute.Params;

import java.util.EnumMap;

public final class SubnetInfrastructureFactory {

	private SubnetInfrastructureFactory() {
		// private constructor to prevent instantiation of factory class
	}

	public static SubnetStructure provision(EnumMap<Params, String> configMap, SubnetStructure infrastructure) {
		return switch (infrastructure) {
			case null -> provision(configMap);
			case VirtualNetwork vcn ->  provision(configMap, vcn);
			case NetworkGateway networkGateway -> provision(configMap, networkGateway);
			case RouterTable routerTable -> provision(configMap, routerTable);
			case Subnet subnet -> provision(configMap, subnet);
			case SubnetStructure infra -> throw new UnsupportedOperationException(String.format("No operation set for type %s", infra.getClass().getSimpleName()));
		};
	}

	private static VirtualNetwork provision(EnumMap<Params, String> configMap) {
		String name = configMap.get(Params.VCN_DISPLAY_NAME);
		VcnArgs args = VcnArgs.builder()
				.displayName(name)
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.cidrBlock(configMap.get(Params.VCN_CIDR_BLOCK))
				.build();
		Vcn vcn = new Vcn(name, args);
		return new VirtualNetwork(vcn.id(), vcn.compartmentId(), vcn.displayName(), vcn.cidrBlock());
	}

	private static NetworkGateway provision(EnumMap<Params, String> configMap, VirtualNetwork vcn) {
		String name = configMap.get(Params.VCN_DISPLAY_NAME);
		InternetGatewayArgs args = InternetGatewayArgs.builder()
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.displayName(configMap.get(Params.INTERNET_GATEWAY))
				.vcnId(vcn.id()).build();
		InternetGateway internetGateway = new InternetGateway(name, args);
		return  new NetworkGateway(internetGateway.id(), internetGateway.compartmentId(), internetGateway.displayName(), vcn);
	}

	private static RouterTable provision(EnumMap<Params, String> configMap, NetworkGateway networkGateway) {
		String name = configMap.get(Params.ROUTETABLE_DISPLAYNAME);
		RouteTableRouteRuleArgs trafficToInternet = RouteTableRouteRuleArgs.builder()
				.description("traffic to/from internet")
				.destination("0.0.0.0/0").destinationType("CIDR_BLOCK")
				.networkEntityId(networkGateway.id()).build();

		RouteTableArgs args = RouteTableArgs.builder()
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.displayName(name)
				.routeRules(trafficToInternet)
				.vcnId(networkGateway.vcn().id()).build();
		RouteTable routeTable = new RouteTable(name, args);
		return new RouterTable(routeTable.id(), routeTable.compartmentId(), routeTable.displayName(), networkGateway);
	}

	private static Subnet provision(EnumMap<Params, String> configMap, RouterTable routeTable) {
		String name = configMap.get(Params.SUBNET_DISPLAYNAME);
		SubnetArgs args = SubnetArgs.builder()
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.displayName(name)
				.cidrBlock(configMap.get(Params.SUBNET_CIDR))
				.prohibitInternetIngress(false)
				.routeTableId(routeTable.id())
				.vcnId(routeTable.id())
				.build();
		com.pulumi.oci.Core.Subnet subnet = new com.pulumi.oci.Core.Subnet(name, args);
		return  new Subnet(subnet.id(), subnet.compartmentId(), subnet.displayName(), subnet.cidrBlock(), routeTable);
	}


}
