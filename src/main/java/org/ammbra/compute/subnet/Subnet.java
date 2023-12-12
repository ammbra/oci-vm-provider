package org.ammbra.compute.subnet;

import com.pulumi.core.Output;

public record Subnet(Output<String> id, Output<String> compartmentId, Output<String> displayName, Output<String> cidrBlock, RouterTable routeTable) implements SubnetStructure {
}
