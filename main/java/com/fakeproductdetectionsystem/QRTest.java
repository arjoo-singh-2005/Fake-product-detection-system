package com.fakeproductdetectionsystem;

import com.fakeproductdetectionsystem.qr.QRCodeGenerator;

public class QRTest {

    public static void main(String[] args) {

        try {

            QRCodeGenerator.generateQRCode(
                    "AJ1001",
                    "AJ1001.png"
            );

            System.out.println(
                    "QR Code generated successfully!"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}