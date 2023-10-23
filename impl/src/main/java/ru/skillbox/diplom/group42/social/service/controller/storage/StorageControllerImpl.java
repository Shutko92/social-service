package ru.skillbox.diplom.group42.social.service.controller.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group42.social.service.dto.storage.StorageDto;
import ru.skillbox.diplom.group42.social.service.service.storage.StorageService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class StorageControllerImpl implements StorageController {

    private final StorageService storageService;

    @Override
    public ResponseEntity<StorageDto> sendingToStorage(MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(storageService.sendingToStorage(imageFile));
    }
}
