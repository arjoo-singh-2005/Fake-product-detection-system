package com.fakeproductdetectionsystem.qr;

import com.fakeproductdetectionsystem.ui.VerifyProductFrame;
import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class QRScannerFrame extends JFrame {

    private Webcam webcam;
    private JLabel cameraLabel;

    private boolean cameraRunning = false;

    public QRScannerFrame() {

        setTitle("QR Code Scanner");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        cameraLabel = new JLabel("Camera is not started");
        cameraLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JButton startButton =
                new JButton("START CAMERA");

        JButton scanButton =
                new JButton("SCAN QR NOW");

        JButton stopButton =
                new JButton("STOP CAMERA");

        JButton closeButton =
                new JButton("CLOSE");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(startButton);
        buttonPanel.add(scanButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(cameraLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(
                e -> startCamera()
        );

        scanButton.addActionListener(
                e -> scanCurrentImage()
        );

        stopButton.addActionListener(
                e -> stopCamera()
        );

        closeButton.addActionListener(
                e -> dispose()
        );
    }

    private void startCamera() {

        if (cameraRunning) {
            return;
        }

        try {

            webcam = Webcam.getDefault();

            if (webcam == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No webcam detected."
                );

                return;
            }

            webcam.open();

            cameraRunning = true;

            Thread cameraThread = new Thread(() -> {

                while (cameraRunning
                        && webcam != null
                        && webcam.isOpen()) {

                    try {

                        BufferedImage image =
                                webcam.getImage();

                        if (image != null) {

                            Image scaledImage =
                                    image.getScaledInstance(
                                            640,
                                            480,
                                            Image.SCALE_FAST
                                    );

                            SwingUtilities.invokeLater(() -> {

                                cameraLabel.setIcon(
                                        new ImageIcon(scaledImage)
                                );

                                cameraLabel.setText("");
                            });

                            String code =
                                    decodeQRCode(image);

                            if (code != null) {

                                cameraRunning = false;

                                final String detectedCode =
                                        code.trim();

                                SwingUtilities.invokeLater(() -> {

                                    closeWebcam();

                                    openVerificationWindow(
                                            detectedCode
                                    );
                                });

                                break;
                            }
                        }

                        Thread.sleep(100);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            });

            cameraThread.setDaemon(true);
            cameraThread.start();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to start webcam.\n"
                            + e.getMessage()
            );
        }
    }

    private void scanCurrentImage() {

        if (!cameraRunning
                || webcam == null
                || !webcam.isOpen()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please start the camera first."
            );

            return;
        }

        try {

            BufferedImage image =
                    webcam.getImage();

            if (image == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to capture camera image."
                );

                return;
            }

            String code =
                    decodeQRCode(image);

            if (code != null) {

                cameraRunning = false;

                final String detectedCode =
                        code.trim();

                closeWebcam();

                openVerificationWindow(
                        detectedCode
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "QR code not detected.\n"
                                + "Keep the QR code clearly visible."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to scan QR code."
            );
        }
    }

    private String decodeQRCode(
            BufferedImage image) {

        try {

            LuminanceSource source =
                    new BufferedImageLuminanceSource(
                            image
                    );

            BinaryBitmap bitmap =
                    new BinaryBitmap(
                            new HybridBinarizer(source)
                    );

            Result result =
                    new MultiFormatReader().decode(
                            bitmap
                    );

            return result.getText();

        } catch (NotFoundException e) {

            return null;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    private void stopCamera() {

        cameraRunning = false;

        closeWebcam();

        cameraLabel.setIcon(null);

        cameraLabel.setText(
                "Camera stopped"
        );
    }

    private void closeWebcam() {

        if (webcam != null
                && webcam.isOpen()) {

            webcam.close();
        }
    }

    private void openVerificationWindow(
            String code) {

        VerifyProductFrame frame =
                new VerifyProductFrame(code);

        frame.setVisible(true);

        dispose();
    }

    @Override
    public void dispose() {

        cameraRunning = false;

        closeWebcam();

        super.dispose();
    }
}