DESCRIPTION = "P2P Wi-Fi Direct camera streaming feature for TechNexion i.MX8M Plus camera boards"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup

COMPATIBLE_MACHINE = "(tep-imx8mp|tek-imx8mp|axon-imx8mp|edm-g-imx8mp)"

RDEPENDS:${PN} = "p2p-stream"
