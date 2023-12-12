package org.ammbra.compute;

import com.pulumi.core.Output;
import org.ammbra.compute.finder.PlatformImage;
import org.ammbra.compute.subnet.Subnet;

public record VirtualMachine(Output<String> displayName, Subnet subnet, PlatformImage imageName){
}
