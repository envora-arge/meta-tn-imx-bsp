SUMMARY = "Set U-Boot fdt_file environment variable at first boot"
SECTION = "bsp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://tn-uboot-fdt-setenv.sh \
    file://tn-uboot-fdt-setenv.service \
"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

# Set in machine conf, e.g.: UBOOT_FDT_FILE = "imx8mp-evk-tevs.dtb"
UBOOT_FDT_FILE ??= ""

inherit systemd allarch

do_install() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/tn-uboot-fdt-setenv.service \
        ${D}${systemd_unitdir}/system/tn-uboot-fdt-setenv.service

    install -d ${D}${sbindir}
    install -m 0755 ${S}/tn-uboot-fdt-setenv.sh \
        ${D}${sbindir}/tn-uboot-fdt-setenv.sh

    # Bake the DTB filename from the Yocto variable into the installed script
    sed -i 's|@UBOOT_FDT_FILE@|${UBOOT_FDT_FILE}|g' \
        ${D}${sbindir}/tn-uboot-fdt-setenv.sh
}

FILES:${PN} = "${sbindir}/tn-uboot-fdt-setenv.sh"

SYSTEMD_SERVICE:${PN} = "tn-uboot-fdt-setenv.service"

# fw_printenv/fw_setenv come from libubootenv or u-boot-fw-utils
RDEPENDS:${PN} += "bash libubootenv-bin"
