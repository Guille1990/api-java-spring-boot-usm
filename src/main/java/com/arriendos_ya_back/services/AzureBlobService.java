package com.arriendos_ya_back.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AzureBlobService {
    private static final Logger logger = LoggerFactory.getLogger(AzureBlobService.class);

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public String subirArchivo(MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) {
            logger.warn("Archivo vacío recibido");
            return null;
        }

        try {
            logger.info("Iniciando carga de archivo: {} ({}MB)", archivo.getOriginalFilename(), archivo.getSize() / 1024 / 1024);
            
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();

            // Verificar que el contenedor existe
            if (!containerClient.exists()) {
                logger.info("Contenedor no existe, creando: {}", containerName);
                containerClient.create();
            }

            // Generar nombre único
            String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            logger.info("Nombre generado: {}", nombreArchivo);

            // Subir archivo a Azure Blob Storage
            BlobClient blobClient = containerClient.getBlobClient(nombreArchivo);
            blobClient.upload(archivo.getInputStream(), archivo.getSize(), true);

            String urlPublica = blobClient.getBlobUrl();
            logger.info("Archivo cargado exitosamente: {}", urlPublica);
            
            return urlPublica;
        } catch (Exception e) {
            logger.error("Error al subir archivo a Azure: ", e);
            throw new IOException("Error al subir archivo: " + e.getMessage(), e);
        }
    }

    public boolean eliminarArchivo(String urlArchivo) {
        try {
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();

            // Extraer nombre del blob de la URL
            String nombreBlob = urlArchivo.substring(urlArchivo.lastIndexOf("/") + 1);

            BlobClient blobClient = containerClient.getBlobClient(nombreBlob);
            blobClient.delete();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
