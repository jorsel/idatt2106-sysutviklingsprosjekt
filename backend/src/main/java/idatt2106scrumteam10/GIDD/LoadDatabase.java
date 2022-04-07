package idatt2106scrumteam10.GIDD;

import idatt2106scrumteam10.GIDD.models.*;
import idatt2106scrumteam10.GIDD.repos.ActivityRepository;
import idatt2106scrumteam10.GIDD.repos.CommentRepository;
import idatt2106scrumteam10.GIDD.repos.LocationRepository;
import idatt2106scrumteam10.GIDD.repos.TagRepository;
import idatt2106scrumteam10.GIDD.services.ImageService;
import idatt2106scrumteam10.GIDD.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ActivityRepository arepo, UserService userService, LocationRepository lrepo, TagRepository trepo, ImageService imageService, CommentRepository crepo) {

        return args -> {
            ClassPathResource classPathResource = new ClassPathResource("DefaultUser.jpg");
            BufferedImage bImage = ImageIO.read(classPathResource.getFile());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos );
            byte [] data = bos.toByteArray();
            imageService.saveImage(new Image(data, "defaultPicture.jpg"));
            imageService.createThumbnail(new Image(data, "defaultPicture.jpg"));

            User u1 = userService.createUser(new User("jens@loe", "jens", "Jens", "Loe", LocalDate.of(1993, 12, 10), 1, true));
            User u2 = userService.createUser(new User("torbjørn@bakke", "torbjørn", "Torbjørn", "Bakke", LocalDate.of(1988, 8, 2), 1, true));
            User u3 = userService.createUser(new User("torbjørn@øverås", "torbjørn", "Torbjørn", "Øverås", LocalDate.of(1990, 2, 10), 1, true));
            User u4 = userService.createUser(new User("stian@selvåg", "stian", "Stian", "Selvåg", LocalDate.of(1995, 5, 2), 1, true));
            User u5 = userService.createUser(new User("jørgen@selsøyvold", "jørgen", "Jørgen", "Selsøyvold", LocalDate.of(1994, 10, 25), 1, true));
            User u6 = userService.createUser(new User("mahmoud@shawish", "mahmoud", "Mahmoud", "Shawish", LocalDate.of(1990, 2, 10), 1, true));
            User u7 = userService.createUser(new User("sindre@strøm", "sindre", "Sindre", "Strøm", LocalDate.of(1990, 2, 10), 1, true));
            User u8 = userService.createUser(new User("thomas@huru", "thomas", "Thomas", "Huru", LocalDate.of(1990, 2, 10), 1, true));
            User u9 = userService.createUser(new User("magnus@sygard", "magnus", "Magnus", "Sygard", LocalDate.of(1990, 2, 10), 1, true));

            Tag t1 = trepo.save(new Tag("Tur"));
            Tag t2 = trepo.save(new Tag("Sosialt"));
            Tag t3 = trepo.save(new Tag("Trening"));
            Tag t4 = trepo.save(new Tag("Innendørs"));
            Tag t5 = trepo.save(new Tag("Lagsport"));
            Tag t6 = trepo.save(new Tag("Rumpeldunk"));
            Tag t7 = trepo.save(new Tag("Utendørs"));
            Tag t8 = trepo.save(new Tag("Mat"));
            Tag t9 = trepo.save(new Tag("Smittevennlig"));
            Tag t10 = trepo.save(new Tag("Glutenfritt"));
            Tag t11 = trepo.save(new Tag("Vegansk"));

            Location l1 = lrepo.save(new Location("Ådalsbruk, Norway", 60.7965463, 11.3111618));
            Location l2 = lrepo.save(new Location("Kongens gate 9, 7013 Trondheim, Norway", 63.43015250000001, 10.3939341));
            Location l3 = lrepo.save(new Location("Sjøgangen 2, 7010 Trondheim, Norway", 63.4362602, 10.3992684));

            Activity a1 = arepo.save(new Activity("Fotballkamp", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(4),
                    22, 2, l1, u1, Stream.of(t3, t5, t7, t10).collect(Collectors.toSet()), Stream.of("Fotballsko").collect(Collectors.toList()), null));
            Activity a2 = arepo.save(new Activity("Rumpeldunk", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2),
                    14, 2, l2, u2, Stream.of(t3, t5, t6, t9, t11).collect(Collectors.toSet()), Stream.of("Sopelime").collect(Collectors.toList()), null));
            Activity a3 = arepo.save(new Activity("Fjelltur", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2),
                    10, 3, l3, u3, Stream.of(t1, t2, t9).collect(Collectors.toSet()), Stream.of("Tursko", "Godt humør").collect(Collectors.toList()), null));
            Activity a4 = arepo.save(new Activity("Innebandy", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(0), LocalDateTime.now().plusDays(0).plusHours(1),
                    16, 2, l1, u4, Stream.of(t3, t4, t5).collect(Collectors.toSet()), Stream.of("Kølle").collect(Collectors.toList()), null));
            Activity a5 = arepo.save(new Activity("Grilling", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(3),
                    25, 1, l2, u5, Stream.of(t2, t7, t8, t9, t10, t11).collect(Collectors.toSet()), Stream.of("Grillmat").collect(Collectors.toList()), null));
            Activity a6 = arepo.save(new Activity("Hundetrening langs ladestien", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(2),
                    10, 1, l3, u6, Stream.of(t1, t2, t3, t7).collect(Collectors.toSet()), Stream.of("Hund").collect(Collectors.toList()), null));
            Activity a7 = arepo.save(new Activity("Festival", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(25), LocalDateTime.now().plusDays(30),
                    250, 1, l1, u7, Stream.of(t2, t7, t8, t9).collect(Collectors.toSet()), Stream.of("Penger").collect(Collectors.toList()), null));
            Activity a8 = arepo.save(new Activity("Pizzakveld på Peppes Pizza", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(3),
                    9, 1, l2, u8, Stream.of(t2, t8, t9, t10, t11).collect(Collectors.toSet()), Stream.of("Gavekort").collect(Collectors.toList()), null));
            Activity a9 = arepo.save(new Activity("Konsert med DDE", "Dette er en beskrivelse av arrangementet.", LocalDateTime.now().plusDays(50), LocalDateTime.now().plusDays(50).plusHours(4),
                    2000, 2, l2, u9, Stream.of(t2, t4, t8, t9).collect(Collectors.toSet()), null, null));
            Comment c1 = crepo.save(new Comment("Dette er en kommentar!", LocalDateTime.now(), u1));
            Comment c2 = crepo.save(new Comment("Hva er en kommentar?", LocalDateTime.now(), u2));
            Comment c3 = crepo.save(new Comment("Kom her, så skal jeg gi deg en kommentar!", LocalDateTime.now(), u3));
            Comment c4 = crepo.save(new Comment("Kan'ke bare kommentar og kommentar!", LocalDateTime.now(), u4));
            a1.setComments(Stream.of(c1, c2).collect(Collectors.toList()));
            a2.setComments(Stream.of(c3, c4).collect(Collectors.toList()));
            arepo.save(a1);
            arepo.save(a2);
        };
    }
}
