package org.ammbra.compute.finder;

import com.pulumi.core.Output;

public record PlatformImage(Output<String> id, ComputeShape shape) implements InstanceStructure {
}
