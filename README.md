# Custom meta-tn-imx-bsp Fork

## Overview
This repository is a customized fork of the TechNexion i.MX BSP (`meta-tn-imx-bsp`). It has been specifically tailored to support our custom camera capabilities, resolving hardware-specific negotiation issues, removing unwanted bloatware, and streamlining the software stack for our embedded production and development environments.

## Motivation & Key Modifications

### 1. CMA Buffer Allocation Fix (VPU Not Negotiated Error)
We encountered a critical `not-negotiated (-4)` error from the Video Processing Unit (VPU) during video processing. This occurred because the default Contiguous Memory Allocator (CMA) buffer allocation (previously constrained to 32MB by default scaling) was insufficient for our high-resolution GStreamer pipelines.
To resolve this, we applied a hardcoded patch in the kernel recipe (`recipes-kernel/linux/linux-imx_%.bbappend`). The `do_patch:append()` task uses `sed` to modify the Device Tree (`imx8mp.dtsi`) before compilation:
- **`size`**: Adjusted to `0x20000000` (512MB) to ensure ample contiguous memory for the VPU.
- **`alloc-ranges`**: Expanded to `0x40000000 0 0xC0000000` to allow the CMA allocator to map memory over a wider, more suitable address space limit.

### 2. Streamlined Build Environment (`tools/setup-environment.sh` & others)
To reduce build times and prevent the inclusion of unwanted packages in our custom, lightweight image, we modified the environment setup scripts (e.g., `tools/setup-environment.sh`). We effectively masked (`BBMASK`) unnecessary recipes such as Docker, Weston, Qt5 components, and removed other unneeded generic tools when building our specific application image.

### 3. Custom Image Recipes
We introduced dedicated image recipes located in `recipes-tn/images/` to cleanly manage our production and development targets:
- **`tn-custom-camera-image.bb`**: The production-ready minimal OS image containing essential Wi-Fi, GStreamer, VPU (Hantro), and camera kernel modules.
- **`tn-custom-camera-image-dev.bb`**: A development-focused extension of the production image. It increases the rootfs extra space by 2GB and includes essential compile-time tools and debuggers such as `cmake`, `make`, `pkgconf`, `gcc`, and `gdb`.

### 4. U-Boot Default DTB Configuration
To ensure seamless boot behavior for our specific camera hardware setup, we directly modified the U-Boot boot script (`recipes-bsp/u-boot/u-boot-script-technexion/bootscript-tsl-arm64.txt`). 
- **Change:** Set the default device tree blob (`fdtfile`) directly to `imx8mp-evk-tevs.dtb` for the TEVS/TSL camera board.
- *Note: This modification is currently pending hardware testing to confirm reliable boot stability.*

### 5. In-House P2P Packages Integration
We integrated our custom developed P2P networking and streaming applications into the Yocto build system by creating dedicated recipes under `recipe-p2p/`:
- **`p2p-wifi-direct_git.bb`**: Pulls and manages the Wi-Fi Direct connection processes, power management, and watchdog bash services.
- **`p2p-stream_git.bb`**: Pulls and compiles the CMake-based GStreamer video streaming application, designed to communicate over the established P2P link.

---

## Building the Images

*Note: Use the standard Yocto build procedures as per your base manifest repository, targeting our customized image names.*

1. **Initialize and sync the repository** via the standard `repo init` and `repo sync` commands.
2. **Setup the build environment** (example for i.MX8MP LPDDR4 EVK):
   ```bash
   DISPLAY=hdmi DISTRO=fsl-imx-xwayland MACHINE=imx8mp-lpddr4-evk source tn-setup-release.sh -b build-image
   ```
3. **Build your desired image**:
   - For Production:
     ```bash
     bitbake tn-custom-camera-image
     ```
   - For Development:
     ```bash
     bitbake tn-custom-camera-image-dev
     ```
