package org.ammbra.compute.subnet;

import com.pulumi.core.Output;

public record NetworkGateway(Output<String> id, Output<String> compartmentId, Output<String> displayName, VirtualNetwork vcn) implements SubnetStructure {
}
