package com.example.EsaySchedule.repository;

import com.example.EsaySchedule.entity.Bookmark;
import com.example.EsaySchedule.entity.BookmarkPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkPK> {


    @Query("SELECT b FROM Bookmark b WHERE b.userId =:userId")
    List<Bookmark> findBookmarkByUserId(@Param("userId") Long userId);

    @Modifying
    void deleteByTeamId(Long teamId);
}
