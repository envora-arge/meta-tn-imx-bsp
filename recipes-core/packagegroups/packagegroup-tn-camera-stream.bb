DESCRIPTION = "P2P Wi-Fi Direct camera streaming feature for TechNexion i.MX8M Plus camera boards"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

COMPATIBLE_MACHINE = "(imx8mp-lpddr4-evk)"

RDEPENDS:${PN} = "p2p-stream"
