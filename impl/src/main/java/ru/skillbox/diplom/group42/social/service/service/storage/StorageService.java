package ru.skillbox.diplom.group42.social.service.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group42.social.service.dto.storage.StorageDto;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final Cloudinary cloudinary;

    public StorageDto sendingToStorage(MultipartFile imageFile) throws IOException {
        log.debug("Entering StorageService method sendingToStorage(MultipartFile imageFile)");
        Map uploadResult = cloudinary
                .uploader()
                .upload(imageFile.getBytes(), ObjectUtils.asMap("folder", "my-folder",
                        "public_id", imageFile.getOriginalFilename()));

        log.info("StorageService method sendingToStorage(MultipartFile imageFile) executed. File "
                + imageFile.getOriginalFilename() + " sent to storage");
        return new StorageDto(uploadResult.get("secure_url").toString());
    }
}
