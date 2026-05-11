SUMMARY = "U-Boot boot script to select TEVS camera DTB at boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"

SRC_URI = "file://tn-camera-bootscr.txt"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

inherit deploy allarch

do_compile() {
    mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
        -n "TEVS camera boot script" -d ${S}/tn-camera-bootscr.txt \
        ${S}/boot.scr
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${S}/boot.scr ${DEPLOYDIR}/boot.scr
}

addtask deploy after do_compile before do_build

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(imx8mp-lpddr4-evk)"
