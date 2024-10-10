package com.ejunior.fisio_api.infra.model.service.file;

import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class InputStreamQrCodeService {

    public InputStream generate(String code) {
        var cod = QRCode.from(code).withSize(250, 250).stream();
        return new ByteArrayInputStream(cod.toByteArray());
    }

}
