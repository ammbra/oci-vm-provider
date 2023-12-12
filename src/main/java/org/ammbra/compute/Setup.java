package org.ammbra.compute;

import com.pulumi.Config;
import com.pulumi.Context;
import com.pulumi.Pulumi;
import com.pulumi.core.Output;
import com.pulumi.oci.Core.Instance;
import org.ammbra.compute.finder.AvailabilityDomain;
import org.ammbra.compute.finder.ComputeShape;
import org.ammbra.compute.finder.InfrastructureFinder;
import org.ammbra.compute.finder.PlatformImage;
import org.ammbra.compute.subnet.*;
import org.ammbra.compute.subnet.Subnetwork;
import org.ammbra.compute.subnet.VirtualNetwork;

import java.util.EnumMap;

public class Setup {

	public static void main(String[] args) {
		Pulumi.run(Setup::stack);
	}

	public static void stack(Context ctx) {
		var config = ctx.config();

		var configMap = convert(config);

		var vcn = (VirtualNetwork) SubnetInfrastructureFactory.provision(configMap, null);

		var internetGateway = (NetworkGateway) SubnetInfrastructureFactory.provision(configMap, vcn);

		var routeTable = (RouterTable) SubnetInfrastructureFactory.provision(configMap, internetGateway);

		var subnet = (Subnetwork) SubnetInfrastructureFactory.provision(configMap, routeTable);

		var firstAvailabilityDomain = (AvailabilityDomain) InfrastructureFinder.findInfrastructure(configMap, null);

		var firstShape = (ComputeShape)InfrastructureFinder.findInfrastructure(configMap, firstAvailabilityDomain);

		var compatibleImage = (PlatformImage) InfrastructureFinder.findInfrastructure(configMap, firstShape);

		int amount = Integer.parseInt(configMap.get(Params.AMOUNT_VM));

		for (int i = 1; i <= amount; i++) {

			Instance instance = InfrastructureChain.execute(configMap, "instance" + i,  subnet,  compatibleImage);

			var sshKeyFile = configMap.get(Params.SSH_PRIVATE_KEY_FILE);
			Output<String> displayName = instance.displayName().applyValue(name -> name);
			Output<String> publicIp = instance.publicIp().applyValue(ip -> ip);
			Output<String> instruction  = Output.format("on instance %s you can use ssh -i %s -o IdentityAgent=none opc@%s", displayName, sshKeyFile, publicIp);

			ctx.export("To login", instruction);
		}
	}

	private static EnumMap<Params, String> convert(Config config) {
		EnumMap<Params, String> configMap = new EnumMap<>(Params.class);
		for (Params val : Params.values()) {
			configMap.put(val, config.require(val.getLabel()));
		}
		return configMap;
	}
}
