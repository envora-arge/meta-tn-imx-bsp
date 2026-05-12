# Build libwpa_client.so by recompiling the four control-interface sources
# with -fPIC.  The upstream wpa_supplicant 2.11 Makefile in poky does NOT
# contain a CONFIG_BUILD_WPA_CLIENT_SO target, so we cannot rely on make to
# produce the library.  Instead we compile the same files (OBJS_c in the
# Makefile) ourselves after the main build, when the source tree and any
# generated headers (build_config.h, includes.h) are already in place.
#
# Include search order mirrors what the main build uses:
#   1. ${S}/wpa_supplicant/   — includes.h, build_config.h (generated)
#   2. ${S}/src/              — root of the library headers
#   3. ${S}/src/utils/        — os.h, common.h, wpa_debug.h …
#   4. ${S}/src/common/       — wpa_ctrl.h

do_compile:append() {
    ${CC} ${CFLAGS} -fPIC \
        -DCONFIG_CTRL_IFACE -DCONFIG_CTRL_IFACE_UNIX \
        -I"${S}/wpa_supplicant" -I"${S}/src" -I"${S}/src/utils" -I"${S}/src/common" \
        -c "${S}/src/common/wpa_ctrl.c"  -o "${WORKDIR}/wpa_ctrl.pic.o"

    ${CC} ${CFLAGS} -fPIC \
        -DCONFIG_CTRL_IFACE -DCONFIG_CTRL_IFACE_UNIX \
        -I"${S}/wpa_supplicant" -I"${S}/src" -I"${S}/src/utils" -I"${S}/src/common" \
        -c "${S}/src/utils/wpa_debug.c"  -o "${WORKDIR}/wpa_debug.pic.o"

    ${CC} ${CFLAGS} -fPIC \
        -DCONFIG_CTRL_IFACE -DCONFIG_CTRL_IFACE_UNIX \
        -I"${S}/wpa_supplicant" -I"${S}/src" -I"${S}/src/utils" -I"${S}/src/common" \
        -c "${S}/src/utils/common.c"     -o "${WORKDIR}/common.pic.o"

    ${CC} ${CFLAGS} -fPIC \
        -DCONFIG_CTRL_IFACE -DCONFIG_CTRL_IFACE_UNIX \
        -I"${S}/wpa_supplicant" -I"${S}/src" -I"${S}/src/utils" -I"${S}/src/common" \
        -c "${S}/src/utils/os_unix.c"    -o "${WORKDIR}/os_unix.pic.o"

    ${CC} ${LDFLAGS} -shared -Wl,-soname,libwpa_client.so \
        -o "${WORKDIR}/libwpa_client.so" \
        "${WORKDIR}/wpa_ctrl.pic.o" \
        "${WORKDIR}/wpa_debug.pic.o" \
        "${WORKDIR}/common.pic.o" \
        "${WORKDIR}/os_unix.pic.o"
}

do_install:append() {
    install -m 0644 ${S}/src/common/wpa_ctrl.h ${D}${includedir}/wpa_ctrl.h

    [ -f "${WORKDIR}/libwpa_client.so" ] \
        || bbfatal "libwpa_client.so not found — check do_compile:append output"

    install -d ${D}${libdir}
    install -m 0755 "${WORKDIR}/libwpa_client.so" ${D}${libdir}/libwpa_client.so
}

# poky's base recipe has PACKAGES =+ "${PN}-lib" with FILES:${PN}-lib = "${libdir}/lib*.so"
# so libwpa_client.so lands in wpa-supplicant-lib automatically — no FILES override needed.
# The header goes to wpa-supplicant-dev via the standard includedir rule.
# tn-custom-camera-image.bb installs wpa-supplicant-lib so the .so is present at runtime.
