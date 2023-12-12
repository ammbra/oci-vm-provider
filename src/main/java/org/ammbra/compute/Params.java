package org.ammbra.compute;

public enum Params {

	COMPARTMENT_OCID("compartment_ocid"),
	VCN_CIDR_BLOCK("vcn_cidr_block"),
	VCN_DISPLAY_NAME("vcn_display_name"),
	VCN_DNS_LABEL("vcn_dns_label"),
	INTERNET_GATEWAY("internetgateway_name"),
	OPERATING_SYSTEM("operating_system"),
	OPERATING_SYSTEM_VERSION("operating_system_version"),
	ROUTETABLE_DISPLAYNAME("routetable_displayname"),
	SUBNET_CIDR("subnet_cidr"),
	SUBNET_DISPLAYNAME("subnet_displayname"),
	AMOUNT_VM("amount_vm"),
	SSH_KEY("ssh_authorized_keys"),
	SSH_PRIVATE_KEY_FILE("ssh_private_key_file");

	private final String label;


	Params(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
