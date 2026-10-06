package com.fakeproductdetectionsystem.qr;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRCodeGenerator {

    public static void generateQRCode(
            String text,
            String filePath) throws Exception {

        int width = 300;
        int height = 300;

        Map<EncodeHintType, Object> hints =
                new HashMap<>();

        hints.put(
                EncodeHintType.MARGIN,
                1
        );

        BitMatrix matrix =
                new MultiFormatWriter().encode(
                        text,
                        BarcodeFormat.QR_CODE,
                        width,
                        height,
                        hints
                );

        Path path =
                new File(filePath).toPath();

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                path
        );
    }
}