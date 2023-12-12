package org.ammbra.compute.finder;

import com.pulumi.core.Output;

public record AvailabilityDomain(Output<String> name, Output<String> compartmentId) implements InstanceStructure {

}
