read -p "Please provide your Pulumi config passphrase"  -r
echo    # (optional) move to a new line
export PULUMI_CONFIG_PASSPHRASE=$REPLY

read -p "Please provide your OCI region: "  -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
echo $REPLY
pulumi config set oci:region $REPLY
fi

read -p "Please provide your tenancy OCID: " -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
echo $REPLY
pulumi config set oci:tenancyOcid $REPLY --secret
fi

read -p "Please provide your userOcid: " -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
pulumi config set oci:userOcid $REPLY --secret
fi

read -p "Please provide your oci fingerprint: " -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
pulumi config set oci:fingerprint $REPLY --secret
fi

read -p "Please provide parent compartment ocid: " -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
pulumi config set compartment_ocid $REPLY
fi

read -p "Please provide the path to your local oci private key" -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
cat $REPLY | pulumi config set oci:privateKey --secret
fi

echo "Create cloudkey ssh keys at in the current dir"
ssh-keygen -t rsa -m PEM -f cloudkey

cat cloudkey.pub | pulumi config set ssh_authorized_keys
pulumi config set ssh_private_key_file cloudkey

pulumi preview
pulumi up
read -p "Please provide the number of compute instances to create: " -n 1 -r
echo    # (optional) move to a new line
if [[   ! -z "$REPLY" ]]
then
pulumi config set amount_vm $REPLY
fi