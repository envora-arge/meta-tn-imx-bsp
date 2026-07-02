# Copyright 2026 TechNexion
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "\
 TechNexion camera-streaming appliance image. Boots straight into the \
 P2P Wi-Fi Direct video stream (see packagegroup-tn-camera-stream) — \
 no container runtime or Qt demo stack.\
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

COMPATIBLE_MACHINE = "(tep-imx8mp|tek-imx8mp|axon-imx8mp|edm-g-imx8mp)"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    packagegroup-core-boot \
    packagegroup-tn-camera-stream \
    packagegroup-fsl-gstreamer1.0 \
    packagegroup-fsl-gstreamer1.0-full \
    connman \
    openssh \
    kernel-modules \
    "

IMAGE_FEATURES += "\
    ssh-server-openssh \
    hwcodecs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston-init', '', d)} \
    "

IMAGE_LINGUAS = " "

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "2048"
IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

WKS_FILE = "${@bb.utils.contains("DISTRO_FEATURES", "virtualization", "tn-spl-rootfs-container.wks.in", "tn-spl-bootpart-rootfs.wks.in", d)}"
WKS_FILE:mx8-nxp-bsp = "${@bb.utils.contains("DISTRO_FEATURES", "virtualization", "tn-imx8-imxboot-rootfs-container.wks.in", "tn-imx8-imxboot-bootpart-rootfs.wks.in", d)}"

EXTRA_USERS_PARAMS = " \
useradd -P technexion technexion; \
usermod -a -G sudo,users,plugdev technexion; \
"

inherit core-image extrausers
