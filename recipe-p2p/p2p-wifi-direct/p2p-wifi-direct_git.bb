SUMMARY = "P2P Wi-Fi Direct Scripts and Services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/yasintuncerr/p2p-wifi-direct.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit systemd

RDEPENDS:${PN} = "wpa-supplicant iw iproute2 bash"

do_install() {
    # Install Scripts
    install -d ${D}${bindir}
    install -m 0755 ${S}/scripts/*.sh ${D}${bindir}/

    # Install systemd services
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/systemd/*.service ${D}${systemd_system_unitdir}/

    # Install configurations
    install -d ${D}${sysconfdir}/wpa_supplicant
    install -m 0644 ${S}/config/*.conf ${D}${sysconfdir}/wpa_supplicant/
}

SYSTEMD_SERVICE:${PN} = "p2p-init.service p2p-watchdog.service p2p-power.service"