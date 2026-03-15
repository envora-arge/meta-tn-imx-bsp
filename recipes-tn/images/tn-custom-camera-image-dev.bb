require tn-custom-camera-image.bb

SUMMARY = "Minimal Camera and VPU OS Image - Development Version"
DESCRIPTION = "Development image extending tn-custom-camera-image with SDK, debug and development tools."

# Development features
# EXTRA_IMAGE_FEATURES global conf değişkenidir, image içerisinde genelde IMAGE_FEATURES kullanılır.
IMAGE_FEATURES += "tools-sdk dev-pkgs"

# Additional development packages
IMAGE_INSTALL += " \
    cmake \
    make \
    pkgconf \
    gdb \
"

# Increase rootfs extra space for dev environment (2GB extra space)
IMAGE_ROOTFS_EXTRA_SPACE = "2097152"

# Note: PREFERRED_PROVIDER tipik olarak conf dosyalarına ait global bir değişkendir, 
# Yocto'da imaj seviyesinde değil distro/local.conf seviyesinde belirlenmesi daha sağlıklıdır. 
# Ancak burada referans olması ve hata vermemesi için eklenmiştir.
PREFERRED_PROVIDER_pkgconfig = "pkgconf"
