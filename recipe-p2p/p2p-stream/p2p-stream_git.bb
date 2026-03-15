SUMMARY = "P2P Wi-Fi Direct Stream Application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/yasintuncerr/p2p-stream.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Cmake is uses for compile but not install target device.
inherit cmake systemd

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-rtsp-server glib-2.0"

SYSTEMD_SERVICE:${PN} = "p2p-stream.service"

do_install:append() {
    install -d ${D}${sysconfdir}/default
    install -m 0644 ${S}/config/video-node.env ${D}${sysconfdir}/default/video-node
}

FILES:${PN} += "${sysconfdir}/p2p-stream/device-profiles/*"