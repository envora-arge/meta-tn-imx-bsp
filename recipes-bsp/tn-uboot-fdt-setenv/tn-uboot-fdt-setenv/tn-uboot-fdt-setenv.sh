#!/bin/sh
# Set U-Boot fdtfile environment variable on first boot.
# fw_env.config must already exist (created by tn-u-boot-fw-env.service).

FDT_FILE="@UBOOT_FDT_FILE@"

if [ -z "${FDT_FILE}" ]; then
    echo "tn-uboot-fdt-setenv: UBOOT_FDT_FILE not configured, skipping"
    exit 1
fi

# Wait up to 30 s for fw_env.config (tn-u-boot-fw-env.service may still be running)
retries=30
while [ ! -f /etc/fw_env.config ] && [ "${retries}" -gt 0 ]; do
    sleep 1
    retries=$((retries - 1))
done

if [ ! -f /etc/fw_env.config ]; then
    echo "tn-uboot-fdt-setenv: /etc/fw_env.config not found, cannot set fdtfile"
    exit 1
fi

current=$(fw_printenv fdtfile 2>/dev/null | cut -d= -f2)
if [ "${current}" = "${FDT_FILE}" ]; then
    echo "tn-uboot-fdt-setenv: fdtfile already set to ${FDT_FILE}, nothing to do"
    systemctl disable tn-uboot-fdt-setenv.service
    exit 0
fi

echo "tn-uboot-fdt-setenv: setting fdtfile=${FDT_FILE}"
if ! fw_setenv fdtfile "${FDT_FILE}"; then
    echo "tn-uboot-fdt-setenv: fw_setenv failed, cannot update fdtfile"
    exit 1
fi

systemctl disable tn-uboot-fdt-setenv.service
echo "tn-uboot-fdt-setenv: rebooting to apply new DTB..."
systemctl reboot
