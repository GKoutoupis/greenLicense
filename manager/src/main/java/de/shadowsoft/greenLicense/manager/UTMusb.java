package de.shadowsoft.greenLicense.manager;


import org.usb4java.*;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.lang.Object;

import java.io.File;

public class UTMusb {
    public static int vid = 6790;
    public static int pid = 29987;

    public static void main(String[] args) {
        Context context = new Context();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS) {
            throw new LibUsbException("Unable to initialize the usb device", result);
        }
        DeviceList list = new DeviceList();
        result = LibUsb.getDeviceList(context, list);
        if (result < 0) throw new LibUsbException("Unable to get device list", result);
        try {
            for (Device device : list) {
                System.out.println(device);
                DeviceDescriptor device_descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, device_descriptor);
                if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to get device descriptor : ", result);
                System.out.println("Product id is : " + device_descriptor.idProduct() + " " + "Vendor id is : " + device_descriptor.idVendor());

                DeviceHandle handle = new DeviceHandle();
                result = LibUsb.open(device, handle);
                if (result < 0) {
                    System.out.println(String.format("Unable to open device: %s. "
                                    + "Continuing without device handle.",
                            LibUsb.strError(result)));
                    handle = null;
                } else {
                    System.out.println("Open.");

//                    if (device_descriptor.bDeviceClass() == 0){
//                        int contextInterface = 0;
//                        LibUsb.claimInterface(handle, contextInterface);
//                        System.out.println(device_descriptor.dump(handle));
//                    }
                }

                if (device_descriptor.idProduct() == pid && device_descriptor.idVendor() == vid) {
                    System.out.println("Product id and vendor id was matched");
                } else {

                    System.out.println("Product id and vendor id was not matched");
                    System.out.println(device_descriptor);
                }
                System.out.println(LibUsb.getDeviceAddress(device));
                System.out.println("-------------------------------------");

            }

        } finally {
            LibUsb.freeDeviceList(list, true);
        }
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            File[] filesInRoot = File.listRoots()[i].listFiles();
            for (int j = 0; j < filesInRoot.length; j++) {
                System.out.println(filesInRoot[j].toString());
            }
        }


    }

}
