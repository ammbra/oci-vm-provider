package org.ammbra.compute.finder;

import com.pulumi.core.Output;

public record ComputeShape(Output<String> name, AvailabilityDomain availabilityDomain) implements InstanceStructure {
}
