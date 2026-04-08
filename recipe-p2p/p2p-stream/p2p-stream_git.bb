SUMMARY = "P2P Wi-Fi Direct Stream Application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/yasintuncerr-embedded-labs/p2p-stream.git;protocol=https;branch=main \
           file://99-camera.rules"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Cmake is uses for compile but not install target device.
inherit cmake pkgconfig systemd

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-rtsp-server glib-2.0 pkgconfig-native"
EXTRA_OECMAKE:append = " -DCMAKE_INSTALL_PREFIX:PATH=${prefix}"

SYSTEMD_AUTO_ENABLE = "disable"

SYSTEMD_SERVICE:${PN} = "p2p-stream.service"

do_install:append() {
    # Some upstream CMake installs to /usr/local; move binary into ${bindir} for Yocto packaging.
    if [ -f ${D}/usr/local/bin/p2p-stream ]; then
        install -d ${D}${bindir}
        mv ${D}/usr/local/bin/p2p-stream ${D}${bindir}/p2p-stream
    fi
    # Remove empty /usr/local leftovers so QA does not flag unshipped directories.
    if [ -d ${D}/usr/local ]; then
        rm -rf ${D}/usr/local
    fi

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${S}/config/video-node.env ${D}${sysconfdir}/default/video-node

    
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/99-camera.rules ${D}${sysconfdir}/udev/rules.d/

    install -d ${D}${sysconfdir}/p2p-stream/device-profiles
    install -m 0644 ${S}/device-profiles/*.ini ${D}${sysconfdir}/p2p-stream/device-profiles/
}

FILES:${PN} += "${sysconfdir}/p2p-stream/device-profiles/* \
                ${sysconfdir}/udev/rules.d/99-camera.rules"