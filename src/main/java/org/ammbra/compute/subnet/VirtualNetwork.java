package org.ammbra.compute.subnet;

import com.pulumi.core.Output;

public record VirtualNetwork(Output<String> id, Output<String> compartmentId, Output<String> displayName, Output<String> cidrBlock) implements SubnetStructure {
}
