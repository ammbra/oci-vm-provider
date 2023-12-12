package org.ammbra.compute;

import com.pulumi.oci.Core.Instance;
import com.pulumi.oci.Core.InstanceArgs;
import com.pulumi.oci.Core.inputs.InstanceAgentConfigArgs;
import com.pulumi.oci.Core.inputs.InstanceCreateVnicDetailsArgs;
import com.pulumi.oci.Core.inputs.InstanceLaunchOptionsArgs;
import com.pulumi.oci.Core.inputs.InstanceSourceDetailsArgs;
import org.ammbra.compute.finder.PlatformImage;
import org.ammbra.compute.subnet.Subnetwork;

import java.util.EnumMap;
import java.util.Map;

public class InfrastructureChain {

	public static final String SOURCE_TYPE = "image";
	public static final String PARAVIRTUALIZED = "PARAVIRTUALIZED";

	private InfrastructureChain() {}

	public static Instance execute(EnumMap<Params, String> configMap, String name, Subnetwork subnet, PlatformImage image) {
		InstanceAgentConfigArgs agentConf = InstanceAgentConfigArgs.builder().isMonitoringDisabled(false).build();
		InstanceCreateVnicDetailsArgs vnicDetails = InstanceCreateVnicDetailsArgs.builder()
				.subnetId(subnet.id())
				.build();
		InstanceSourceDetailsArgs sourceDetails = InstanceSourceDetailsArgs.builder()
				.sourceType(SOURCE_TYPE).sourceId(image.id()).build();
		InstanceLaunchOptionsArgs launchOpt = InstanceLaunchOptionsArgs.builder()
				.bootVolumeType(PARAVIRTUALIZED)
				.networkType(PARAVIRTUALIZED)
				.build();

		return new Instance(name, InstanceArgs.builder()
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.shape(image.shape().name())
				.availabilityDomain(image.shape().availabilityDomain().name())
				.metadata(Map.of(Params.SSH_PRIVATE_KEY_FILE.getLabel(), configMap.get(Params.SSH_KEY)))
				.agentConfig(agentConf)
				.preserveBootVolume(true)
				.createVnicDetails(vnicDetails)
				.sourceDetails(sourceDetails)
				.launchOptions(launchOpt)
				.build()
		);
	}
}
