package ru.serov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.serov.entity.WikimediaData;

public interface WikimediaDataRepository extends JpaRepository<WikimediaData, Long> {

}
