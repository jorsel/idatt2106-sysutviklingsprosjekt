package idatt2106scrumteam10.GIDD.services;

import idatt2106scrumteam10.GIDD.models.Image;
import idatt2106scrumteam10.GIDD.repos.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    Logger logger = Logger.getLogger(idatt2106scrumteam10.GIDD.services.ImageService.class.getName());

    public Image saveImage(Image image) throws IOException {
        if (image == null || image.getContent() == null) {
            logger.log(Level.WARNING, "Can not save empty image");
            return null;
        }
        Image fromDatabase = imageRepository.findByContentAndName(image.getContent(), image.getName()).orElse(null);
        if (fromDatabase != null && fromDatabase.equals(image)) {
            return fromDatabase;
        }
        logger.log(Level.INFO, "Saving image to database");
        return imageRepository.save(image);
    }

    public Image createThumbnail(Image image) throws IOException {
        if (image == null || image.getContent() == null) {
            logger.log(Level.WARNING, "Can not save empty image");
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(image.getContent());
        BufferedImage bufferedImage = ImageIO.read(bis);
        BufferedImage bufferedImage1 = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);
        bufferedImage1.createGraphics().drawImage(bufferedImage.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage1, "jpg", bos);
        Image thumbnail = new Image(bos.toByteArray(), "thumbnail_" + image.getName());
        Image fromDatabase = imageRepository.findByContentAndName(thumbnail.getContent(), thumbnail.getName()).orElse(null);
        if (fromDatabase != null && fromDatabase.equals(thumbnail)) {
            return fromDatabase;
        }
        return imageRepository.save(thumbnail);
    }

    public Image getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

}
