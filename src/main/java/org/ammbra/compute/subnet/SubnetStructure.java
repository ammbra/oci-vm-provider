package org.ammbra.compute.subnet;


public sealed interface SubnetStructure permits NetworkGateway, RouterTable, Subnetwork, VirtualNetwork { }
