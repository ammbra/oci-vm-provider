package org.ammbra.compute;

import com.pulumi.core.Output;
import org.ammbra.compute.finder.PlatformImage;
import org.ammbra.compute.subnet.Subnetwork;

public record VirtualMachine(Output<String> displayName, Subnetwork subnet, PlatformImage imageName){
}
