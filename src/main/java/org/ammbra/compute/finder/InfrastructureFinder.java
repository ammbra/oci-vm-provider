package org.ammbra.compute.finder;

import com.pulumi.core.Output;
import com.pulumi.oci.Core.CoreFunctions;
import com.pulumi.oci.Core.inputs.GetImagesArgs;
import com.pulumi.oci.Core.inputs.GetShapesArgs;
import com.pulumi.oci.Identity.IdentityFunctions;
import com.pulumi.oci.Identity.inputs.GetAvailabilityDomainsArgs;
import com.pulumi.oci.Identity.outputs.GetAvailabilityDomainsAvailabilityDomain;
import org.ammbra.compute.Params;

import java.util.EnumMap;

public final class InfrastructureFinder {

	private InfrastructureFinder() {
		// private constructor to prevent instantiation of factory class
	}

	public static InstanceStructure findInfrastructure(EnumMap<Params, String> configMap, InstanceStructure instance) {
		return switch (instance) {
			case null -> findFirstAvailabilityDomain(configMap);
			case AvailabilityDomain availabilityDomain -> findFirstShape(configMap, availabilityDomain);
			case ComputeShape shape -> findFirstImage(configMap, shape);
			case PlatformImage platformImage -> throw new UnsupportedOperationException("No operation associated to " + platformImage.getClass().getSimpleName());
		};
	}

	private static AvailabilityDomain findFirstAvailabilityDomain(EnumMap<Params, String> configMap) {
		Output<GetAvailabilityDomainsAvailabilityDomain> availableDomain = IdentityFunctions.getAvailabilityDomains(GetAvailabilityDomainsArgs.builder()
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.build()).applyValue(result -> result.availabilityDomains().getFirst());
		Output<String> name = availableDomain.applyValue(GetAvailabilityDomainsAvailabilityDomain::name);
		Output<String> compartmentId = availableDomain.applyValue(GetAvailabilityDomainsAvailabilityDomain::compartmentId);
		return new AvailabilityDomain(name, compartmentId);
	}

	private static PlatformImage findFirstImage(EnumMap<Params, String> configMap, ComputeShape shape) {
		Output<String> id = CoreFunctions.getImages(GetImagesArgs.builder()
				.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
				.operatingSystem(configMap.get(Params.OPERATING_SYSTEM))
				.operatingSystemVersion(configMap.get(Params.OPERATING_SYSTEM_VERSION))
				.shape(shape.name()).sortBy("TIMECREATED").sortOrder("DESC")
				.build()).applyValue(result -> result.images().getFirst().id());
		return new PlatformImage(id, shape);
	}

	private static ComputeShape findFirstShape(EnumMap<Params, String> configMap, AvailabilityDomain availabilityDomain) {
		Output<String> name = CoreFunctions.getShapes(GetShapesArgs.builder()
						.compartmentId(configMap.get(Params.COMPARTMENT_OCID))
						.availabilityDomain(availabilityDomain.name()).build())
				.applyValue(result -> {
					var shapes = result.shapes();
					var firstShape = shapes.stream().filter(res -> res.name().contains(configMap.get(Params.SHAPE))).findFirst();
					return firstShape.orElse(shapes.getFirst()).name();
				});
		return new ComputeShape(name, availabilityDomain);
	}

}
