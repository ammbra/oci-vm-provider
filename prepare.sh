#install pulumi
if ! command -v pulumi &> /dev/null
then
    echo "pulumi could not be found. pulumi CLI will be installed"
    curl -fsSL https://get.pulumi.com | sh
fi

#create a local state file directory
mkdir oci-stack-statefile
pulumi login file://oci-stack-statefile

#pulumi stack select
pulumi new https://github.com/ammbra/oci-vm-provider.git --force

sh setup.sh