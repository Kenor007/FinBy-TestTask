package by.fin.product_card.service.impl;

import by.fin.product_card.error_handling.exception.BadRequestException;
import by.fin.product_card.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static by.fin.product_card.error_handling.constant.ExceptionAnswer.ERROR_DOWNLOADING_IMAGE;
import static by.fin.product_card.error_handling.constant.ExceptionAnswer.ERROR_SAVING_IMAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {
    private static final String X_API_KEY = "X-Api-Key";
    private static final String IMAGE_FILE = "image_file";
    private static final String IMAGE_PNG = "image.png";
    @Value("${remove-bg-api.api-url}")
    private String removeBgApiUrl;
    @Value("${remove-bg-api.x-api-key}")
    private String apiKey;
    @Value("${remove-bg-api.destination-path.source}")
    private String destinationSourcePath;
    @Value("${remove-bg-api.destination-path.processed}")
    private String destinationProcessedPath;
    private final WebClient webClient;

    @Override
    public byte[] processPhoto(String imageUrl, String photoName) {
        log.debug("Downloading the source photo");
        byte[] sourceImage = downloadImage(imageUrl, destinationSourcePath + photoName);
        log.debug("Removing the background");
        byte[] processedImage = removeBackground(sourceImage);
        log.debug("Saving the processed photo");
        saveImage(processedImage, destinationProcessedPath + photoName);
        return processedImage;
    }

    private byte[] downloadImage(String imageUrl, String destinationPath) {
        try {
            WebClient.ResponseSpec responseSpec = webClient.get()
                    .uri(imageUrl)
                    .retrieve();
            byte[] imageBytes = Optional.ofNullable(responseSpec.bodyToMono(byte[].class).block())
                    .orElseThrow(() -> new RuntimeException(String.format(ERROR_DOWNLOADING_IMAGE, imageUrl)));
            saveImage(imageBytes, destinationPath);
            return imageBytes;
        } catch (Exception e) {
            log.error(String.format(ERROR_DOWNLOADING_IMAGE, imageUrl), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    private void saveImage(byte[] imageBytes, String destinationPath) {
        Path path = Paths.get(destinationPath);
        try {
            Files.createDirectories(path.getParent());
            try (FileOutputStream fos = new FileOutputStream(destinationPath)) {
                fos.write(imageBytes);
            }
        } catch (IOException e) {
            log.error(String.format(ERROR_SAVING_IMAGE, destinationPath), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    private byte[] removeBackground(byte[] imageBytes) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(IMAGE_FILE, new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return IMAGE_PNG;
            }
        });
        WebClient.ResponseSpec responseSpec = webClient.post()
                .uri(removeBgApiUrl)
                .header(X_API_KEY, apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve();
        return responseSpec.bodyToMono(byte[].class).block();
    }
}