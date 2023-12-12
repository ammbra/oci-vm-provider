package org.ammbra.compute.subnet;

import com.pulumi.core.Output;

public record RouterTable(Output<String> id, Output<String> compartmentId, Output<String> displayName, NetworkGateway networkGateway) implements SubnetStructure {
}
