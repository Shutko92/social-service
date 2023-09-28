package ru.skillbox.diplom.group42.social.service;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group42.social.service.dto.storage.StorageDto;
import ru.skillbox.diplom.group42.social.service.service.storage.StorageService;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private MultipartFile multipartFile;
    private StorageService storageService;
    @Test
    public void funSendingToStorageReturnNotNull(){
        storageService = new StorageService(cloudinary);
        try {
            StorageDto storageDto = storageService.sendingToStorage(multipartFile) ;
            assert storageDto != null;
        } catch (Exception ex){

        }
    }
}
