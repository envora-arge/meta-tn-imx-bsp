SUMMARY = "Minimal Camera and VPU OS Image"
LICENSE = "MIT"

inherit core-image

#System Base
IMAGE_INSTALL += " \
    packagegroup-core-boot \
    dropbear \
    nano \
    htop \
    git \
    iperf3 \
"

# VPU (Hantro) and Multimedia (GStreamer, V4L2)
IMAGE_INSTALL += " \
    imx-vpu-hantro \
    imx-gpu-g2d \
    imx-gst1.0-plugin \
    v4l-utils \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
"

# Wi-Fi (NXP 8997 PCIe), Hotspot and Network
IMAGE_INSTALL += " \
    linux-firmware-pcie8997 \
    wpa-supplicant \
    hostapd \
    iw \
    iproute2 \
    p2p-stream \
    p2p-wifi-direct \
"

# Camera Kernel Modules and TechNexion Drivers
IMAGE_INSTALL += " \
    kernel-modules \
"


# Debian Package Manager Support
# NOTE: This only makes Yocto produce packages in .deb format.
# Running 'apt update' at runtime will show an empty list because Yocto
# does not connect to any external internet repo. Only packages added via
# IMAGE_INSTALL at build time are included in the image.
PACKAGE_CLASSES = "package_deb"
IMAGE_FEATURES += "package-management"

# Desktop/Interface packages
IMAGE_FEATURES:remove = "x11-base x11-sato wayland"

IMAGE_INSTALL:remove = " \
    tn-apt-list \
    vizionsdk-dev \
    vizionviewer \
    packagegroup-tn-tools \
    packagegroup-tn-vizionsdk \
    packagegroup-tn-wlan \
    get-support-info \
    glmark2 \
    bluez5 \
    ofono \
    avahi-daemon \
    rpcbind \
    busybox-syslog \
    busybox-klogd \
"

DISTRO_FEATURES:remove = "bluetooth nfc zeroconf 3g nfs"
