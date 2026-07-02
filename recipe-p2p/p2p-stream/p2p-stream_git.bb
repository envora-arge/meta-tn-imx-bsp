SUMMARY = "P2P Wi-Fi Direct Stream Application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/envora-arge/p2p-stream.git;protocol=ssh;branch=main \
           file://99-camera.rules"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

# Only boards with the TEVS camera + Wi-Fi Direct hardware carry this feature.
COMPATIBLE_MACHINE = "(tep-imx8mp|tek-imx8mp|axon-imx8mp|edm-g-imx8mp)"

inherit cmake pkgconfig systemd

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-rtsp-server glib-2.0 pkgconfig-native wpa-supplicant"

RDEPENDS:${PN} += "wpa-supplicant iw iproute2 bash"

SYSTEMD_SERVICE:${PN} = "p2p-stream.service p2p-wifi-init.service p2p-wifi-power.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install:append() {
    # Runtime config — split by purpose:
    #   stream.conf           non-secret app/stream parameters (0644)
    #   wifi-credentials.conf WPA-PSK identity, restricted (0600)
    install -d ${D}${sysconfdir}/p2p-stream
    install -m 0644 ${S}/config/stream.conf ${D}${sysconfdir}/p2p-stream/stream.conf
    install -m 0600 ${S}/config/wifi-credentials.conf ${D}${sysconfdir}/p2p-stream/wifi-credentials.conf

    install -d ${D}${sysconfdir}/p2p-stream/device-profiles
    install -m 0644 ${S}/device-profiles/*.ini ${D}${sysconfdir}/p2p-stream/device-profiles/

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/99-camera.rules ${D}${sysconfdir}/udev/rules.d/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/systemd/p2p-stream.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${S}/systemd/p2p-wifi-init.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${S}/systemd/p2p-wifi-power.service ${D}${systemd_system_unitdir}/

    install -d ${D}${libexecdir}/p2p-stream
    install -m 0755 ${S}/wifi/p2p-wifi-init.sh ${D}${libexecdir}/p2p-stream/
    install -m 0755 ${S}/wifi/p2p-wifi-power.sh ${D}${libexecdir}/p2p-stream/
}

FILES:${PN} += "${sysconfdir}/p2p-stream \
                ${sysconfdir}/udev/rules.d/99-camera.rules \
                ${libexecdir}/p2p-stream"
