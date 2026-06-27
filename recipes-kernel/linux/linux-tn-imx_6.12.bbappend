FILESEXTRAPATHS:prepend := "${THISDIR}/file:"

# --- NXP Orijinal 960MB CMA Değerini Geri Getiren Yama ---
SRC_URI += "file://0001-restore-nxp-cma-960m.patch"
# ---------------------------------------------------------

# SD card (usdhc2): board does not support UHS 1.8V signalling (dual-mode),
# force no-1-8-v so the card stays in 3.3V high-speed mode.
do_patch:append() {
    sed -i '/usdhc2: mmc@30b50000 {/,/};/{/no-1-8-v;/d}' \
        ${S}/arch/arm64/boot/dts/freescale/imx8mp.dtsi
    sed -i '/usdhc2: mmc@30b50000 {/,/};/s/\(status = "disabled";\)/\1\n\t\t\t\tno-1-8-v;/' \
        ${S}/arch/arm64/boot/dts/freescale/imx8mp.dtsi
}

SRC_URI:append:rescue = " \
       file://logo.ppm \
       file://rescue-fragment.cfg \
       "
DELTA_KERNEL_DEFCONFIG:rescue = "rescue-fragment.cfg"

do_copy_defconfig:append:rescue () {
    cp ${UNPACKDIR}/logo.ppm ${S}/drivers/video/logo/logo_linux_clut224.ppm
}

# Disable the second TEVS camera (CSI1, i2c@30a40000) in the deployed DTB.
# Without this, slow I2C ENXIO errors from the unpopulated CSI1 camera cause
# mxc_isi.0 to unregister (video3 disappears) before userspace can open it.
DEPENDS:append:tn-camera = " dtc-native"

do_deploy:append:tn-camera () {
    dtb="${DEPLOYDIR}/imx8mp-evk-tevs.dtb"
    if [ -f "${dtb}" ]; then
        fdtput -t s "${dtb}" \
            "/soc@0/bus@30800000/i2c@30a40000/tevs@48" \
            status disabled
    fi
}